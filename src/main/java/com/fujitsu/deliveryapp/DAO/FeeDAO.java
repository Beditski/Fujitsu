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

    public String calculateFee(FeeRules feeRules) throws Exception {
        String sql1 = "SELECT * FROM weather_station WHERE name = '" + feeRules.getRegion() + "' LIMIT 1";
        PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
        ResultSet weatherStationInfo = preparedStatement1.executeQuery();

        String sql2 = "SELECT * FROM business_rules WHERE location = '" + feeRules.getRegion() + "'";
        PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
        ResultSet feeCalculationRules = preparedStatement2.executeQuery();


        double fee = 0;

        if (feeRules.getTransport().equalsIgnoreCase("car")) {
            return feeCalculationRules.getString(1);
        }
        else if (feeRules.getTransport().equalsIgnoreCase("scooter")) {
            if (weatherStationInfo.getDouble(3) > feeCalculationRules.getDouble(9) ||
                    weatherStationInfo.getString(4).equalsIgnoreCase("glaze") ||
                    weatherStationInfo.getString(4).equalsIgnoreCase("hale") ||
                    weatherStationInfo.getString(4).equalsIgnoreCase("thunder")) {
                throw new Exception("Usage of selected vehicle type is forbidden");
            }
            // Determine base fee (scooter or bike)
            if (feeRules.getTransport().equalsIgnoreCase("scooter"))
                fee += feeCalculationRules.getDouble(2);
            else
                fee += feeCalculationRules.getDouble(3);

            // air temperature (ATEF) extra fee
            if (weatherStationInfo.getDouble(2) < feeCalculationRules.getDouble(5))
                fee += feeCalculationRules.getDouble(7);
            else if (weatherStationInfo.getDouble(2) < feeCalculationRules.getDouble(4))
                fee += feeCalculationRules.getDouble(6);

            // wind speed (WSEF) extra fee
            if (weatherStationInfo.getDouble(3) > feeCalculationRules.getDouble(8))
                fee += feeCalculationRules.getDouble(7);

            // weather phenomenon (WPEF) extra fee
            if (weatherStationInfo.getString(4).equalsIgnoreCase("snow") ||
                    weatherStationInfo.getString(4).equalsIgnoreCase("sleet"))
                fee += feeCalculationRules.getDouble(11);
            if (weatherStationInfo.getString(4).equalsIgnoreCase("snow") ||
                    weatherStationInfo.getString(4).equalsIgnoreCase("sleet"))
                fee += feeCalculationRules.getDouble(12);
        }

        return String.valueOf(fee);
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