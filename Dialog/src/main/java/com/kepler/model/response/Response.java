package com.kepler.model.response;

import lombok.Data;

@Data
public class Response {
    private Boolean end_session;
    private String text;
    private String tts;
}
