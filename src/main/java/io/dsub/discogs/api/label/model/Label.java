package io.dsub.discogs.api.label.model;

import io.dsub.discogs.api.entity.BaseEntity;
import io.dsub.discogs.api.label.dto.LabelDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "label")
public class Label extends BaseEntity<Integer> {
    @Id
    @Column("id")
    private Integer id;

    @NotBlank
    @Size(max = 255)
    @Column("name")
    private String name;

    @Size(max = 255)
    @Column("data_quality")
    private String dataQuality;

    @Column("profile")
    private String profile;

    @Column("contact_info")
    private String contactInfo;

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
