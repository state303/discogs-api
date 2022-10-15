package io.dsub.discogs.api.master.model;

import io.dsub.discogs.api.core.entity.PersistableBaseEntity;
import io.dsub.discogs.api.master.command.MasterCommand;
import io.dsub.discogs.api.master.dto.MasterDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@With
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "master")
public class Master extends PersistableBaseEntity<Long> {
    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Column("created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column("last_modified_at")
    private LocalDateTime lastModifiedAt;

    @Size(max = 255)
    @Column("data_quality")
    private String dataQuality;

    @Size(max = 2000)
    @Column("title")
    private String title;

    @Min(0)
    @Column("year")
    private Short year;

    public MasterDTO toDTO() {
        return MasterDTO.builder()
                .id(id)
                .dataQuality(dataQuality)
                .title(title)
                .year(year)
                .build();
    }

    public static Master fromCreateCommand(MasterCommand.Create command) {
        return Master.builder()
                .id(command.getId())
                .dataQuality(command.getDataQuality())
                .title(command.getTitle())
                .year(command.getYear())
                .build();
    }

    public Master withMutableDataFrom(MasterCommand.Update command) {
        return Master.builder()
                .id(this.id)
                .dataQuality(command.getDataQuality())
                .title(command.getTitle())
                .year(command.getYear())
                .build();
    }
}
