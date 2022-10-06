package io.dsub.discogs.api.release.model;

import io.dsub.discogs.api.core.entity.BaseEntity;
import io.dsub.discogs.api.release.dto.ReleaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@Table(name = "release")
public class Release extends BaseEntity<Long> {
    @Id
    @Column("id")
    private Long id;

    @Column("country")
    private String country;

    @Column("data_quality")
    private String dataQuality;

    @Column("has_valid_day")
    private boolean hasValidDay;

    @Column("has_valid_month")
    private boolean hasValidMonth;

    @Column("has_valid_year")
    private boolean hasValidYear;

    @Column("master_id")
    private Long masterId;

    @Column("is_master")
    private boolean isMaster;

    @Column("notes")
    private String notes;

    @Column("release_date")
    private LocalDateTime releaseDate;

    @Column("release_date_reported")
    private String releaseDateReported;

    @Column("status")
    private String status;

    @Column("title")
    private String title;

    public ReleaseDTO toDTO() {
        return ReleaseDTO.builder()
                .id(id)
                .country(country)
                .dataQuality(dataQuality)
                .releaseDate(releaseDate)
                .releaseDateReported(releaseDateReported)
                .hasValidDay(hasValidDay)
                .hasValidMonth(hasValidMonth)
                .hasValidYear(hasValidYear)
                .isMaster(isMaster)
                .masterId(masterId)
                .notes(notes)
                .title(title)
                .status(status)
                .build();
    }
}
