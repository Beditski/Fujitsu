package com.fujitsu.deliveryapp.controllers;

import com.fujitsu.deliveryapp.DAO.FeeCalculationRulesDAO.FeeCalculationRulesDAO;
import com.fujitsu.deliveryapp.models.FeeCalculationRules;
import com.fujitsu.deliveryapp.controllers.exceptions.InvalidCityParameterException;
import com.fujitsu.deliveryapp.controllers.exceptions.NonPositiveArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;

/**
 * Controller for handling GET and POST request related to the fee calculation rules.
 */
@Controller
@RequestMapping("/fee")
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
     * @param city city name
     * @return correct city name
     * @throws InvalidCityParameterException thrown if invalid city was choosen.
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
     * Loads /fee page.
     * @return /fee html page
     */
    @GetMapping("")
    private String loadPage() {
        return "/fee";
    }

    /**
     * Processes input parameters, creates a model and sends it to DAO to calculate delivery fee.
     * Throws an errors if parameters are incorrect.
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
     * @param dateFormat date and time for time moment, at which fee is wanted to be calculated (optional).
     * @throws Exception if something goes wrong - throw a corresponding exception.
     */
    @GetMapping("/calculator")
    private String getFee(@RequestParam(value = "city") String city,
                          @RequestParam(value = "transport") String transport,

                          @RequestParam(value = "rbfCar", required = false) Double rbfCar,
                          @RequestParam(value = "rbfScooter", required = false) Double rbfScooter,
                          @RequestParam(value = "rbfBike", required = false) Double rbfBike,

                          @RequestParam(value = "atefTemperature", required = false) Double atefTemperature,
                          @RequestParam(value = "atefTemperatureMin", required = false) Double atefTemperatureMin,
                          @RequestParam(value = "atefTemperatureFee", required = false) Double atefTemperatureFee,
                          @RequestParam(value = "atefTemperatureFeeMax", required = false) Double atefTemperatureFeeMax,

                          @RequestParam(value = "wsefSpeed", required = false) Double wsefSpeed,
                          @RequestParam(value = "wsefSpeedMax", required = false) Double wsefSpeedMax,
                          @RequestParam(value = "wsefFee", required = false) Double wsefFee,

                          @RequestParam(value = "wpefSnowOrSleetFee", required = false) Double wpefSnowOrSleetFee,
                          @RequestParam(value = "wpefSnowRainFee", required = false) Double wpefSnowRainFee,

                          @RequestParam(value = "dateformat", required = false) DateFormat dateFormat
    ) throws Exception {
        // Handle case error
        city = getCityNam(city);
        // Get weather station's name
        String weatherStation = getWeatherStation(city);
        // Control of input correctness
        if (rbfController(rbfCar, rbfScooter, rbfBike) || atefController(atefTemperatureFee, atefTemperatureFeeMax) || wsfeController(wsefSpeed, wsefSpeedMax, wsefFee) || wpfeController(wpefSnowOrSleetFee, wpefSnowRainFee)) {
            throw new NonPositiveArgumentException();
        }

        // Create a model for calculating feeCalculationRules rules.
        FeeCalculationRules feeCalculationRules = new FeeCalculationRules(
                city, weatherStation, transport,
                rbfCar, rbfBike, rbfScooter,
                atefTemperature, atefTemperatureMin, atefTemperatureFee, atefTemperatureFeeMax,
                wsefSpeed, wsefSpeedMax,wsefFee,
                wpefSnowOrSleetFee, wpefSnowRainFee,
                // If date format is null - add null, if not - convert to timestamp and add
                dateFormat == null ? null : dateFormat.getCalendar().getTimeInMillis() / 1000);

        // Requesting and setting absent parameters in feeCalculationRules calculation.
        this.feeCalculationRulesDAO.setFeeCalculationRules(feeCalculationRules);
        System.out.println(this.feeCalculationRulesDAO.calculateFee(feeCalculationRules));
        return "fee/calculator";
    }

    /**
     * Processes input parameters, creates a model and sends it to DAO to set new delivery fee rules.
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
     */
    @GetMapping("/rules")
    private String setRules(@RequestParam(value = "city") String city,

                            @RequestParam(value = "rbfCar", required = false) Double rbfCar,
                            @RequestParam(value = "rbfScooter", required = false) Double rbfScooter,
                            @RequestParam(value = "rbfBike", required = false) Double rbfBike,

                            @RequestParam(value = "atefTemperature", required = false) Double atefTemperature,
                            @RequestParam(value = "atefTemperatureMin", required = false) Double atefTemperatureMin,
                            @RequestParam(value = "atefTemperatureFee", required = false) Double atefTemperatureFee,
                            @RequestParam(value = "atefTemperatureFeeMax", required = false) Double atefTemperatureFeeMax,

                            @RequestParam(value = "wsefSpeed", required = false) Double wsefSpeed,
                            @RequestParam(value = "wsefSpeedMax", required = false) Double wsefSpeedMax,
                            @RequestParam(value = "wsefFee", required = false) Double wsefFee,

                            @RequestParam(value = "wpefSnowOrSleetFee", required = false) Double wpefSnowOrSleetFee,
                            @RequestParam(value = "wpefSnowRainFee", required = false) Double wpefSnowRainFee
    ) throws InvalidCityParameterException {
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

        this.feeCalculationRulesDAO.updateFeeRules(feeCalculationRules);

        return "fee/rules";
    }
}
