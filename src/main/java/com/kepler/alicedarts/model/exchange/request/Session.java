package com.kepler.alicedarts.model.exchange.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Session {
    private Integer message_id;
    private String session_id;
    private String skill_id;
    private User user;
    private Application application;
    private @JsonProperty("new") boolean new_;
    private String user_id;
}
