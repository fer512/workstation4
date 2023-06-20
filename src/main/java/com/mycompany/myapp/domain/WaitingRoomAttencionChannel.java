package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.WaitingRoomAttencionChannelType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A WaitingRoomAttencionChannel.
 */
@Document(collection = "waiting_room_attencion_channel")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WaitingRoomAttencionChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("type")
    private WaitingRoomAttencionChannelType type;

    @DBRef
    private WaitingRoom waitingRoom;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WaitingRoomAttencionChannel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public WaitingRoomAttencionChannel name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WaitingRoomAttencionChannelType getType() {
        return this.type;
    }

    public WaitingRoomAttencionChannel type(WaitingRoomAttencionChannelType type) {
        this.setType(type);
        return this;
    }

    public void setType(WaitingRoomAttencionChannelType type) {
        this.type = type;
    }

    public WaitingRoom getWaitingRoom() {
        return this.waitingRoom;
    }

    public void setWaitingRoom(WaitingRoom waitingRoom) {
        if (this.waitingRoom != null) {
            this.waitingRoom.setAttencionChannel(null);
        }
        if (waitingRoom != null) {
            waitingRoom.setAttencionChannel(this);
        }
        this.waitingRoom = waitingRoom;
    }

    public WaitingRoomAttencionChannel waitingRoom(WaitingRoom waitingRoom) {
        this.setWaitingRoom(waitingRoom);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WaitingRoomAttencionChannel)) {
            return false;
        }
        return id != null && id.equals(((WaitingRoomAttencionChannel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WaitingRoomAttencionChannel{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
