package com.example.footprint.controller.football;

import com.example.footprint.domain.dto.football.MatchDto;
import com.example.footprint.domain.dto.football.TeamStatsDto;
import com.example.footprint.domain.dto.football.combinations.CompetitionSeasonsDto;
import com.example.footprint.service.football.competition.CompetitionService;
import com.example.footprint.service.football.match.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/competitions")
@RequiredArgsConstructor
public class CompetitionController {
    private final CompetitionService competitionService;
    private final MatchService matchService;

    @GetMapping("/all-competitions")
    public ResponseEntity<?> getAllCompetitions() {
        List<CompetitionSeasonsDto> competitions = competitionService.getAllCompetitions();

        if (competitions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("message", "No competitions!"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("competitions", competitions));
    }

    @GetMapping("/{competitionId}/{seasonId}")
    public ResponseEntity<?> getMatchesByCompetition(@PathVariable int competitionId, @PathVariable int seasonId) {
        if (competitionId <= 0 || seasonId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("messsage", "Invalid parameters!"));
        }

        Map<String, List<MatchDto>> matches = matchService.getMatchesByCompetitionAndSeason(competitionId, seasonId);

        if (matches.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("message", "No matches for this competition and season!"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("matches", matches));
    }

    @GetMapping("/standings/{competitionId}/{seasonId}")
    public ResponseEntity<?> getStandingsByCompetition(@PathVariable int competitionId, @PathVariable int seasonId) {
        if (competitionId <= 0 || seasonId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("messsage", "Invalid parameters!"));
        }

        List<TeamStatsDto> standings = matchService.getStandingsByCompetitionAndSeason(competitionId, seasonId);

        if (standings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("message", "No standings for this competition and season!"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("standings", standings));
    }
}
