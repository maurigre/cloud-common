package io.github.mgr.cloud.feign.feign;

import io.github.mgr.cloud.feign.feign.config.FeignRetryAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.retry.support.RetryTemplate;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RetryTemplate.class)
public class FeignRetryAutoConfiguration implements EnvironmentAware {

    private Environment environment;

    @Bean
	public FeignRetryAspect feignRetryAspect() {
		return new FeignRetryAspect(this.environment);
	}

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
