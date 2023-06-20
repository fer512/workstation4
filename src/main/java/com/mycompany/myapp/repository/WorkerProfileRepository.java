package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WorkerProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the WorkerProfile entity.
 */
@Repository
public interface WorkerProfileRepository extends MongoRepository<WorkerProfile, Long> {
    @Query("{}")
    Page<WorkerProfile> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<WorkerProfile> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<WorkerProfile> findOneWithEagerRelationships(Long id);
}
