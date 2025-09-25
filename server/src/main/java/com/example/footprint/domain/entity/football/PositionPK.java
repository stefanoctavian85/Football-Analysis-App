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
public class PositionPK implements Serializable {
    @Column(name = "position_id", nullable = false)
    private int positionId;

    @Column(name = "lineup_id", nullable = false)
    private int lineupId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PositionPK that = (PositionPK) o;
        return positionId == that.positionId && lineupId == that.lineupId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionId, lineupId);
    }
}
