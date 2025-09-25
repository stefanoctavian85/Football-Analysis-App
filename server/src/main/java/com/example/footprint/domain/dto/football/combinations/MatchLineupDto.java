package com.example.footprint.domain.dto.football.combinations;

import com.example.footprint.domain.dto.football.MatchDto;
import com.example.footprint.domain.entity.football.Team;
import lombok.Data;

import java.util.List;

@Data
public class MatchLineupDto {
    private MatchDto match;
    private Team team;
    private List<PlayersPositionsDto> players;
}
