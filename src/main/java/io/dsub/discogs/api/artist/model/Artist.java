package io.dsub.discogs.api.artist.model;

import io.dsub.discogs.api.artist.command.ArtistCommand;
import io.dsub.discogs.api.artist.dto.ArtistDTO;
import io.dsub.discogs.api.entity.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;


@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "artist")
public class Artist extends BaseEntity<Integer> {
    @Id
    @Column("id")
    private Integer id;

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
                .build();
    }

    public ArtistDTO toDTO() {
        return ArtistDTO.builder()
                .id(id)
                .profile(profile)
                .dataQuality(dataQuality)
                .realName(realName)
                .name(name)
                .build();
    }
}
