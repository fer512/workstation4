package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AttencionChannel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the AttencionChannel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttencionChannelRepository extends MongoRepository<AttencionChannel, Long> {}
