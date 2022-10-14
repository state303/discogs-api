package io.dsub.discogs.api.core.entity;

import lombok.Builder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

public abstract class PersistableBaseEntity<T> implements Persistable<T> {
    @Transient
    protected boolean isNew = false;

    @Override
    @Transient
    public boolean isNew() {
        return this.isNew;
    }

    public void setAsNew() {
        this.isNew = true;
    }
}