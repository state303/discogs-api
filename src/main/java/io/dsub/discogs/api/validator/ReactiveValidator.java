package io.dsub.discogs.api.validator;

import reactor.core.publisher.Mono;

public interface ReactiveValidator {
    <T> Mono<T> validate(T object, Class<?>... groups);
}
