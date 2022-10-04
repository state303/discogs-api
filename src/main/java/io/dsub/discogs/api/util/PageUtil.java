package io.dsub.discogs.api.util;

import io.dsub.discogs.api.Constants;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public final class PageUtil {
    private PageUtil(){}
    public static Pageable getOrDefaultPageable(Pageable pageable) {
        if (pageable == null) {
            return PageRequest.of(Constants.DEFAULT_PAGE_INDEX, Constants.DEFAULT_PAGE_SIZE);
        }
        return pageable;
    }
}
