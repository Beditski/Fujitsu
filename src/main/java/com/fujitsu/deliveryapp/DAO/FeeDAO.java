package com.fujitsu.deliveryapp.DAO;

import com.fujitsu.deliveryapp.models.FeeRules;
import org.springframework.stereotype.Component;

@Component
public class FeeDAO {
    public String calculateFee(FeeRules fee) {
        return String.valueOf(0.0);
    }

    public String updateFeeRules(FeeRules fee) {
        return null;
    }

    public void resetFeeRules() {

    }
}
