package com.fujitsu.deliveryapp.controllers;

import com.fujitsu.deliveryapp.DAO.FeeCalculationRulesDAO.FeeCalculationRulesDAO;
import com.fujitsu.deliveryapp.config.exceptions.InvalidVehicleParameter;
import com.fujitsu.deliveryapp.config.exceptions.UsageOfSelectedVehicleForbidden;
import com.fujitsu.deliveryapp.models.FeeCalculationRules;
import com.fujitsu.deliveryapp.config.exceptions.InvalidCityParameterException;
import com.fujitsu.deliveryapp.config.exceptions.NonPositiveArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

/**
 * Controller for handling GET and POST request related to the fee calculation rules.
 */
@Controller
@RequestMapping("/api")
public class FeeCalculationRulesController {
    private final FeeCalculationRulesDAO feeCalculationRulesDAO;

    /**
     * Constructor for FeeCalculationRules. Injects dependency of FeeCalculationRulesDAO.
     * @param feeCalculationRulesDAO feeCalculationRules data access object.
     */
    @Autowired
    public FeeCalculationRulesController(FeeCalculationRulesDAO feeCalculationRulesDAO) {
        this.feeCalculationRulesDAO = feeCalculationRulesDAO;
    }

    /**
     * Return true if invalid vehicle was selected.
     */
    private static boolean vehicleController(String vehicle) {
        return (!(vehicle.equalsIgnoreCase("car") || vehicle.equalsIgnoreCase("bike") || vehicle.equalsIgnoreCase("scooter")));
    }

    /**
     * Return true if any of the non-null parameters is non-positive.
     */
    private static boolean rbfController(Double rbfCar, Double rbfScooter, Double rbfBike) {
        return rbfCar != null && rbfCar <= 0 || rbfScooter != null && rbfScooter <= 0 || rbfBike != null && rbfBike <= 0;
    }

    /**
     * Return true if any of the non-null parameters is non-positive.
     */
    private static boolean atefController(Double atefTemperatureFee, Double atefTemperatureFeeMax) {
        return atefTemperatureFee != null && atefTemperatureFee <= 0 || atefTemperatureFeeMax != null && atefTemperatureFeeMax <= 0;
    }

    /**
     * Return true if any of the non-null parameters is non-positive.
     */
    private static boolean wsfeController(Double wsefSpeed, Double wsefSpeedMax, Double wsefFee) {
        return wsefSpeed != null && wsefSpeed <= 0 || wsefSpeedMax != null && wsefSpeedMax <= 0 || wsefFee != null && wsefFee <= 0;
    }

    /**
     * Return true if any of the non-null parameters is non-positive.
     */
    private static boolean wpfeController(Double wpefSnowOrSleetFee, Double wpefSnowRainFee) {
        return wpefSnowOrSleetFee != null && wpefSnowOrSleetFee <= 0 || wpefSnowRainFee != null && wpefSnowRainFee <= 0;
    }

    /**
     * Controls whether city name is correct or not, handles case errors,
     * throws InvalidCityParameterException exception if wrong city
     * was specified in the request.
     * @param city city name.
     * @return correct city name.
     * @throws InvalidCityParameterException thrown when invalid city is passed as parameter.
     */
    private static String getCityNam(String city) throws InvalidCityParameterException {
        return switch (city.toLowerCase()) {
            case "tallinn" -> "Tallinn";
            case "tartu" -> "Tartu";
            case "pärnu" -> "Pärnu";
            default -> throw new InvalidCityParameterException();
        };
    }

    /**
     * Returns local weather station's name.
     */
    private static String getWeatherStation(String city) {
        return switch (city.toLowerCase()) {
            case "tallinn" -> "Tallinn-Harku";
            case "tartu" -> "Tartu-Tõravere";
            case "pärnu" -> "Pärnu";
            default -> throw new IllegalStateException("Unexpected value");
        };
    }


