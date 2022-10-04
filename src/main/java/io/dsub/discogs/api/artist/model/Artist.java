package io.dsub.discogs.api.artist.model;

import io.dsub.discogs.api.artist.command.ArtistCommand;
import io.dsub.discogs.api.artist.dto.ArtistDTO;
import io.dsub.discogs.api.entity.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;


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
    private String name;

    @Column("real_name")
    private String realName;

    @Column("profile")
    private String profile;

    @Column("data_quality")
    private String dataQuality;

    public Artist withMutableDataFrom(ArtistCommand.UpdateArtistCommand that) {
        return Artist.builder()
                .id(this.id)
                .profile(that.getProfile())
                .dataQuality(that.getDataQuality())
                .realName(that.getRealName())
                .name(that.getName())
                .build();
    }

    public Mono<ArtistDTO> toDTO() {
        return Mono.just(ArtistDTO.builder()
                .id(id)
                .profile(profile)
                .dataQuality(dataQuality)
                .realName(realName)
                .name(name)
                .build());
    }
}
