package com.example.footprint.domain.dto.football.combinations;

import com.example.footprint.domain.dto.football.SeasonDto;
import lombok.Data;

import java.util.List;

@Data
public class CompetitionSeasonsDto {
    private int competitionId;
    private String countryName;
    private String competitionName;
    private String competitionGender;
    private List<SeasonDto> seasons;
}
