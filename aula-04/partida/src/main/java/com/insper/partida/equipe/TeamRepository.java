package com.insper.partida.equipe;

// import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface TeamRepository extends MongoRepository<Team, String> {
    Team findByIdentifier(String identifier);
}
