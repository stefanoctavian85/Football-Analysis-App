package com.example.footprint.service.football.match;

import com.example.footprint.domain.dto.football.MatchDto;
import com.example.footprint.domain.dto.football.PositionDto;
import com.example.footprint.domain.dto.football.TeamStatsDto;
import com.example.footprint.domain.dto.football.combinations.MatchLineupDto;
import com.example.footprint.domain.dto.football.combinations.PlayersPositionsDto;
import com.example.footprint.domain.dto.football.combinations.TeamPlayersDto;
import com.example.footprint.domain.entity.football.Lineup;
import com.example.footprint.domain.entity.football.Match;
import com.example.footprint.domain.entity.football.Player;
import com.example.footprint.domain.entity.football.Position;
import com.example.footprint.domain.entity.mapper.MatchMapper;
import com.example.footprint.domain.entity.mapper.PositionMapper;
import com.example.footprint.repository.football.LineupRepository;
import com.example.footprint.repository.football.MatchRepository;
import com.example.footprint.repository.football.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    private final MatchRepository matchRepository;
    private final LineupRepository lineupRepository;
    private final PositionRepository positionRepository;
    private final MatchMapper matchMapper;
    private final PositionMapper positionMapper;

    @Override
    public Map<String, List<MatchDto>> getMatchesByCompetitionAndSeason(int competitionId, int seasonId) {
        Map<String, List<MatchDto>> matchesByTeam = new HashMap<>();
        List<Match> allMatches = matchRepository
                .findByCompetition_CompetitionPK_CompetitionIdAndCompetition_CompetitionPK_SeasonId(competitionId, seasonId);

        for (Match match : allMatches) {
            matchesByTeam.computeIfAbsent(match.getHomeTeam().getTeamName(),
                    k -> new ArrayList<>()).add(matchMapper.toDto(match));
            matchesByTeam.computeIfAbsent(match.getAwayTeam().getTeamName(),
                    k -> new ArrayList<>()).add(matchMapper.toDto(match));
        }

        return new TreeMap<>(matchesByTeam);
    }

    @Override
    public List<TeamStatsDto> getStandingsByCompetitionAndSeason(int competitionId, int seasonId) {
        List<Match> allMatches = matchRepository
                .findByCompetition_CompetitionPK_CompetitionIdAndCompetition_CompetitionPK_SeasonId(competitionId, seasonId);

        Map<String, TeamStatsDto> standings = new HashMap<>();

        for (Match match : allMatches) {
            String homeTeamName = match.getHomeTeam().getTeamName();
            String awayTeamName = match.getAwayTeam().getTeamName();

            int homeTeamId = match.getHomeTeam().getTeamId();
            int awayTeamId = match.getAwayTeam().getTeamId();

            standings.putIfAbsent(homeTeamName, new TeamStatsDto(homeTeamId, homeTeamName, 0, 0, 0, 0, 0, 0, 0, 0));
            standings.putIfAbsent(awayTeamName, new TeamStatsDto(awayTeamId, awayTeamName, 0, 0, 0, 0,0, 0, 0, 0));

            TeamStatsDto homeTeam = standings.get(homeTeamName);
            TeamStatsDto awayTeam = standings.get(awayTeamName);

            homeTeam.setNrMatchesPlayed(homeTeam.getNrMatchesPlayed() + 1);
            awayTeam.setNrMatchesPlayed(awayTeam.getNrMatchesPlayed() + 1);

            int homeScore = match.getHomeScore();
            int awayScore = match.getAwayScore();

            if (homeScore > awayScore) {
                homeTeam.setNrWins(homeTeam.getNrWins() + 1);
                awayTeam.setNrLosses(awayTeam.getNrLosses() + 1);
                homeTeam.setNrPoints(homeTeam.getNrPoints() + 3);
            } else if (homeScore == awayScore) {
                homeTeam.setNrDraws(homeTeam.getNrDraws() + 1);
                awayTeam.setNrDraws(awayTeam.getNrDraws() + 1);
                homeTeam.setNrPoints(homeTeam.getNrPoints() + 1);
                awayTeam.setNrPoints(awayTeam.getNrPoints() + 1);
            } else {
                homeTeam.setNrLosses(homeTeam.getNrLosses() + 1);
                awayTeam.setNrWins(awayTeam.getNrWins() + 1);
                awayTeam.setNrPoints(awayTeam.getNrPoints() + 3);
            }

            homeTeam.setGoalsScored(homeTeam.getGoalsScored() + homeScore);
            homeTeam.setGoalsConceded(homeTeam.getGoalsConceded() + awayScore);
            awayTeam.setGoalsScored(awayTeam.getGoalsScored() + awayScore);
            awayTeam.setGoalsConceded(awayTeam.getGoalsConceded() + homeScore);
            homeTeam.setGoalsDifference(homeTeam.getGoalsScored() - homeTeam.getGoalsConceded());
            awayTeam.setGoalsDifference(awayTeam.getGoalsScored() - awayTeam.getGoalsConceded());
        }


        return standings.values().stream().sorted(
                Comparator.comparingInt(TeamStatsDto::getNrPoints).reversed()
                .thenComparing(Comparator.comparing(TeamStatsDto::getGoalsDifference).reversed())).collect(Collectors.toList());
    }

    @Override
    public List<MatchDto> getMatchesForATeam(int competitionId, int seasonId, int teamId) {
        List<Match> matches = matchRepository.findMatchesByCompetitionSeasonAndTeamId(competitionId, seasonId, teamId);
        List<MatchDto> result = new ArrayList<>();

        for (Match match : matches) {
            result.add(matchMapper.toDto(match));
        }

        result.sort(Comparator.comparing(MatchDto::getMatchWeek).reversed());

        return result;
    }

    public List<PlayersPositionsDto> setPlayersLineupForMatch(List<Lineup> matchPlayers, int matchId) {
        List<PlayersPositionsDto> teamPlayers = new ArrayList<>();

        for (Lineup lineup : matchPlayers) {
            PlayersPositionsDto player = new PlayersPositionsDto();
            player.setPlayer(lineup.getPlayer());
            player.setJerseyNumber(lineup.getJerseyNumber());
            List<PositionDto> positionDtos = new ArrayList<>();
            List<Position> playersPositions = positionRepository.findByMatch_MatchIdAndPlayer_PlayerId(matchId, lineup.getPlayer().getPlayerId());

            for (Position position : playersPositions) {
                positionDtos.add(positionMapper.toDto(position));
            }

            player.setPositions(positionDtos);
            teamPlayers.add(player);
        }

        return teamPlayers;
    }

    @Override
    public MatchLineupDto getLineupsForMatch(int matchId) {
        Match matchFound = matchRepository.findByMatchId(matchId);

        if (matchFound == null) {
            return new MatchLineupDto();
        }

        int homeTeamId = matchFound.getHomeTeam().getTeamId();
        int awayTeamId = matchFound.getAwayTeam().getTeamId();

        MatchLineupDto matchLineupDto = new MatchLineupDto();
        matchLineupDto.setMatch(matchMapper.toDto(matchFound));

        TeamPlayersDto homeTeam = new TeamPlayersDto();
        homeTeam.setTeam(matchFound.getHomeTeam());

        List<Lineup> homeTeamLineup = lineupRepository.findByMatch_MatchIdAndTeam_TeamId(matchId, homeTeamId);
        homeTeam.setPlayers(setPlayersLineupForMatch(homeTeamLineup, matchId));

        TeamPlayersDto awayTeam = new TeamPlayersDto();
        awayTeam.setTeam(matchFound.getAwayTeam());

        List<Lineup> awayTeamLineup = lineupRepository.findByMatch_MatchIdAndTeam_TeamId(matchId, awayTeamId);
        awayTeam.setPlayers(setPlayersLineupForMatch(awayTeamLineup, matchId));

        matchLineupDto.setHomeTeam(homeTeam);
        matchLineupDto.setAwayTeam(awayTeam);

        return matchLineupDto;
    }
}
