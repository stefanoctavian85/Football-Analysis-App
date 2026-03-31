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
    public List<CompetitionSeasonsDto> getAllCompetitions(int page, int pageLimit) {
        List<Competition> competitions = competitionRepository.findAll();
        Map<String, CompetitionSeasonsDto> finalCompetitions = new LinkedHashMap<>();

        for (Competition c : competitions) {
            CompetitionSeasonsDto dto = finalCompetitions.get(c.getCompetitionName());

            if (dto != null) {
                dto.getSeasons().add(new SeasonDto(c.getCompetitionPK().getSeasonId(), c.getSeasonName()));
            } else {
                dto = new CompetitionSeasonsDto();
                dto.setCompetitionId(c.getCompetitionPK().getCompetitionId());
                dto.setCompetitionName(c.getCompetitionName());
                dto.setCompetitionGender(c.getCompetitionGender());
                dto.setCountryName(c.getCountryName());
                List<SeasonDto> seasons = new ArrayList<>();
                seasons.add(new SeasonDto(c.getCompetitionPK().getSeasonId(), c.getSeasonName()));
                dto.setSeasons(seasons);
                finalCompetitions.put(c.getCompetitionName(), dto);
            }
        }

        List<CompetitionSeasonsDto> result = new ArrayList<>(finalCompetitions.values());
        result.sort(Comparator.comparing(CompetitionSeasonsDto::getCountryName));

        int totalItems = result.size();
        int fromIndex = (page - 1) * pageLimit;

        if (fromIndex >= totalItems) return List.of();

        int toIndex = Math.min(fromIndex + pageLimit, totalItems);

        return result.subList(fromIndex, toIndex);
    }

    @Override
    public int countAllCompetitions() {
        List<Competition> competitions = competitionRepository.findAll();
        return (int) competitions.stream()
                .map(competition -> competition.getCompetitionName())
                .distinct()
                .count();
    }


}
