package org.example.checkout;

import java.time.LocalDate;

public class CouponService {

    /**
     * Regra:
     * - DESC10: 10%, sempre válido
     * - DESC20: 20%, exige subtotal >= 100 e não estar expirado (expiryInclusive)
     * - FRETEGRATIS: percent = 0, freeShipping = true, apenas ativa se peso <= 5 (checado fora)
     * - null, "", desconhecido, expirado, mínimo não atendido => ignorar (0%, false)
     */
    public CouponResult evaluate(String code, LocalDate today, LocalDate expiryInclusive, double subtotal) {
        if (code == null || code.isBlank()) {
            return new CouponResult(0.0, false);
        }
        switch (code.trim().toUpperCase()) {
            case "DESC10":
                return new CouponResult(0.10, false);
            case "DESC20":
                boolean notExpired = expiryInclusive == null || !today.isAfter(expiryInclusive);
                if (subtotal >= 100.0 && notExpired) {
                    return new CouponResult(0.20, false);
                }
                return new CouponResult(0.0, false);
            case "FRETEGRATIS":
                return new CouponResult(0.0, true);
            default:
                return new CouponResult(0.0, false);
        }
    }
}