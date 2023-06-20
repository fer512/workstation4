package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.OrderQueue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the OrderQueue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderQueueRepository extends MongoRepository<OrderQueue, Long> {}
