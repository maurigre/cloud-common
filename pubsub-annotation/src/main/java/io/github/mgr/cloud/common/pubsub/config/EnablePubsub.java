package io.github.mgr.cloud.common.pubsub.config;

import io.github.mgr.cloud.common.pubsub.PubsubAutoConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(PubsubAutoConfiguration.class)
public @interface EnablePubsub {
}
