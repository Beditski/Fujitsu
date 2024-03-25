package com.fujitsu.deliveryapp.controllers;

//import com.fujitsu.deliveryapp.DAO.FeeDAO;
import com.fujitsu.deliveryapp.DAO.FeeDAO;
import com.fujitsu.deliveryapp.models.FeeRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.text.DateFormat;

@Controller
@RequestMapping("/fee")
public class FeeController {

    private final FeeDAO feeDAO;

    @Autowired
    public FeeController(FeeDAO feeDAO) {
        this.feeDAO = feeDAO;
    }

    @GetMapping("/calculator")
//    @ResponseBody
    private String getFee(@RequestParam(value = "city") String city,
                         @RequestParam(value = "transport") String transport,
                         @RequestParam(value = "atef", required = false) double[] atef,
                         @RequestParam(value = "wsef", required = false) double[] wsef,
                         @RequestParam(value = "wpef", required = false) double[] wpef,
                         @RequestParam(value = "datetime", required = false) DateFormat dateTime
    ) throws SQLException {


        FeeRules fee = new FeeRules(city, transport, null, atef, wsef, wpef, dateTime);
//        System.out.println(fee.getRegion());
//        System.out.println(this.feeDAO.calculateFee(fee));
        return "fee/calculator";
    }

    @PostMapping("/rules")
    private String setFee(@RequestParam(value = "city") String city,
                         @RequestParam(value = "rbf", required = false) double[] rbf,
                         @RequestParam(value = "atef", required = false) double[] atef,
                         @RequestParam(value = "wsef", required = false) double[] wsef,
                         @RequestParam(value = "wpef", required = false) double[] wpef
    ) {
        FeeRules fee = new FeeRules(city, null, atef, wsef, wpef, wpef, null);
//        this.feeDAO.updateFeeRules(fee);
        return null;
    }

    @PostMapping("/rules/reset")
//    @ResponseBody
    private String setFee() {
//        this.feeDAO.resetFeeRules();
        return null;
    }
}
