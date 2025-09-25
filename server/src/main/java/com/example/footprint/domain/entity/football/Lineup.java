package com.example.footprint.domain.entity.football;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lineups")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Lineup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int lineupId;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "jersey_number")
    private int jerseyNumber;
}
