package com.example.footprint.domain.entity.football;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "competitions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Competition {
    @EmbeddedId
    private CompetitionPK competitionPK;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "competition_name")
    private String competitionName;

    @Column(name = "competition_gender")
    private String competitionGender;

    @Column(name = "season_name")
    private String seasonName;
}
