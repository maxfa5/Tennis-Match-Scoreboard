package org.project.controller;

import org.project.service.FinishedMatchesPersistenceService;
import org.project.service.MatchScoreCalculationService;
import org.project.service.OngoingMatchesService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.project.DTO.CreateMatchDTO;
import org.project.DTO.IncrementScoreDTO;
import org.project.DTO.FinishedMatchDTO;
import org.project.model.FinishedMatch;
import org.project.model.OngoingMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;

@Controller
@RequestMapping("/")
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

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("/new-match")
    public ModelAndView newMatchPage() {
        return new ModelAndView("new-match");
    }

    @PostMapping("/new-match")
    @ResponseBody
    public OngoingMatch createNewMatch(@RequestBody CreateMatchDTO createMatchDTO) throws Exception{
        OngoingMatch match;
        try {
            match = ongoingMatchesService.createNewMatch(createMatchDTO);   
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        return ongoingMatchesService.getOngoingMatch(match.getId());
    }

    @GetMapping("/match-score")
    public String matchScorePage(@RequestParam("uuid") UUID matchId, Model model) {
        OngoingMatch match = ongoingMatchesService.getOngoingMatch(matchId);
        if (match == null) {
            return "redirect:/matches";
        }
        model.addAttribute("match", match);
        return "match-score";
    }

    @PostMapping("/match-score")
    @ResponseBody
    public String IncrementScore(@RequestBody IncrementScoreDTO incrementScoreDTO){
        if(matchScoreCalculationService.IncrementScore(incrementScoreDTO)){
            return "Match finished";
        } else {
            return "Match not finished";
        }
    }
    @GetMapping("/api/matches")
    @ResponseBody
    public List<FinishedMatchDTO> getMatchesApi(
            @RequestParam(value = "page", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "filter_by_player_name", defaultValue = "", required = false) String playerName) {
        List<FinishedMatch> matches = finishedMatchesPersistenceService.getFinishedMatches(pageNumber, playerName);
        return matches.stream()
                .map(match -> new FinishedMatchDTO(
                    match.getPlayer1().getName(),
                    match.getPlayer2().getName(),
                    match.getWinner().getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/matches")
    public String getMatches(Model model,
            @RequestParam(value = "page", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "filter_by_player_name", defaultValue = "", required = false) String playerName) {
        List<FinishedMatch> matches = finishedMatchesPersistenceService.getFinishedMatches(pageNumber, playerName);
        model.addAttribute("matches", matches);
        model.addAttribute("pageNumber", pageNumber);
        boolean hasNextPage = matches.size() == 5;
        model.addAttribute("hasNextPage", hasNextPage);
        return "matches";
    }
}
