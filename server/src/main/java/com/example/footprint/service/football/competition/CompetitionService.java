package com.example.footprint.service.football.competition;

import com.example.footprint.domain.dto.football.CompetitionDto;
import com.example.footprint.domain.dto.football.combinations.CompetitionSeasonsDto;

import java.util.List;

public interface CompetitionService {
    List<CompetitionSeasonsDto> getAllCompetitions();
}
