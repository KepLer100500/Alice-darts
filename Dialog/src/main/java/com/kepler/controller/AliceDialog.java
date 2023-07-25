package com.kepler.controller;

import com.kepler.model.Command;
import com.kepler.model.ResultCalculations;
import com.kepler.model.Tokens;
import com.kepler.model.request.InputData;
import com.kepler.model.response.OutputData;
import com.kepler.model.response.Response;
import com.kepler.proxy.RabbitConsumer;
import com.kepler.proxy.RabbitProducer;
import com.kepler.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.kepler.model.Command.*;


@Slf4j
@RestController
@RequestMapping ("darts/api/v1/") // url for alice dialog webhook
public class AliceDialog {
    @Autowired
    private RabbitProducer rabbitProducer;

    @Autowired
    private RabbitConsumer rabbitConsumer;

    @Autowired
    private UserService userService;

    private Command command; // last command from dialog

    /**
     * Get text form dialog with Alice, parse and calculate points and return sum points
     * @param inputData
     * @return OutputData outputData
     */
    @PostMapping("/")
    public OutputData entryPoint(@RequestBody InputData inputData) {
        OutputData outputData = buildOutputData(inputData);
        Response response = buildResponse();
        String text = "";
        if(inputData.getSession().isNew_()) {
            command = GREETING;
        }

        switch (command) {
            case GREETING : text = greetingUser(inputData); break;
            case REGISTER : text = registerUser(inputData); break;
            case NAME_VERIFICATION : text = confirmNickName(inputData); break;
            case RENAME : text = renameUser(inputData); break;
            case START_GAME : text = confirmStartGame(inputData); break;
            default: text = "Неизвестная команда. Повторите пожалуйста запрос.";
        }

        response.setText(text);

        // Integer summaryPoints = sendTextAndReceiveCalculatedPoints(inputData.getRequest().getNlu().getTokens());
        // Response response = buildResponse(summaryPoints);
        //log.info("Send to dialog: {}", summaryPoints);

        outputData.setResponse(response);
        return outputData;
    }

    private String confirmStartGame(InputData inputData) {
        if(inputData.getRequest().getOriginal_utterance().toLowerCase().contains("да") ||
                inputData.getRequest().getOriginal_utterance().toLowerCase().contains("ага")) {
            return "Игра начата";
        } else {
            return "Игра не начата";
        }
    }

    private String renameUser(InputData inputData) {
        userService.renameUser(inputData.getSession().getUser_id(), inputData.getRequest().getOriginal_utterance());
        command = Command.NAME_VERIFICATION;
        return "Мне называть вас " + inputData.getRequest().getOriginal_utterance() + "?";
    }

    private String confirmNickName(InputData inputData) {
        if(inputData.getRequest().getOriginal_utterance().toLowerCase().contains("да") ||
                inputData.getRequest().getOriginal_utterance().toLowerCase().contains("ага")) {
            command = Command.START_GAME;
            return "Хорошо, " + userService.getNickName(inputData.getSession().getUser_id()) + ". Начать игру?";
        } else {
            command = Command.RENAME;
            return "А как тогда вас называть?";
        }
    }

    private String registerUser(InputData inputData) {
        userService.registerUser(inputData.getSession().getUser_id(),
                inputData.getRequest().getOriginal_utterance());
        command = Command.NAME_VERIFICATION;
        return "Мне называть вас " + inputData.getRequest().getOriginal_utterance() + "?";
    }

    private String greetingUser(InputData inputData) {
        if(userService.isNewUser(inputData.getSession().getUser_id())) { // check user in DB
            command = Command.REGISTER;
            return  "Здравствуйте. Кажется вы в первый раз запустили этот навык, представьтесь, как вас зовут?";
        } else {
            String username = userService.getNickName(inputData.getSession().getUser_id());
            command = Command.NAME_VERIFICATION;
            return  "Здравствуйте. Кажется вы уже запускали этот навык. В прошлый раз я звала вас " + username + ". Продолжить называть вас так же?";
        }
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
     * @param
     * @return Response response
     */
    // private Response buildResponse(Integer summaryPoints) {
    private Response buildResponse() {
        return Response.builder()
                .end_session(false)
                //.text("Вы набрали " + summaryPoints + " очков")
                .build();
    }

}
