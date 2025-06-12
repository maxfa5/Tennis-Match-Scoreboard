package org.project.DTO;

import java.util.UUID;

import lombok.Data;

@Data
public class IncrementScoreDTO {
    private UUID matchId;
    private int addToPLayer1;
    private int addToPLayer2;
}
