package com.kepler.model.dialog.request;

import lombok.Data;

@Data
public class InputData {
    private Meta meta;
    private Session session;
    private Request request;
    private String version;
}

