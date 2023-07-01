package com.kepler.alicedarts.model.exchange.request;

import lombok.Data;

@Data
public class Nlu {
    private String[] tokens;
    private Entities[] entities;
    private Intents intents;
}
