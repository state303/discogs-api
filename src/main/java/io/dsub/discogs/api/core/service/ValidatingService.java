package io.dsub.discogs.api.core.service;

import reactor.core.publisher.Mono;

public interface ValidatingService {
    <T> Mono<T> validate(T item);
}