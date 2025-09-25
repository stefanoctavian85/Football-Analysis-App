package com.example.footprint.domain.dto.football;

import com.example.footprint.domain.entity.football.Country;
import lombok.Data;

@Data
public class TeamDto {
    private int teamId;
    private String teamName;
    private String teamGender;
    private Country country;
}
