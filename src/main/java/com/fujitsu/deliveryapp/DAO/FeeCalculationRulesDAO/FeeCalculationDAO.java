package com.fujitsu.deliveryapp.DAO.FeeCalculationRulesDAO;

import com.fujitsu.deliveryapp.models.FeeCalculationRules;
import com.fujitsu.deliveryapp.DAO.exceptions.UsageOfSelectedVehicleForbidden;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Helper class for FeeCalculationRulesDAO
 */
public class FeeCalculationDAO {
    /**
     * Throws an exception if weather conditions are to sever for the delivery on bike or scooter.
     * @param feeCalculationRules rules for calculating delivery fees.
     * @param weatherStationInfo information about regional weather station and weather conditions.
     * @throws UsageOfSelectedVehicleForbidden thrwon if weather conditions are too sever.
     * @throws SQLException thrown if there are any problems related to DB connection and data extraction
     */
    protected static void controlWeatherConditions(FeeCalculationRules feeCalculationRules, ResultSet weatherStationInfo) throws UsageOfSelectedVehicleForbidden, SQLException {
        if (feeCalculationRules.getWsefSpeedMax() < weatherStationInfo.getDouble("WIND_SPEED") ||
                weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("glaze") ||
                weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("hale") ||
                weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("thunder")) {
            throw new UsageOfSelectedVehicleForbidden("Usage of selected vehicle type is forbidden");
        }
    }

    /**
     * Calculates fee based on the information in the FeeCalculationRules class.
     * @param feeCalculationRules set of fee rules.
     * @param weatherStationInfo weather station's info.
     * @return fee values based on the given fee rules
     * @throws UsageOfSelectedVehicleForbidden throw an exception if transport is either scooter or bike
     * and if weather conditions are not suitable for the scooter/bike delivery
     * @throws SQLException thrown if there is a problem with DB.
     */
    protected static String calculateFee(FeeCalculationRules feeCalculationRules, ResultSet weatherStationInfo) throws UsageOfSelectedVehicleForbidden, SQLException {
        Double fee;
        if (feeCalculationRules.getTransport().equalsIgnoreCase("car")) {
            return feeCalculationRules.getRbfCar().toString(); // Return RBF for the car.
        }
        else {
            // If selected vehicle is either scooter or bike and weather conditions are too sever - throw an exception
            controlWeatherConditions(feeCalculationRules, weatherStationInfo);
            // Determine base fee (scooter or bike)
            fee = addScooterOrBikeFee(feeCalculationRules);
            // Adding air temperature extra fee (ATEF) if required conditions are met
            fee += atefFee(feeCalculationRules, weatherStationInfo);
            // Adding wind speed extra fee (WSEF) if required conditions are met
            fee += wsfeFee(feeCalculationRules, weatherStationInfo);
            // Adding weather phenomenon extra fee related with snow or sleet (WPEF) if required conditions are met
            fee += wpfeFee(feeCalculationRules, weatherStationInfo);
        }
        return String.valueOf(fee);
    }


    /**
     * Returns scooter or bike regional base fee based on passed fee calculation rules.
     * @param feeCalculationRules model with rules for fee calculations.
     * @return bike or scooter regional base fee.
     */
    protected static Double addScooterOrBikeFee(FeeCalculationRules feeCalculationRules) {
        Double fee = 0.0;
        if (feeCalculationRules.getTransport().equalsIgnoreCase("scooter"))
            fee += feeCalculationRules.getRbfScooter();
        else
            fee += feeCalculationRules.getRbfBike();
        return fee;
    }

    /**
     * Returns air temperature extra fee (ATEF) if required conditions are met.
     * @param feeCalculationRules model with rules for fee calculations.
     * @param weatherStationInfo info about weather condition.
     * @return air temperature extra fee (ATEF) if required conditions are met.
     * @throws SQLException in case something goes wrong..
     */
    protected static Double atefFee(FeeCalculationRules feeCalculationRules, ResultSet weatherStationInfo) throws SQLException {
        Double fee = 0.0;
        if (feeCalculationRules.getAtefTemperatureMin() < weatherStationInfo.getDouble("AIR_TEMPERATURE"))
            fee += feeCalculationRules.getAtefFeeMax();
        else if (feeCalculationRules.getAtefTemperature() < weatherStationInfo.getDouble("AIR_TEMPERATURE"))
            fee += feeCalculationRules.getAtefFee();
        return fee;
    }

    /**
     * Returns wind speed extra fee (WSEF) if required conditions are met
     * @param feeCalculationRules model with rules for fee calculations.
     * @param weatherStationInfo info about weather condition.
     * @return air temperature extra fee (ATEF) if required conditions are met.
     * @throws SQLException in case something goes wrong..
     */
    protected static Double wsfeFee(FeeCalculationRules feeCalculationRules, ResultSet weatherStationInfo) throws SQLException {
        Double fee = 0.0;
        if (feeCalculationRules.getWsefSpeed() < weatherStationInfo.getDouble("WIND_SPEED"))
            fee += feeCalculationRules.getWsefFee();
        return fee;
    }

    /**
     * Returns weather phenomenon extra fee (WSEF) if required conditions are met
     * @param feeCalculationRules model with rules for fee calculations.
     * @param weatherStationInfo info about weather condition.
     * @return air temperature extra fee (ATEF) if required conditions are met.
     * @throws SQLException in case something goes wrong..
     */
    protected static Double wpfeFee(FeeCalculationRules feeCalculationRules, ResultSet weatherStationInfo) throws SQLException {
        Double fee = 0.0;
        // Adding weather phenomenon extra fee related with snow or sleet (WPEF) if required conditions are met
        if (weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("snow") ||
                weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("sleet"))
            fee += feeCalculationRules.getWpefSnowOrSleetFee();

        // Adding weather phenomenon extra fee related with snow or sleet (WPEF) if required conditions are met
        if (weatherStationInfo.getString("WEATHER_PHENOMENON").equalsIgnoreCase("rain"))
            fee += feeCalculationRules.getWpefRainFee();
        return fee;
    }

}
