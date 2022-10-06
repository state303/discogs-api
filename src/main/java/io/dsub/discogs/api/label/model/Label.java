package io.dsub.discogs.api.label.model;

import io.dsub.discogs.api.entity.BaseEntity;
import io.dsub.discogs.api.label.command.LabelCommand;
import io.dsub.discogs.api.label.dto.LabelDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "label")
public class Label extends BaseEntity<Integer> {
    @Id
    @Column("id")
    private Integer id;

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
