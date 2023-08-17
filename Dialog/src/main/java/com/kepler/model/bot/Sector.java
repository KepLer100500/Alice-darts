package com.kepler.model.bot;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Sector {
    private String title;
    private Integer chance;
}

