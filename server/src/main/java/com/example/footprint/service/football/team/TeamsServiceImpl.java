package com.example.footprint.service.football.team;

import com.example.footprint.domain.dto.football.TeamDto;
import com.example.footprint.domain.entity.football.Country;
import com.example.footprint.domain.entity.football.Team;
import com.example.footprint.repository.football.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamsServiceImpl implements TeamsService {
    private final TeamRepository teamRepository;
    @Override
    public List<TeamDto> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        List<TeamDto> result = new ArrayList<>();

        for (Team team : teams) {
            TeamDto teamDto = new TeamDto();
            teamDto.setTeamId(team.getTeamId());
            teamDto.setTeamName(team.getTeamName());
            teamDto.setTeamGender(team.getTeamGender());

            Country countryDto = new Country();
            countryDto.setCountryId(team.getCountry().getCountryId());
            countryDto.setCountryName(team.getCountry().getCountryName());

            teamDto.setCountry(countryDto);

            result.add(teamDto);
        }

        return result;
    }
}
