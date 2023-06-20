package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Queue;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Queue entity.
 */
@Repository
public interface QueueRepository extends MongoRepository<Queue, Long> {
    @Query("{}")
    Page<Queue> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Queue> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Queue> findOneWithEagerRelationships(Long id);
}
