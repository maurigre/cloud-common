package io.github.mgr.cloud.common.pubsub.config;

import io.github.mgr.cloud.common.pubsub.annotations.PubSubListener;
import io.github.mgr.cloud.common.pubsub.handlers.PubSubListenerErrorHandler;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import jakarta.annotation.PreDestroy;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PubSubListenerRegister implements SmartInitializingSingleton {

    private static final String TAG_CUSTOM_MESSAGE_ID = "custom_message_id";
    private final ApplicationContext applicationContext;
    private final PubSubProperties pubSubProperties;
    private final Environment environment;
    private final List<Subscriber> subscribers = new ArrayList<>();
    private final AtomicReference<String> basePackageAtomicReference = new AtomicReference<>();

    @Override
    public void afterSingletonsInstantiated() {
        registerPubSubListeners();
    }

    private void registerPubSubListeners() {
        Map<String, Object> beansWithAnnotation = getFilteredComponentBeans();

        beansWithAnnotation
            .forEach((beanName, bean) -> {
                Method[] methods = bean.getClass().getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(PubSubListener.class)) {
                        PubSubListener annotation = method.getAnnotation(PubSubListener.class);
                        final var subscription = resolveProperty(annotation.subscription());
                        final var errorHandlerBeanName = annotation.errorHandler();
                        final var errorHandler = applicationContext.getBean(toLowerCaseFirstLetter(errorHandlerBeanName));

                        startSubscriber(subscription, bean, method, errorHandler);
                    }
                }
            });
    }

    private void startSubscriber(String subscription, Object targetBean, Method method, Object errorHandler) {
        try {
            final var subscriptionName = ProjectSubscriptionName.of(pubSubProperties.projectId(), subscription);
            final var messageReceiver = getMessageReceiver(targetBean, method, errorHandler, subscription);
            final var subscriber = Subscriber.newBuilder(subscriptionName, messageReceiver).build();

            subscriber.startAsync().awaitRunning();
            subscribers.add(subscriber);
            log.info("[pubsub-listener-register] Registered subscription {} by bean: {}, found method: {}", subscription,
                targetBean.getClass().getSimpleName(), method.getName());

        } catch (Exception e) {
            log.error("[pubsub-listener-register] Failed to start subscriber for subscription {}: {}", subscription, e.getMessage(), e);
        }
    }

    private MessageReceiver getMessageReceiver(Object targetBean, Method method, Object errorHandler, String subscription) {
        return (message, consumer) -> {
            MDC.clear();
            MDC.put("message_id", message.getAttributesOrDefault(TAG_CUSTOM_MESSAGE_ID, message.getMessageId()));
            try {
                method.invoke(targetBean, message);
                consumer.ack();

            } catch (Exception ex) {
                errorHandler(errorHandler, message, consumer, subscription, ex);
            }
        };
    }

    private void errorHandler(
        final Object errorHandler,
        final PubsubMessage message,
        final AckReplyConsumer consumer,
        final String subscription,
        final Exception ex
    ) {
        if (errorHandler instanceof PubSubListenerErrorHandler handler) {
            handler.handleError(message, consumer, subscription, ex);
        } else {
            log.error("[pubsub-listener-register] Error handling class not found: {}", ex.getMessage(), ex);
            consumer.nack();
        }
    }

    @PreDestroy
    public void onShutdown() {
        for (Subscriber subscriber : subscribers) {
            if (subscriber != null) {
                subscriber.stopAsync().awaitTerminated();
            }
        }
    }

    private String toLowerCaseFirstLetter(String input) {
        if (StringUtils.isBlank(input)) {
            return input;
        }
        char[] chars = input.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    private String resolveProperty(String property) {
        if (StringUtils.isBlank(property)) {
            log.error("[pubsub-listener-register] Property is blank or null");
            throw new IllegalArgumentException("[pubsub-listener-register] Property is blank or null");
        }

        if (property.startsWith("${") && property.endsWith("}")) {
            String propertyName = property.substring(2, property.length() - 1);
            property = environment.getProperty(propertyName);
        }

        if (StringUtils.isBlank(property)) {
            log.error("[pubsub-listener-register] Error resolving property subscription: {}", property);
            throw new IllegalArgumentException("[pubsub-listener-register] Error resolving property subscription: " + property);
        }

        return property;
    }

    private Map<String, Object> getFilteredComponentBeans() {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Component.class);

        return beansWithAnnotation.entrySet()
            .stream()
            .filter(this::isListener)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean isListener(Map.Entry<String, Object> entry) {
        final var className = entry.getValue().getClass().getSimpleName();
        if (!className.endsWith("Listener")) {
            return false;
        }

        final var basePackage = getBasePackage();
        if (basePackage != null && !basePackage.isEmpty()) {
            return entry.getValue().getClass().getPackageName().startsWith(basePackage);
        }

        return true;
    }

    public String getBasePackage() {
        return basePackageAtomicReference.updateAndGet(currentValue -> {
            if (currentValue == null) {
                Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(SpringBootApplication.class);

                if (beansWithAnnotation.isEmpty()) {
                    log.warn("[pubsub-listener-register] No beans annotated with @SpringBootApplication were found");
                    return "";
                }

                Object mainAppBean = beansWithAnnotation.values().iterator().next();
                Class<?> mainAppClass = mainAppBean.getClass();

                if (mainAppClass.getName().contains("$$")) {
                    mainAppClass = mainAppClass.getSuperclass();
                }

                return mainAppClass.getPackageName();
            }
            return currentValue;
        });
    }
}
