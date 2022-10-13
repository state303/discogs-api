package io.dsub.discogs.api.label.model;

import io.dsub.discogs.api.core.entity.PersistableBaseEntity;
import io.dsub.discogs.api.label.command.LabelCommand;
import io.dsub.discogs.api.label.dto.LabelDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "label")
public class Label extends PersistableBaseEntity<Long> {
    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Column("created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column("last_modified_at")
    private LocalDateTime lastModifiedAt;

    @Column("name")
    private String name;

    @Column("data_quality")
    private String dataQuality;

    @Column("profile")
    private String profile;

    @Column("contact_info")
    private String contactInfo;

    public Label withMutableDataFrom(LabelCommand.Update command) {
        return Label.builder()
                .id(this.id)
                .name(command.getName())
                .dataQuality(command.getDataQuality())
                .profile(command.getProfile())
                .contactInfo(command.getContactInfo())
                .build();
    }

    public LabelDTO toDTO() {
        return LabelDTO.builder()
                .id(id)
                .name(name)
                .profile(profile)
                .contactInfo(contactInfo)
                .dataQuality(dataQuality)
                .build();
    }
}
