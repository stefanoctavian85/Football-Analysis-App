package com.example.footprint.domain.entity.football;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "matches")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int matchId;

    @Column(name = "match_date")
    private Date matchDate;

    @Column(name = "kick_off")
    private LocalTime kickOff;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "competition_id", referencedColumnName = "competition_id"),
            @JoinColumn(name = "season_id", referencedColumnName = "season_id")
    })
    private Competition competition;

    @Column(name = "home_score", nullable = false)
    private int homeScore;

    @Column(name = "away_score", nullable = false)
    private int awayScore;

    @Column(name = "match_week")
    private int matchWeek;

    @OneToOne
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @OneToOne
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;
}
