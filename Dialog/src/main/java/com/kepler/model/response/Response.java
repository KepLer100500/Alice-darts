package com.kepler.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    private Boolean end_session;
    private String text;
    private String tts;
}
