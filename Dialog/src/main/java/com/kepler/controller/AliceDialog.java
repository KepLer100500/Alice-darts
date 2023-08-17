package com.kepler.controller;

import com.kepler.model.game.Command;
import com.kepler.model.calculator.ResultCalculations;
import com.kepler.model.calculator.Tokens;
import com.kepler.model.dialog.request.InputData;
import com.kepler.model.dialog.response.OutputData;
import com.kepler.model.dialog.response.Response;
import com.kepler.proxy.RabbitConsumer;
import com.kepler.proxy.RabbitProducer;
import com.kepler.service.Bot;
import com.kepler.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.kepler.model.game.Command.*;


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

    @Autowired
    private Bot bot;

    private OutputData outputData;
    private Response response;
    private Command command; // last command from dialog
    private Integer turnCounter;
    private Integer totalUserScopes;
    private Integer totalBotScopes;

    /**
     * Get text form dialog with Alice, parse and calculate points and return sum points
     * @param inputData
     * @return OutputData outputData
     */
    @PostMapping("/")
    public OutputData entryPoint(@RequestBody InputData inputData) {
        outputData = buildOutputData(inputData);
        response = buildResponse();

        String text = "";

        if(inputData.getSession().isNew_()) {
            command = GREETING;
            resetGameCounters();
        }

        switch (command) {
            case GREETING : text = greetingUser(inputData); break;
            case REGISTER : text = registerUser(inputData); break;
            case NAME_VERIFICATION : text = confirmNickName(inputData); break;
            case RENAME : text = renameUser(inputData); break;
            case START_GAME : text = confirmStartGame(inputData); break;
            case USER_TURN: text = userTurn(inputData); break;
            default: text = "Неизвестная команда. Повторите пожалуйста запрос.";
        }

        response.setText(text);

        outputData.setResponse(response);
        return outputData;
    }

    private void resetGameCounters() {
        turnCounter = 0;
        totalUserScopes = 0;
        totalBotScopes = 0;
    }

    private String userTurn(InputData inputData) {

            Integer userPoints = sendTextAndReceiveCalculatedPoints(inputData.getRequest().getNlu().getTokens());
            totalUserScopes += userPoints;
            Integer botPoints = sendTextAndReceiveCalculatedPoints(bot.makeMove());
            totalBotScopes += botPoints;
            turnCounter++;

            if(turnCounter >= 3) {
                String text = "";
                if(totalUserScopes > totalBotScopes) {
                    text = "Поздравляем, вы выиграли! Вы набрали " + totalUserScopes + " очков. Сыграем ещё?";
                } else {
                    text = "Вы проиграли, ваши очки " + totalUserScopes + ". Сыграем ещё?";
                }
                command = START_GAME;
                resetGameCounters();
                return text;
            }

            return "Вы заработали " + userPoints + " очков... А я заработала " + botPoints + " очков. Ваш ход.";
    }

    private String confirmStartGame(InputData inputData) {
        if(inputData.getRequest().getOriginal_utterance().toLowerCase().contains("да") ||
                inputData.getRequest().getOriginal_utterance().toLowerCase().contains("ага")) {
            command = USER_TURN;
            return "Игра начата, ваш ход. Какие секторы вы поразили?";
        } else {
            command = GREETING;
            return "Тогда, пожалуй, перезапущу навык, просто потому что я могу это сделать, почему бы и нет.";
        }
    }

    private String renameUser(InputData inputData) {
        userService.renameUser(inputData.getSession().getUser_id(), inputData.getRequest().getOriginal_utterance());
        command = NAME_VERIFICATION;
        return "Мне называть вас " + inputData.getRequest().getOriginal_utterance() + "?";
    }

    private String confirmNickName(InputData inputData) {
        if(inputData.getRequest().getOriginal_utterance().toLowerCase().contains("да") ||
                inputData.getRequest().getOriginal_utterance().toLowerCase().contains("ага")) {
            command = START_GAME;
            return "Хорошо, " + userService.getNickName(inputData.getSession().getUser_id()) + ". Начать игру?";
        } else {
            command = RENAME;
            return "А как тогда вас называть?";
        }
    }

    private String registerUser(InputData inputData) {
        userService.registerUser(inputData.getSession().getUser_id(),
                inputData.getRequest().getOriginal_utterance());
        command = NAME_VERIFICATION;
        return "Мне называть вас " + inputData.getRequest().getOriginal_utterance() + "?";
    }

    private String greetingUser(InputData inputData) {
        if(userService.isNewUser(inputData.getSession().getUser_id())) { // check user in DB
            command = REGISTER;
            return  "Здравствуйте. Кажется вы в первый раз запустили этот навык, представьтесь, как вас зовут?";
        } else {
            String username = userService.getNickName(inputData.getSession().getUser_id());
            command = NAME_VERIFICATION;
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
    private Response buildResponse() {
        return Response.builder()
                .end_session(false)
                .build();
    }

}
