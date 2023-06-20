package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WaitingRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the WaitingRoom entity.
 */
@Repository
public interface WaitingRoomRepository extends MongoRepository<WaitingRoom, Long> {
    @Query("{}")
    Page<WaitingRoom> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<WaitingRoom> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<WaitingRoom> findOneWithEagerRelationships(Long id);
}
