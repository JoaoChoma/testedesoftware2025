package org.example.checkout;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class CheckoutService {

    private final CouponService couponService;
    private final ShippingService shippingService;

    public CheckoutService(CouponService couponService, ShippingService shippingService) {
        this.couponService = Objects.requireNonNull(couponService);
        this.shippingService = Objects.requireNonNull(shippingService);
    }

    /**
     * Calcula o total:
     * 1) subtotal dos itens
     * 2) percentuais de desconto (tier + primeiraCompra + cupom%), com teto de 30%
     * 3) imposto 12% somente sobre itens NÃO BOOK, após desconto percentual
     * 4) frete conforme regras
     */
    public CheckoutResult checkout(
            List<Item> itens,
            CustomerTier tier,
            boolean primeiraCompra,
            String region,
            double peso,
            String couponCode,
            LocalDate today,
            LocalDate couponExpiryInclusive
    ) {
        Objects.requireNonNull(itens, "itens");
        Objects.requireNonNull(tier, "tier");
        Objects.requireNonNull(today, "today");

        double subtotal = 0.0;
        double subtotalTributavel = 0.0;
        for (Item i : itens) {
            double s = i.subtotal();
            subtotal += s;
            if (!"BOOK".equalsIgnoreCase(i.getCategoria())) {
                subtotalTributavel += s;
            }
        }
        subtotal = Money.round2(subtotal);
        subtotalTributavel = Money.round2(subtotalTributavel);

        // descontos percentuais
        double tierPct = (tier == CustomerTier.SILVER ? 0.05 : (tier == CustomerTier.GOLD ? 0.10 : 0.0));
        double firstPct = (primeiraCompra && subtotal >= 50.0) ? 0.05 : 0.0;

        CouponResult cr = couponService.evaluate(couponCode, today, couponExpiryInclusive, subtotal);
        double couponPct = cr.percent;

        double totalPct = tierPct + firstPct + couponPct;
        if (totalPct > 0.30) totalPct = 0.30;

        double discountValue = Money.round2(subtotal * totalPct);
        double baseAposDesconto = Money.round2(subtotal - discountValue);

        // imposto 12% apenas sobre parte tributável, após desconto
        double proporcaoTributavel = (subtotal == 0.0) ? 0.0 : (subtotalTributavel / subtotal);
        double baseTributavelAposDesc = Money.round2(baseAposDesconto * proporcaoTributavel);
        double tax = Money.round2(baseTributavelAposDesc * 0.12);

        // frete
        boolean freeByCouponWeight = cr.freeShipping && peso <= 5.0;
        double shipping = shippingService.calculate(region, peso, baseAposDesconto, freeByCouponWeight);

        double total = Money.round2(baseAposDesconto + tax + shipping);

        return new CheckoutResult(subtotal, discountValue, tax, shipping, total);
    }
}
