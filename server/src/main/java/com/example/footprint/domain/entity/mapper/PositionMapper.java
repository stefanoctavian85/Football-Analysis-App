package com.example.footprint.domain.entity.mapper;

import com.example.footprint.domain.dto.football.PositionDto;
import com.example.footprint.domain.entity.football.Position;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PositionMapper {
    PositionDto toDto(Position position);
    Position fromDto(PositionDto positionDto);
}
