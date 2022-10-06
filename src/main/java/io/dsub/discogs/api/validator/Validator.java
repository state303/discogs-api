package io.dsub.discogs.api.validator;

import reactor.core.publisher.Mono;

public interface Validator {
    <T> Mono<T> validate(T object);
}
