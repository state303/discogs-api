package io.dsub.discogs.api.validator;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

@RequiredArgsConstructor
public class ReactiveValidatorImpl implements ReactiveValidator {
    private final Validator validator;

    @Override
    public <T> Mono<T> validate(T object, Class<?>... groups) {
        return Mono.just(validator.validate(object, groups))
                .flatMap(violations -> {
                    if (violations.isEmpty()) {
                        return Mono.just(object);
                    } else {
                        return Mono.error(new ConstraintViolationException(violations));
                    }
                });
    }
}
