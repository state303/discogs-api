package io.dsub.discogs.api.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

public abstract class BaseEntity<T> implements Persistable<T> {

    @CreatedDate
    @Column("created_at")
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column("last_modified_at")
    protected LocalDateTime lastModifiedAt;

    @Transient
    protected boolean isNew = false;

    @Override
    @Transient
    public boolean isNew() {
        return this.isNew;
    }

    private void setAsNew() {
        this.isNew = true;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdDate) {
        this.createdAt = createdDate;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedDate) {
        this.lastModifiedAt = lastModifiedDate;
    }

}
