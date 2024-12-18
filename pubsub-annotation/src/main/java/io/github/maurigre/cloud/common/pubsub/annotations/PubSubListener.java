package io.github.maurigre.cloud.common.pubsub.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
 * The interface Pub sub listener.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PubSubListener {
  /**
   * Subscription string.
   *
   * @return the string
   */
  String subscription();

  /**
   * Error handler string.
   *
   * @return the string
   */
  String errorHandler();
}
