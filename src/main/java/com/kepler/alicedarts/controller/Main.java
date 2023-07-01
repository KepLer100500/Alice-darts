package com.kepler.alicedarts.controller;

import com.kepler.alicedarts.model.exchange.request.InputData;
import com.kepler.alicedarts.model.exchange.response.OutputData;
import com.kepler.alicedarts.model.exchange.response.Response;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@RequestMapping ("/")
public class Main {
    @PostMapping("/")
    public OutputData home(@RequestBody InputData inputData) {
        OutputData outputData = new OutputData();
        Response response = new Response();
        outputData.setVersion(inputData.getVersion());
        outputData.setSession(inputData.getSession());
        response.setEnd_session(false);

        response.setText(inputData.getRequest().getOriginal_utterance());
        log.info(inputData.getRequest().getOriginal_utterance());

        outputData.setResponse(response);
        return outputData;
    }
}
