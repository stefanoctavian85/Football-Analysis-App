package com.example.footprint.service.football.match;

import com.example.footprint.domain.dto.football.MatchDto;
import com.example.footprint.domain.entity.football.Match;
import com.example.footprint.domain.entity.mapper.MatchMapper;
import com.example.footprint.repository.football.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;

    @Override
    public Map<String, List<MatchDto>> getMatchesByCompetitionAndSeason(int competitionId, int seasonId) {
        Map<String, List<MatchDto>> matchesByTeam = new HashMap<>();
        List<Match> allMatches = matchRepository
                .findByCompetition_CompetitionPK_CompetitionIdAndCompetition_CompetitionPK_SeasonId(competitionId, seasonId);

        for (Match match : allMatches) {
            matchesByTeam.computeIfAbsent(match.getHomeTeam().getTeamName(),
                    k -> new ArrayList<>()).add(matchMapper.toDto(match));
            matchesByTeam.computeIfAbsent(match.getAwayTeam().getTeamName(),
                    k -> new ArrayList<>()).add(matchMapper.toDto(match));
        }

        return new TreeMap<>(matchesByTeam);
    }
}
