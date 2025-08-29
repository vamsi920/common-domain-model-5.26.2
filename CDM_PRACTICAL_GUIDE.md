# 🚀 CDM Practical Guide: Get Started with Financial Products

## 🎯 Your CDM Learning Journey Starts Here!

You now have a fully working CDM environment. Let's dive into practical examples and build your understanding step by step.

---

## 📋 What You Have Set Up

✅ **Maven Project**: Fully compiled and ready  
✅ **CDM Classes**: Generated from Rosetta models  
✅ **Examples**: Working code you can run  
✅ **Dependencies**: All required libraries

---

## 🏗️ CDM Architecture: The Big Picture

### Core CDM Philosophy

CDM represents **financial reality in code**. It's not about legal documents - it's about:

- **Economic Terms**: What gets paid, when, and how
- **Risk Dimensions**: Market, credit, liquidity risks
- **Lifecycle Events**: Trade, settlement, termination

### Key CDM Packages

```
cdm/
├── base/           # Building blocks (dates, amounts, parties)
├── product/        # Product definitions
│   ├── template/   # Product templates
│   ├── asset/      # Economic terms (rates, payouts)
│   └── common/     # Shared components
├── event/          # Lifecycle events
├── observable/     # Market data
└── legaldocumentation/  # Legal terms
```

---

## 💡 Understanding CDM: Economic vs Legal

### Traditional Approach (Legal-First)

```xml
<!-- ISDA Master Agreement -->
<agreement>
  <party1>Bank A</party1>
  <party2>Corporation B</party2>
  <trade>
    <fixedRate>5.0%</fixedRate>
    <floatingRate>LIBOR</floatingRate>
  </trade>
</agreement>
```

### CDM Approach (Economic-First)

```java
// Economic reality: Two cash flow streams
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
```

**Key Difference**: CDM focuses on **economic cash flows**, not legal documentation.

---

## 🔧 Your First CDM Product: Interest Rate Swap

### Step 1: Understand the Economic Reality

An Interest Rate Swap has **two legs**:

1. **Fixed Leg**: Pays a fixed rate (e.g., 5%)
2. **Floating Leg**: Pays a floating rate (e.g., 3M LIBOR)

### Step 2: Build the Product in CDM

Let's examine the existing example:

```java
// From InterestRatePayoutCreation.java
public static InterestRatePayout getFixedRatePayout(BigDecimal fixedRate) {
    return InterestRatePayout.builder()
        .setPriceQuantity(ResolvablePriceQuantity.builder()
            .setQuantitySchedule(ReferenceWithMetaNonNegativeQuantitySchedule.builder()
                .setReference(Reference.builder()
                    .setScope("DOCUMENT")
                    .setReference("quantity-2"))))
        .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder()
            .setValue(DayCountFractionEnum._30E_360).build())
        .setRateSpecification(RateSpecification.builder()
            .setFixedRate(FixedRateSpecification.builder()
                .setRateSchedule(RateSchedule.builder()
                    .setPrice(ReferenceWithMetaPriceSchedule.builder()
                        .setReference(Reference.builder()
                            .setScope("DOCUMENT")
                            .setReference("price-1"))
                        .setValue(Price.builder()
                            .setValue(fixedRate)
                            .setUnit(UnitType.builder().setCurrencyValue("EUR"))
                            .setPerUnitOf(UnitType.builder().setCurrencyValue("EUR"))
                            .setPriceType(PriceTypeEnum.INTEREST_RATE))))))
        .setPayerReceiver(PayerReceiver.builder()
            .setPayer(CounterpartyRoleEnum.PARTY_2)
            .setReceiver(CounterpartyRoleEnum.PARTY_1))
        .build();
}
```

### Step 3: Key CDM Concepts in Action

#### 1. **References** (Avoiding Duplication)

```java
.setReference(Reference.builder()
    .setScope("DOCUMENT")
    .setReference("quantity-2"))
```

Instead of repeating the notional amount everywhere, we reference it.

#### 2. **Composition** (Building Complex from Simple)

```java
InterestRatePayout.builder()
    .setRateSpecification(...)      // Rate terms
    .setPayerReceiver(...)          // Payment direction
    .setDayCountFraction(...)       // Day count convention
    .build()
```

#### 3. **Economic Terms** (What Matters for Risk)

- **Rate Specification**: How the rate is calculated
- **Payer/Receiver**: Who pays whom
- **Day Count Fraction**: How time periods are calculated

---

## 🎯 Running Your First CDM Example

### Command to Run Examples

```bash
cd examples
mvn exec:java -Dexec.mainClass="com.regnosys.cdm.example.EnumSerialisation"
```

### What This Shows You

1. **CDM Enums**: How financial terms are standardized
2. **Serialization**: How CDM objects become JSON
3. **Validation**: Built-in business rules

---

## 📚 Learning Path: Week by Week

### Week 1: Foundation (Days 1-7)

#### Day 1-2: CDM Philosophy

- **Read**: CDM_LEARNING_GUIDE.md (the comprehensive guide)
- **Practice**: Run the EnumSerialisation example
- **Understand**: Economic-first vs legal-first approach

#### Day 3-4: Basic Building Blocks

- **Study**: `cdm.base` package
- **Practice**: Create simple date and amount objects
- **Understand**: References and composition

