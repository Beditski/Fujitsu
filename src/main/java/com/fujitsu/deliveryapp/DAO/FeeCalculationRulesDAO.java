package com.fujitsu.deliveryapp.DAO;

import com.fujitsu.deliveryapp.models.FeeCalculationRules;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

/**
 * Data Access Object for requesting data in regard to calculating fees.
 */
@Component
public class FeeCalculationRulesDAO {
    private static String dataBaseDriver;
    private static String dataBaseUrl;
    private static String dataBaseUsername;
    private static String dataBasePassword;
    private static Connection connection;

    /**
     * Constructor for FeeCalculationRulesDAO. Method injects values for DB driver, url, username and password.
     * @param dataBaseDriver driver.
     * @param dataBaseUrl DB url.
     * @param dataBaseUsername DB username.
     * @param dataBasePassword DB password.
     */
    public FeeCalculationRulesDAO(@Value("org.h2.Driver") String dataBaseDriver,
                                  @Value("jdbc:h2:~/test;AUTO_SERVER=TRUE") String dataBaseUrl,
                                  @Value("sa") String dataBaseUsername,
                                  @Value("") String dataBasePassword) {
        FeeCalculationRulesDAO.dataBaseDriver = dataBaseDriver;
        FeeCalculationRulesDAO.dataBaseUrl = dataBaseUrl;
        FeeCalculationRulesDAO.dataBaseUsername = dataBaseUsername;
        FeeCalculationRulesDAO.dataBasePassword = dataBasePassword;

    }

