package com.fujitsu.deliveryapp.controllers;

import com.fujitsu.deliveryapp.DAO.FeeDAO;
import com.fujitsu.deliveryapp.models.FeeRules;
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

    @GetMapping("/calculator")
    @ResponseBody
    private String getFee(@RequestParam(value = "region") String region,
                         @RequestParam(value = "transport") String transport,
                         @RequestParam(value = "atef", required = false) double[] atef,
                         @RequestParam(value = "wsef", required = false) double[] wsef,
                         @RequestParam(value = "wpef", required = false) double[] wpef,
                         @RequestParam(value = "datetime", required = false) DateFormat dateTime
    ) {
        FeeRules fee = new FeeRules(region, transport, null, atef, wsef, wpef, dateTime);
        return this.feeDAO.calculateFee(fee);
    }

    @PostMapping("/rules")
    @ResponseBody
    private String setFee(@RequestParam(value = "region") String region,
                         @RequestParam(value = "rbf", required = false) double[] rbf,
                         @RequestParam(value = "atef", required = false) double[] atef,
                         @RequestParam(value = "wsef", required = false) double[] wsef,
                         @RequestParam(value = "wpef", required = false) double[] wpef
    ) {
        FeeRules fee = new FeeRules(region, null, atef, wsef, wpef, wpef, null);
        this.feeDAO.updateFeeRules(fee);
        return null;
    }

    @PostMapping("/rules/reset")
    @ResponseBody
    private String setFee() {
        this.feeDAO.resetFeeRules();
        return null;
    }
}
