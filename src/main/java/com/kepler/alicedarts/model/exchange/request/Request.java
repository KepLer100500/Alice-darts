package com.kepler.alicedarts.model.exchange.request;

import lombok.Data;

@Data
public class Request {
    private String command;
    private String original_utterance;
    private Nlu nlu;
    private Markup markup;
    private String type;
}
