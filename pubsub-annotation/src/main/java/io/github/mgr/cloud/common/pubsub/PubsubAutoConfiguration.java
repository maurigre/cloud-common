package io.github.mgr.cloud.common.pubsub;

import io.github.mgr.cloud.common.pubsub.config.PubSubListenerRegister;
import io.github.mgr.cloud.common.pubsub.config.PubSubProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EnableConfigurationProperties(PubSubProperties.class)
public class PubsubAutoConfiguration {

    private final ApplicationContext applicationContext;
    private final PubSubProperties pubSubProperties;
    private final Environment environment;

    public PubsubAutoConfiguration(ApplicationContext applicationContext, PubSubProperties pubSubProperties, Environment environment) {
        this.applicationContext = applicationContext;
        this.pubSubProperties = pubSubProperties;
        this.environment = environment;
    }

    @Bean
    public PubSubListenerRegister pubSubListenerRegister() {
        return new PubSubListenerRegister(applicationContext, pubSubProperties, environment);
    }
}
