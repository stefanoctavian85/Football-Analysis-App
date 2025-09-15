package com.example.footprint.domain.dto.football;

import lombok.Data;

import java.time.LocalTime;
import java.util.Date;

@Data
public class MatchDto {
    private int matchId;
    private Date matchDate;
    private LocalTime kickOff;
    private int competitionId;
    private int seasonId;
    private int homeScore;
    private int awayScore;
    private int matchWeek;
    private String homeTeamName;
    private String awayTeamName;
}
