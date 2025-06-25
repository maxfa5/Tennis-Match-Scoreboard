package org.project.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.project.model.FinishedMatch;
import org.project.model.Player;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FinishedMatchRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FinishedMatchRepository finishedMatchRepository;

    private Player player1;
    private Player player2;
    private Player player3;

    @BeforeEach
    void setUp() {
        // Очищаем базу данных перед каждым тестом
        entityManager.clear();
        
        // Создаем тестовых игроков
        player1 = new Player("John");
        player2 = new Player("Jane");
        player3 = new Player("Bob");
        
        entityManager.persistAndFlush(player1);
        entityManager.persistAndFlush(player2);
        entityManager.persistAndFlush(player3);
    }

    @Test
    @DisplayName("Должен найти все матчи")
    void shouldFindAllMatches() {
        // Given
        FinishedMatch match1 = new FinishedMatch(player1, player2, player1);
        FinishedMatch match2 = new FinishedMatch(player2, player3, player2);
        
        entityManager.persistAndFlush(match1);
        entityManager.persistAndFlush(match2);

        // When
        List<FinishedMatch> matches = finishedMatchRepository.findAll();

        // Then
        assertEquals(2, matches.size());
    }

    @Test
    @DisplayName("Должен найти матчи с пагинацией")
    void shouldFindMatchesWithPagination() {
        // Given
        FinishedMatch match1 = new FinishedMatch(player1, player2, player1);
        FinishedMatch match2 = new FinishedMatch(player2, player3, player2);
        FinishedMatch match3 = new FinishedMatch(player1, player3, player3);
        
        entityManager.persistAndFlush(match1);
        entityManager.persistAndFlush(match2);
        entityManager.persistAndFlush(match3);

        // When
        List<FinishedMatch> matches = finishedMatchRepository.findAll(PageRequest.of(0, 2)).getContent();

        // Then
        assertEquals(2, matches.size());
    }

    @Test
    @DisplayName("Должен найти матчи по имени игрока")
    void shouldFindMatchesByPlayerName() {
        // Given
        FinishedMatch match1 = new FinishedMatch(player1, player2, player1);
        FinishedMatch match2 = new FinishedMatch(player2, player3, player2);
        FinishedMatch match3 = new FinishedMatch(player1, player3, player3);
        
        entityManager.persistAndFlush(match1);
        entityManager.persistAndFlush(match2);
        entityManager.persistAndFlush(match3);

        // When
        List<FinishedMatch> matches = finishedMatchRepository.findByPlayerNameContaining("John", PageRequest.of(0, 10)).getContent();

        // Then
        assertEquals(2, matches.size());
        assertTrue(matches.stream().anyMatch(match -> 
            match.getPlayer1().getName().equals("John") || match.getPlayer2().getName().equals("John")));
    }

    @Test
    @DisplayName("Должен найти матчи по частичному совпадению имени")
    void shouldFindMatchesByPartialPlayerName() {
        // Given
        Player playerJo = new Player("Jo");
        entityManager.persistAndFlush(playerJo);
        
        FinishedMatch match1 = new FinishedMatch(player1, player2, player1);
        FinishedMatch match2 = new FinishedMatch(playerJo, player3, playerJo);
        
        entityManager.persistAndFlush(match1);
        entityManager.persistAndFlush(match2);

        // When
        List<FinishedMatch> matches = finishedMatchRepository.findByPlayerNameContaining("Jo", PageRequest.of(0, 10)).getContent();

        // Then
        assertEquals(2, matches.size());
    }

    @Test
    @DisplayName("Должен вернуть пустой список при отсутствии совпадений")
    void shouldReturnEmptyListWhenNoMatches() {
        // Given
        FinishedMatch match1 = new FinishedMatch(player1, player2, player1);
        entityManager.persistAndFlush(match1);

        // When
        List<FinishedMatch> matches = finishedMatchRepository.findByPlayerNameContaining("NonExistent", PageRequest.of(0, 10)).getContent();

        // Then
        assertTrue(matches.isEmpty());
    }
} 