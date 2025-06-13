package org.project.DTO;

import lombok.Data;

@Data
public class FinishedMatchDTO {
    private String player1Name;
    private String player2Name;
    private String winnerName;

    public FinishedMatchDTO(String player1Name, String player2Name, String winnerName) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.winnerName = winnerName;
    }
} 