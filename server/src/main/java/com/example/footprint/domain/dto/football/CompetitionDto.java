package com.example.footprint.domain.dto.football;

import lombok.Data;

@Data
public class CompetitionDto {
    private int competitionId;
    private String countryName;
    private String competitionName;
    private String competitionGender;
    private SeasonDto seasonDto;
}
