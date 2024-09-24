package io.github.mgr.cloud.feign.feign.config;


import feign.RetryableException;
import io.github.mgr.cloud.common.feign.annotations.Backoff;
import io.github.mgr.cloud.common.feign.annotations.FeignRetry;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Aspect
public class FeignRetryAspect {

    private static final Logger log = LoggerFactory.getLogger(FeignRetryAspect.class);
    private static final String RETRY_API_COUNT = "retry-api-count";
    private final Environment environment;

    public FeignRetryAspect(Environment environment) {
        this.environment = environment;
    }

    @Around("@annotation(feignRetry)")
    public Object retry(ProceedingJoinPoint joinPoint, FeignRetry feignRetry) throws Throwable {
        final Method method = getCurrentMethod(joinPoint);
        final RetryTemplate retryTemplate = createRetryTemplate(feignRetry);
        final Map<String, String> contextMap = MDC.getCopyOfContextMap();

            return retryTemplate.execute(context -> {
                int retryCount = context.getRetryCount();
                MDC.setContextMap(contextMap);
                MDC.put(RETRY_API_COUNT, String.valueOf(retryCount));
                log.info("[feign-retry-aspect-config] Sending request method: {}, max attempt: {}, delay: {}, retryCount: {}",
                    method.getName(), resolveProperty(feignRetry.maxAttempt()), resolveProperty(feignRetry.backoff().delay()), retryCount);

                return joinPoint.proceed(joinPoint.getArgs());
            });
    }

    private RetryTemplate createRetryTemplate(FeignRetry feignRetry) {
        final var retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(prepareBackOffPolicy(feignRetry));
        retryTemplate.setRetryPolicy(prepareSimpleRetryPolicy(feignRetry));
        return retryTemplate;
    }

    private BackOffPolicy prepareBackOffPolicy(FeignRetry feignRetry) {
        final var multiplier = getDoubleField(feignRetry.backoff(), Backoff::multiplier);

        if (multiplier != 0) {
            return prepareExponentialBackOffPolicy(feignRetry);
        }
        return prepareFixedBackOffPolicy(feignRetry);
    }

    private FixedBackOffPolicy prepareFixedBackOffPolicy(FeignRetry feignRetry) {
        final var delay = getLongField(feignRetry.backoff(), Backoff::delay);
        final var fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(delay);
        return fixedBackOffPolicy;
    }

    private ExponentialBackOffPolicy prepareExponentialBackOffPolicy(FeignRetry feignRetry) {
        final var delay = getLongField(feignRetry.backoff(), Backoff::delay);
        final var maxDelay = getLongField(feignRetry.backoff(), Backoff::maxDelay);
        final var multiplier = getDoubleField(feignRetry.backoff(), Backoff::multiplier);
        final var backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(delay);
        backOffPolicy.setMaxInterval(maxDelay);
        backOffPolicy.setMultiplier(multiplier);
        return backOffPolicy;
    }

    private SimpleRetryPolicy prepareSimpleRetryPolicy(FeignRetry feignRetry) {
        final var maxAttempt = getIntegerField(feignRetry, FeignRetry::maxAttempt);
        Map<Class<? extends Throwable>, Boolean> policyMap = new HashMap<>();
        policyMap.put(RetryableException.class, true);

        for (Class<? extends Throwable> t : feignRetry.include()) {
            policyMap.put(t, true);
        }

        return new SimpleRetryPolicy(maxAttempt, policyMap, true);
    }

    private Method getCurrentMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    private <T> Optional<String> getField(T obj, Function<T, String> fieldGetter) {
        return Optional.ofNullable(obj)
            .map(fieldGetter)
            .map(this::resolveProperty)
            .filter(StringUtils::isNotBlank)
            .filter(StringUtils::isNumeric);
    }

    private <T> Long getLongField(T obj, Function<T, String> fieldGetter) {
        return getField(obj, fieldGetter)
            .map(Long::valueOf)
            .orElse(0L);
    }

    private <T> Double getDoubleField(T obj, Function<T, String> fieldGetter) {
        return getField(obj, fieldGetter)
            .map(Double::valueOf)
            .orElse(0.0D);
    }

    private <T> Integer getIntegerField(T obj, Function<T, String> fieldGetter) {
        return getField(obj, fieldGetter)
            .map(Integer::valueOf)
            .orElse(0);
    }

    private String resolveProperty(String property) {
        if (property.isEmpty()) {
            return null;
        }

        String propertyName = property;
        if (property.startsWith("${") && property.endsWith("}")) {
            propertyName = property.substring(2, (property.length() - 1));
        }

        return environment.getProperty(propertyName);
    }
}
