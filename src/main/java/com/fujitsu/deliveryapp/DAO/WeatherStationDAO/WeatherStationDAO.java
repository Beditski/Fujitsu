package com.fujitsu.deliveryapp.DAO.WeatherStationDAO;

import java.sql.*;
import java.util.List;

import com.fujitsu.deliveryapp.models.WeatherStation;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Data Access Object for storing weather stations' info.
 */
@Component
public class WeatherStationDAO {
    private static String dataBaseDriver;
    private static String dataBaseUrl;
    private static String dataBaseUsername;
    private static String dataBasePassword;
    private static Connection connection;

    /**
     * Constructor for WeatherStationDAO. Method injects values for the DB driver, url, username and password.
     * @param dataBaseDriver driver.
     * @param dataBaseUrl DB url.
     * @param dataBaseUsername DB username.
     * @param dataBasePassword DB password.
     */
    @Autowired
    public WeatherStationDAO(@Value("org.h2.Driver") String dataBaseDriver,
                  @Value("jdbc:h2:~/test;AUTO_SERVER=TRUE") String dataBaseUrl,
                  @Value("sa") String dataBaseUsername,
                  @Value("") String dataBasePassword) {
        WeatherStationDAO.dataBaseDriver = dataBaseDriver;
        WeatherStationDAO.dataBaseUrl = dataBaseUrl;
        WeatherStationDAO.dataBaseUsername = dataBaseUsername;
        WeatherStationDAO.dataBasePassword = dataBasePassword;

    }

    /**
     * WeatherStationDAO init-method. Creates connection with the DB.
     */
    @PostConstruct
    private void createDataBaseConnection() {
        try {
            Class.forName(WeatherStationDAO.dataBaseDriver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            WeatherStationDAO.connection = DriverManager.getConnection(dataBaseUrl, dataBaseUsername, dataBasePassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * WeatherStationDAO destroy-method. Closes connection with the DB.
     */
    @PreDestroy
    private void closeDataBaseConnection() throws SQLException {
        WeatherStationDAO.connection.close();
    }

    /**
     * Add info about weather stations to the WEATHER_STATION table.
     * @param weatherStations list with weather stations to be added.
     * @throws SQLException throw if connection is broken or request is invalid.
     */
    public void updateWeatherData(List<WeatherStation> weatherStations) throws SQLException {
        connection = DriverManager.getConnection(dataBaseUrl, dataBaseUsername, dataBasePassword);
        weatherStations.forEach(this::updateWeatherStationTable);
    }

    /**
     * Add weather station into the WEATHER_STATION table.
     * @param weatherStation weather station to be added
     */
    private void updateWeatherStationTable(WeatherStation weatherStation) {
        try (Statement statement = connection.createStatement()) {
            String SQL = "INSERT INTO weather_station VALUES(" + weatherStation + ")";
            statement.executeUpdate(SQL);
//            System.out.println(SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
