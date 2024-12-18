package io.github.maurigre.cloud.common.feign.annotations;

import io.github.maurigre.cloud.common.feign.FeignRetryAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * The interface Enable feign retry.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(FeignRetryAutoConfiguration.class)
public @interface EnableFeignRetry {
}
