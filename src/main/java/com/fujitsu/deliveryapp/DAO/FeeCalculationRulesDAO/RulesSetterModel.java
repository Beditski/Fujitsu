package com.fujitsu.deliveryapp.DAO.FeeCalculationRulesDAO;

import com.fujitsu.deliveryapp.models.FeeCalculationRules;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Helper class for FeeCalculationRulesDAO
 */
public class RulesSetterModel {

    /**
     * Sets rules for calculating delivery fee. Non specified parameters are taken from the
     * Data Base (BUSINESS_RULES table).
     * @param feeCalculationRules - object which contains specified request parameters.
     * @throws SQLException throws exception if somthing goes wrong...
     */
    protected static void setRules(FeeCalculationRules feeCalculationRules, ResultSet feeCalculationRulesResultSet) throws SQLException {
        // Add RBF values from DB to feeCalculationRules if they were not specified during the request
        setRbfValues(feeCalculationRules, feeCalculationRulesResultSet);
        // Add ATEF values from DB to feeCalculationRules if they were not specified during the request
        setAtefValues(feeCalculationRules, feeCalculationRulesResultSet);
        // Add WSEF values from DB to feeCalculationRules if they were not specified during the request
        setWsefValues(feeCalculationRules, feeCalculationRulesResultSet);
        // Add WPEF values from DB to feeCalculationRules if it was not specified during the request
        setWpefValues(feeCalculationRules, feeCalculationRulesResultSet);
        // Set timestamp if it was not specified
        setTimestampValue(feeCalculationRules);
    }

    /**
     * Add RBF values from DB to feeCalculationRules if they were not specified during the request.
     * @param feeCalculationRules rules for calculating delivery fee.
     * @param feeCalculationRulesResultSet result set with data.
     * @throws SQLException in case something goes wrong...
     */
    protected static void setRbfValues(FeeCalculationRules feeCalculationRules, ResultSet feeCalculationRulesResultSet) throws SQLException {
        if (feeCalculationRules.getRbfCar() == null) {
            feeCalculationRules.setRbfCar(feeCalculationRulesResultSet.getDouble("RBF_CAR"));
        }
        if (feeCalculationRules.getRbfScooter() == null) {
            feeCalculationRules.setRbfScooter(feeCalculationRulesResultSet.getDouble("RBF_SCOOTER"));
        }
        if (feeCalculationRules.getRbfBike() == null) {
            feeCalculationRules.setRbfBike(feeCalculationRulesResultSet.getDouble("RBF_BIKE"));
        }
    }

    /**
     * Add ATEF values from DB to feeCalculationRules if they were not specified during the request.
     * @param feeCalculationRules rules for calculating delivery fee.
     * @param feeCalculationRulesResultSet result set with data.
     * @throws SQLException in case something goes wrong...
     */
    protected static void setAtefValues(FeeCalculationRules feeCalculationRules, ResultSet feeCalculationRulesResultSet) throws SQLException {
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
    }

    /**
     * Add WSEF values from DB to feeCalculationRules if they were not specified during the request.
     * @param feeCalculationRules rules for calculating delivery fee.
     * @param feeCalculationRulesResultSet result set with data.
     * @throws SQLException in case something goes wrong...
     */
    protected static void setWsefValues(FeeCalculationRules feeCalculationRules, ResultSet feeCalculationRulesResultSet) throws SQLException {
        if (feeCalculationRules.getWsefSpeed() == null) {
            feeCalculationRules.setWsefSpeed(feeCalculationRulesResultSet.getDouble("WSEF_SPEED"));
        }
        if (feeCalculationRules.getWsefSpeedMax() == null) {
            feeCalculationRules.setWsefSpeedMax(feeCalculationRulesResultSet.getDouble("WSEF_SPEED_MAX"));
        }
        if (feeCalculationRules.getWsefFee() == null) {
            feeCalculationRules.setWsefFee(feeCalculationRulesResultSet.getDouble("WSEF_FEE"));
        }
    }

    /**
     * Add WPEF values from DB to feeCalculationRules if they were not specified during the request.
     * @param feeCalculationRules rules for calculating delivery fee.
     * @param feeCalculationRulesResultSet result set with data.
     * @throws SQLException in case something goes wrong...
     */
    protected static void setWpefValues(FeeCalculationRules feeCalculationRules, ResultSet feeCalculationRulesResultSet) throws SQLException {
        if (feeCalculationRules.getWpefSnowOrSleetFee() == null) {
            feeCalculationRules.setWpefSnowOrSleetFee(feeCalculationRulesResultSet.getDouble("WPEF_SNOW_FEE"));
        }
        if (feeCalculationRules.getWpefRainFee() == null) {
            feeCalculationRules.setWpefRainFee(feeCalculationRulesResultSet.getDouble("WPEF_RAIN_FEE"));
        }
    }

    /**
     * Set timestamp if it was not specified.
     * @param feeCalculationRules rules for calculating delivery fee.
     * @throws SQLException in case something goes wrong...
     */
    public static void setTimestampValue(FeeCalculationRules feeCalculationRules) throws SQLException {
        if (feeCalculationRules.getTimestamp() == null) {
            feeCalculationRules.setTimestamp(System.currentTimeMillis() / 1000);
        }
    }
}