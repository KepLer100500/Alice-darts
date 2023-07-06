package com.kepler.model.response;

import com.kepler.model.request.Session;
import lombok.Data;

@Data
public class OutputData {
    private String version;
    private Session session;
    private Response response;
}
