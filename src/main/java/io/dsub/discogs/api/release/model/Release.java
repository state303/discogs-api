package io.dsub.discogs.api.release.model;

import io.dsub.discogs.api.core.entity.PersistableBaseEntity;
import io.dsub.discogs.api.release.command.ReleaseCommand;
import io.dsub.discogs.api.release.dto.ReleaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@Table(name = "release")
public class Release extends PersistableBaseEntity<Long> {
    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Column("created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column("last_modified_at")
    private LocalDateTime lastModifiedAt;

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
    private LocalDate releaseDate;

    @Column("listed_release_date")
    private String listedReleaseDate;

    @Column("status")
    private String status;

    @Column("title")
    private String title;

    public ReleaseDTO toDTO() {
        return ReleaseDTO.builder()
                .id(id)
                .country(country)
                .dataQuality(dataQuality)
                .hasValidDay(hasValidDay)
                .hasValidMonth(hasValidMonth)
                .hasValidYear(hasValidYear)
                .masterId(masterId)
                .isMaster(isMaster)
                .notes(notes)
                .releaseDate(releaseDate)
                .listedReleaseDate(listedReleaseDate)
                .status(status)
                .title(title)
                .build();
    }

    public static Release fromCreateCommand(ReleaseCommand.Create command) {
        return Release.builder()
                .id(command.getId())
                .country(command.getCountry())
                .dataQuality(command.getDataQuality())
                .listedReleaseDate(command.getReleaseDate())
                .build();
    }
}
