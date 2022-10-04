package io.dsub.discogs.api.genre.model;


import io.dsub.discogs.api.entity.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Size;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "genre")
public class Genre extends BaseEntity<String> {
    @Id
    @Column("name")
    @Size(min = 1, max = 255)
    private String name;

    @Override
    public String getId() {
        return getName();
    }
}
