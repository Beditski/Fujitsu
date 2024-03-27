package com.fujitsu.deliveryapp.controllers;

import com.fujitsu.deliveryapp.DAO.FeeCalculationRulesDAO;
import com.fujitsu.deliveryapp.models.FeeCalculationRules;
import com.fujitsu.deliveryapp.projectExceptions.controllers.FeeCalculationRulesController.InvalidCityParameterException;
import com.fujitsu.deliveryapp.projectExceptions.controllers.FeeCalculationRulesController.NonPositiveArgumentException;
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
     * Loads /fee page.
     * @return /fee html page
     */
    @GetMapping("")
    private String loadPage() {
        return "/fee";
    }

    /**
     * Processes input parameters. Throws an errors if parameters are incorrect.
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
        /*
         * Determines weather station's name, handles case error,
         * throw InvalidCityParameterException exception if wrong city
         * was specified in the request.
         */
        String weatherStation = switch (city.toLowerCase()) {
            case "tallinn" -> "Tallinn-Harku";
            case "tartu" -> "Tartu-Tõravere";
            case "pärnu" -> "Pärnu";
            default -> throw new InvalidCityParameterException();
        };

        /*
         * Controls whether all fee parameters and wind speed parameters are
         * positive, if not throws NonPositiveArgumentException.
         */
        if (rbfCar != null && rbfCar <= 0 ||
                rbfScooter != null && rbfScooter <= 0 ||
                rbfBike != null && rbfBike <= 0 ||
                atefTemperatureFee != null && atefTemperatureFee <= 0 ||
                atefTemperatureFeeMax != null && atefTemperatureFeeMax <= 0 ||
                wsefSpeed != null && wsefSpeed <= 0 ||
                wsefSpeedMax != null && wsefSpeedMax <= 0 ||
                wsefFee != null && wsefFee <= 0 ||
                wpefSnowOrSleetFee != null && wpefSnowOrSleetFee <= 0 ||
                wpefSnowRainFee != null && wpefSnowRainFee <= 0) {
            throw new NonPositiveArgumentException();
        }

        // Create model for calculating fee rules.
        FeeCalculationRules fee = new FeeCalculationRules(city, weatherStation, transport,
                rbfCar, rbfBike, rbfScooter,
                atefTemperature, atefTemperatureMin, atefTemperatureFee, atefTemperatureFeeMax,
                wsefSpeed, wsefSpeedMax,wsefFee,
                wpefSnowOrSleetFee, wpefSnowRainFee,
                // If date format is null - add null, if not - convert to timestamp and add
                dateFormat == null ? null : dateFormat.getCalendar().getTimeInMillis() / 1000);

        // Requesting and setting absent parameters in fee calculation.
        feeCalculationRulesDAO.setFeeCalculationRules(fee);
        System.out.println(this.feeCalculationRulesDAO.calculateFee(fee));
        return "fee/calculator";
    }
}
