package com.kepler.model.dialog.request;

import lombok.Data;

@Data
public class Entities {
    private String type;
    private Tokens tokens;
    private Object value;
}