    /**
     * FeeCalculationRulesDAO init-method. Creates connection with the DB.
     */
    @PostConstruct
    private void createDataBaseConnection() {
        try {
            Class.forName(FeeCalculationRulesDAO.dataBaseDriver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            FeeCalculationRulesDAO.connection = DriverManager.getConnection(dataBaseUrl, dataBaseUsername, dataBasePassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * FeeCalculationRulesDAO destroy-method. Closes connection with the DB.
     */
    @PreDestroy
    private void closeDataBaseConnection() throws SQLException {
        FeeCalculationRulesDAO.connection.close();
    }

    /**
     * Sets rules for calculating delivery fee. Non specified parameters are taken from the Data Base (BUSINESS_RULES table).
     * @param feeCalculationRules - object which contains specified request parameters.
     * @throws SQLException throws exception if somthing goes wrong...
     */
    public void setFeeCalculationRules(FeeCalculationRules feeCalculationRules) throws SQLException {
        // Request fee calculation rules from the DB, table business_rules
        ResultSet feeCalculationRulesResultSet = readFromDB(
                "SELECT * FROM business_rules WHERE location = '" + feeCalculationRules.getCity() + "'");

        // Add RBF values from DB to feeCalculationRules if it was not specified during the request
        if (feeCalculationRules.getRbfCar() == null) {
            feeCalculationRules.setRbfCar(feeCalculationRulesResultSet.getDouble("RBF_CAR"));
        }
        if (feeCalculationRules.getRbfScooter() == null) {
            feeCalculationRules.setRbfScooter(feeCalculationRulesResultSet.getDouble("RBF_SCOOTER"));
        }
        if (feeCalculationRules.getRbfBike() == null) {
            feeCalculationRules.setRbfBike(feeCalculationRulesResultSet.getDouble("RBF_BIKE"));
        }

        // Add ATEF values from DB to feeCalculationRules if it was not specified during the request
        if (feeCalculationRules.getAtefTemperature() == null) {
            feeCalculationRules.setAtefTemperature(feeCalculationRulesResultSet.getDouble("ATEF_TEMPERATURE"));
        }
        if (feeCalculationRules.getAtefTemperatureMin() == null) {
            feeCalculationRules.setAtefTemperatureMin(feeCalculationRulesResultSet.getDouble("ATEF_TEMPERATURE_MIN"));
        }
        if (feeCalculationRules.getAtefFee() == null) {
            feeCalculationRules.setAtefFee(feeCalculationRulesResultSet.getDouble("ATEF_FEE"));
        }
        if (feeCalculationRules.getAtefFeeMax() == null) {
            feeCalculationRules.setAtefFeeMax(feeCalculationRulesResultSet.getDouble("ATEF_FEE_MAX"));
        }

        // Add WSEF values from DB to feeCalculationRules if it was not specified during the request
        if (feeCalculationRules.getWsefSpeed() == null) {
            feeCalculationRules.setWsefSpeed(feeCalculationRulesResultSet.getDouble("WSEF_SPEED"));
        }
        if (feeCalculationRules.getWsefSpeedMax() == null) {
            feeCalculationRules.setWsefSpeedMax(feeCalculationRulesResultSet.getDouble("WSEF_SPEED_MAX"));
        }
        if (feeCalculationRules.getWsefFee() == null) {
            feeCalculationRules.setWsefFee(feeCalculationRulesResultSet.getDouble("WSEF_FEE"));
        }

        // Add WPEF values from DB to feeCalculationRules if it was not specified during the request
        if (feeCalculationRules.getWpefSnowOrSleetFee() == null) {
            feeCalculationRules.setWpefSnowOrSleetFee(feeCalculationRulesResultSet.getDouble("WPEF_SNOW_FEE"));
        }
        if (feeCalculationRules.getWpefRainFee() == null) {
            feeCalculationRules.setWpefRainFee(feeCalculationRulesResultSet.getDouble("WPEF_RAIN_FEE"));
        }

        // Set timestamp if it was not specified
        if (feeCalculationRules.getTimestamp() == null) {
            feeCalculationRules.setTimestamp(System.currentTimeMillis() / 1000);
        }
    }

    /**
     * Calculates fee based on the information in the FeeCalculationRules class.
     * @param feeCalculationRules set of fee rules.
     * @return fee values based on the given fee rules
     * @throws Exception throw an exception if transport is either scooter or bike and
     * if weather conditions are not suitable for the scooter/bike delivery.
     */
    public String calculateFee(FeeCalculationRules feeCalculationRules) throws Exception {
        // Request weather condition from the DB (WEATHER_STATION table).
        ResultSet weatherStationInfo = readFromDB(
                "SELECT * FROM weather_station WHERE name = '" + feeCalculationRules.getWeatherStationName() +
                        "' ORDER BY ABS(timestamp - " + feeCalculationRules.getTimestamp() +") ASC LIMIT 1;");
        double fee = 0;

        /* Column Number   Column names in BUSINESS_RULES table   Column names in WEATHER_STATIONS table
         * --------------------------------------------------------------------------------------------
         *       1         LOCATION                               WMO_CODE
         *       2         RBF_CAR                                NAME
         *       3         RBF_SCOOTER                            AIR_TEMPERATURE
         *       4         RBF_BIKE                               WIND_SPEED
         *       5         ATEF_TEMPERATURE                       WEATHER_PHENOMENON
         *       6         ATEF_TEMPERATURE_MIN                   TIMESTAMP
         *       7         ATEF_FEE
         *       8         ATEF_FEE_MAX
         *       9         WSEF_SPEED
         *      10         WSEF_SPEED_MAX
         *      11  	   WSEF_FEE
         *      12         WPEF_SNOW_FEE
         *      13         WPEF_RAIN_FEE
         */


        if (feeCalculationRules.getTransport().equalsIgnoreCase("car")) {
            return feeCalculationRules.getRbfCar().toString(); // Return RBF for the car.
        }
        else {
            // Determine base fee (scooter or bike)
            if (feeCalculationRules.getTransport().equalsIgnoreCase("scooter"))
                fee += feeCalculationRules.getRbfScooter();
            else
                fee += feeCalculationRules.getRbfBike();

            // If selected vehicle is either scooter or bike and weather conditions are too sever - throw an exception
            if (feeCalculationRules.getWsefSpeedMax() < weatherStationInfo.getDouble("WIND_SPEED") ||
                    weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("glaze") ||
                    weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("hale") ||
                    weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("thunder")) {
                throw new Exception("Usage of selected vehicle type is forbidden");
            }

            // Adding air temperature extra fee (ATEF) if required conditions are met
            if (feeCalculationRules.getAtefTemperatureMin() < weatherStationInfo.getDouble("AIR_TEMPERATURE"))
                fee += feeCalculationRules.getAtefFeeMax();
            else if (feeCalculationRules.getAtefTemperature() < weatherStationInfo.getDouble("AIR_TEMPERATURE"))
                fee += feeCalculationRules.getAtefFee();

            // Adding wind speed extra fee (WSEF) if required conditions are met
            if (feeCalculationRules.getWsefSpeed() < weatherStationInfo.getDouble("WIND_SPEED"))
                fee += feeCalculationRules.getWsefFee();

            // Adding weather phenomenon extra fee related with snow or sleet (WPEF) if required conditions are met
            if (weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("snow") ||
                    weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("sleet"))
                fee += feeCalculationRules.getWpefSnowOrSleetFee();

            // Adding weather phenomenon extra fee related with snow or sleet (WPEF) if required conditions are met
            if (weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("rain"))
                fee += feeCalculationRules.getWpefRainFee();
        }

        return String.valueOf(fee);
    }

    /**
     * Takes sql command as String, prepares it as a statement and returns a ResultSet.
     * @param sql string with sql command.
     * @return resultSet with requested data.
     * @throws SQLException throws if connection is broken or sql command is invalid.
     */
    private ResultSet readFromDB(String sql) throws SQLException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateFeeRules(FeeCalculationRules fee) {
        return null;
    }

    public void resetFeeRules() {

    }
}