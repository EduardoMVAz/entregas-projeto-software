package com.insper.partida.aposta;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface BetRespository extends MongoRepository<Bet, String> {
    List<Bet> findByGameIdentifier(String gameId);
}
