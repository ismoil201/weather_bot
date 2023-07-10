package org.example;

import org.example.models.CurrentWeather;
import org.example.service.IconService;
import org.example.service.WeatherService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherBot extends TelegramLongPollingBot {

    WeatherService weatherService;

    IconService iconService;

    Map<Long, Location> locationMap;





    public WeatherBot(String botToken, WeatherService weatherService, IconService iconService) {
        super(botToken);
        this.weatherService = weatherService;
        locationMap = new HashMap<>();
        this.iconService = iconService;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage()){
            SendMessage sendMessage = new SendMessage();

            Long chatId = update.getMessage().getChatId();
            sendMessage.setChatId(chatId);


            String userName = update.getMessage().getChat().getUserName();
            String text = update.getMessage().getText();


            printLogs(userName,text);


//
            if(update.getMessage().hasLocation()){
                Location location = update.getMessage().getLocation();
//-----------------------------------------------------------------------------------
//
//                if(update.getMessage().hasLocation()){
//                    location =update.getMessage().getLocation();
//                    locationMap.put(chatId,location);
//
//                } else if (locationMap.get(chatId)!=null) {
//                    location = locationMap.get(chatId);
//                }
//-----------------------------------------------------------------------------------

                CurrentWeather currentWeather =  callWeather(location);
//

                String massage = "-----------------------------\n\t\t\uD83D\uDD0EInformation\n\n" +
                        "\t \uD83D\uDCCDCity : "+currentWeather.getName()+"\uD83C\uDF03 \n" +
                        "\t \uD83C\uDF21\uFE0F "+currentWeather.getMain().getTemp()+" degrees\n" +
//                        iconService.getEmoji(currentWeather.getWeather().get(0).getIcon())+
                        "\t ⛅\uFE0F"+currentWeather.getWeather().get(0).getMain()+" bulutli\n" +
                        "\t \uD83C\uDF2B Bosim "+currentWeather.getMain().getPressure()+"\n"+
                        "\t \uD83D\uDCA7Namlik "+currentWeather.getMain().getHumidity()+"\n"+
                        "\t \uD83D\uDCA8Shamol tezligi "+ currentWeather.getWind().getSpeed()+
                        "\n\n-----------------------------";

                sendMessage.setText(massage);
                System.out.println(massage);
                executeMassage(sendMessage);
            }



            switch (text) {
                case "/start" -> {

                  startMenu(sendMessage,userName);

                }
                case "Uzbek\uD83C\uDDFA\uD83C\uDDFF" -> {
                    sendMessage.setText("Assalomu aleykum\uD83D\uDC4B");
                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    List<KeyboardRow> uzButtons = new ArrayList<>();
                    KeyboardRow keyboardRow = new KeyboardRow();

                    KeyboardButton botInfo = new KeyboardButton();
                    botInfo.setText("Bot haqida malumot\uD83E\uDD16\uD83D\uDCDD");
                    KeyboardButton weatherInfo = new KeyboardButton();
                    weatherInfo.setText("Ob-Havo haqida malumot⛅\uFE0F");
                    KeyboardButton back = new KeyboardButton();
                    back.setText("Orqaga qaytish⬅\uFE0F");


                    keyboardRow.add(weatherInfo);
                    keyboardRow.add(botInfo);
                    keyboardRow.add(back);

                    uzButtons.add(keyboardRow);

                    replyKeyboardMarkup.setKeyboard(uzButtons);
                    sendMessage.setReplyMarkup(replyKeyboardMarkup);

//                    executeMassage(sendMessage);

                }
                case "Ob-Havo haqida malumot⛅\uFE0F" ->
                {
                    sendMessage.setText("Ob havo ⛅\uFE0F malumot bilish uchun lakatsiya joylang");
                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    List<KeyboardRow> obHavoButton = new ArrayList<>();
                    KeyboardRow keyboardRows = new KeyboardRow();


                    KeyboardButton obHavoInfo = new KeyboardButton();
                    obHavoInfo.setText("Lakatsiya joylash\uD83D\uDCCD");
                    obHavoInfo.setRequestLocation(true);

                    KeyboardButton orqaga = new KeyboardButton();
                    orqaga.setText("Orqaga qaytish⬅\uFE0F");

                    keyboardRows.add(obHavoInfo);
                    keyboardRows.add(orqaga);

                    obHavoButton.add(keyboardRows);

                    replyKeyboardMarkup.setKeyboard(obHavoButton);
                    sendMessage.setReplyMarkup(replyKeyboardMarkup);
//                    executeMassage(sendMessage);

                }
                case "Bot haqida malumot\uD83E\uDD16\uD83D\uDCDD" -> {
                    sendMessage.setText("Bot haqida malumot \uD83E\uDD16:\n" +
                            "Погода 24⛅\uFE0F - bu ilg'or AI ob-havo yordamchisi," +
                            " foydalanuvchilarga ob-havo haqida ma'lumot berish uchun mo'ljallangan va " +
                            "real vaqtda prognozlar.");
//                    executeMassage(sendMessage);

                }

                case "Orqaga qaytish⬅\uFE0F" -> {
                    startMenu(sendMessage,userName);
//                    executeMassage(sendMessage);

                }
                case "English\uD83C\uDDEC\uD83C\uDDE7" -> {
                    sendMessage.setText("Hello\uD83D\uDC4B");
                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    List<KeyboardRow> buttons = new ArrayList<>();
                    KeyboardRow keyboardRow = new KeyboardRow();

                    KeyboardButton weatherInfo = new KeyboardButton();
                    weatherInfo.setText("Weather information⛅\uFE0F");

                    KeyboardButton botInfo = new KeyboardButton();
                    botInfo.setText("About the bot\uD83E\uDD16\uD83D\uDCDD");

                    KeyboardButton back = new KeyboardButton();
                    back.setText("Go back⬅\uFE0F");

                    keyboardRow.add(weatherInfo);
                    keyboardRow.add(botInfo);
                    keyboardRow.add(back);

                    buttons.add(keyboardRow);


                    replyKeyboardMarkup.setKeyboard(buttons);
                    sendMessage.setReplyMarkup(replyKeyboardMarkup);
//                    executeMassage(sendMessage);

                }case "Weather information⛅\uFE0F"->{
                    sendMessage.setText("To find out the weather ⛅\uFE0F, add location\uD83D\uDCCD");
                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    List<KeyboardRow> buttons = new ArrayList<>();
                    KeyboardRow keyboardRow = new KeyboardRow();

                    KeyboardButton getLoc = new KeyboardButton();
                    getLoc.setText("Add location\uD83D\uDCCD");
                    getLoc.setRequestLocation(true);

                    KeyboardButton back = new KeyboardButton();
                    back.setText("Go back⬅\uFE0F");

                    keyboardRow.add(getLoc);
                    keyboardRow.add(back);

                    buttons.add(keyboardRow);

                    replyKeyboardMarkup.setKeyboard(buttons);
                    sendMessage.setReplyMarkup(replyKeyboardMarkup);
//                    executeMassage(sendMessage);

                }
                case "About the bot\uD83E\uDD16\uD83D\uDCDD"->{
                    sendMessage.setText("Weather 24⛅\uFE0F " +
                            "is an advanced artificial intelligence based weather \n" +
                            "assistant designed to provide users \n" +
                            "with real-time weather information and forecasts.");
                    executeMassage(sendMessage);

                }case "Go back⬅\uFE0F"->{
                    startMenu(sendMessage,userName);
                }

                case "Русский\uD83C\uDDF7\uD83C\uDDFA" -> {
                    sendMessage.setText("здравствуйте\uD83D\uDC4B");
                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    List<KeyboardRow> buttonsru = new ArrayList<>();
                    KeyboardRow keyboardRow = new KeyboardRow();

                    KeyboardButton weatherInfo = new KeyboardButton();
                    weatherInfo.setText("Прогноз погоды⛅\uFE0F");

                    KeyboardButton botInfo = new KeyboardButton();
                    botInfo.setText("о боте\uD83E\uDD16\uD83D\uDCDD");

                    KeyboardButton back = new KeyboardButton();
                    back.setText("назад⬅\uFE0F");

                    keyboardRow.add(weatherInfo);
                    keyboardRow.add(botInfo);
                    keyboardRow.add(back);

                    buttonsru.add(keyboardRow);

                    replyKeyboardMarkup.setKeyboard(buttonsru);
                    sendMessage.setReplyMarkup(replyKeyboardMarkup);
//                    executeMassage(sendMessage);


                }case "о боте\uD83E\uDD16\uD83D\uDCDD"->{
                    sendMessage.setText("Погода 24⛅\uFE0F— это " +
                            "продвинутый помощник погоды на базе искусственного интеллекта, \n" +
                            "предназначенный для предоставления пользователям информации о \n" +
                            "погоде и прогнозов в реальном времени");
//                    executeMassage(sendMessage);

                }
                case "Прогноз погоды⛅\uFE0F"->{
                    sendMessage.setText("Чтобы узнать погоду ⛅\uFE0F, добавьте геоданные");

                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    List<KeyboardRow> buttons = new ArrayList<>();
                    KeyboardRow keyboardRow = new KeyboardRow();

                    KeyboardButton getLoc = new KeyboardButton();
                    getLoc.setText("Ставит геолокацию\uD83D\uDCCD");
                    getLoc.setRequestLocation(true);

                    KeyboardButton back = new KeyboardButton();
                    back.setText("назад⬅\uFE0F");

                    keyboardRow.add(getLoc);
                    keyboardRow.add(back);

                    buttons.add(keyboardRow);

                    replyKeyboardMarkup.setKeyboard(buttons);
                    sendMessage.setReplyMarkup(replyKeyboardMarkup);

//                    executeMassage(sendMessage);

                }case "назад⬅\uFE0F"->{

                    startMenu(sendMessage,userName);
//                    executeMassage(sendMessage);

                }
                default -> {
                    sendMessage.setText("/start Error tug'ri yozing yoki Xatolik yuz berdi ");
                }

            }
            executeMassage(sendMessage);
        }


    }

    private  void startMenu(SendMessage sendMessage, String userName){
        sendMessage.setText("Hello\uD83D\uDC4B " + userName + "\nEnter language : ");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardButtons = new ArrayList<>();
        KeyboardRow keyboardRows = new KeyboardRow();

        KeyboardButton uzLanguage = new KeyboardButton();
        uzLanguage.setText("Uzbek\uD83C\uDDFA\uD83C\uDDFF");
        KeyboardButton ruLanguage = new KeyboardButton();
        ruLanguage.setText("Русский\uD83C\uDDF7\uD83C\uDDFA");
        KeyboardButton enLanguage = new KeyboardButton();
        enLanguage.setText("English\uD83C\uDDEC\uD83C\uDDE7");


        keyboardRows.add(uzLanguage);
        keyboardRows.add(enLanguage);
        keyboardRows.add(ruLanguage);

        keyboardButtons.add(keyboardRows);

        replyKeyboardMarkup.setKeyboard(keyboardButtons);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

    }
    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername() {
        return "Pogodaa24_bot";
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    private void printLogs(String userName, String text){

        System.out.println(userName+" ---> "+text );
    }
    private CurrentWeather callWeather(Location location){


        CurrentWeather currentWeather= weatherService.getMyWeather(location.getLongitude().toString(),
                location.getLatitude().toString());

        return currentWeather;
    }

    private void executeMassage(SendMessage sendMessage){
        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
