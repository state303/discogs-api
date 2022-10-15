package io.dsub.discogs.api.style.model;

import io.dsub.discogs.api.core.entity.PersistableBaseEntity;
import io.dsub.discogs.api.style.dto.StyleDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Getter
@Builder
@AllArgsConstructor
@Table(name = "style")
public class Style extends PersistableBaseEntity<String> {
    @Id
    @NotBlank
    @Size(max = 255)
    @Column("name")
    private String name;

    @NotNull
    @Column("created_at")
    private LocalDateTime createdAt;

    @Override
    @Transient
    public String getId() {
        return getName();
    }

    public StyleDTO toDTO() {
        return StyleDTO.builder().name(getName()).build();
    }
}
