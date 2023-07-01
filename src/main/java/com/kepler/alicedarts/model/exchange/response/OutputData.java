package com.kepler.alicedarts.model.exchange.response;

import com.kepler.alicedarts.model.exchange.request.Session;
import lombok.Data;

@Data
public class OutputData {
    private String version;
    private Session session;
    private Response response;
}
