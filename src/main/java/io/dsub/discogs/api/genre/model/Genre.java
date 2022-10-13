package io.dsub.discogs.api.genre.model;


import io.dsub.discogs.api.core.entity.PersistableBaseEntity;
import io.dsub.discogs.api.genre.dto.GenreDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@Table(name = "genre")
public class Genre extends PersistableBaseEntity<String> {
    @Id
    @Column("name")
    @NotBlank
    @Size(max = 255)
    private String name;

    @Column("created_at")
    @NotNull
    private LocalDateTime createdAt;

    @Override
    @Transient
    public String getId() {
        return getName();
    }

    public GenreDTO toDTO() {
        return new GenreDTO(getName());
    }
}
