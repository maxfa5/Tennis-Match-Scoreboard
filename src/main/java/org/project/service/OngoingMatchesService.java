package org.project.service;

import org.project.DTO.CreateMatchDTO;
import org.project.DTO.IncrementScoreDTO;
import org.project.model.FinishedMatch;
import org.project.model.OngoingMatch;
import org.project.model.Player;
import org.project.repository.FinishedMatchRepository;
import org.project.repository.PLayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class OngoingMatchesService {
    private HashMap<UUID, OngoingMatch> ongoingMatches = new HashMap<>();
    private PLayerRepository playerRepository;
    private FinishedMatchRepository finishedMatchRepository;
    private static final int PAGE_SIZE = 10;
    @Autowired
    public OngoingMatchesService(PLayerRepository playerRepository, FinishedMatchRepository finishedMatchRepository) {
        this.playerRepository = playerRepository;
        this.finishedMatchRepository = finishedMatchRepository;
    }
    
    public OngoingMatch getOngoingMatch(UUID matchId) {
        return ongoingMatches.get(matchId);
    }

    public OngoingMatch createNewMatch(CreateMatchDTO createMatchDTO) {
        Player player1 = playerRepository.findByName(createMatchDTO.getPlayerName1());
        if (player1 == null) {
            player1 = new Player(createMatchDTO.getPlayerName1());
            player1 = playerRepository.save(player1);
        }

        Player player2 = playerRepository.findByName(createMatchDTO.getPlayerName2());
        if (player2 == null) {
            player2 = new Player(createMatchDTO.getPlayerName2());
            player2 = playerRepository.save(player2);
        }
        
        OngoingMatch match = new OngoingMatch(player1.getName(), player2.getName());
        ongoingMatches.put(match.getId(), match);
        return match;
    }

    public boolean IncrementScore(IncrementScoreDTO incrementScoreDTO) {
        OngoingMatch match = getOngoingMatch(incrementScoreDTO.getMatchId());
        if (match == null) {
            throw new RuntimeException("Match not found");
        }

        match.setScorePlayer1(match.getScorePlayer1() + incrementScoreDTO.getAddToPLayer1());
        match.setScorePlayer2(match.getScorePlayer2() + incrementScoreDTO.getAddToPLayer2());
        System.out.println("Score updated - Player1: " + incrementScoreDTO.getAddToPLayer1() + ", Player2: " + incrementScoreDTO.getAddToPLayer2());

        if (checkIfMatchIsOver(match)) {
            Player player1 = playerRepository.findByName(match.getPlayer1Name());
            Player player2 = playerRepository.findByName(match.getPlayer2Name());
            Player winner = playerRepository.findByName(solveWinnerName(match));
            
            FinishedMatch finishedMatch = new FinishedMatch(player1, player2, winner);
            finishedMatchRepository.save(finishedMatch);
            ongoingMatches.remove(match.getId());
            return true;
        }
        return false;
    }

    private boolean checkIfMatchIsOver(OngoingMatch match) {
        return match.getScorePlayer1() >= 11 || match.getScorePlayer2() >= 11;
    }

    private String solveWinnerName(OngoingMatch match) {
        if (match.getScorePlayer1() >= 11) {
            return match.getPlayer1Name();
        } else if (match.getScorePlayer2() >= 11) {
            return match.getPlayer2Name();
        }
        return null;
    }

    public List<OngoingMatch> getOngoingMatches(int number, String playerName) {
        return ongoingMatches.values().stream()
        .filter(match -> match.getPlayer1Name().equals(playerName) || match.getPlayer2Name().equals(playerName))
        .skip((number - 1) * PAGE_SIZE)
        .limit(PAGE_SIZE)
        .toList();
    }
}
