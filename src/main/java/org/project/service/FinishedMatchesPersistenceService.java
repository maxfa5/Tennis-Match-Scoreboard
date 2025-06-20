package org.project.service;

import java.util.List;

import org.project.model.FinishedMatch;
import org.project.model.OngoingMatch;
import org.project.model.Player;
import org.project.repository.FinishedMatchRepository;
import org.project.repository.PlayerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinishedMatchesPersistenceService {
    private final  OngoingMatchesService ongoingMatchesService;
    private final PlayerRepository playerRepository;
    private static final int PAGE_SIZE = 5;
        private final FinishedMatchRepository finishedMatchRepository;

        @Autowired
        public FinishedMatchesPersistenceService(FinishedMatchRepository finishedMatchRepository, OngoingMatchesService ongoingMatchesService, PlayerRepository playerRepository) {
            this.finishedMatchRepository = finishedMatchRepository;
            this.ongoingMatchesService = ongoingMatchesService;
            this.playerRepository = playerRepository;
        }
        
        public List<FinishedMatch> getFinishedMatches(int pageNumber, String playerName) {
            if (playerName.equals("")) {
                return finishedMatchRepository.findAll(PageRequest.of(pageNumber - 1, PAGE_SIZE)).getContent();
            }else{
            return finishedMatchRepository.findAll(PageRequest.of(pageNumber - 1, PAGE_SIZE)).getContent()
            .stream().
            filter(match -> match.getPlayer1().getName().equals(playerName) || match.getPlayer2().getName().equals(playerName)).toList();}
    }

    public FinishedMatch finishMatch(OngoingMatch match, Player winner){
        Player player1 = this.playerRepository.findByName(match.getPlayer1Name());
        Player player2 = this.playerRepository.findByName(match.getPlayer2Name());
        FinishedMatch finishedMatch = new FinishedMatch(player1, player2, winner);
            finishedMatchRepository.save(finishedMatch);
            ongoingMatchesService.removeMatch(match.getId());
            return finishedMatch;
    }
}
