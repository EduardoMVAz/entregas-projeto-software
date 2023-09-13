package com.insper.partida.game;

import com.insper.partida.equipe.Team;
import com.insper.partida.equipe.TeamService;
import com.insper.partida.equipe.dto.SaveTeamDTO;
import com.insper.partida.game.dto.EditGameDTO;
import com.insper.partida.game.dto.GameReturnDTO;
import com.insper.partida.game.dto.SaveGameDTO;
import com.insper.partida.tabela.Tabela;
import com.insper.partida.tabela.TabelaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TabelaService tabelaService;

    public Page<GameReturnDTO> listGames(String home, String away, Integer attendance, Pageable pageable) {
        if (home != null && away != null) {

            Team tHome = teamService.getTeam(home);
            Team tAway = teamService.getTeam(away);

            Page<Game> games = gameRepository.findByHomeAndAway(tHome.getIdentifier(), tAway.getIdentifier(), pageable);
            return games.map(game -> GameReturnDTO.covert(game));

        } else if (attendance != null) {
            Page<Game> games =  gameRepository.findByAttendanceGreaterThan(attendance, pageable);
            return games.map(game -> GameReturnDTO.covert(game));
        }
        Page<Game> games =  gameRepository.findAll(pageable);
        return games.map(game -> GameReturnDTO.covert(game));
    }

    public GameReturnDTO saveGame(SaveGameDTO saveGameDTO) {

        Team teamM = teamService.getTeam(saveGameDTO.getHome());
        Team teamV = teamService.getTeam(saveGameDTO.getAway());

        if (teamM == null || teamV == null) {
            return null;
        }

        Game game = new Game();
        game.setIdentifier(UUID.randomUUID().toString());
        game.setHome(teamM.getIdentifier());
        game.setAway(teamV.getIdentifier());
        game.setAttendance(0);
        game.setScoreHome(0);
        game.setScoreAway(0);
        game.setGameDate(LocalDateTime.now());
        game.setStatus("SCHEDULED");

        gameRepository.save(game);
        return GameReturnDTO.covert(game);

    }

    public GameReturnDTO editGame(String identifier, EditGameDTO editGameDTO) {
        // Edita o Jogo
        Game gameBD = gameRepository.findByIdentifier(identifier);

        gameBD.setScoreAway(editGameDTO.getScoreAway());
        gameBD.setScoreHome(editGameDTO.getScoreHome());
        gameBD.setAttendance(editGameDTO.getAttendance());
        gameBD.setStatus("FINISHED");

        Game game = gameRepository.save(gameBD);

        // Atualiza Tabelas

        // Tabela do time da casa
        String timeAway = gameBD.getAway();
        Tabela tabelaAway = tabelaService.getTabela(timeAway);

        tabelaAway.setPontos(tabelaAway.getPontos() + verificaResultado(timeAway, game));
        tabelaAway.setVitorias(tabelaAway.getVitorias() + (verificaVitorias(timeAway, game) ? 1 : 0));
        tabelaAway.setDerrotas(tabelaAway.getDerrotas() + (verificaDerrotas(timeAway, game) ? 1 : 0));
        tabelaAway.setEmpates(tabelaAway.getEmpates() + (verificaEmpates(timeAway, game) ? 1 : 0));
        tabelaAway.setGolsPro(tabelaAway.getGolsPro() + verificaGolsPro(timeAway, game));
        tabelaAway.setGolsContra(tabelaAway.getGolsContra()  + verificaGolsContra(timeAway, game));
        tabelaAway.setJogos(tabelaAway.getJogos() + 1);
        tabelaService.saveTabela(tabelaAway);

        // Tabela do time de fora
        String timeHome = game.getHome();
        Tabela tabelaHome = tabelaService.getTabela(timeHome);

        tabelaHome.setPontos(tabelaHome.getPontos() + verificaResultado(timeHome, game));
        tabelaHome.setVitorias(tabelaHome.getVitorias() + (verificaVitorias(timeHome, game) ? 1 : 0));
        tabelaHome.setDerrotas(tabelaHome.getDerrotas() + (verificaDerrotas(timeHome, game) ? 1 : 0));
        tabelaHome.setEmpates(tabelaHome.getEmpates() + (verificaEmpates(timeHome, game) ? 1 : 0));
        tabelaHome.setGolsPro(tabelaHome.getGolsPro() + verificaGolsPro(timeHome, game));
        tabelaHome.setGolsContra(tabelaHome.getGolsContra()  + verificaGolsContra(timeHome, game));
        tabelaHome.setJogos(tabelaHome.getJogos() + 1);
        tabelaService.saveTabela(tabelaHome);

        return GameReturnDTO.covert(game);
    }

    public void deleteGame(String identifier) {
        Game gameBD = gameRepository.findByIdentifier(identifier);
        if (gameBD != null) {
            gameRepository.delete(gameBD);
        }
    }

    public Integer getScoreTeam(String identifier) {
        return tabelaService.getTabela(identifier).getPontos();
    }

    public GameReturnDTO getGame(String identifier) {
        return GameReturnDTO.covert(gameRepository.findByIdentifier(identifier));
    }

    public void generateData() {

        String [] teams = {"botafogo", "palmeiras", "gremio", "flamengo", "fluminense", "bragantino", "atletico-mg", "athletico-pr", "fortaleza", "cuiaba", "sao-paulo",
                        "internacional", "cruzeiro", "corinthians", "goias", "bahia", "santos", "vasco", "coritiba", "america-mg"};

        for (String team : teams) {
            SaveTeamDTO saveTeamDTO = new SaveTeamDTO();
            saveTeamDTO.setName(team);
            saveTeamDTO.setStadium(team);
            saveTeamDTO.setIdentifier(team);

            Team teamDB = teamService.getTeam(team);
            if (teamDB == null) {
                teamService.saveTeam(saveTeamDTO);
            }
        }

        for (int i = 0; i < 1000; i++) {

            Integer team1 = new Random().nextInt(20);
            Integer team2 = new Random().nextInt(20);
            while  (team1 == team2  ) {
                team2 = new Random().nextInt(20);
            }

            Game game = new Game();
            EditGameDTO editGameDTO = new EditGameDTO();
            game.setIdentifier(UUID.randomUUID().toString());
            game.setHome(teams[team1]);
            game.setAway(teams[team2]);
            game.setStadium(teams[team1]);
            gameRepository.save(game);


            // atualiza por meio do método editGame, para atualizar a tabela
            editGameDTO.setScoreHome(new Random().nextInt(4));
            editGameDTO.setScoreAway(new Random().nextInt(4));
            editGameDTO.setAttendance(new Random().nextInt(4) * 1000);
            editGame(game.getIdentifier(), editGameDTO);
        }
    }

    public List<Game> getGameByTeam(String identifier) {
        return gameRepository.findByHomeOrAway(identifier, identifier);
    }

    private Integer verificaResultado(String time, Game game) {
        if (game.getScoreHome() == game.getScoreAway()) {
            return 1;
        }
        if (game.getHome().equals(time) && game.getScoreHome() > game.getScoreAway()) {
            return 3;
        }
        if (game.getAway().equals(time) && game.getScoreAway() > game.getScoreHome()) {
            return 3;
        }
        return 0;
    }

    private Integer verificaGolsPro(String time, Game game) {
        if (game.getHome().equals(time)) {
            return game.getScoreHome();
        }
        return game.getScoreAway();
    }

    private Integer verificaGolsContra(String time, Game game) {
        if (game.getHome().equals(time)) {
            return game.getScoreAway();
        }
        return game.getScoreHome();
    }

    private boolean verificaVitorias(String time, Game game) {
        if (game.getHome().equals(time) && game.getScoreHome() > game.getScoreAway()) {
            return true;
        }
        if (game.getAway().equals(time) && game.getScoreAway() > game.getScoreHome()) {
            return true;
        }
        return false;
    }

    private boolean verificaDerrotas(String time, Game game) {
        if (game.getHome().equals(time) && game.getScoreHome() < game.getScoreAway()) {
            return true;
        }
        if (game.getAway().equals(time) && game.getScoreAway() < game.getScoreHome()) {
            return true;
        }
        return false;
    }

    private boolean verificaEmpates(String time, Game game) {
        if (game.getScoreHome() == game.getScoreAway()) {
            return true;
        }
        return false;
    }
}
