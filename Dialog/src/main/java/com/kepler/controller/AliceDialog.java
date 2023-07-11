package com.kepler.controller;

import com.kepler.model.ResultCalculations;
import com.kepler.model.Tokens;
import com.kepler.model.request.InputData;
import com.kepler.model.response.OutputData;
import com.kepler.model.response.Response;
import com.kepler.proxy.RabbitConsumer;
import com.kepler.proxy.RabbitProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping ("darts/api/v1/") // url for alice dialog webhook
public class AliceDialog {
    @Autowired
    private RabbitProducer rabbitProducer;

    @Autowired
    private RabbitConsumer rabbitConsumer;

    /**
     * Get text form dialog with Alice, parse and calculate points and return sum points
     * @param inputData
     * @return OutputData outputData
     */
    @PostMapping("/")
    public OutputData entryPoint(@RequestBody InputData inputData) {
        OutputData outputData = buildOutputData(inputData);
        Integer summaryPoints = sendTextAndReceiveCalculatedPoints(inputData.getRequest().getNlu().getTokens());
        Response response = buildResponse(summaryPoints);
        log.info("Send to dialog: {}", summaryPoints);
        outputData.setResponse(response);
        return outputData;
    }

    /**
     * Send array words through rabbitmq, calculate sum points and get it
     * @param tokens
     * @return Integer result
     */
    private Integer sendTextAndReceiveCalculatedPoints(String[] tokens) {
        rabbitProducer.sendMessage(
                Tokens.builder().tokens(tokens).build()
        );
        ResultCalculations result = rabbitConsumer.receiveMessage();
        return result.getResultCalculations();
    }

    /**
     * Build main receive message for dialog
     * @param inputData
     * @return OutputData outputData
     */
    private OutputData buildOutputData(InputData inputData) {
        return OutputData.builder()
                .version(inputData.getVersion())
                .session(inputData.getSession())
                .build();
    }

    /**
     * Build text message for receive to dialog
     * @param summaryPoints
     * @return Response response
     */
    private Response buildResponse(Integer summaryPoints) {
        return Response.builder()
                .end_session(false)
                .text("Вы набрали " + summaryPoints + " очков")
                .build();
    }

}
