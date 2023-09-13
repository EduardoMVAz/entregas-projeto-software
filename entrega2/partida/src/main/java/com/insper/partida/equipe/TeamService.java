package com.insper.partida.equipe;

import com.insper.partida.equipe.dto.SaveTeamDTO;
import com.insper.partida.equipe.dto.TeamReturnDTO;
import com.insper.partida.tabela.Tabela;
import com.insper.partida.tabela.TabelaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class  TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired 
    private TabelaRepository tabelaRepository;


    public List<TeamReturnDTO> listTeams() {
        return teamRepository.findAll().stream().map(team -> TeamReturnDTO.covert(team)).collect(Collectors.toList());
    }

    public TeamReturnDTO saveTeam(SaveTeamDTO saveTeam) {
        if (!teamRepository.existsByIdentifier(saveTeam.getIdentifier())) {
            Team team = new Team();
            team.setName(saveTeam.getName());
            team.setIdentifier(saveTeam.getIdentifier());

            // cria a tabela aqui
            Tabela tabela = new Tabela();
            tabela.setTime(team.getIdentifier());
            tabelaRepository.save(tabela);

            
            team = teamRepository.save(team);
            return  TeamReturnDTO.covert(team);
        }
        return null;
    }


    public void deleteTeam(String identifier) {

        Team team = teamRepository.findByIdentifier(identifier);
        if (team != null) {
            teamRepository.delete(team);
        }

    }

    public Team getTeam(String identifier) {
        return teamRepository.findByIdentifier(identifier);
    }
}
