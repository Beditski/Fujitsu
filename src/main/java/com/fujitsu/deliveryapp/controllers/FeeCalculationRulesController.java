package com.fujitsu.deliveryapp.controllers;

//import com.fujitsu.deliveryapp.DAO.FeeDAO;
import com.fujitsu.deliveryapp.DAO.FeeDAO;
import com.fujitsu.deliveryapp.models.FeeCalculationRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;

@Controller
@RequestMapping("/fee")
public class FeeController {

    private final FeeDAO feeDAO;

    @Autowired
    public FeeController(FeeDAO feeDAO) {
        this.feeDAO = feeDAO;
    }

    /**
     *
     * @return
     */
    @GetMapping("")
    private String loadPage() {
        return "/fee";
    }

    /**
     *
     * @param city
     * @param transport
     * @param rbfCar
     * @param rbfScooter
     * @param rbfBike
     * @param atefTemperature
     * @param atefTemperatureMin
     * @param atefTemperatureFee
     * @param atefTemperatureFeeMax
     * @param wsefSpeed
     * @param wsefSpeedMax
     * @param wsefFee
     * @param wpefSnowOrSleetFee
     * @param wpefSnowRainFee
     * @param dateTime
     * @return
     * @throws Exception
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

                          @RequestParam(value = "datetime", required = false) DateFormat dateTime
    ) throws Exception {

        FeeCalculationRules fee = new FeeCalculationRules(city, transport,
                rbfCar, rbfBike, rbfScooter,
                atefTemperature, atefTemperatureMin, atefTemperatureFee, atefTemperatureFeeMax,
                wsefSpeed, wsefSpeedMax,wsefFee,
                wpefSnowOrSleetFee,wpefSnowRainFee,
                dateTime);
    feeDAO.setFeeCalculationRules(fee);
        System.out.println(this.feeDAO.calculateFee(fee));
        return "fee/calculator";
    }

    /**
     *
     * @param city
     * @param rbf
     * @param atef
     * @param wsef
     * @param wpef
     * @return
     */
    @PostMapping("/rules")
    private String setFee(@RequestParam(value = "city") String city,
                         @RequestParam(value = "rbf", required = false) Double[] rbf,
                         @RequestParam(value = "atef", required = false) Double[] atef,
                         @RequestParam(value = "wsef", required = false) Double[] wsef,
                         @RequestParam(value = "wpef", required = false) Double[] wpef
    ) {
//        FeeCalculationRules fee = new FeeCalculationRules(city, null, atef, wsef, wpef, wpef, null);
//        this.feeDAO.updateFeeRules(fee);
        return null;
    }

    /**
     *
     * @return
     */
    @PostMapping("/rules/reset")
//    @ResponseBody
    private String setFee() {
//        this.feeDAO.resetFeeRules();
        return null;
    }
}
