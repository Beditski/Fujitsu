package com.fujitsu.deliveryapp.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import com.fujitsu.deliveryapp.DAO.WeatherStationDAO.WeatherStationDAO;
import com.fujitsu.deliveryapp.models.WeatherStation;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 * Controller for implementing weather stations' info cronjob request.
 */
@Controller
public class WeatherStationController {
    private final String weatherDataUrl;
    private final WeatherStationDAO weatherStationDAO;
    private final RestTemplate restTemplate;

    /**
     * Constructor of WeatherStationController controller-class.
     * @param weatherDataUrl url for the request from Estonian Environment Agency.
     * @param weatherStationDAO Weather Station Data Access Object.
     * @param restTemplate tool for making an HTTP request.
     */
    @Autowired
    private WeatherStationController(
            @Value("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php") String weatherDataUrl,
            WeatherStationDAO weatherStationDAO, RestTemplate restTemplate) {
        this.weatherDataUrl = weatherDataUrl;
        this.weatherStationDAO = weatherStationDAO;
        this.restTemplate = restTemplate;
    }

    /**
     * Requests weather data from the weather portal of the Estonian Environment Agency with the frequency of
     * every hour, 15 minutes after a full hour (HH:15:00) and then adds it to the database.
     */
    @Scheduled(cron = "* 15 1 * * *")
    private void fetchWeatherData() throws SQLException {
        // Parse page into String
        String weatherData = this.restTemplate.getForObject(weatherDataUrl, String.class);
        // If result not null - then update weather info
        if (weatherData != null) {
            weatherStationDAO.updateWeatherData(parseWeatherData(weatherData));
        }
    }

    /**
     * Takes int string format content of XML file with weather stations' information, processes it and
     * returns the list of WeatherStation classes, which contains the following weather stations:
     * Tallinn-Harku, Tartu-Tõravere, Pärnu.Each weatherStation class contains: wmo code, name, air
     * temperature, wind speed, weather phenomenon, timestamp.
     * @param weatherData parsed XML file which contains weather stations' information.
     * @return WeatherStation list.
     */
    private static List<WeatherStation> parseWeatherData(String weatherData) {
        /*
         * Remove all new line characters, tabulations and tabulation-like whitespace characters and
         * </station> substrings. Removal of </station> allows to split the string easily later on into
         * the array of strings, where each string contains weather station's info and information about
         * the weather conditions in the location of this station.
         */
        weatherData = weatherData.replaceAll("</station>|[\n\t]|/| {4}", "");

        /*
         * Find the index of a substring in weatherData where the timestamp value begins,
         * where it ends and extract the content between (i.e. extract timestamp itself), then
         * parse in to long type.
         */
        long timestamp = Long.parseLong(extractSubstring(weatherData, "timestamp=\"", "\""));


        /*
         * Remove all the information in the weatherData before the first substring "<station>" (i.e. timestamp), since it is redundant. Then split
         * weatherData by the deliminator "<station>". Each string in the resulted array contains weather station's info and information about the
         * weather conditions in the location of this station.
         */
        String[] weatherStations = weatherData.substring(weatherData.indexOf("<station>") + "<station>".length()).split("<station>");

        List<WeatherStation> weatherStationList = new ArrayList<>();

        /*
         * Iterate over all weather stations. If current weather is in Tallinn, Tartu or Pärnu - add its information to the DB.
         */
        for (String station : weatherStations) {
            if (station.contains("<name>Tallinn-Harku<name>") || station.contains("<name>Tartu-Tõravere<name>") || station.contains("<name>Pärnu<name>")) {
                WeatherStation weatherStation = new WeatherStation(
                        extractSubstring(station, "<name>"),
                        Integer.parseInt(extractSubstring(station, "<wmocode>")),
                        Double.parseDouble(extractSubstring(station, "<airtemperature>")),
                        Double.parseDouble(extractSubstring(station, "<windspeed>")),
                        extractSubstring(station, "<phenomenon>"),
                        timestamp
                );
                weatherStationList.add(weatherStation);
            }
        }
        return weatherStationList;
    }

    /**
     * Returns the first substring of the input between two specified delimiters.
     * @param content text string.
     * @param firstDelimiter first delimiter.
     * @param secondDelimiter second delimiter.
     * @return the first substring between two delimiters.
     */
    private static String extractSubstring(String content, String firstDelimiter, String secondDelimiter) {
        int startIndex = content.indexOf(firstDelimiter) + firstDelimiter.length();
        int endIndex = content.indexOf(secondDelimiter, startIndex);

        return content.substring(startIndex, endIndex);
    }

    /**
     * Returns the first substring of the input between two identical delimiters.
     * @param content string of content.
     * @param delimiter delimiter.
     * @return the first substring between two delimiters
     */
    private static String extractSubstring(String content, String delimiter) {
        return extractSubstring(content, delimiter, delimiter);
    }
}