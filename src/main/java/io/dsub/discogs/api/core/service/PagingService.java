package io.dsub.discogs.api.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

public interface PagingService {
    default <T> Mono<Page<T>> getPagedResult(Mono<Long> count, Pageable pageable, Flux<T> items) {
        return count.flatMap(c -> items
                        .buffer(pageable.getPageSize(), pageable.getPageNumber() + 1)
                        .elementAt(pageable.getPageNumber(), new ArrayList<>())
                        .map(bufferedItems -> new PageImpl<>(bufferedItems, pageable, c)));
    }
}