package com.example.footprint.repository.football;

import com.example.footprint.domain.entity.football.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Integer> {

}
