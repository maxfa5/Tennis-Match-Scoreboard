package org.project.model;

import lombok.Data;

@Data
public class OngoingMatch {
    
    private int player1Id;
    
    private int player2Id;
    private int scorePlayer1;
    private int scorePlayer2;

    public OngoingMatch(int player1Id, int player2Id) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.scorePlayer1 = 0;
        this.scorePlayer2 = 0;
    }
}
