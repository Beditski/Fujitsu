package com.fujitsu.deliveryapp.DAO.FeeCalculationRulesDAO;

import com.fujitsu.deliveryapp.config.exceptions.UsageOfSelectedVehicleForbidden;
import com.fujitsu.deliveryapp.models.FeeCalculationRules;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

import static com.fujitsu.deliveryapp.DAO.FeeCalculationRulesDAO.FeeCalculationRulesSetterDAO.*;

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
    private FeeCalculationRulesDAO(@Value("org.h2.Driver") String dataBaseDriver,
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


    /**
     * Sets rules for calculating delivery fee. Non specified parameters are taken from the
     * Data Base (BUSINESS_RULES table).
     * @param feeCalculationRules - object which contains specified request parameters.
     * @throws SQLException throws exception if somthing goes wrong...
     */
    public void setFeeCalculationRules(FeeCalculationRules feeCalculationRules) throws SQLException {
        // Request fee calculation rules from the DB, table business_rules
        ResultSet feeCalculationRulesResultSet = readFromDB(
                "SELECT * FROM business_rules WHERE location = '" + feeCalculationRules.getCity() + "'");

        setRules(feeCalculationRules, feeCalculationRulesResultSet);
    }

    /**
     * Calculates fee based on the information in the FeeCalculationRules class.
     * @param feeCalculationRules set of fee rules.
     * @return fee values based on the given fee rules
     * @throws UsageOfSelectedVehicleForbidden throw an exception if transport is either scooter or bike and
     * if weather conditions are not suitable for the scooter/bike delivery.
     * @throws SQLException thrown if there is any problem related to DB connection or request.
     */
    public String calculateFee(FeeCalculationRules feeCalculationRules) throws UsageOfSelectedVehicleForbidden, SQLException {
        // Request weather condition from the DB (WEATHER_STATION table).
        ResultSet weatherStationInfo = readFromDB(
                "SELECT * FROM weather_station WHERE name = '" + feeCalculationRules.getWeatherStationName() +
                        "' ORDER BY ABS(timestamp - " + feeCalculationRules.getTimestamp() +") ASC LIMIT 1;");
        System.out.println(weatherStationInfo.getString("TIMESTAMP"));
        return FeeCalculationDAO.calculateFee(feeCalculationRules, weatherStationInfo);
    }

    /**
     * Sets new fee rules. If all rules in feeCalculationRules are null, then throw NullPointer exception.
     * @param feeCalculationRules set of fee rules.
     * @throws NullPointerException if all passed rules are nulls - throw an error.
     * @throws SQLException thrown if there is any problem related to DB connection or request.
     */
    public void updateFeeRules(FeeCalculationRules feeCalculationRules) throws SQLException {
        String sql = "UPDATE BUSINESS_RULES SET ";
        if (feeCalculationRules.getRbfCar() != null) sql += "RBF_CAR = " + feeCalculationRules.getRbfCar() + ", ";
        if (feeCalculationRules.getRbfScooter() != null) sql += "RBF_SCOOTER = " + feeCalculationRules.getRbfScooter() + ", ";
        if (feeCalculationRules.getRbfBike() != null) sql += "RBF_BIKE = " + feeCalculationRules.getRbfBike() + ", ";

        if (feeCalculationRules.getAtefTemperature() != null) sql += "ATEF_TEMPERATURE = " + feeCalculationRules.getAtefTemperature() + ", ";
        if (feeCalculationRules.getAtefTemperatureMin() != null)sql += "ATEF_TEMPERATURE_MIN = " + feeCalculationRules.getAtefTemperatureMin() + ", ";
        if (feeCalculationRules.getAtefFee() != null) sql +=  "ATEF_FEE = " + feeCalculationRules.getAtefFee() + ", ";
        if (feeCalculationRules.getAtefFeeMax() != null) sql += "ATEF_FEE_MAX = " + feeCalculationRules.getAtefFeeMax() + ", ";

        if (feeCalculationRules.getWsefSpeed() != null) sql += "WSEF_SPEED = " + feeCalculationRules.getWsefSpeed() + ", ";
        if (feeCalculationRules.getWsefSpeedMax() != null) sql += "WSEF_SPEED_MAX = " + feeCalculationRules.getWsefSpeedMax() + ", ";
        if (feeCalculationRules.getWsefFee() != null) sql += "WSEF_FEE = " + feeCalculationRules.getWsefFee() + ", ";

        if (feeCalculationRules.getWpefSnowOrSleetFee() != null) sql += "WPEF_SNOW_FEE = " + feeCalculationRules.getWpefSnowOrSleetFee() + ", ";
        if (feeCalculationRules.getWpefRainFee() != null) sql += "WPEF_RAIN_FEE = " + feeCalculationRules.getWpefRainFee()  + ", ";

        sql = sql.trim();

        // Throw error if all rules are null (no parameters were sent)
        if (sql.endsWith("SET")) throw new NullPointerException();
        // If there is an extra coma at the end of statement - remove it.
        if (sql.charAt(sql.length() - 1) == ',') sql = sql.substring(0, sql.length() - 1) + " ";

        sql += "WHERE LOCATION = " + "'" + feeCalculationRules.getCity() + "'" +  ";";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
    }

    /**
     * Takes 3 optional parameters. If all parameters are nulls - all business rules are being reset to default
     * values. If at least one parameter is not null, null parameters will not be reset to default values.
     * All true parameters will cause a reset to default values for business rules in the corresponding city.
     * @param bool1 reset business rules to default values in Tallinn.
     * @param bool2 reset business rules to default values in Tartu.
     * @param bool3 reset business rules to default values in Pärnu.
     * @throws SQLException thrown if there is any problem with DB connection or DB request.
     */
    public void resetFeeRules(Boolean bool1, Boolean bool2, Boolean bool3) throws SQLException {
        String sql1 = "UPDATE BUSINESS_RULES SET RBF_CAR = 4, RBF_SCOOTER = 3.5, RBF_BIKE = 3, " +
                          "ATEF_TEMPERATURE = 0, ATEF_TEMPERATURE_MIN = -10, ATEF_FEE = 0.5, ATEF_FEE_MAX = 1," +
                          "WSEF_SPEED = 10, WSEF_SPEED_MAX = 20, WSEF_FEE = 0.5," +
                          "WPEF_SNOW_FEE = 1, WPEF_RAIN_FEE = 1" +
                      "WHERE LOCATION = 'Tallinn';";
        String sql2 = "UPDATE BUSINESS_RULES " +
                      "SET " +
                          "RBF_CAR = 3.5, RBF_SCOOTER = 3, RBF_BIKE = 2.5, " +
                          "ATEF_TEMPERATURE = 0, ATEF_TEMPERATURE_MIN = -10, ATEF_FEE = 0.5, ATEF_FEE_MAX = 1," +
                          "WSEF_SPEED = 10, WSEF_SPEED_MAX = 20, WSEF_FEE = 0.5," +
                          "WPEF_SNOW_FEE = 1, WPEF_RAIN_FEE = 1" +
                      "WHERE LOCATION = 'Tartu';";
        String sql3 = "UPDATE BUSINESS_RULES " +
                      "SET " +
                          "RBF_CAR = 3, RBF_SCOOTER = 2.5, RBF_BIKE = 2, " +
                          "ATEF_TEMPERATURE = 0, ATEF_TEMPERATURE_MIN = -10, ATEF_FEE = 0.5, ATEF_FEE_MAX = 1," +
                          "WSEF_SPEED = 10, WSEF_SPEED_MAX = 20, WSEF_FEE = 0.5," +
                          "WPEF_SNOW_FEE = 1, WPEF_RAIN_FEE = 1" +
                "     WHERE LOCATION = 'Pärnu';";

        // Reset to default values in all rows if all inputs are nulls
        if (bool1 == null && bool2 == null && bool3 == null) {
            connection.prepareStatement(sql1).execute();
            connection.prepareStatement(sql2).execute();
            connection.prepareStatement(sql3).execute();
        }
        // If not null and true - reset
        if (bool1 != null && bool1) connection.prepareStatement(sql1).execute();
        if (bool2 != null && bool2) connection.prepareStatement(sql2).execute();
        if (bool3 != null && bool3) connection.prepareStatement(sql3).execute();
    }
}