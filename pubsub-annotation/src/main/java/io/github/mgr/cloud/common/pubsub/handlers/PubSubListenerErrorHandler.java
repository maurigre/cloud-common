package io.github.mgr.cloud.common.pubsub.handlers;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.pubsub.v1.PubsubMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@FunctionalInterface
public interface PubSubListenerErrorHandler {

  void handleError(@NotNull final PubsubMessage message,
                   @NotNull final AckReplyConsumer consumer,
                   @NotBlank final String subscription,
                   final Exception exception);

}
