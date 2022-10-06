package io.dsub.discogs.api.validator;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;

@RequiredArgsConstructor
public class ValidatorImpl implements Validator {

    private final javax.validation.Validator delegate;

    public <T> Mono<T> validate(T object) {
        return Mono.just(delegate.validate(object))
                .flatMap(violations -> {
                    if (violations.isEmpty()) {
                        return Mono.just(object);
                    } else {
                        return Mono.error(new ConstraintViolationException(violations));
                    }
                });
    }
}