#### Day 5-7: Product Templates

- **Study**: `cdm.product.template` package
- **Practice**: Create simple product templates
- **Understand**: Product vs TradableProduct

### Week 2: Core Products (Days 8-14)

#### Day 8-9: Interest Rate Products

- **Study**: `cdm.product.asset.InterestRatePayout`
- **Practice**: Create fixed and floating rate legs
- **Run**: `InterestRatePayoutCreation` examples

#### Day 10-11: Credit Products

- **Study**: `cdm.product.asset.CreditDefaultPayout`
- **Practice**: Create simple CDS structures
- **Understand**: Protection terms and premium legs

#### Day 12-14: Foreign Exchange Products

- **Study**: `cdm.product.asset.FxPayout`
- **Practice**: Create FX forwards and swaps
- **Understand**: Currency pairs and settlement

### Week 3: Advanced Features (Days 15-21)

#### Day 15-16: Validation

- **Study**: Validation framework
- **Practice**: Run validation examples
- **Understand**: Business rule enforcement

#### Day 17-18: Events

- **Study**: `cdm.event` package
- **Practice**: Create lifecycle events
- **Understand**: Event-driven architecture

#### Day 19-21: Market Data

- **Study**: `cdm.observable` package
- **Practice**: Create rate observations
- **Understand**: Market data integration

### Week 4: Real-World Application (Days 22-28)

#### Day 22-23: Complex Products

- **Study**: Multi-leg products
- **Practice**: Combine multiple payouts
- **Understand**: Product composition

#### Day 24-25: Regulatory Compliance

- **Study**: Legal documentation
- **Practice**: Link economic to legal terms
- **Understand**: Compliance requirements

#### Day 26-28: Integration

- **Study**: System integration patterns
- **Practice**: Serialization/deserialization
- **Understand**: Production deployment

---

## 🔍 Deep Dive: Understanding CDM Objects

### 1. **Product Template** (The Foundation)

```java
Product product = Product.builder()
    .setProductId(ProductIdentifier.builder()
        .setIssuer("Bank A")
        .setProductType("Interest Rate Swap")
        .build())
    .build();
```

**Purpose**: Defines what the product is (not how it behaves)

### 2. **Tradable Product** (Can Be Traded)

```java
TradableProduct tradableProduct = TradableProduct.builder()
    .setProduct(product)
    .build();
```

**Purpose**: Makes the product tradeable (adds trading capabilities)

### 3. **Economic Terms** (The Cash Flows)

```java
InterestRatePayout payout = InterestRatePayout.builder()
    .setRateSpecification(...)  // How rate is calculated
    .setPayerReceiver(...)      // Who pays whom
    .setDayCountFraction(...)   // How time is calculated
    .build();
```

**Purpose**: Defines the economic behavior

### 4. **Events** (Lifecycle)

```java
Trade trade = Trade.builder()
    .setTradeDate(Date.of(2024, 1, 15))
    .setTradableProduct(tradableProduct)
    .build();
```

**Purpose**: Records when things happen

---

## 🛠️ Practical Exercises

### Exercise 1: Create a Simple Fixed Rate Bond

```java
// Your task: Create a fixed rate bond with:
// - 5% coupon rate
// - Annual payments
// - 5-year maturity
// - $1,000,000 notional
```

### Exercise 2: Create a Forward Rate Agreement (FRA)

```java
// Your task: Create a 3x6 FRA with:
// - 3M LIBOR reference rate
// - 6-month period
// - $10,000,000 notional
```

### Exercise 3: Create a Credit Default Swap (CDS)

```java
// Your task: Create a single-name CDS with:
// - 100 basis points premium
// - Quarterly payments
// - $5,000,000 notional
```

---

## 🎯 Key Takeaways for Success

### 1. **Think Economically, Not Legally**

- Focus on cash flows, not legal documents
- Ask: "Who pays what, when, and how?"

### 2. **Use Composition**

- Build complex products from simple components
- Don't try to create everything at once

### 3. **Leverage References**

- Use references to avoid duplication
- Link related data without copying

### 4. **Embrace Validation**

- CDM has built-in business rules
- Let the framework catch your mistakes

### 5. **Start Simple**

- Begin with basic products
- Add complexity gradually

---

## 🚀 Next Steps

1. **Run the Examples**: Execute all available examples
2. **Study the Code**: Understand how examples work
3. **Modify Parameters**: Change rates, dates, amounts
4. **Create Your Own**: Build simple products from scratch
5. **Explore Validation**: Understand business rules
6. **Practice Serialization**: Work with JSON/XML

---

## 📖 Resources

- **CDM Documentation**: `docs/` directory
- **Examples**: `examples/src/main/java/`
- **Generated Code**: `rosetta-project/src/generated/java/`
- **Tests**: `tests/src/test/java/`

---

## 🎉 You're Ready!

You now have:

- ✅ A working CDM environment
- ✅ Understanding of CDM philosophy
- ✅ Practical examples to learn from
- ✅ A clear learning path
- ✅ Tools to experiment and build

**Start with the examples, understand the patterns, and build your CDM expertise step by step!**

Remember: **CDM is about representing financial reality in code**. Focus on the economics, and the rest will follow.

