package org.project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
public class OngoingMatch {
    private final UUID id;
    
    private String player1Name;
    
    private String player2Name;
    private int scorePlayer1;//0 15 30 40 
    private int scorePlayer2;
    private int countGamesPlayer1;//0 1 2 3...6/7
    private int countGamesPlayer2;
    private int countSetsPlayer1;//0 1 2
    private int countSetsPlayer2;

    public OngoingMatch(String player1Name, String player2Name) {
        this.id = UUID.randomUUID();
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.scorePlayer1 = 0;
        this.scorePlayer2 = 0;

    }
}
