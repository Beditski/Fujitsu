package com.fujitsu.deliveryapp.DAO.FeeCalculationRulesDAO;

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
     * @throws Exception throw an exception if transport is either scooter or bike and
     * if weather conditions are not suitable for the scooter/bike delivery or if there
     * is a problem with DB.
     */
    public String calculateFee(FeeCalculationRules feeCalculationRules) throws Exception {
        // Request weather condition from the DB (WEATHER_STATION table).
        ResultSet weatherStationInfo = readFromDB(
                "SELECT * FROM weather_station WHERE name = '" + feeCalculationRules.getWeatherStationName() +
                        "' ORDER BY ABS(timestamp - " + feeCalculationRules.getTimestamp() +") ASC LIMIT 1;");

        return FeeCalculationDAO.calculateFee(feeCalculationRules, weatherStationInfo);
    }


    public String updateFeeRules(FeeCalculationRules feeCalculationRules) {
        return null;
    }

    public void resetFeeRules() {

    }
}