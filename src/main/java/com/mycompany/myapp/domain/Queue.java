package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.QueueStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Queue.
 */
@Document(collection = "queue")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Queue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("status")
    private QueueStatus status;

    @DBRef
    @Field("attencionChannel")
    private AttencionChannel attencionChannel;

    @DBRef
    @Field("company")
    @JsonIgnoreProperties(value = { "waitingRooms", "branches", "queues", "workers", "workerProfiles" }, allowSetters = true)
    private Company company;

    @DBRef
    @Field("branches")
    @JsonIgnoreProperties(value = { "company", "waitingRooms", "queues", "workers", "workerProfiles" }, allowSetters = true)
    private Set<Branch> branches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Queue id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Queue name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QueueStatus getStatus() {
        return this.status;
    }

    public Queue status(QueueStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(QueueStatus status) {
        this.status = status;
    }

    public AttencionChannel getAttencionChannel() {
        return this.attencionChannel;
    }

    public void setAttencionChannel(AttencionChannel attencionChannel) {
        this.attencionChannel = attencionChannel;
    }

    public Queue attencionChannel(AttencionChannel attencionChannel) {
        this.setAttencionChannel(attencionChannel);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Queue company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Set<Branch> getBranches() {
        return this.branches;
    }

    public void setBranches(Set<Branch> branches) {
        this.branches = branches;
    }

    public Queue branches(Set<Branch> branches) {
        this.setBranches(branches);
        return this;
    }

    public Queue addBranch(Branch branch) {
        this.branches.add(branch);
        branch.getQueues().add(this);
        return this;
    }

    public Queue removeBranch(Branch branch) {
        this.branches.remove(branch);
        branch.getQueues().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Queue)) {
            return false;
        }
        return id != null && id.equals(((Queue) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Queue{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
