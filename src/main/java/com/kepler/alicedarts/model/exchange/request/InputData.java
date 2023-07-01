package com.kepler.alicedarts.model.exchange.request;

import lombok.Data;

@Data
public class InputData {
    private Meta meta;
    private Session session;
    private Request request;
    private String version;
}

