package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.AttencionChannelType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A AttencionChannel.
 */
@Document(collection = "attencion_channel")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttencionChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("type")
    private AttencionChannelType type;

    @DBRef
    private Queue queue;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AttencionChannel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AttencionChannel name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttencionChannelType getType() {
        return this.type;
    }

    public AttencionChannel type(AttencionChannelType type) {
        this.setType(type);
        return this;
    }

    public void setType(AttencionChannelType type) {
        this.type = type;
    }

    public Queue getQueue() {
        return this.queue;
    }

    public void setQueue(Queue queue) {
        if (this.queue != null) {
            this.queue.setAttencionChannel(null);
        }
        if (queue != null) {
            queue.setAttencionChannel(this);
        }
        this.queue = queue;
    }

    public AttencionChannel queue(Queue queue) {
        this.setQueue(queue);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttencionChannel)) {
            return false;
        }
        return id != null && id.equals(((AttencionChannel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttencionChannel{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
