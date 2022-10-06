package io.dsub.discogs.api.style.model;

import io.dsub.discogs.api.entity.BaseEntity;
import io.dsub.discogs.api.style.dto.StyleDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Size;

@Table(name = "style")

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Style extends BaseEntity<String> {
    @Id
    @Size(min = 1, max = 255)
    @Column("name")
    private String name;

    @Override
    @Transient
    public String getId() {
        return getName();
    }

    public StyleDTO toDTO() {
        return new StyleDTO(getName());
    }
}
