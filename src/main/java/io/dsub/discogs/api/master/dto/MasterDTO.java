package io.dsub.discogs.api.master.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@With
@Data
@Builder
public class MasterDTO {
    private final Long id;
    private final String dataQuality;
    private final String title;
    private final Short year;
}
