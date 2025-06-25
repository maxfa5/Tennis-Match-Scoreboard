package org.project.service;

import org.project.DTO.IncrementScoreDTO;
import org.project.model.OngoingMatch;
import org.project.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchScoreCalculationService {
    private final OngoingMatchesService ongoingMatchesService;
    private final PlayerRepository playerRepository;
    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService;
    @Autowired
    MatchScoreCalculationService(OngoingMatchesService ongoingMatchesService, PlayerRepository playerRepository, FinishedMatchesPersistenceService finishedMatchesPersistenceService) {
        this.ongoingMatchesService = ongoingMatchesService;
        this.playerRepository = playerRepository;
        this.finishedMatchesPersistenceService = finishedMatchesPersistenceService;
    }
    
    public boolean IncrementScore(IncrementScoreDTO incrementScoreDTO) {
        boolean isTiebreak = isTiebreak(ongoingMatchesService.getOngoingMatch(incrementScoreDTO.getMatchId()));

        OngoingMatch match = ongoingMatchesService.getOngoingMatch(incrementScoreDTO.getMatchId());
        if (match == null) {
            throw new RuntimeException("Match not found");
        }

        // Check if we need to start a tiebreak
        if (!isTiebreak && match.getCountGamesPlayer1() == 6 && match.getCountGamesPlayer2() == 6) {
            isTiebreak = true;

        }

        if (isTiebreak) {
            // Handle tiebreak scoring
            if (incrementScoreDTO.getAddToPLayer1() == 1) {
                match.setScorePlayer1(match.getScorePlayer1() + 1);
            } else if (incrementScoreDTO.getAddToPLayer2() == 1) {
                match.setScorePlayer2(match.getScorePlayer2() + 1);
            }

            // Check for tiebreak win
            if ((match.getScorePlayer1() >= 7 && match.getScorePlayer1() - match.getScorePlayer2() >= 2) ||
                (match.getScorePlayer2() >= 7 && match.getScorePlayer2() - match.getScorePlayer1() >= 2)) {
                // Award the set to the winner
                if (match.getScorePlayer1() > match.getScorePlayer2()) {
                    match.setCountSetsPlayer1(match.getCountSetsPlayer1() + 1);
                } else {
                    match.setCountSetsPlayer2(match.getCountSetsPlayer2() + 1);
                }
                // Reset scores and games for the new set
                isTiebreak = false;
                match.setScorePlayer1(0);
                match.setScorePlayer2(0);
                match.setCountGamesPlayer1(0);
                match.setCountGamesPlayer2(0);
            }
        } else {
            int scoreVal1PLayer = match.getScorePlayer1();
            int scoreVal2PLayer = match.getScorePlayer2();
            //score
            if ((scoreVal1PLayer==0|| scoreVal1PLayer==15) && incrementScoreDTO.getAddToPLayer1()==1) {
                match.setScorePlayer1( scoreVal1PLayer + 15);
            }else if ((scoreVal2PLayer==0 || scoreVal2PLayer==15) && incrementScoreDTO.getAddToPLayer2()==1) {
                match.setScorePlayer2( scoreVal2PLayer + 15);
            }else if (incrementScoreDTO.getAddToPLayer1()==1) {
                match.setScorePlayer1( scoreVal1PLayer + 10);
            }else if (incrementScoreDTO.getAddToPLayer2()==1) {
                match.setScorePlayer2( scoreVal2PLayer + 10);
            }
        }

        //games
        if(!isTiebreak && match.getScorePlayer1() > 40 &&(match.getScorePlayer1() - match.getScorePlayer2() >= 20) ){
            match.setCountGamesPlayer1(match.getCountGamesPlayer1() + 1);
            match.setScorePlayer1(0);
            match.setScorePlayer2(0);
        }else if(!isTiebreak && match.getScorePlayer2() > 40 &&(match.getScorePlayer2() - match.getScorePlayer1() >= 20) ){
            match.setCountGamesPlayer2(match.getCountGamesPlayer2() + 1);
            match.setScorePlayer1(0);
            match.setScorePlayer2(0);
        }
        //sets
        if ((match.getCountGamesPlayer1() > 6 && (match.getCountGamesPlayer1() - match.getCountGamesPlayer2() >= 2))){
            match.setCountSetsPlayer1(match.getCountSetsPlayer1() + 1);
            match.setScorePlayer1(0);
            match.setScorePlayer2(0);
            match.setCountGamesPlayer1(0);
            match.setCountGamesPlayer2(0);
        }else if ((match.getCountGamesPlayer2() > 6 && (match.getCountGamesPlayer2() - match.getCountGamesPlayer1() >= 2))){
            match.setCountSetsPlayer2(match.getCountSetsPlayer2() + 1);
            match.setScorePlayer1(0);
            match.setScorePlayer2(0);
            match.setCountGamesPlayer1(0);
            match.setCountGamesPlayer2(0);
        }

        if (checkIfMatchIsOver(match)) {
            finishedMatchesPersistenceService.finishMatch(match, playerRepository.findByName(solveWinnerName(match)));
            return true;
        }
        return false;
    }
    public boolean isTiebreak(OngoingMatch match) {
        if (match.getCountGamesPlayer1() >= 6 && match.getCountGamesPlayer2() >= 6 && (match.getCountGamesPlayer1() - match.getCountGamesPlayer2() < 2 && match.getCountGamesPlayer2() - match.getCountGamesPlayer1() < 2)) {
            return true;
        }
        return false;
    }

    public void setGames(OngoingMatch match, int countGamesPlayer1, int countGamesPlayer2) {
        match.setCountGamesPlayer1(countGamesPlayer1);
        match.setCountGamesPlayer2(countGamesPlayer2);
    }


    private boolean checkIfMatchIsOver(OngoingMatch match) {
        if ((match.getCountSetsPlayer1() > 1 || (match.getCountSetsPlayer2() > 1))) {
            return true;
        }
        return false;
    }

    private String solveWinnerName(OngoingMatch match) {
        if ((match.getCountSetsPlayer1() > 1 ) ) {
            return match.getPlayer1Name();
        } else if ((match.getCountSetsPlayer2() > 1)) {
            return match.getPlayer2Name();
        }
        return null;
    }

}
