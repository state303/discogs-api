package io.dsub.discogs.api.dto;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

public abstract class BaseDTO {

    @LastModifiedDate
    @Column("last_modified_at")
    protected LocalDateTime lastModifiedAt;

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedDate) {
        this.lastModifiedAt = lastModifiedDate;
    }
}
