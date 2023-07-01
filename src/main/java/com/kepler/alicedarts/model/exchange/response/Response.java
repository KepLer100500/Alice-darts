package com.kepler.alicedarts.model.exchange.response;

import lombok.Data;

@Data
public class Response {
    private Boolean end_session;
    private String text;
    private String tts;
}
