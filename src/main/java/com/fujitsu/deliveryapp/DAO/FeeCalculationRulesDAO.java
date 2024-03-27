package com.fujitsu.deliveryapp.DAO;

import com.fujitsu.deliveryapp.models.FeeCalculationRules;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class FeeDAO {
    private static String dataBaseDriver;
    private static String dataBaseUrl;
    private static String dataBaseUsername;
    private static String dataBasePassword;
    private static Connection connection;

    public FeeDAO(@Value("org.h2.Driver") String dataBaseDriver,
                  @Value("jdbc:h2:~/test;AUTO_SERVER=TRUE") String dataBaseUrl,
                  @Value("sa") String dataBaseUsername,
                  @Value("") String dataBasePassword) {
        FeeDAO.dataBaseDriver = dataBaseDriver;
        FeeDAO.dataBaseUrl = dataBaseUrl;
        FeeDAO.dataBaseUsername = dataBaseUsername;
        FeeDAO.dataBasePassword = dataBasePassword;

    }

    @PostConstruct
    private void createDataBaseConnection() {
        try {
            Class.forName(FeeDAO.dataBaseDriver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            FeeDAO.connection = DriverManager.getConnection(dataBaseUrl, dataBaseUsername, dataBasePassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    private void closeDataBaseConnection() throws SQLException {
        FeeDAO.connection.close();
    }

    /**
     * Sets rules for calculating delivery fee. Non specified parameters are taken from the Data Base (BUSINESS_RULES table).
     * @param feeCalculationRules - object which contains specified in request parameters.
     * @throws SQLException
     */
    public void setFeeCalculationRules(FeeCalculationRules feeCalculationRules) throws SQLException {
        // Request fee calculation rules from the DB, table business_rules
        ResultSet feeCalculationRulesResultSet = readFromDB(
                "SELECT * FROM business_rules WHERE location = '" + feeCalculationRules.getRegion() + "'");

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

        System.out.println(feeCalculationRules);
    }

    /**
     * Calculates fee based on the information in the FeeCalculationRules class. <br>
     * @param feeCalculationRules set of fee rules.
     * @return fee values based on the given fee rules
     * @throws Exception throw an exception if transport is either scooter or bike and
     * if weather conditions are not suitable for the scooter/bike delivery.
     */
    public String calculateFee(FeeCalculationRules feeCalculationRules) throws Exception {
        // Request weather conditions from the DB, table weather_stations
        ResultSet weatherStationInfo = readFromDB(
                "SELECT * FROM weather_station WHERE name = '" + feeCalculationRules.getRegion() + "' LIMIT 1");
        // Request fee calculation rules from the DB, table business_rules
        ResultSet feeCalculationRulesResultSet = readFromDB(
                "SELECT * FROM business_rules WHERE location = '" + feeCalculationRules.getRegion() + "'");

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
            return feeCalculationRules.getRbfCar().toString();
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
     * Takes as String with sql SELECT request, prepares it as a statement and returns a ResultSet.
     * @param sql string with sql command.
     * @return resultSet with requested data.
     * @throws SQLException
     */
    private ResultSet readFromDB(String sql) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet;
    }



    public String updateFeeRules(FeeCalculationRules fee) {
        return null;
    }

    public void resetFeeRules() {

    }
}
//    CREATE TABLE business_rules (
//        location varchar(20) NOT NULL,
//    RBF_car numeric NOT NULL,
//    RBF_scooter numeric NOT NULL,
//    RBF_bike  numeric NOT NULL,
//    ATEF_temperature numeric NOT NULL,
//    ATEF_price numeric NOT NULL,
//    ATEF_price_max numeric NOT NULL,
//    WSEF_speed numeric NOT NULL,
//    WSEF_speed_max numeric NOT NULL,
//    WSEF_speed_price numeric NOT NULL,
//    WPEF_snow_price numeric NOT NULL,
//    WPEF_rain_price numeric NOT NULL
//)