package com.example.footprint.domain.dto.football.combinations;

import com.example.footprint.domain.entity.football.Team;
import lombok.Data;

import java.util.List;

@Data
public class TeamPlayersDto {
    private Team team;
    private List<PlayersPositionsDto> players;
}
