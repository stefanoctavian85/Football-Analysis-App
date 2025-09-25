package com.example.footprint.domain.entity.mapper;

import com.example.footprint.domain.dto.football.TeamDto;
import com.example.footprint.domain.entity.football.Team;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    TeamDto toDto(Team team);
    Team fromDto(TeamDto teamDto);
}
