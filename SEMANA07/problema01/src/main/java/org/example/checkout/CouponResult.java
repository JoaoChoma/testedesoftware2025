package org.example.checkout;

final class CouponResult {
    final double percent;      // 0.10 = 10%  |  0.0 se sem desconto %
    final boolean freeShipping;

    CouponResult(double percent, boolean freeShipping) {
        this.percent = percent;
        this.freeShipping = freeShipping;
    }
}