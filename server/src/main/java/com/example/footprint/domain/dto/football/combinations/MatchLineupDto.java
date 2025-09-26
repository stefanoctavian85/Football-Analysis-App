package com.example.footprint.domain.dto.football.combinations;

import com.example.footprint.domain.dto.football.MatchDto;
import lombok.Data;

@Data
public class MatchLineupDto {
    private MatchDto match;
    private TeamPlayersDto homeTeam;
    private TeamPlayersDto awayTeam;

    public boolean isEmpty() {
        return match == null && homeTeam == null && awayTeam == null;
    }
}
