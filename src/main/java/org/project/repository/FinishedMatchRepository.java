package org.project.repository;

import org.project.model.FinishedMatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FinishedMatchRepository extends JpaRepository<FinishedMatch, Integer> {
    
    @Query("SELECT fm FROM FinishedMatch fm WHERE fm.player1.name LIKE %:playerName% OR fm.player2.name LIKE %:playerName%")
    Page<FinishedMatch> findByPlayerNameContaining(@Param("playerName") String playerName, Pageable pageable);
}
