package com.example.footprint.repository.football;

import com.example.footprint.domain.entity.football.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Integer> {
    List<Match> findByCompetition_CompetitionPK_CompetitionIdAndCompetition_CompetitionPK_SeasonId(int competitionId, int seasonId);
}
