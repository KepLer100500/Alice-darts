package com.kepler.controller;

import com.kepler.model.rabbitExchange.Tokens;
import com.kepler.model.request.InputData;
import com.kepler.model.response.OutputData;
import com.kepler.model.response.Response;
import com.kepler.proxy.RabbitProducer;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@RequestMapping ("/")
public class Main {
    @Autowired
    RabbitProducer rabbitProducer;

    @PostMapping("/")
    public OutputData home(@RequestBody InputData inputData) {
        OutputData outputData = new OutputData();
        Response response = new Response();
        outputData.setVersion(inputData.getVersion());
        outputData.setSession(inputData.getSession());
        response.setEnd_session(false);

        log.info(inputData.getRequest().getOriginal_utterance()); // logging text request to dialog with alice
        response.setText(String.valueOf(inputData.getRequest().getOriginal_utterance())); // send to user input text
        outputData.setResponse(response);

        rabbitProducer.sendMessage(
                Tokens.builder().tokens(
                        inputData.getRequest().getNlu().getTokens()
                ).build()
        );

        return outputData;
    }
}
