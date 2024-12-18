package io.github.maurigre.cloud.common.pubsub.config;

import static java.util.Objects.requireNonNull;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

/**
 * Configuration properties for Google Cloud Pub/Sub.
 *
 * @param projectId        the Google Cloud project ID
 * @param maxRetryAttempts the maximum number of retry attempts
 * @param main             the main Pub/Sub properties
 * @param fallback         the fallback Pub/Sub properties
 */
@Builder
@ConfigurationProperties(prefix = "google.cloud.pubsub")
public record PubSubProperties(
    String projectId,
    Integer maxRetryAttempts,
    MainProperties main,
    FallbackProperties fallback
) {

    /**
     * Creates a new instance of PubSubProperties.
     *
     * @param projectId        the Google Cloud project ID
     * @param maxRetryAttempts the maximum number of retry attempts
     * @param main             the main Pub/Sub properties
     * @param fallback         the fallback Pub/Sub properties
     */
    @ConstructorBinding
    public PubSubProperties(String projectId, Integer maxRetryAttempts, MainProperties main, FallbackProperties fallback) {
        this.projectId = requireNonNull(projectId);
        this.maxRetryAttempts = requireNonNull(maxRetryAttempts);
        this.main = requireNonNull(main);
        this.fallback = requireNonNull(fallback);
    }

    /**
     * Main properties for Google Cloud Pub/Sub.
     *
     * @param topic        the main Pub/Sub topic name
     * @param subscription the main Pub/Sub subscription name
     */
    @Builder
    public record MainProperties(
        String topic,
        String subscription
    ) {

        /**
         * Creates a new instance of MainProperties.
         *
         * @param topic        the main Pub/Sub topic name
         * @param subscription the main Pub/Sub subscription name
         */
        @ConstructorBinding
        public MainProperties(String topic, String subscription) {
            this.topic = requireNonNull(topic);
            this.subscription = requireNonNull(subscription);
        }
    }

    /**
     * Fallback properties for Google Cloud Pub/Sub.
     *
     * @param topic the fallback Pub/Sub topic name
     */
    @Builder
    public record FallbackProperties(
        String topic
    ) {

        /**
         * Creates a new instance of FallbackProperties.
         *
         * @param topic the fallback Pub/Sub topic name
         */
        @ConstructorBinding
        public FallbackProperties(String topic) {
            this.topic = requireNonNull(topic);
        }
    }
}
