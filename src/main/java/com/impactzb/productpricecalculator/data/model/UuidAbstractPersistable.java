package com.impactzb.productpricecalculator.data.model;

import org.springframework.data.domain.Persistable;
import org.springframework.data.util.ProxyUtils;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.UUID;

@MappedSuperclass
public abstract class UuidAbstractPersistable implements Persistable<UUID> {
    @Id
    @GeneratedValue
    @Column(name="id", columnDefinition = "uuid", unique=true, nullable=false)
    private UUID id;

    protected UuidAbstractPersistable() {
    }

    @Nullable
    public UUID getId() {
        return this.id;
    }

    protected void setId(@Nullable UUID id) {
        this.id = id;
    }

    @Transient
    public boolean isNew() {
        return null == this.getId();
    }

    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), this.getId());
    }

    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!this.getClass().equals(ProxyUtils.getUserClass(obj))) {
            return false;
        } else {
            UuidAbstractPersistable that = (UuidAbstractPersistable) obj;
            return null != this.getId() && this.getId().equals(that.getId());
        }
    }

    public int hashCode() {
        int hashCode = 17;
        hashCode += null == this.getId() ? 0 : this.getId().hashCode() * 31;
        return hashCode;
    }
}