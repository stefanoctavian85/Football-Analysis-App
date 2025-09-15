package com.example.footprint.repository.football;

import com.example.footprint.domain.entity.football.Competition;
import com.example.footprint.domain.entity.football.CompetitionPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionRepository extends JpaRepository<Competition, CompetitionPK> {

}
