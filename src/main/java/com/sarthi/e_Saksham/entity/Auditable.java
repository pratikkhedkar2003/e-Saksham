package com.sarthi.e_Saksham.entity;

import com.sarthi.e_Saksham.domain.LoggedInUser;
import com.sarthi.e_Saksham.domain.RequestContext;
import com.sarthi.e_Saksham.exception.ESakshamApiException;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.Objects;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
    @NotNull
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @NotNull
    @Column(name = "updated_by")
    private Long updatedBy;

    @NotNull
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @PrePersist
    public void beforePersist() {
        LoggedInUser loggedInUser = RequestContext.getLoggedInUser();
        if(Objects.isNull(loggedInUser)) {
            throw new ESakshamApiException("Cannot persist entity without user ID in RequestContext for this thread");
        }
        setCreatedBy(loggedInUser.userId());
        setUpdatedBy(loggedInUser.userId());
    }

    @PreUpdate
    public void beforeUpdate() {
        LoggedInUser loggedInUser = RequestContext.getLoggedInUser();
        if(Objects.isNull(loggedInUser)) {
            throw new ESakshamApiException("Cannot update entity without user ID in RequestContext for this thread");
        }
        setUpdatedBy(loggedInUser.userId());
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
