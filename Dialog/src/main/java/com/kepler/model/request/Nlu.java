package com.kepler.model.request;

import lombok.Data;

@Data
public class Nlu {
    private String[] tokens;
    private Entities[] entities;
    private Intents intents;
}
