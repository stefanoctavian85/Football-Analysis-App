package com.example.footprint.domain.entity.mapper;

import com.example.footprint.domain.dto.football.MatchDto;
import com.example.footprint.domain.entity.football.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchMapper {
    @Mapping(source = "competition.competitionPK.competitionId", target = "competitionId")
    @Mapping(source = "competition.competitionPK.seasonId", target = "seasonId")
    @Mapping(source = "homeTeam.teamName", target = "homeTeamName")
    @Mapping(source = "awayTeam.teamName", target = "awayTeamName")
    @Mapping(target = "winner", expression = "java(getWinner(match))")
    MatchDto toDto(Match match);

    @Mapping(source = "competitionId", target = "competition.competitionPK.competitionId")
    @Mapping(source = "seasonId", target = "competition.competitionPK.seasonId")
    @Mapping(source = "homeTeamName", target = "homeTeam.teamName")
    @Mapping(source = "awayTeamName", target = "awayTeam.teamName")
    Match fromDto(MatchDto matchDto);

    default String getWinner(Match match) {
        if (match.getHomeScore() > match.getAwayScore()) {
            return match.getHomeTeam().getTeamName();
        } else if (match.getHomeScore() < match.getAwayScore()) {
            return match.getAwayTeam().getTeamName();
        } else {
            return "Draw";
        }
    }
}
