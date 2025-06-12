package org.project.controller;

import org.project.service.OngoingMatchesService;
import org.project.DTO.CreateMatchDTO;
import org.project.model.OngoingMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatchScoreController {
    private final OngoingMatchesService ongoingMatchesService;
    
    @Autowired
    public MatchScoreController(OngoingMatchesService ongoingMatchesService){
        this.ongoingMatchesService =ongoingMatchesService;
    }

    @PostMapping("/new-match")
    public OngoingMatch createNewMatch(@RequestBody CreateMatchDTO createMatchDTO){
        OngoingMatch ongoingMatch = ongoingMatchesService.createNewMatch(createMatchDTO);
        return ongoingMatch;
    }
}
