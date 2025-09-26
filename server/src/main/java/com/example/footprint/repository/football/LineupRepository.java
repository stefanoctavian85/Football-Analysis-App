package com.example.footprint.repository.football;

import com.example.footprint.domain.entity.football.Lineup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineupRepository extends JpaRepository<Lineup, Integer> {
    List<Lineup> findByMatch_MatchIdAndTeam_TeamId(int matchId, int teamId);
}
