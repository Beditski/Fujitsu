package com.fujitsu.deliveryapp.DAO;

import com.fujitsu.deliveryapp.models.FeeRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class FeeDAO {
    private static final String URL = "jdbc:h2:~/test;AUTO_SERVER=TRUE";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";
    private static Connection connection;

    @Autowired
    private FeeDAO() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            FeeDAO.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calculates fee based on the information in the FeeRules class. <br>
     * @param feeRules set of fee rules.
     * @return fee values based on the given fee rules
     * @throws Exception throw an exception if transport is either scooter or bike and
     * if weather conditions are not suitable for the scooter/bike delivery.
     */
    public String calculateFee(FeeRules feeRules) throws Exception {
        // Request weather conditions from the DB, table weather_stations
        ResultSet weatherStationInfo = readFromDB(
                "SELECT * FROM weather_station WHERE name = '" + feeRules.getRegion() + "' LIMIT 1");
        // Request fee calculation rules from the DB, table business_rules
        ResultSet feeCalculationRules = readFromDB(
                "SELECT * FROM business_rules WHERE location = '" + feeRules.getRegion() + "'");

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

        if (feeRules.getTransport().equalsIgnoreCase("car")) {
            return feeCalculationRules.getString("RBF_CAR");
        }
        else {
            // Determine base fee (scooter or bike)
            if (feeRules.getTransport().equalsIgnoreCase("scooter"))
                fee += feeCalculationRules.getDouble("RBF_SCOOTER");
            else
                fee += feeCalculationRules.getDouble("RBF_BIKE");

            // If selected vehicle is either scooter or bike and weather conditions are too sever - throw an exception
            if (feeCalculationRules.getDouble("WSEF_SPEED_MAX") < weatherStationInfo.getDouble("WIND_SPEED") ||
                    weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("glaze") ||
                    weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("hale") ||
                    weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("thunder")) {
                throw new Exception("Usage of selected vehicle type is forbidden");
            }

            // Adding air temperature extra fee (ATEF) if required conditions are met
            if (feeCalculationRules.getDouble("ATEF_TEMPERATURE_MIN") < weatherStationInfo.getDouble("AIR_TEMPERATURE") )
                fee += feeCalculationRules.getDouble("ATEF_FEE_MAX");
            else if (weatherStationInfo.getDouble("ATEF_TEMPERATURE") < feeCalculationRules.getDouble("AIR_TEMPERATURE"))
                fee += feeCalculationRules.getDouble("ATEF_FEE");

            // Adding wind speed extra fee (WSEF) if required conditions are met
            if (feeCalculationRules.getDouble("WSEF_SPEED") < weatherStationInfo.getDouble("WIND_SPEED"))
                fee += feeCalculationRules.getDouble("WSEF_FEE");

            // Adding weather phenomenon extra fee related with snow or sleet (WPEF) if required conditions are met
            if (weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("snow") ||
                    weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("sleet"))
                fee += feeCalculationRules.getDouble("WPEF_SNOW_FEE");

            // Adding weather phenomenon extra fee related with snow or sleet (WPEF) if required conditions are met
            if (weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("rain"))
                fee += feeCalculationRules.getDouble("WPEF_RAIN_FEE");
        }

        return String.valueOf(fee);
    }

    private ResultSet readFromDB(String sql) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet;
    }



    public String updateFeeRules(FeeRules fee) {
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