package org.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class FinishedMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player1_id", nullable = false)
    private Player player1;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player2_id", nullable = false)
    private Player player2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id", nullable = false)
    private Player winner;
}
