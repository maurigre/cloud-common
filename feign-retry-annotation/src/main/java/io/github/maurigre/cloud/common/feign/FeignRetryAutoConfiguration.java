package io.github.maurigre.cloud.common.feign;

import io.github.maurigre.cloud.common.feign.config.FeignRetryAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.retry.support.RetryTemplate;


/**
 * The type Feign retry auto configuration.
 */
@EnableAspectJAutoProxy
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RetryTemplate.class)
public class FeignRetryAutoConfiguration implements EnvironmentAware {

    private Environment environment;


    /**
     * Feign retry aspect feign retry aspect.
     *
     * @return the feign retry aspect
     */
    @Bean
	public FeignRetryAspect feignRetryAspect() {
		return new FeignRetryAspect(this.environment);
	}

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
