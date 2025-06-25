package org.project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.DTO.IncrementScoreDTO;
import org.project.model.OngoingMatch;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchScoreCalculationServiceTest {

    @Mock
    private OngoingMatchesService ongoingMatchesService;

    @InjectMocks
    private MatchScoreCalculationService matchScoreCalculationService;

    private OngoingMatch testMatch;
    private UUID matchId;

    @BeforeEach
    void setUp() {
        testMatch = new OngoingMatch("John", "Jane");
        matchId = testMatch.getId();
    }

    @Test
    @DisplayName("Должен увеличить счет первого игрока")
    void shouldIncrementPlayer1Score() {
        // Given
        IncrementScoreDTO dto = new IncrementScoreDTO();
        dto.setMatchId(matchId);
        dto.setAddToPLayer1(1);
        dto.setAddToPLayer2(0);
        when(ongoingMatchesService.getOngoingMatch(matchId)).thenReturn(testMatch);

        // When
        boolean result = matchScoreCalculationService.IncrementScore(dto);

        // Then
        assertFalse(result); // Матч не закончен
        assertEquals(15, testMatch.getScorePlayer1());
        assertEquals(0, testMatch.getScorePlayer2());
        verify(ongoingMatchesService, times(1)).getOngoingMatch(matchId);
    }

    @Test
    @DisplayName("Должен увеличить счет второго игрока")
    void shouldIncrementPlayer2Score() {
        // Given
        IncrementScoreDTO dto = new IncrementScoreDTO();
        dto.setMatchId(matchId);
        dto.setAddToPLayer1(0);
        dto.setAddToPLayer2(1);
        when(ongoingMatchesService.getOngoingMatch(matchId)).thenReturn(testMatch);

        // When
        boolean result = matchScoreCalculationService.IncrementScore(dto);

        // Then
        assertFalse(result); // Матч не закончен
        assertEquals(0, testMatch.getScorePlayer1());
        assertEquals(15, testMatch.getScorePlayer2());
        verify(ongoingMatchesService, times(1)).getOngoingMatch(matchId);
    }

    @Test
    @DisplayName("Должен вернуть false если матч не найден")
    void shouldReturnFalseWhenMatchNotFound() {
        // Given
        IncrementScoreDTO dto = new IncrementScoreDTO();
        dto.setMatchId(matchId);
        dto.setAddToPLayer1(1);
        dto.setAddToPLayer2(0);
        when(ongoingMatchesService.getOngoingMatch(matchId)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> matchScoreCalculationService.IncrementScore(dto));
    }


    @Test
    @DisplayName("Если игрок 1 выигрывает очко при счёте 40-40, гейм не заканчивается")
    void shouldReturnTrueWhenMatchFinished() {
        // Given
        IncrementScoreDTO dto = new IncrementScoreDTO();
        dto.setMatchId(matchId);
        dto.setAddToPLayer1(1);
        dto.setAddToPLayer2(0);
        when(ongoingMatchesService.getOngoingMatch(matchId)).thenReturn(testMatch);
        boolean result = matchScoreCalculationService.IncrementScore(dto);
        matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        IncrementScoreDTO dto2 = new IncrementScoreDTO();
        dto2.setMatchId(matchId);
        dto2.setAddToPLayer1(0);
        dto2.setAddToPLayer2(1);
        boolean result2 = matchScoreCalculationService.IncrementScore(dto);
        result2 = matchScoreCalculationService.IncrementScore(dto);

        result = matchScoreCalculationService.IncrementScore(dto);
result2 = matchScoreCalculationService.IncrementScore(dto2);

        // Then
        assertFalse(result);
        assertFalse(result2);
        verify(ongoingMatchesService, times(7)).getOngoingMatch(matchId);
    }

    @Test
    @DisplayName("При счёте 6-6 начинается тайбрейк вместо обычного гейма")
    void shouldReturnTrueWhenTiebreakStarted() {
        // Given
        matchScoreCalculationService.setGames(testMatch, 6, 6);
        when(ongoingMatchesService.getOngoingMatch(matchId)).thenReturn(testMatch);
        assertTrue(matchScoreCalculationService.isTiebreak(ongoingMatchesService.getOngoingMatch(matchId)));
    }

    @Test
    @DisplayName("При счёте 6-6  тайбрейк продолжается счёт по 1 очку")
    void shouldReturnTrueWhenTiebreakStarted2() {
        // Given
        matchScoreCalculationService.setGames(testMatch, 6, 6);
        IncrementScoreDTO dto = new IncrementScoreDTO();
        dto.setMatchId(matchId);
        dto.setAddToPLayer1(1);
        dto.setAddToPLayer2(0);
        when(ongoingMatchesService.getOngoingMatch(matchId)).thenReturn(testMatch);
        boolean result = matchScoreCalculationService.IncrementScore(dto);
        assertFalse(result);
        assertEquals(1, testMatch.getScorePlayer1());
        assertEquals(0, testMatch.getScorePlayer2());

        when(ongoingMatchesService.getOngoingMatch(matchId)).thenReturn(testMatch);
        assertTrue(matchScoreCalculationService.isTiebreak(ongoingMatchesService.getOngoingMatch(matchId)));

    }

    @Test
    @DisplayName("тайбрейк завершается при разрыве в 2 очка")
    void shouldReturnTrueWhenTiebreakFinished() {
        // Given
        matchScoreCalculationService.setGames(testMatch, 7, 6);
        IncrementScoreDTO dto = new IncrementScoreDTO();
        dto.setMatchId(matchId);
        dto.setAddToPLayer1(1);
        dto.setAddToPLayer2(0);
        when(ongoingMatchesService.getOngoingMatch(matchId)).thenReturn(testMatch);

        boolean result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);

        assertFalse(result);
        assertEquals(1, testMatch.getCountSetsPlayer1());
        assertEquals(0, testMatch.getCountSetsPlayer2());


    }

    @Test
    @DisplayName("тайбрейк завершается при разрыве в 2 очка")
    void shouldReturnTrueWhenTiebreakFinished2Player() {
        // Given
        matchScoreCalculationService.setGames(testMatch, 6, 7);
        IncrementScoreDTO dto = new IncrementScoreDTO();
        dto.setMatchId(matchId);
        dto.setAddToPLayer1(0);
        dto.setAddToPLayer2(1);
        when(ongoingMatchesService.getOngoingMatch(matchId)).thenReturn(testMatch);

        boolean result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);

        assertFalse(result);
        assertEquals(0, testMatch.getCountSetsPlayer1());
        assertEquals(1, testMatch.getCountSetsPlayer2());


    }

    
    @Test
    @DisplayName("подсчёт сетов для игрока 1")
    void shouldReturnTrueWhenSetsCounted() {
        // Given
        matchScoreCalculationService.setGames(testMatch, 6, 5);
        IncrementScoreDTO dto = new IncrementScoreDTO();
        dto.setMatchId(matchId);
        dto.setAddToPLayer1(1);
        dto.setAddToPLayer2(0);
        when(ongoingMatchesService.getOngoingMatch(matchId)).thenReturn(testMatch);

        boolean result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);

        assertFalse(result);
        assertEquals(1, testMatch.getCountSetsPlayer1());
        assertEquals(0, testMatch.getCountSetsPlayer2());


    }

        
    @Test
    @DisplayName("подсчёт сетов для игрока 2")
    void shouldReturnTrueWhenSetsCounted2() {
        // Given
        matchScoreCalculationService.setGames(testMatch, 5, 6);
        IncrementScoreDTO dto = new IncrementScoreDTO();
        dto.setMatchId(matchId);
        dto.setAddToPLayer1(0);
        dto.setAddToPLayer2(1);
        when(ongoingMatchesService.getOngoingMatch(matchId)).thenReturn(testMatch);

        boolean result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);
        result = matchScoreCalculationService.IncrementScore(dto);

        assertFalse(result);
        assertEquals(0, testMatch.getCountSetsPlayer1());
        assertEquals(1, testMatch.getCountSetsPlayer2());


    }
} 