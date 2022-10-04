package io.dsub.discogs.api.config;

import io.dsub.discogs.api.validator.ReactiveValidator;
import io.dsub.discogs.api.validator.ReactiveValidatorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validator;

@Configuration
public class ValidatorConfig {
    @Bean
    public ReactiveValidator reactiveValidator(Validator validator) {
        return new ReactiveValidatorImpl(validator);
    }
}
