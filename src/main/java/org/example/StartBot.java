package org.example;

import org.example.service.IconService;
import org.example.service.IconServiceImpl;
import org.example.service.WeatherService;
import org.example.service.WeatherServiceImpl;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class StartBot {
    public static void main(String[] args) {

        IconService iconService = new IconServiceImpl();
        WeatherService weatherService = new WeatherServiceImpl();
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            WeatherBot weatherBot = new WeatherBot("6360479840:AAGgMrN_nfm0L9U6mbYOO_sbU4dFEKAP1RQ",
                    weatherService, iconService);

            telegramBotsApi.registerBot(weatherBot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}