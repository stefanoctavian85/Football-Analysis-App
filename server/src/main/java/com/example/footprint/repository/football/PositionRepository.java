package com.example.footprint.repository.football;

import com.example.footprint.domain.entity.football.Position;
import com.example.footprint.domain.entity.football.PositionPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, PositionPK> {
    List<Position> findByMatch_MatchIdAndPlayer_PlayerId(int matchId, int playerId);
}
