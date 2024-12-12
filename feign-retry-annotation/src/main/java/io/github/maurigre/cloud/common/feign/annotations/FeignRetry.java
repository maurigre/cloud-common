package io.github.maurigre.cloud.common.feign.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * The interface Feign retry.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FeignRetry {

    /**
     * Backoff backoff.
     *
     * @return the backoff
     */
    Backoff backoff() default @Backoff();

    /**
     * Max attempt string.
     *
     * @return the string
     */
    String maxAttempt() default "3";

    /**
     * Include class [ ].
     *
     * @return the class [ ]
     */
    Class<? extends Throwable>[] include() default {};
}
