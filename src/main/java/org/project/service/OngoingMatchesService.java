package org.project.service;

import org.project.DTO.CreateMatchDTO;
import org.project.model.OngoingMatch;
import org.project.model.Player;
import org.project.repository.PLayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class OngoingMatchesService {
    private List<OngoingMatch> ongoingMatches = new ArrayList<>();
    private PLayerRepository playerRepository;

    @Autowired
    public OngoingMatchesService(PLayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }
    
    public OngoingMatch getOngoingMatch(int matchId) {
        return ongoingMatches.get(matchId);
    }

    public OngoingMatch createNewMatch(CreateMatchDTO createMatchDTO) {
        Player player1 = playerRepository.findByName(createMatchDTO.getPlayerName1());
        if (player1 == null) {
            System.out.println("player1 is null\n\n\n\n\n\n\n");
            System.out.println(createMatchDTO.getPlayerName1());

            player1 = new Player(createMatchDTO.getPlayerName1());
            player1 = playerRepository.save(player1);
        }

        Player player2 = playerRepository.findByName(createMatchDTO.getPlayerName2());
        if (player2 == null) {
            player2 = new Player(createMatchDTO.getPlayerName2());
            player2 = playerRepository.save(player2);
        }
        
        OngoingMatch match = new OngoingMatch(player1.getId(), player2.getId());
        ongoingMatches.add(match);
        return match;
    }
}
