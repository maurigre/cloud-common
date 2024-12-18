package io.github.maurigre.cloud.common.feign.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Backoff.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Backoff {

    /**
     * Delay string.
     *
     * @return the string
     */
    String delay() default "1000";

    /**
     * Max delay string.
     *
     * @return the string
     */
    String maxDelay() default "0";

    /**
     * Multiplier string.
     *
     * @return the string
     */
    String multiplier() default "0.0";
}
