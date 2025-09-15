package com.example.footprint.service.football.match;

import com.example.footprint.domain.dto.football.MatchDto;
import com.example.footprint.domain.entity.football.Match;

import java.util.List;
import java.util.Map;

public interface MatchService {
    Map<String, List<MatchDto>> getMatchesByCompetitionAndSeason(int competitionId, int seasonId);
}
