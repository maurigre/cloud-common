package io.github.mgr.cloud.common.feign.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FeignRetry {

  Backoff backoff() default @Backoff();

  String maxAttempt() default "3";

  Class<? extends Throwable>[] include() default {};
}
