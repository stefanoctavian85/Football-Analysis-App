package com.example.footprint.repository.football;

import com.example.footprint.domain.entity.football.Lineup;
import com.example.footprint.domain.entity.football.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineupRepository extends JpaRepository<Lineup, Integer> {
    List<Player> findDistinctByMatch_MatchIdAndTeam_TeamId(int matchId, int teamId);
}
