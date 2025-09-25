package com.example.footprint.service.football.match;

import com.example.footprint.domain.dto.football.MatchDto;
import com.example.footprint.domain.dto.football.TeamStatsDto;
import com.example.footprint.domain.dto.football.combinations.MatchLineupDto;

import java.util.List;
import java.util.Map;

public interface MatchService {
    Map<String, List<MatchDto>> getMatchesByCompetitionAndSeason(int competitionId, int seasonId);
    List<TeamStatsDto> getStandingsByCompetitionAndSeason(int competitionId, int seasonId);
    List<MatchDto> getMatchesForATeam(int competitionId, int seasonId, int teamId);
    Map<String, MatchLineupDto> getLineupsForMatch(int matchId);
}
