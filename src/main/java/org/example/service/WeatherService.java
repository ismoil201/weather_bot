package org.example.service;

import org.example.models.CurrentWeather;

public interface WeatherService {
    CurrentWeather getMyWeather(String lon, String lat);

}
