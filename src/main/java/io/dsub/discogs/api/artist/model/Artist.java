package io.dsub.discogs.api.artist.model;

import io.dsub.discogs.api.artist.command.ArtistCommand;
import io.dsub.discogs.api.artist.dto.ArtistDTO;
import io.dsub.discogs.api.core.entity.PersistableBaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "artist")
public class Artist extends PersistableBaseEntity<Long> {
    @Id
    @NotNull
    @Min(0)
    @Column("id")
    private Long id;

    @Column("created_at")
    @NotNull
    private LocalDateTime createdAt;

    @Column("last_modified_at")
    @NotNull
    private LocalDateTime lastModifiedAt;

    @Column("name")
    @NotBlank
    private String name;

    @Column("real_name")
    private String realName;

    @Column("profile")
    private String profile;

    @Column("data_quality")
    private String dataQuality;

    public Artist withMutableDataFrom(ArtistCommand.Update command) {
        return Artist.builder()
                .id(this.id)
                .profile(command.getProfile())
                .dataQuality(command.getDataQuality())
                .realName(command.getRealName())
                .name(command.getName())
                .lastModifiedAt(LocalDateTime.now())
                .build();
    }

    public ArtistDTO toDTO() {
        return ArtistDTO.builder()
                .id(this.id)
                .profile(this.profile)
                .dataQuality(this.dataQuality)
                .realName(this.realName)
                .name(this.name)
                .build();
    }
}
