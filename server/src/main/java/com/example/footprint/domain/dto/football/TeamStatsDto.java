package com.example.footprint.domain.dto.football;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamStatsDto {
    private int teamId;
    private String teamName;
    private int nrMatchesPlayed;
    private int nrWins;
    private int nrDraws;
    private int nrLosses;
    private int goalsScored;
    private int goalsConceded;
    private int goalsDifference;
    private int nrPoints;
}
