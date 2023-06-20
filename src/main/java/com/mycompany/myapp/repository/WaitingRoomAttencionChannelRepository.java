package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WaitingRoomAttencionChannel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the WaitingRoomAttencionChannel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WaitingRoomAttencionChannelRepository extends MongoRepository<WaitingRoomAttencionChannel, Long> {}
