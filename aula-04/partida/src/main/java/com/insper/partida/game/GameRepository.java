package com.insper.partida.game;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {
    Game findByIdentifier(String identifier);

    Page<Game> findByHomeAndAway(String tHome, String tAway, Pageable pageable);

    Page<Game> findByAttendanceGreaterThan(Integer attendance, Pageable pageable);
}
