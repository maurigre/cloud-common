package io.github.maurigre.cloud.common.pubsub;

import io.github.maurigre.cloud.common.pubsub.config.PubSubListenerRegister;
import io.github.maurigre.cloud.common.pubsub.config.PubSubProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


/**
 * The type Pubsub auto configuration.
 */
@Configuration
@EnableConfigurationProperties(PubSubProperties.class)
public class PubsubAutoConfiguration {

    private final ApplicationContext applicationContext;
    private final PubSubProperties pubSubProperties;
    private final Environment environment;

  /**
   * Instantiates a new Pubsub auto configuration.
   *
   * @param applicationContext the application context
   * @param pubSubProperties   the pub sub properties
   * @param environment        the environment
   */
  public PubsubAutoConfiguration(ApplicationContext applicationContext, PubSubProperties pubSubProperties, Environment environment) {
        this.applicationContext = applicationContext;
        this.pubSubProperties = pubSubProperties;
        this.environment = environment;
    }

  /**
   * Pub sub listener register pub sub listener register.
   *
   * @return the pub sub listener register
   */
  @Bean
    public PubSubListenerRegister pubSubListenerRegister() {
        return new PubSubListenerRegister(applicationContext, pubSubProperties, environment);
    }
}
