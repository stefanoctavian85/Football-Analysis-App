package com.example.footprint.domain.entity.football;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "positions")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Position {
    @EmbeddedId
    private PositionPK positionPK;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "from_time")
    private String fromTime;

    @Column(name = "to_time")
    private String toTime;

    @Column(name = "from_period")
    private Integer fromPeriod;

    @Column(name = "to_period")
    private Integer toPeriod = -1;

    @Column(name = "start_reason")
    private String startReason;

    @Column(name = "end_reason")
    private String endReason;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;
}
