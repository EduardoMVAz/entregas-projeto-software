package com.insper.partida.aposta;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Document("Bet")
public class Bet {

    @Id
    private String id;
    private BetStatus status;
    private BetResult result;

    private String gameIdentifier;

}
