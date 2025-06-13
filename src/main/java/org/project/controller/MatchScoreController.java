package org.project.controller;

import org.project.service.FinishedMatchesPersistenceService;
import org.project.service.MatchScoreCalculationService;
import org.project.service.OngoingMatchesService;

import java.util.List;
import java.util.UUID;

import org.project.DTO.CreateMatchDTO;
import org.project.DTO.IncrementScoreDTO;
import org.project.model.FinishedMatch;
import org.project.model.OngoingMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;

@RestController
public class MatchScoreController {
    private final OngoingMatchesService ongoingMatchesService;
    private final MatchScoreCalculationService matchScoreCalculationService;
    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService;
    @Autowired
    public MatchScoreController(OngoingMatchesService ongoingMatchesService, FinishedMatchesPersistenceService finishedMatchesPersistenceService, MatchScoreCalculationService matchScoreCalculationService){
        this.ongoingMatchesService =ongoingMatchesService;
        this.finishedMatchesPersistenceService = finishedMatchesPersistenceService;
        this.matchScoreCalculationService = matchScoreCalculationService;
    }

    @PostMapping("/new-match")
    public OngoingMatch createNewMatch(@RequestBody CreateMatchDTO createMatchDTO){
        return getMatch(ongoingMatchesService.createNewMatch(createMatchDTO).getId());
    }
    @GetMapping("/match-score")
    public OngoingMatch getMatch(@RequestParam("uuid") UUID matchId) {
        return ongoingMatchesService.getOngoingMatch(matchId);
    }

    @PostMapping("/match-score")
    public String IncrementScore(@RequestBody IncrementScoreDTO incrementScoreDTO){
        if(matchScoreCalculationService.IncrementScore(incrementScoreDTO)){
            return "Match finished";
        } else {
            return "Match not finished";
        }
    }

    @GetMapping("/matches")
    public List<FinishedMatch> getMatches(
            @RequestParam(value = "page", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "filter_by_player_name", required = false) String playerName) {
        return finishedMatchesPersistenceService.getFinishedMatches(pageNumber, playerName);
    }
}
