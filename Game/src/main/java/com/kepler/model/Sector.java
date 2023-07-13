package com.kepler.model;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Sector {
    private String title;
    private Integer chance;
}

