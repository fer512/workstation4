package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A OrderQueue.
 */
@Document(collection = "order_queue")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderQueue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull
    @Field("order")
    private Long order;

    @DBRef
    @Field("queue")
    @JsonIgnoreProperties(value = { "attencionChannel", "company", "branches" }, allowSetters = true)
    private Queue queue;

    @DBRef
    @Field("workerProfile")
    @JsonIgnoreProperties(value = { "attencionChannel", "callableQueues", "company", "branches" }, allowSetters = true)
    private WorkerProfile workerProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderQueue id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrder() {
        return this.order;
    }

    public OrderQueue order(Long order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Queue getQueue() {
        return this.queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public OrderQueue queue(Queue queue) {
        this.setQueue(queue);
        return this;
    }

    public WorkerProfile getWorkerProfile() {
        return this.workerProfile;
    }

    public void setWorkerProfile(WorkerProfile workerProfile) {
        this.workerProfile = workerProfile;
    }

    public OrderQueue workerProfile(WorkerProfile workerProfile) {
        this.setWorkerProfile(workerProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderQueue)) {
            return false;
        }
        return id != null && id.equals(((OrderQueue) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderQueue{" +
            "id=" + getId() +
            ", order=" + getOrder() +
            "}";
    }
}
