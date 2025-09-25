package com.example.footprint.domain.dto.football;

import com.example.footprint.domain.entity.football.PositionPK;
import lombok.Data;

@Data
public class PositionDto {
    private PositionPK positionPK;
    private String position;
    private String fromTime;
    private String toTime;
    private Integer fromPeriod;
    private Integer toPeriod = -1;
    private String startReason;
    private String endReason;
}
