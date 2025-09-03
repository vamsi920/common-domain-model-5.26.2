package com.regnosys.cdm.example;

import cdm.base.datetime.*;
import cdm.base.datetime.daycount.DayCountFractionEnum;
import cdm.base.datetime.daycount.metafields.FieldWithMetaDayCountFractionEnum;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.ReferenceWithMetaNonNegativeQuantitySchedule;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.PayerReceiver;
import cdm.product.asset.InterestRatePayout;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.schedule.PaymentDates;
import cdm.product.common.settlement.ResolvablePriceQuantity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.records.Date;

import java.util.Map;

/**
 * Simple Interest Rate Swap Example with JSON Input
 * Takes flat JSON input and builds basic CDM objects
 */
public class IRSWithJSONInput {

    public static void main(String[] args) {
        try {
            System.out.println("🎯 CDM IRS Example with JSON Input");
            System.out.println("==================================");

            // Sample JSON input (flat dictionary)
            String jsonInput = "{\n" +
                    "    \"tradeId\": \"IRS-001\",\n" +
                    "    \"notional\": 10000000,\n" +
                    "    \"currency\": \"EUR\",\n" +
                    "    \"fixedRate\": 0.05,\n" +
                    "    \"floatingIndex\": \"EUR_LIBOR_BBA\",\n" +
                    "    \"floatingTenor\": 3,\n" +
                    "    \"effectiveDate\": \"2024-01-15\",\n" +
                    "    \"terminationDate\": \"2026-01-15\",\n" +
                    "    \"fixedPaymentFrequency\": 6,\n" +
                    "    \"floatingPaymentFrequency\": 3,\n" +
                    "    \"fixedDayCount\": \"_30E_360\",\n" +
                    "    \"floatingDayCount\": \"ACT_365_FIXED\",\n" +
                    "    \"businessDayConvention\": \"MODFOLLOWING\"\n" +
                    "}";

            System.out.println("📥 JSON Input:");
            System.out.println(jsonInput);

            // Parse JSON input
            Map<String, Object> tradeData = parseJsonInput(jsonInput);
            System.out.println("✅ JSON parsed successfully");

            // Build basic CDM objects
            InterestRatePayout fixedLeg = buildBasicFixedLeg(tradeData);
            System.out.println("✅ Basic Fixed Rate Leg built");

            // Display results
            displayResults(tradeData, fixedLeg);

            System.out.println("\n🎉 CDM IRS processing completed successfully!");
            System.out.println("Ready for validation, serialization, or regulatory reporting.");

        } catch (Exception e) {
            System.err.println("❌ Error processing IRS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Map<String, Object> parseJsonInput(String jsonInput) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonInput, Map.class);
    }

    private static InterestRatePayout buildBasicFixedLeg(Map<String, Object> tradeData) {
        System.out.println("\n🔨 Building Basic Fixed Rate Leg...");

        // Extract values from trade data
        double notional = ((Number) tradeData.get("notional")).doubleValue();
        String currency = (String) tradeData.get("currency");
        double fixedRate = ((Number) tradeData.get("fixedRate")).doubleValue();
        String effectiveDate = (String) tradeData.get("effectiveDate");
        String terminationDate = (String) tradeData.get("terminationDate");
        int fixedPaymentFreq = ((Number) tradeData.get("fixedPaymentFrequency")).intValue();
        String fixedDayCount = (String) tradeData.get("fixedDayCount");
        String businessDayConv = (String) tradeData.get("businessDayConvention");

        // Parse enums
        DayCountFractionEnum dayCountEnum = DayCountFractionEnum.valueOf(fixedDayCount);
        BusinessDayConventionEnum businessDayEnum = BusinessDayConventionEnum.valueOf(businessDayConv);

        // Build basic InterestRatePayout with minimal required fields
        return InterestRatePayout.builder()
                .setPriceQuantity(ResolvablePriceQuantity.builder()
                        .setQuantitySchedule(ReferenceWithMetaNonNegativeQuantitySchedule.builder()
                                .setReference(Reference.builder()
                                        .setScope("DOCUMENT")
                                        .setReference("notional-fixed")
                                        .build())
                                .build())
                        .build())

                .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder()
                        .setValue(dayCountEnum)
                        .build())

                .setCalculationPeriodDates(CalculationPeriodDates.builder()
                        .setEffectiveDate(AdjustableOrRelativeDate.builder()
                                .setAdjustableDate(AdjustableDate.builder()
                                        .setUnadjustedDate(parseDate(effectiveDate))
                                        .setDateAdjustments(BusinessDayAdjustments.builder()
                                                .setBusinessDayConvention(businessDayEnum))))
                        .setTerminationDate(AdjustableOrRelativeDate.builder()
                                .setAdjustableDate(AdjustableDate.builder()
                                        .setUnadjustedDate(parseDate(terminationDate))
                                        .setDateAdjustments(BusinessDayAdjustments.builder()
                                                .setBusinessDayConvention(businessDayEnum)))))

                .setPaymentDates(PaymentDates.builder()
                        .setPaymentFrequency(Frequency.builder()
                                .setPeriodMultiplier(fixedPaymentFreq)
                                .setPeriod(PeriodExtendedEnum.M)))

                .setPayerReceiver(PayerReceiver.builder()
                        .setPayer(CounterpartyRoleEnum.PARTY_1)
                        .setReceiver(CounterpartyRoleEnum.PARTY_2)
                        .build())

                .build();
    }

    private static Date parseDate(String dateStr) {
        String[] parts = dateStr.split("-");
        return Date.of(
                Integer.parseInt(parts[0]), // year
                Integer.parseInt(parts[1]), // month
                Integer.parseInt(parts[2]) // day
        );
    }

    private static void displayResults(Map<String, Object> tradeData, InterestRatePayout fixedLeg) {
        System.out.println("\n📊 Results Summary:");
        System.out.println("===================");
        System.out.println("Trade ID: " + tradeData.get("tradeId"));
        System.out.println("Notional: " + tradeData.get("currency") + " " + tradeData.get("notional"));
        System.out.println("Fixed Rate: " + ((Number) tradeData.get("fixedRate")).doubleValue() * 100 + "%");
        System.out.println(
                "Floating Index: " + tradeData.get("floatingIndex") + " " + tradeData.get("floatingTenor") + "M");
        System.out.println("Effective Date: " + tradeData.get("effectiveDate"));
        System.out.println("Termination Date: " + tradeData.get("terminationDate"));
        System.out.println("Fixed Leg created successfully with CDM objects");
        System.out.println("CDM objects are ready for validation, serialization, or regulatory reporting");
    }
}
