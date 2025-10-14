package org.example.checkout;

public class ShippingService {

    /**
     * Frete:
     * - grátis se freeShipping == true OU subtotal >= 300
     * - senão por região/peso:
     *   SUL/SUDESTE: 20/35/50
     *   NORTE:       30/55/80
     *   outras:      fixo 40
     */
    public double calculate(String region, double weight, double subtotal, boolean freeShipping) {
        if (weight < 0) throw new IllegalArgumentException("weight < 0");

        if (freeShipping || subtotal >= 300.0) return 0.0;

        String r = region == null ? "" : region.trim().toUpperCase();

        if ("SUL".equals(r) || "SUDESTE".equals(r)) {
            if (weight <= 2.0) return 20.0;
            if (weight <= 5.0) return 35.0;
            return 50.0;
        } else if ("NORTE".equals(r)) {
            if (weight <= 2.0) return 30.0;
            if (weight <= 5.0) return 55.0;
            return 80.0;
        } else {
            return 40.0;
        }
    }
}
