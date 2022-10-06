package io.dsub.discogs.api.config;

import io.dsub.discogs.api.validator.Validator;
import io.dsub.discogs.api.validator.ValidatorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorConfig {
    @Bean
    public Validator reactiveValidator(javax.validation.Validator validator) {
        return new ValidatorImpl(validator);
    }
}
