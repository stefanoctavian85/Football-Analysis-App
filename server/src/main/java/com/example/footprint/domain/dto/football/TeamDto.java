package com.example.footprint.domain.dto.football;

import lombok.Data;

@Data
public class TeamDto {
    private int teamId;
    private String teamName;
    private String teamGender;
    private CountryDto country;
}
