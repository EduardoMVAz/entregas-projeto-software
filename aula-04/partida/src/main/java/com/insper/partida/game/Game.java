package com.insper.partida.game;

import com.insper.partida.equipe.Team;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Document("game")
public class Game {

    @Id
    private String id;

    private String identifier;

    private Integer scoreHome;

    private Integer scoreAway;

    private Team home;

    private Team away;

    private Integer attendance;

    private LocalDateTime gameDate;

    private String status;

}