    /**
     * Processes request parameters, creates a model with fee calculation rules and sends it to DAO to calculate delivery fee.
     * If invalid parameter was passed to the request - it won't be processed.
     * Throws an exception if any of parameters are incorrect.
     * @param city city name for requested delivery.
     * @param transport delivery transport type.
     * @param rbfCar regional based fee for the car (optional).
     * @param rbfScooter regional based fee for the scooter (optional).
     * @param rbfBike regional based fee for the bike (optional).
     * @param atefTemperature boundary for low air temperature extra fee for bike and scooter deliver (optional).
     * @param atefTemperatureMin lowest boundary for low air temperature extra fee for bike and scooter delivery (optional).
     * @param atefTemperatureFee low air temperature extra fee for bike and scooter delivery (optional).
     * @param atefTemperatureFeeMax maximal low air temperature extra fee for bike and scooter delivery (optional).
     * @param wsefSpeed wind speed boundary for extra fee for bike and scooter delivery (optional).
     * @param wsefSpeedMax maximum allowed wind speed for bike and scooter delivery for bike and scooter delivery (optional).
     * @param wsefFee wind speed extra fee for bike and scooter delivery (optional).
     * @param wpefSnowOrSleetFee wind phenomenon (snow or sleet) extra fee for bike and scooter delivery (optional).
     * @param wpefSnowRainFee wind phenomenon (rain) extra fee for bike and scooter delivery (optional).
     * @param dateTime date and time for time moment, at which fee is wanted to be calculated (optional).
     * @throws SQLException exception related with a sql request.
     * @throws UsageOfSelectedVehicleForbidden thrown when selected transport is either scooter or bike and weather is too sever.
     * @throws InvalidCityParameterException thrown when invalid city is passed as parameter.
     */
    @GetMapping("/fee")
    private String getFee(@RequestParam(value = "city") String city,
                          @RequestParam(value = "transport") String transport,

                          @RequestParam(value = "rbf_car", required = false) Double rbfCar,
                          @RequestParam(value = "rbf_scooter", required = false) Double rbfScooter,
                          @RequestParam(value = "rbf_bike", required = false) Double rbfBike,

                          @RequestParam(value = "atef_temperature", required = false) Double atefTemperature,
                          @RequestParam(value = "atef_temperature_min", required = false) Double atefTemperatureMin,
                          @RequestParam(value = "atef_temperature_fee", required = false) Double atefTemperatureFee,
                          @RequestParam(value = "atef_temperature_fee_max", required = false) Double atefTemperatureFeeMax,

                          @RequestParam(value = "wsef_speed", required = false) Double wsefSpeed,
                          @RequestParam(value = "wsef_speed_max", required = false) Double wsefSpeedMax,
                          @RequestParam(value = "wsef_fee", required = false) Double wsefFee,

                          @RequestParam(value = "wpef_snow_or_sleetFee", required = false) Double wpefSnowOrSleetFee,
                          @RequestParam(value = "wpef_snow_rain_fee", required = false) Double wpefSnowRainFee,

                          @RequestParam(value = "datetime", required = false) String dateTime,
                          Model model
    ) throws SQLException, UsageOfSelectedVehicleForbidden, InvalidCityParameterException {
        // Handle case error
        city = getCityNam(city);
        // Get weather station's name
        String weatherStation = getWeatherStation(city);
        // Control of the input correctness
        if (vehicleController(transport))
            throw new InvalidVehicleParameter();
        if (rbfController(rbfCar, rbfScooter, rbfBike) || atefController(atefTemperatureFee, atefTemperatureFeeMax) || wsfeController(wsefSpeed, wsefSpeedMax, wsefFee) || wpfeController(wpefSnowOrSleetFee, wpefSnowRainFee)) {
            throw new NonPositiveArgumentException();
        }

        // Create formatter for datetime.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
        // Create a model for calculating feeCalculationRules rules.
        FeeCalculationRules feeCalculationRules = new FeeCalculationRules(
                city, weatherStation, transport.toLowerCase(),
                rbfCar, rbfBike, rbfScooter,
                atefTemperature, atefTemperatureMin, atefTemperatureFee, atefTemperatureFeeMax,
                wsefSpeed, wsefSpeedMax,wsefFee,
                wpefSnowOrSleetFee, wpefSnowRainFee,
                // If datetime is null - add null, if not - convert it to timestamp and add to the model
                dateTime == null ? null : LocalDateTime.parse(dateTime, formatter).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000);

        // Requesting and setting absent parameters in feeCalculationRules calculation.
        this.feeCalculationRulesDAO.setFeeCalculationRules(feeCalculationRules);
        String fee = this.feeCalculationRulesDAO.calculateFee(feeCalculationRules);
        model.addAttribute("message", "Delivery is " + fee + " euro.");
        return "api/fee";
    }


    @GetMapping("/rules")
    private String getRules(Model model) throws SQLException {
        String[] rules = feeCalculationRulesDAO.getFeeCalculationRules().split("\n");
        model.addAttribute("message", rules);
        return "api/rules";
    }


