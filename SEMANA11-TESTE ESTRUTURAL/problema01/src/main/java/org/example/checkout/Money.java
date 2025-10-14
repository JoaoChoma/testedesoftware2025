package org.example.checkout;

import java.math.BigDecimal;
import java.math.RoundingMode;

final class Money {
    private Money() {}

    static double round2(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}