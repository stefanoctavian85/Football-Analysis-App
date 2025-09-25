package com.example.footprint.repository.football;

import com.example.footprint.domain.entity.football.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Integer> {
    List<Match> findByCompetition_CompetitionPK_CompetitionIdAndCompetition_CompetitionPK_SeasonId(int competitionId, int seasonId);

    @Query("SELECT m FROM Match m WHERE m.competition.competitionPK.competitionId = :competitionId " +
            "AND m.competition.competitionPK.seasonId = :seasonId " +
            "AND (m.homeTeam.teamId = :teamId OR m.awayTeam.teamId = :teamId)")
    List<Match> findMatchesByCompetitionSeasonAndTeamId(@Param("competitionId") int competitionId,
                                                        @Param("seasonId") int seasonId,
                                                        @Param("teamId") int teamId);

    Match findByMatchId(int matchId);
}
