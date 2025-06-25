package org.project.service;

import org.project.DTO.CreateMatchDTO;

import org.project.model.OngoingMatch;
import org.project.model.Player;
import org.project.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OngoingMatchesService {
    private ConcurrentHashMap<UUID, OngoingMatch> ongoingMatches = new ConcurrentHashMap<>();
    private PlayerRepository playerRepository;
    @Autowired
    public OngoingMatchesService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }
    
    public OngoingMatch getOngoingMatch(UUID matchId) {
        return ongoingMatches.get(matchId);
    }

    public OngoingMatch createNewMatch(CreateMatchDTO createMatchDTO) throws Exception {
        Player player1 = playerRepository.findByName(createMatchDTO.getPlayerName1());
        if (player1 == null) {
            player1 = new Player(createMatchDTO.getPlayerName1());
            if (player1.getName().length() >=41) {
                throw new Exception("Слишком длинный ник игрока >40 - запрещён");
            }
            player1 = playerRepository.save(player1);
        }

        Player player2 = playerRepository.findByName(createMatchDTO.getPlayerName2());
        if (player2 == null) {
            player2 = new Player(createMatchDTO.getPlayerName2());
            if (player2.getName().length() >=41) {
                throw new Exception("Слишком длинный ник игрока >40 - запрещён");
            }
            player2 = playerRepository.save(player2);
        }
        if (player1.getName().equals(player2.getName())) {
            throw new Exception("у игроков одинаковые имена");
            
        }
        
        OngoingMatch match = new OngoingMatch(player1.getName(), player2.getName());
        ongoingMatches.put(match.getId(), match);
        return match;
    }

    // public List<OngoingMatch> getOngoingMatches(int number, String playerName) {
    //     return ongoingMatches.values().stream()
    //     .filter(match -> match.getPlayer1Name().equals(playerName) || match.getPlayer2Name().equals(playerName))
    //     .skip((number - 1) * PAGE_SIZE)
    //     .limit(PAGE_SIZE)
    //     .toList();
    // }
    public void removeMatch(UUID matchId){
        ongoingMatches.remove(matchId);
    }
}
