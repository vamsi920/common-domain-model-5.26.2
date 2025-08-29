# 🎯 CDM Learning Guide: From Economic Concepts to Contractual Products

## 📚 Table of Contents

1. [Economic Foundation](#economic-foundation)
2. [CDM Architecture Overview](#cdm-architecture-overview)
3. [Core CDM Concepts](#core-cdm-concepts)
4. [Contractual Products](#contractual-products)
5. [Practical Implementation](#practical-implementation)
6. [Advanced Topics](#advanced-topics)

---

## 🏗️ Economic Foundation

### What is CDM?

The **Common Domain Model (CDM)** is a standardized, machine-readable blueprint for financial products that covers the entire transaction lifecycle. It's designed to:

- **Standardize** how financial products are represented
- **Enable** interoperability between systems
- **Support** regulatory compliance
- **Facilitate** risk management

### Key Economic Concepts in CDM:

#### 1. **Economic Events**

- **Trade Events**: Initial trade execution
- **Lifecycle Events**: Modifications, assignments, terminations
- **Settlement Events**: Cash flows, deliveries
- **Valuation Events**: Mark-to-market, pricing

#### 2. **Economic Terms**

- **Payment Terms**: Who pays what, when, and how
- **Calculation Terms**: How amounts are computed
- **Settlement Terms**: How obligations are fulfilled
- **Reference Terms**: Market data, indices, rates

#### 3. **Risk Dimensions**

- **Market Risk**: Price movements, interest rates, FX
- **Credit Risk**: Counterparty default
- **Liquidity Risk**: Ability to exit positions
- **Operational Risk**: Settlement, documentation

---

## 🏛️ CDM Architecture Overview

### Core CDM Packages:

```
cdm/
├── base/           # Fundamental building blocks
├── product/        # Product definitions and templates
├── event/          # Economic events and lifecycle
├── observable/     # Market data and pricing
├── legaldocumentation/  # Legal terms and conditions
└── security/       # Security-specific features
```

### Key Design Principles:

1. **Separation of Concerns**: Economic terms vs. legal terms
2. **Composition over Inheritance**: Build complex products from simple components
3. **Reference-based**: Link related data without duplication
4. **Validation-driven**: Built-in business rule validation

---

## 🔧 Core CDM Concepts

### 1. **Product Template** (`cdm.product.template`)

The foundation of any financial product:

```java
// Core product structure
Product product = Product.builder()
    .setProductId(ProductIdentifier.builder()
        .setIssuer("Bank A")
        .setProductType("Interest Rate Swap")
        .build())
    .build();

// Tradable product (can be traded)
TradableProduct tradableProduct = TradableProduct.builder()
    .setProduct(product)
    .build();
```

### 2. **Economic Terms** (`cdm.product.asset`)

Define the economic characteristics:

```java
// Interest rate payout
InterestRatePayout payout = InterestRatePayout.builder()
    .setRateSpecification(RateSpecification.builder()
        .setFixedRate(FixedRateSpecification.builder()
            .setRateSchedule(RateSchedule.builder()
                .setPrice(Price.builder()
                    .setValue(BigDecimal.valueOf(0.05))
                    .setUnit(UnitType.builder().setCurrencyValue("USD"))
                    .build())
                .build())
            .build())
        .build())
    .build();
```

### 3. **Events** (`cdm.event`)

Represent lifecycle events:

```java
// Trade event
Trade trade = Trade.builder()
    .setTradeDate(Date.of(2024, 1, 15))
    .setTradableProduct(tradableProduct)
    .setTradeIdentifier(Arrays.asList(
        TradeIdentifier.builder()
            .setIssuer("Bank A")
            .setTradeId("TRADE-001")
            .build()))
    .build();
```

---

## 📋 Contractual Products

### 1. **Interest Rate Products**

#### Interest Rate Swap (IRS)

```java
// Fixed-for-Floating IRS
InterestRatePayout fixedLeg = InterestRatePayout.builder()
    .setRateSpecification(RateSpecification.builder()
        .setFixedRate(FixedRateSpecification.builder()
            .setRateSchedule(RateSchedule.builder()
                .setPrice(Price.builder()
                    .setValue(BigDecimal.valueOf(0.05))
                    .setUnit(UnitType.builder().setCurrencyValue("USD"))
                    .build())
                .build())
            .build())
        .build())
    .setPayerReceiver(PayerReceiver.builder()
        .setPayer(CounterpartyRoleEnum.PARTY_1)
        .setReceiver(CounterpartyRoleEnum.PARTY_2)
        .build())
    .build();

InterestRatePayout floatingLeg = InterestRatePayout.builder()
    .setRateSpecification(RateSpecification.builder()
        .setFloatingRate(FloatingRateSpecification.builder()
            .setRateOptionValue(FloatingRateOption.builder()
                .setFloatingRateIndexValue(FloatingRateIndexEnum.USD_LIBOR_BBA)
                .setIndexTenor(Period.builder()
                    .setPeriod(PeriodEnum.M)
                    .setPeriodMultiplier(3))
                .build())
            .build())
        .build())
    .setPayerReceiver(PayerReceiver.builder()
        .setPayer(CounterpartyRoleEnum.PARTY_2)
        .setReceiver(CounterpartyRoleEnum.PARTY_1)
        .build())
    .build();
```

#### Forward Rate Agreement (FRA)

```java
// FRA with 3x6 structure
InterestRatePayout fraPayout = InterestRatePayout.builder()
    .setRateSpecification(RateSpecification.builder()
        .setFloatingRate(FloatingRateSpecification.builder()
            .setRateOptionValue(FloatingRateOption.builder()
                .setFloatingRateIndexValue(FloatingRateIndexEnum.USD_LIBOR_BBA)
                .setIndexTenor(Period.builder()
                    .setPeriod(PeriodEnum.M)
                    .setPeriodMultiplier(3))
                .build())
            .build())
        .build())
    .setCalculationPeriodDates(CalculationPeriodDates.builder()
        .setEffectiveDate(AdjustableOrRelativeDate.builder()
            .setAdjustableDate(AdjustableDate.builder()
                .setUnadjustedDate(Date.of(2024, 4, 15))
                .build())
            .build())
        .setTerminationDate(AdjustableOrRelativeDate.builder()
            .setAdjustableDate(AdjustableDate.builder()
                .setUnadjustedDate(Date.of(2024, 7, 15))
                .build())
            .build())
        .build())
    .build();
```

### 2. **Credit Products**

#### Credit Default Swap (CDS)

```java
// Single-name CDS
CreditDefaultPayout cdsPayout = CreditDefaultPayout.builder()
    .setProtectionTerms(ProtectionTerms.builder()
        .setReferenceObligation(ReferenceObligation.builder()
            .setIssuer("Corporation XYZ")
            .setObligationType(ObligationTypeEnum.BOND)
            .build())
        .build())
    .setPremiumLeg(PremiumLeg.builder()
        .setPremiumRate(RateSchedule.builder()
            .setPrice(Price.builder()
                .setValue(BigDecimal.valueOf(0.025))
                .setUnit(UnitType.builder().setCurrencyValue("USD"))
                .build())
            .build())
        .build())
    .build();
```

### 3. **Foreign Exchange Products**

#### FX Forward

```java
// USD/EUR FX Forward
FxPayout fxPayout = FxPayout.builder()
    .setFxRate(FxRate.builder()
        .setQuotedCurrencyPair(QuotedCurrencyPair.builder()
            .setCurrency1("USD")
            .setCurrency2("EUR")
            .build())
        .setRate(Price.builder()
            .setValue(BigDecimal.valueOf(0.85))
            .setUnit(UnitType.builder().setCurrencyValue("EUR"))
            .setPerUnitOf(UnitType.builder().setCurrencyValue("USD"))
            .build())
        .build())
    .setSettlementDate(AdjustableDate.builder()
        .setUnadjustedDate(Date.of(2024, 3, 15))
        .build())
    .build();
```

---

## 💻 Practical Implementation

### 1. **Creating a Simple Interest Rate Swap**

```java
public class InterestRateSwapExample {

    public static void createSimpleIRS() {
        // 1. Create the product template
        Product product = Product.builder()
            .setProductId(ProductIdentifier.builder()
                .setIssuer("Bank A")
                .setProductType("Interest Rate Swap")
                .build())
            .build();

        TradableProduct tradableProduct = TradableProduct.builder()
            .setProduct(product)
            .build();

        // 2. Create fixed leg (5% fixed rate)
        InterestRatePayout fixedLeg = InterestRatePayout.builder()
            .setRateSpecification(RateSpecification.builder()
                .setFixedRate(FixedRateSpecification.builder()
                    .setRateSchedule(RateSchedule.builder()
                        .setPrice(Price.builder()
                            .setValue(BigDecimal.valueOf(0.05))
                            .setUnit(UnitType.builder().setCurrencyValue("USD"))
                            .build())
                        .build())
                    .build())
                .build())
            .setPayerReceiver(PayerReceiver.builder()
                .setPayer(CounterpartyRoleEnum.PARTY_1)
                .setReceiver(CounterpartyRoleEnum.PARTY_2)
                .build())
            .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder()
                .setValue(DayCountFractionEnum._30E_360)
                .build())
            .build();

        // 3. Create floating leg (3M LIBOR)
        InterestRatePayout floatingLeg = InterestRatePayout.builder()
            .setRateSpecification(RateSpecification.builder()
                .setFloatingRate(FloatingRateSpecification.builder()
                    .setRateOptionValue(FloatingRateOption.builder()
                        .setFloatingRateIndexValue(FloatingRateIndexEnum.USD_LIBOR_BBA)
                        .setIndexTenor(Period.builder()
                            .setPeriod(PeriodEnum.M)
                            .setPeriodMultiplier(3))
                        .build())
                    .build())
                .build())
            .setPayerReceiver(PayerReceiver.builder()
                .setPayer(CounterpartyRoleEnum.PARTY_2)
                .setReceiver(CounterpartyRoleEnum.PARTY_1)
                .build())
            .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder()
                .setValue(DayCountFractionEnum.ACT_360)
                .build())
            .build();

        // 4. Create the trade
        Trade trade = Trade.builder()
            .setTradeDate(Date.of(2024, 1, 15))
            .setTradableProduct(tradableProduct)
            .setTradeIdentifier(Arrays.asList(
                TradeIdentifier.builder()
                    .setIssuer("Bank A")
                    .setTradeId("IRS-2024-001")
                    .build()))
            .build();

        System.out.println("Interest Rate Swap created successfully!");
        System.out.println("Fixed Rate: 5%");
        System.out.println("Floating Rate: 3M USD LIBOR");
        System.out.println("Trade Date: " + trade.getTradeDate());
    }
}
```

### 2. **Validation and Business Rules**

```java
public class ValidationExample {

    public static void validateProduct(InterestRatePayout payout) {
        // Get validator factory
        ValidatorFactory.Default validatorFactory = new ValidatorFactory.Default();

        // Get validators for InterestRatePayout
        InterestRatePayoutMeta meta = (InterestRatePayoutMeta) payout.metaData();
        List<Validator<?>> validators = meta.dataRules(validatorFactory);

        // Add cardinality validator
        validators.add(meta.validator());

        // Run all validators
        validators.stream()
            .map(validator -> validator.validate(
                RosettaPath.valueOf("InterestRatePayout"),
                payout))
            .filter(result -> !result.isSuccess())
            .forEach(result -> {
                System.out.println("Validation Error: " + result.getFailureReason());
            });
    }
}
```

### 3. **Serialization and Deserialization**

```java
public class SerializationExample {

    public static void serializeToJSON(InterestRatePayout payout) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new RosettaModule());

            String json = mapper.writeValueAsString(payout);
            System.out.println("JSON Representation:");
            System.out.println(json);

            // Deserialize back
            InterestRatePayout deserialized = mapper.readValue(json, InterestRatePayout.class);
            System.out.println("Deserialization successful!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

## 🚀 Advanced Topics

### 1. **Complex Product Structures**

#### Multi-Leg Products

```java
// Interest Rate Swap with multiple legs
List<InterestRatePayout> legs = Arrays.asList(
    createFixedLeg(),
    createFloatingLeg(),
    createInflationLeg()
);

// Combine into a single product
Product multiLegProduct = Product.builder()
    .setProductId(ProductIdentifier.builder()
        .setIssuer("Bank A")
        .setProductType("Multi-Leg Interest Rate Swap")
        .build())
    .build();
```

### 2. **Event Processing**

#### Lifecycle Events

```java
// Assignment event
AssignmentEvent assignment = AssignmentEvent.builder()
    .setEventDate(Date.of(2024, 2, 1))
    .setAssignedParty(CounterpartyRoleEnum.PARTY_3)
    .setOriginalParty(CounterpartyRoleEnum.PARTY_2)
    .build();

// Termination event
TerminationEvent termination = TerminationEvent.builder()
    .setEventDate(Date.of(2024, 12, 31))
    .setTerminationType(TerminationTypeEnum.EARLY_TERMINATION)
    .build();
```

### 3. **Market Data Integration**

#### Observable Assets

```java
// LIBOR rate observation
FloatingRateOption libor = FloatingRateOption.builder()
    .setFloatingRateIndexValue(FloatingRateIndexEnum.USD_LIBOR_BBA)
    .setIndexTenor(Period.builder()
        .setPeriod(PeriodEnum.M)
        .setPeriodMultiplier(3))
    .build();

// Price observation
Price price = Price.builder()
    .setValue(BigDecimal.valueOf(0.025))
    .setUnit(UnitType.builder().setCurrencyValue("USD"))
    .setPriceType(PriceTypeEnum.INTEREST_RATE)
    .build();
```

---

## 📖 Learning Path

### Week 1: Foundation

1. **Day 1-2**: Understand economic concepts and CDM architecture
2. **Day 3-4**: Master basic product templates and economic terms
3. **Day 5-7**: Practice with simple interest rate products

### Week 2: Core Products

1. **Day 1-2**: Interest Rate Swaps and FRAs
2. **Day 3-4**: Credit Default Swaps
3. **Day 5-7**: Foreign Exchange products

### Week 3: Advanced Features

1. **Day 1-2**: Validation and business rules
2. **Day 3-4**: Event processing and lifecycle
3. **Day 5-7**: Market data and pricing

### Week 4: Real-World Application

1. **Day 1-2**: Complex multi-leg products
2. **Day 3-4**: Regulatory compliance
3. **Day 5-7**: Integration with trading systems

---

## 🎯 Key Takeaways

1. **CDM is Economic-First**: Focus on economic terms, not legal terms
2. **Composition is Key**: Build complex products from simple components
3. **Validation is Built-in**: Business rules are enforced automatically
4. **Events Drive Lifecycle**: Everything is event-driven
5. **Reference Everything**: Use references to avoid duplication

---

## 🔗 Next Steps

1. **Run the Examples**: Execute the provided examples
2. **Build Your Own**: Create simple products from scratch
3. **Explore Validation**: Understand business rule validation
4. **Study Events**: Learn about lifecycle events
5. **Practice Serialization**: Work with JSON/XML formats

Remember: **CDM is about representing financial reality in code**. Start simple, understand the economic concepts, and build complexity gradually!

