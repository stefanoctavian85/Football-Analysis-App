package com.example.footprint.domain.dto.football;

import com.example.footprint.domain.entity.football.Player;
import lombok.Data;

@Data
public class LineupDto {
    private int lineupId;
    private MatchDto match;
    private TeamDto team;
    private Player player;
    private int jerseyNumber;
}
