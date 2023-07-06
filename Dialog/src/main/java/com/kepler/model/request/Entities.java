package com.kepler.model.request;

import lombok.Data;

@Data
public class Entities {
    private String type;
    private Tokens tokens;
    private Object value;
}
