package com.fujitsu.deliveryapp.DAO;


import java.sql.*;
import java.util.List;

import com.fujitsu.deliveryapp.models.WeatherStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class WeatherStationDAO {
    private static final String URL = "jdbc:h2:~/test;AUTO_SERVER=TRUE";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    private static Connection connection;

    @Autowired
    public WeatherStationDAO() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            WeatherStationDAO.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateWeatherData(List<WeatherStation> weatherStations) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
        weatherStations.forEach(this::updateWeatherStationTable);
    }

    private void updateWeatherStationTable(WeatherStation weatherStation) {
        try (Statement statement = connection.createStatement()) {
            String SQL = "INSERT INTO weather_station VALUES(" + weatherStation + ")";
            statement.executeUpdate(SQL);
            System.out.println(SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
