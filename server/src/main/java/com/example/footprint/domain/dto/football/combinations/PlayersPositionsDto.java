package com.example.footprint.domain.dto.football.combinations;

import com.example.footprint.domain.dto.football.PositionDto;
import com.example.footprint.domain.entity.football.Player;
import lombok.Data;

import java.util.List;

@Data
public class PlayersPositionsDto {
    private Player player;
    private int jerseyNumber;
    private List<PositionDto> positions;
}
