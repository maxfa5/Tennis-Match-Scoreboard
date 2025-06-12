package org.project.repository;

import org.project.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PLayerRepository extends JpaRepository<Player, Integer> {

    Player findByName(String playerName);
}
