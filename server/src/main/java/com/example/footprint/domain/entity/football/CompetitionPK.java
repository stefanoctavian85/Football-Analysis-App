package com.example.footprint.domain.entity.football;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Data
public class CompetitionPK implements Serializable {
    @Column(name = "competition_id")
    private int competitionId;
    @Column(name = "season_id")
    private int seasonId;

    @Override
    public int hashCode() {
        return Objects.hash(competitionId, seasonId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof CompetitionPK competitionPK)) return false;

        return competitionId == competitionPK.competitionId && seasonId == competitionPK.seasonId;
    }
}
