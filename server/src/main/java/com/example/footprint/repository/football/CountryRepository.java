package com.example.footprint.repository.football;

import com.example.footprint.domain.entity.football.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer> {

}
