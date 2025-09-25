package com.example.footprint.controller.football;

import com.example.footprint.domain.dto.football.MatchDto;
import com.example.footprint.domain.dto.football.combinations.MatchLineupDto;
import com.example.footprint.service.football.match.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchesController {
    private final MatchService matchService;

    @GetMapping("/{competitionId}/{seasonId}/{teamId}")
    public ResponseEntity<?> getMatchesForATeam(@PathVariable int competitionId, @PathVariable int seasonId,
                                                @PathVariable int teamId) {
        if (competitionId <= 0 || seasonId <= 0 || teamId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid parameters!"));
        }

        List<MatchDto> matches = matchService.getMatchesForATeam(competitionId, seasonId, teamId);

        if (matches.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("message", "No matches for this competition, season and team!"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("matches", matches));
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<?> getDetailsForAMatch(@PathVariable int matchId) {
        if (matchId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid parameters!"));
        }

        Map<String, MatchLineupDto> lineupsForMatch = matchService.getLineupsForMatch(matchId);

        if (lineupsForMatch.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("message", "No lineup available for this match!"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("lineups", lineupsForMatch));
    }
}