    /**
     * Processes request parameters, creates a model with new calculation rules and sends it to DAO to set new delivery fee rules.
     * If invalid parameter was passed to the request - it won't be processed. If all new business rules
     * are nulls - a NullPointerException will be thrown.
     * Throws an errors if parameters are incorrect.
     * @param city city name for requested delivery.
     * @param rbfCar regional based fee for the car (optional).
     * @param rbfScooter regional based fee for the scooter (optional).
     * @param rbfBike regional based fee for the bike (optional).
     * @param atefTemperature boundary for low air temperature extra fee for bike and scooter deliver (optional).
     * @param atefTemperatureMin lowest boundary for low air temperature extra fee for bike and scooter delivery (optional).
     * @param atefTemperatureFee low air temperature extra fee for bike and scooter delivery (optional).
     * @param atefTemperatureFeeMax maximal low air temperature extra fee for bike and scooter delivery (optional).
     * @param wsefSpeed wind speed boundary for extra fee for bike and scooter delivery (optional).
     * @param wsefSpeedMax maximum allowed wind speed for bike and scooter delivery for bike and scooter delivery (optional).
     * @param wsefFee wind speed extra fee for bike and scooter delivery (optional).
     * @param wpefSnowOrSleetFee wind phenomenon (snow or sleet) extra fee for bike and scooter delivery (optional).
     * @param wpefSnowRainFee wind phenomenon (rain) extra fee for bike and scooter delivery (optional).
     * @throws NullPointerException if all rules are nulls this exception wil be thrown.
     * @throws InvalidCityParameterException if invalid city was passed this exception will be thrown.
     * @throws SQLException thrown if there is any problem related to DB connection or request.
     */
    @PostMapping("/rules/new")
    private String setRules(@RequestParam(value = "city") String city,

                            @RequestParam(value = "rbf_car", required = false) Double rbfCar,
                            @RequestParam(value = "rbf_scooter", required = false) Double rbfScooter,
                            @RequestParam(value = "rbf_bike", required = false) Double rbfBike,

                            @RequestParam(value = "atef_temperature", required = false) Double atefTemperature,
                            @RequestParam(value = "atef_temperature_min", required = false) Double atefTemperatureMin,
                            @RequestParam(value = "atef_temperature_fee", required = false) Double atefTemperatureFee,
                            @RequestParam(value = "atef_temperature_fee_max", required = false) Double atefTemperatureFeeMax,

                            @RequestParam(value = "wsef_speed", required = false) Double wsefSpeed,
                            @RequestParam(value = "wsef_speed_max", required = false) Double wsefSpeedMax,
                            @RequestParam(value = "wsef_fee", required = false) Double wsefFee,

                            @RequestParam(value = "wpef_snow_or_sleetFee", required = false) Double wpefSnowOrSleetFee,
                            @RequestParam(value = "wpef_snow_rain_fee", required = false) Double wpefSnowRainFee,
                            Model model
    ) throws InvalidCityParameterException, SQLException {
        // Handle case error
        city = getCityNam(city);
        // Get weather station's name
        String weatherStation = getWeatherStation(city);
        // Control of input correctness
        if (rbfController(rbfCar, rbfScooter, rbfBike) || atefController(atefTemperatureFee, atefTemperatureFeeMax) || wsfeController(wsefSpeed, wsefSpeedMax, wsefFee) || wpfeController(wpefSnowOrSleetFee, wpefSnowRainFee)) {
            throw new NonPositiveArgumentException();
        }
        // Create a model for setting new fee rules.
        FeeCalculationRules feeCalculationRules = new FeeCalculationRules(
                city, weatherStation, null,
                rbfCar, rbfBike, rbfScooter,
                atefTemperature, atefTemperatureMin, atefTemperatureFee, atefTemperatureFeeMax,
                wsefSpeed, wsefSpeedMax, wsefFee,
                wpefSnowOrSleetFee, wpefSnowRainFee,
                null);
        this.feeCalculationRulesDAO.updateFeeCalculationRules(feeCalculationRules);
        String[] rules = feeCalculationRulesDAO.getFeeCalculationRules().split("\n");
        model.addAttribute("message", rules);
        return "api/rules";
    }

    /**
     * Takes 3 optional request parameters. If all parameters are nulls - all business rules will be reset to default
     * values. If at least one parameter is not null, null parameters will not be reset to default values.
     * All true parameters will cause reset to default values for business rules in the corresponding city.
     * @param bool1 reset business rules to default values in Tallinn.
     * @param bool2 reset business rules to default values in Tartu.
     * @param bool3 reset business rules to default values in Pärnu.
     * @return a html page.
     * @throws SQLException thrown if there is any problem with DB connection or DB request.
     */
    @PostMapping("/rules/old")
    private String resetRules(
            @RequestParam(value = "tallinn", required = false) Boolean bool1,
            @RequestParam(value = "tartu", required = false) Boolean bool2,
            @RequestParam(value = "pärnu", required = false) Boolean bool3,
            Model model
    ) throws SQLException {
        this.feeCalculationRulesDAO.resetFeeRules(bool1, bool2, bool3);
        String[] rules = feeCalculationRulesDAO.getFeeCalculationRules().split("\n");
        model.addAttribute("message", rules);
        return "api/rules";
    }
}
