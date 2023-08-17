package com.kepler.model.dialog.request;

import lombok.Data;

@Data
public class Meta {
    private String locale;
    private String timezone;
    private String client_id;
    private Interfaces interfaces;
}
