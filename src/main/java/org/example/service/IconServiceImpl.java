package org.example.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.CurrentWeather;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.InflaterInputStream;

public class IconServiceImpl implements  IconService{
    @Override
    public String getEmoji(String iconCode) {
        File file = new File("src/main/resources/icon.json");
        Map<String, String> iconMap = new HashMap<>();
        try {
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String row;
            StringBuilder stringBuilder = new StringBuilder();
            while ((row=bufferedReader.readLine())!=null){
                stringBuilder.append(row);

                Type typeToken = new TypeToken<Map<String, String>>(){
                }.getType();
                Gson  gson = new Gson();

               iconMap = gson.fromJson(stringBuilder.toString(),typeToken);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return iconMap.get(iconCode);
    }
}
