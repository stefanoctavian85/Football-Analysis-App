package com.example.footprint.service.football.competition;

import com.example.footprint.domain.dto.football.SeasonDto;
import com.example.footprint.domain.dto.football.combinations.CompetitionSeasonsDto;
import com.example.footprint.domain.entity.football.Competition;
import com.example.footprint.repository.football.CompetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {
    private final CompetitionRepository competitionRepository;

    @Override
    public List<CompetitionSeasonsDto> getAllCompetitions() {
        List<Competition> competitions = competitionRepository.findAll();
        Map<String, CompetitionSeasonsDto> finalCompetitions = new HashMap<>();

        for (Competition c : competitions) {
            CompetitionSeasonsDto dto = finalCompetitions.get(c.getCompetitionName());

            if (dto != null) {
                List<SeasonDto> competitionSeasons = dto.getSeasons();
                competitionSeasons.add(new SeasonDto(c.getCompetitionPK().getSeasonId(), c.getSeasonName()));
                dto.setSeasons(competitionSeasons);
            } else {
                dto = new CompetitionSeasonsDto();
                dto.setCompetitionId(c.getCompetitionPK().getCompetitionId());
                dto.setCompetitionName(c.getCompetitionName());
                dto.setCompetitionGender(c.getCompetitionGender());
                dto.setCountryName(c.getCountryName());
                List<SeasonDto> competitionSeasons = new ArrayList<>();
                competitionSeasons.add(new SeasonDto(c.getCompetitionPK().getSeasonId(), c.getSeasonName()));
                dto.setSeasons(competitionSeasons);
            }

            finalCompetitions.put(c.getCompetitionName(), dto);
        }

        List<CompetitionSeasonsDto> result = new ArrayList<>(finalCompetitions.values());
        result.sort(Comparator.comparing(CompetitionSeasonsDto::getCountryName));

        return result;
    }
}
