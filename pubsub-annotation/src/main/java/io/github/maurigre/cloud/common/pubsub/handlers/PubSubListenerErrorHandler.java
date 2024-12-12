package io.github.maurigre.cloud.common.pubsub.handlers;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.pubsub.v1.PubsubMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

/**
 * The interface Pub sub listener error handler.
 */
@FunctionalInterface
public interface PubSubListenerErrorHandler {

  /**
   * Handle error void.
   *
   * @param message      the message
   * @param consumer     the consumer
   * @param subscription the subscription
   * @param exception    the exception
   */
  void handleError(@NotNull final PubsubMessage message,
                   @NotNull final AckReplyConsumer consumer,
                   @NotBlank final String subscription,
                   final Exception exception);

}
