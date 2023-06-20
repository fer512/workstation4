package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.WaitingRoomStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A WaitingRoom.
 */
@Document(collection = "waiting_room")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WaitingRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("status")
    private WaitingRoomStatus status;

    @DBRef
    @Field("attencionChannel")
    private WaitingRoomAttencionChannel attencionChannel;

    @DBRef
    @Field("worker")
    @JsonIgnoreProperties(value = { "company", "branches", "waitingRoom" }, allowSetters = true)
    private Set<Worker> workers = new HashSet<>();

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

    public WaitingRoom id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public WaitingRoom name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WaitingRoomStatus getStatus() {
        return this.status;
    }

    public WaitingRoom status(WaitingRoomStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(WaitingRoomStatus status) {
        this.status = status;
    }

    public WaitingRoomAttencionChannel getAttencionChannel() {
        return this.attencionChannel;
    }

    public void setAttencionChannel(WaitingRoomAttencionChannel waitingRoomAttencionChannel) {
        this.attencionChannel = waitingRoomAttencionChannel;
    }

    public WaitingRoom attencionChannel(WaitingRoomAttencionChannel waitingRoomAttencionChannel) {
        this.setAttencionChannel(waitingRoomAttencionChannel);
        return this;
    }

    public Set<Worker> getWorkers() {
        return this.workers;
    }

    public void setWorkers(Set<Worker> workers) {
        if (this.workers != null) {
            this.workers.forEach(i -> i.setWaitingRoom(null));
        }
        if (workers != null) {
            workers.forEach(i -> i.setWaitingRoom(this));
        }
        this.workers = workers;
    }

    public WaitingRoom workers(Set<Worker> workers) {
        this.setWorkers(workers);
        return this;
    }

    public WaitingRoom addWorker(Worker worker) {
        this.workers.add(worker);
        worker.setWaitingRoom(this);
        return this;
    }

    public WaitingRoom removeWorker(Worker worker) {
        this.workers.remove(worker);
        worker.setWaitingRoom(null);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public WaitingRoom company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Set<Branch> getBranches() {
        return this.branches;
    }

    public void setBranches(Set<Branch> branches) {
        this.branches = branches;
    }

    public WaitingRoom branches(Set<Branch> branches) {
        this.setBranches(branches);
        return this;
    }

    public WaitingRoom addBranch(Branch branch) {
        this.branches.add(branch);
        branch.getWaitingRooms().add(this);
        return this;
    }

    public WaitingRoom removeBranch(Branch branch) {
        this.branches.remove(branch);
        branch.getWaitingRooms().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WaitingRoom)) {
            return false;
        }
        return id != null && id.equals(((WaitingRoom) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WaitingRoom{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
