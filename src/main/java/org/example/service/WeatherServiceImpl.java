package org.example.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.CurrentWeather;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;

public class WeatherServiceImpl implements WeatherService{
    FileServiceImpl fileService = new FileServiceImpl();
    @Override
    public CurrentWeather getMyWeather(String lat, String lon) {
//37.319885    126.833160
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?" +
                    "lat=37.319885"+
                    "&lon=126.833160"+
                    "&units=metric&appid=b18b0810c0b69f4ce19e8b2f8041446a");

            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String row;
            StringBuilder stringBuilder =new StringBuilder();
            while ((row = bufferedReader.readLine()) != null) {
                stringBuilder.append(row);
            }
            String text = stringBuilder.toString();

            fileService.fileWrite(text+" /\n");

            Type typeToken = new TypeToken<CurrentWeather>() {
            }.getType();
            Gson gson = new Gson();
            CurrentWeather currentWeather = gson.fromJson(stringBuilder.toString(), typeToken);

            System.out.println("temp"+currentWeather.getMain().getTemp());

            return currentWeather;





        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}


//    URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=41.2827379&lon=69.1145595&units=metric&appid=b18b0810c0b69f4ce19e8b2f8041446a");
//
//            URLConnection urlConnection = url.openConnection();
//            InputStream inputStream = urlConnection.getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String row;
//            StringBuilder stringBuilder= new StringBuilder();
//            while ((row=bufferedReader.readLine())==null){
//                stringBuilder.append(row);
//            }
//
//
//            Gson gson = new Gson();
//            CurrentWeather currentWeather = gson.fromJson(stringBuilder.toString(),CurrentWeather.class);
//
//            System.out.println("Trom"+currentWeather.getName());
//
//            return currentWeather;