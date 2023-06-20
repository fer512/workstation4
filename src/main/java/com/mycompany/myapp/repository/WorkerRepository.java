package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Worker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Worker entity.
 */
@Repository
public interface WorkerRepository extends MongoRepository<Worker, Long> {
    @Query("{}")
    Page<Worker> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Worker> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Worker> findOneWithEagerRelationships(Long id);
}
