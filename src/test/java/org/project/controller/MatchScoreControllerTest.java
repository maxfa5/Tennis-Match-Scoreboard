package org.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.project.DTO.CreateMatchDTO;
import org.project.DTO.IncrementScoreDTO;
import org.project.model.OngoingMatch;
import org.project.service.FinishedMatchesPersistenceService;
import org.project.service.MatchScoreCalculationService;
import org.project.service.OngoingMatchesService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatchScoreController.class)
class MatchScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OngoingMatchesService ongoingMatchesService;

    @MockBean
    private MatchScoreCalculationService matchScoreCalculationService;

    @MockBean
    private FinishedMatchesPersistenceService finishedMatchesPersistenceService;

    private OngoingMatch testMatch;
    private UUID matchId;

    @BeforeEach
    void setUp() {
        testMatch = new OngoingMatch("John", "Jane");
        matchId = testMatch.getId();
    }

    @Test
    @DisplayName("Должен вернуть главную страницу")
    void shouldReturnHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @DisplayName("Должен вернуть страницу нового матча")
    void shouldReturnNewMatchPage() throws Exception {
        mockMvc.perform(get("/new-match"))
                .andExpect(status().isOk())
                .andExpect(view().name("new-match"));
    }

    @Test
    @DisplayName("Должен создать новый матч")
    void shouldCreateNewMatch() throws Exception {
        // Given
        CreateMatchDTO createMatchDTO = new CreateMatchDTO();
        createMatchDTO.setPlayerName1("John");
        createMatchDTO.setPlayerName2("Jane");

        when(ongoingMatchesService.createNewMatch(any(CreateMatchDTO.class))).thenReturn(testMatch);
        when(ongoingMatchesService.getOngoingMatch(matchId)).thenReturn(testMatch);

        // When & Then
        mockMvc.perform(post("/new-match")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMatchDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(matchId.toString()))
                .andExpect(jsonPath("$.player1Name").value("John"))
                .andExpect(jsonPath("$.player2Name").value("Jane"));
    }

    @Test
    @DisplayName("Должен вернуть страницу счета матча")
    void shouldReturnMatchScorePage() throws Exception {
        // Given
        when(ongoingMatchesService.getOngoingMatch(matchId)).thenReturn(testMatch);

        // When & Then
        mockMvc.perform(get("/match-score")
                .param("uuid", matchId.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("match-score"))
                .andExpect(model().attribute("match", testMatch));
    }

    @Test
    @DisplayName("Должен перенаправить если матч не найден")
    void shouldRedirectWhenMatchNotFound() throws Exception {
        // Given
        when(ongoingMatchesService.getOngoingMatch(matchId)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/match-score")
                .param("uuid", matchId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/matches"));
    }

    @Test
    @DisplayName("Должен увеличить счет")
    void shouldIncrementScore() throws Exception {
        // Given
        IncrementScoreDTO incrementScoreDTO = new IncrementScoreDTO();
        incrementScoreDTO.setMatchId(matchId);
        incrementScoreDTO.setAddToPLayer1(1);
        incrementScoreDTO.setAddToPLayer2(0);

        when(matchScoreCalculationService.IncrementScore(any(IncrementScoreDTO.class))).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/match-score")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incrementScoreDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Match not finished"));
    }
} 