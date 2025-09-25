package com.example.footprint.domain.entity.football;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "players")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int playerId;

    @Column(name = "player_name", nullable = false)
    private String playerName;

    @Column(name = "player_nickname")
    private String playerNickname;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
}
