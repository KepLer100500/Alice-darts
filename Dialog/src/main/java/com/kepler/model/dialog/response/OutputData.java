package com.kepler.model.dialog.response;

import com.kepler.model.dialog.request.Session;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OutputData {
    private String version;
    private Session session;
    private Response response;
}
