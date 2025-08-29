package com.regnosys.cdm.example;

import cdm.base.datetime.*;
import cdm.base.datetime.daycount.DayCountFractionEnum;
import cdm.base.datetime.daycount.metafields.FieldWithMetaDayCountFractionEnum;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.ReferenceWithMetaNonNegativeQuantitySchedule;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.PayerReceiver;
import cdm.observable.asset.FloatingRateOption;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.ReferenceWithMetaPriceSchedule;
import cdm.product.asset.FixedRateSpecification;
import cdm.product.asset.FloatingRateSpecification;
import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.RateSpecification;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.schedule.PaymentDates;
import cdm.product.common.schedule.RateSchedule;
import cdm.product.common.settlement.ResolvablePriceQuantity;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.records.Date;

import java.math.BigDecimal;

/**
 * Simple Interest Rate Swap Example
 * This demonstrates the most basic IRS structure: Fixed vs Floating
 */
public class SimpleIRSExample {

    public static void main(String[] args) {
        System.out.println("🎯 Creating Simple Interest Rate Swap...");
        System.out.println("========================================");

        // Create Fixed Rate Leg (5% fixed)
        InterestRatePayout fixedLeg = createFixedLeg();
        System.out.println("✅ Fixed Leg Created:");
        System.out.println("   - Rate: 5.0%");
        System.out.println("   - Payer: Party 1");
        System.out.println("   - Receiver: Party 2");

        // Create Floating Rate Leg (3M LIBOR)
        InterestRatePayout floatingLeg = createFloatingLeg();
        System.out.println("✅ Floating Leg Created:");
        System.out.println("   - Rate: 3M LIBOR");
        System.out.println("   - Payer: Party 2");
        System.out.println("   - Receiver: Party 1");

        // Display the IRS structure
        System.out.println("\n📊 IRS Structure:");
        System.out.println("==================");
        System.out.println("Party 1 pays: 5.0% fixed");
        System.out.println("Party 1 receives: 3M LIBOR");
        System.out.println("Party 2 pays: 3M LIBOR");
        System.out.println("Party 2 receives: 5.0% fixed");
        System.out.println("Notional: EUR 10,000,000");
        System.out.println("Term: 2 years");

        System.out.println("\n🎉 Simple IRS created successfully!");
        System.out.println("CDM objects are ready for validation, serialization, or regulatory reporting.");
    }

    /**
     * Create a fixed rate leg paying 5%
     */
    private static InterestRatePayout createFixedLeg() {
        return InterestRatePayout.builder()
                .setPriceQuantity(ResolvablePriceQuantity.builder()
                        .setQuantitySchedule(ReferenceWithMetaNonNegativeQuantitySchedule.builder()
                                .setReference(Reference.builder()
                                        .setScope("DOCUMENT")
                                        .setReference("notional-1")
                                        .build())
                                .build()))

                .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder()
                        .setValue(DayCountFractionEnum._30E_360)
                        .build())

                .setCalculationPeriodDates(CalculationPeriodDates.builder()
                        .setEffectiveDate(AdjustableOrRelativeDate.builder()
                                .setAdjustableDate(AdjustableDate.builder()
                                        .setUnadjustedDate(Date.of(2024, 1, 15))
                                        .setDateAdjustments(BusinessDayAdjustments.builder()
                                                .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING))))
                        .setTerminationDate(AdjustableOrRelativeDate.builder()
                                .setAdjustableDate(AdjustableDate.builder()
                                        .setUnadjustedDate(Date.of(2026, 1, 15))
                                        .setDateAdjustments(BusinessDayAdjustments.builder()
                                                .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)))))

                .setPaymentDates(PaymentDates.builder()
                        .setPaymentFrequency(Frequency.builder()
                                .setPeriodMultiplier(6)
                                .setPeriod(PeriodExtendedEnum.M)))

                .setRateSpecification(RateSpecification.builder()
                        .setFixedRate(FixedRateSpecification.builder()
                                .setRateSchedule(RateSchedule.builder()
                                        .setPrice(ReferenceWithMetaPriceSchedule.builder()
                                                .setReference(Reference.builder()
                                                        .setScope("DOCUMENT")
                                                        .setReference("price-1"))
                                                .setValue(Price.builder()
                                                        .setValue(BigDecimal.valueOf(0.05)) // 5%
                                                        .setUnit(UnitType.builder().setCurrencyValue("EUR"))
                                                        .setPerUnitOf(UnitType.builder().setCurrencyValue("EUR"))
                                                        .setPriceType(PriceTypeEnum.INTEREST_RATE)
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .build())

                .setPayerReceiver(PayerReceiver.builder()
                        .setPayer(CounterpartyRoleEnum.PARTY_1)
                        .setReceiver(CounterpartyRoleEnum.PARTY_2)
                        .build())

                .build();
    }

    /**
     * Create a floating rate leg paying 3M LIBOR
     */
    private static InterestRatePayout createFloatingLeg() {
        return InterestRatePayout.builder()
                .setPriceQuantity(ResolvablePriceQuantity.builder()
                        .setQuantitySchedule(ReferenceWithMetaNonNegativeQuantitySchedule.builder()
                                .setReference(Reference.builder()
                                        .setScope("DOCUMENT")
                                        .setReference("notional-2")
                                        .build())
                                .build()))

                .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder()
                        .setValue(DayCountFractionEnum.ACT_365_FIXED)
                        .build())

                .setCalculationPeriodDates(CalculationPeriodDates.builder()
                        .setEffectiveDate(AdjustableOrRelativeDate.builder()
                                .setAdjustableDate(AdjustableDate.builder()
                                        .setUnadjustedDate(Date.of(2024, 1, 15))
                                        .setDateAdjustments(BusinessDayAdjustments.builder()
                                                .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING))))
                        .setTerminationDate(AdjustableOrRelativeDate.builder()
                                .setAdjustableDate(AdjustableDate.builder()
                                        .setUnadjustedDate(Date.of(2026, 1, 15))
                                        .setDateAdjustments(BusinessDayAdjustments.builder()
                                                .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)))))

                .setPaymentDates(PaymentDates.builder()
                        .setPaymentFrequency(Frequency.builder()
                                .setPeriodMultiplier(3)
                                .setPeriod(PeriodExtendedEnum.M)))

                .setRateSpecification(RateSpecification.builder()
                        .setFloatingRate(FloatingRateSpecification.builder()
                                .setRateOptionValue(FloatingRateOption.builder()
                                        .setFloatingRateIndexValue(FloatingRateIndexEnum.EUR_LIBOR_BBA)
                                        .setIndexTenor(Period.builder()
                                                .setPeriod(PeriodEnum.M)
                                                .setPeriodMultiplier(3)))
                                .build())
                        .build())

                .setPayerReceiver(PayerReceiver.builder()
                        .setPayer(CounterpartyRoleEnum.PARTY_2)
                        .setReceiver(CounterpartyRoleEnum.PARTY_1)
                        .build())

                .build();
    }
}
