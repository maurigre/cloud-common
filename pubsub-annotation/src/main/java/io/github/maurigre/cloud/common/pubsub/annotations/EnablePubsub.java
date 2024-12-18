package io.github.maurigre.cloud.common.pubsub.annotations;

import io.github.maurigre.cloud.common.pubsub.PubsubAutoConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;


/**
 * The interface Enable pubsub.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(PubsubAutoConfiguration.class)
public @interface EnablePubsub {
}
