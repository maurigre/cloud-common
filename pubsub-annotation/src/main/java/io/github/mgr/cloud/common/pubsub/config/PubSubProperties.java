package io.github.mgr.cloud.common.pubsub.config;

import static java.util.Objects.requireNonNull;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Builder
@ConfigurationProperties(prefix = "google.cloud.pubsub")
public record PubSubProperties(
    String projectId,
    Integer maxRetryAttempts,
    MainProperties main,
    FallbackProperties fallback
) {

  @ConstructorBinding
  public PubSubProperties(String projectId, Integer maxRetryAttempts, MainProperties main, FallbackProperties fallback) {
    this.projectId = requireNonNull(projectId);
    this.maxRetryAttempts = requireNonNull(maxRetryAttempts);
    this.main = requireNonNull(main);
    this.fallback = requireNonNull(fallback);
  }

  @Builder
  public record MainProperties(
      String topic,
      String subscription
  ) {

    @ConstructorBinding
    public MainProperties(String topic, String subscription) {
      this.topic = requireNonNull(topic);
      this.subscription = requireNonNull(subscription);
    }
  }

  @Builder
  public record FallbackProperties(
      String topic
  ) {

    @ConstructorBinding
    public FallbackProperties(String topic) {
      this.topic = requireNonNull(topic);
    }
  }
}
