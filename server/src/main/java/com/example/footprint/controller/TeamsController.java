package com.example.footprint.controller;

import com.example.footprint.domain.dto.football.TeamDto;
import com.example.footprint.service.football.team.TeamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamsController {
    private final TeamsService teamsService;

    @GetMapping("/all-teams")
    public ResponseEntity<?> getAllTeams() {
        List<TeamDto> teams = teamsService.getAllTeams();

        if (teams.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "No teams!"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("teams", teams));
    }
}
