package org.example.checkout;

public final class CheckoutResult {
    public final double subtotal;
    public final double discountValue;
    public final double tax;
    public final double shipping;
    public final double total;

    CheckoutResult(double subtotal, double discountValue, double tax, double shipping, double total) {
        this.subtotal = subtotal;
        this.discountValue = discountValue;
        this.tax = tax;
        this.shipping = shipping;
        this.total = total;
    }
}
