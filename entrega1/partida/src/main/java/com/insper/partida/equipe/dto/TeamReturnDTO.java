package com.insper.partida.equipe.dto;

import com.insper.partida.equipe.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamReturnDTO {
    private String identifier;
    private String name;

    public static TeamReturnDTO covert(Team team) {
        TeamReturnDTO teamReturnDTO = new TeamReturnDTO();
        teamReturnDTO.setIdentifier(team.getIdentifier());
        teamReturnDTO.setName(team.getName());
        return teamReturnDTO;
    }
}
