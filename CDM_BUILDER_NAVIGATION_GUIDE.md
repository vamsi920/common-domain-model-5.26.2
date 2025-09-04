# CDM Builder Navigation Guide

## 🎯 Understanding the CDM Builder Pattern

The CDM (Common Domain Model) uses a **builder pattern** where each object is constructed step by step. This can be overwhelming, but there are strategies to navigate it effectively.

## 🧠 Mental Model: Think Like Building a House

```
Foundation → Structure → Details → Finishing
   ↓           ↓         ↓         ↓
Dates      → Legs    → Rates   → Parties
Notional   → Schedules → DayCounts → References
```

## 🛠️ Tools to Navigate CDM

### 1. **Rosetta UI (Your Best Friend!)**

- **URL**: Usually at your CDM installation URL
- **Type Browser**: Search for any class (e.g., `InterestRatePayout`)
- **Builder Tab**: Shows all available setters
- **Sample Tab**: Working examples
- **Documentation**: Field descriptions and requirements

### 2. **IDE Features**

- **IntelliSense**: Type `.set` and use autocomplete
- **Hover Documentation**: Hover over methods to see details
- **Go to Definition**: Ctrl+Click (or Cmd+Click) to see the class

### 3. **CDM Sample Files**

- **Location**: `rosetta-source/src/main/resources/cdm-sample-files/`
- **Format**: XML examples for each product type
- **Use**: Map XML structure to Java builders

## 📋 Step-by-Step Navigation Strategy

### **Step 1: Start with the End in Mind**

Instead of building randomly, ask:

- What am I trying to create? (e.g., Interest Rate Swap)
- What are the main components? (e.g., Fixed Leg, Floating Leg)
- What does each component need? (e.g., Dates, Rates, Notional)

### **Step 2: Use the "Inside-Out" Approach**

Build the innermost objects first, then work outward:

```java
// ❌ DON'T: Try to build everything at once
// ✅ DO: Build step by step

// 1. Start with basic Date
Date effectiveDate = Date.of(2024, 1, 15);

// 2. Wrap in AdjustableDate
AdjustableDate adjustableDate = AdjustableDate.builder()
    .setUnadjustedDate(effectiveDate)
    .build();

// 3. Wrap in AdjustableOrRelativeDate
AdjustableOrRelativeDate adjustableOrRelativeDate = AdjustableOrRelativeDate.builder()
    .setAdjustableDate(adjustableDate)
    .build();
```

### **Step 3: Check What's Required vs Optional**

- **Required fields**: Usually don't have default values
- **Optional fields**: Can be skipped if not needed
- **Use Rosetta UI** to see which fields are required

## 🔍 How to Use Rosetta UI Effectively

### **Navigation Steps:**

1. **Go to Type Browser**
2. **Search for your class** (e.g., `InterestRatePayout`)
3. **Click on the class**
4. **Go to "Builder" tab**
5. **See all available setters**
6. **Check "Sample" tab for examples**

### **What to Look For:**

- **Field names** (what you can set)
- **Field types** (what objects you need to create)
- **Required vs optional** indicators
- **Sample values** and examples

## 📚 Using CDM Sample Files

### **Available Samples:**

- **Interest Rate Derivatives**: `ird-ex01-vanilla-swap.xml`
- **Credit Derivatives**: Various credit product examples
- **FX Derivatives**: Currency swap examples
- **Equity Derivatives**: Stock option examples

### **How to Use Samples:**

1. **Find relevant sample** for your product type
2. **Look at XML structure** to understand hierarchy
3. **Map XML elements** to Java builder calls
4. **Use as template** for your own implementation

## 🎯 Practical Example: Building an IRS Step by Step

### **What We Need:**

```
InterestRatePayout (Fixed Leg)
├── PriceQuantity (notional amount)
├── DayCountFraction (30E/360)
├── CalculationPeriodDates (start/end dates)
├── PaymentDates (frequency)
├── RateSpecification (fixed rate)
└── PayerReceiver (who pays/receives)
```

### **Step-by-Step Construction:**

```java
// 1. Build the notional reference
Reference notionalRef = Reference.builder()
    .setScope("DOCUMENT")
    .setReference("notional-1")
    .build();

// 2. Build the price quantity
ResolvablePriceQuantity priceQuantity = ResolvablePriceQuantity.builder()
    .setQuantitySchedule(ReferenceWithMetaNonNegativeQuantitySchedule.builder()
        .setReference(notionalRef)
        .build())
    .build();

// 3. Build the day count fraction
FieldWithMetaDayCountFractionEnum dayCount = FieldWithMetaDayCountFractionEnum.builder()
    .setValue(DayCountFractionEnum._30E_360)
    .build();

// 4. Continue building outward...
```

## 🧩 Memory Map Strategies

### **Strategy 1: Tree Visualization**

Draw the object hierarchy on paper or use a mind map tool.

### **Strategy 2: Required vs Optional Checklist**

- [ ] Required: Notional amount
- [ ] Required: Start date
- [ ] Required: End date
- [ ] Required: Rate specification
- [ ] Optional: Day count fraction (has default)
- [ ] Optional: Business day adjustments

### **Strategy 3: Domain Knowledge**

Think about what a real financial product needs:

- **Money**: Notional amount
- **Time**: Start/end dates, frequency
- **Risk**: Rate specifications
- **Parties**: Who's involved

## 🚀 Pro Tips

### **Tip 1: Use Rosetta UI First**

Always start with Rosetta UI to understand the class structure before coding.

### **Tip 2: Build Incrementally**

Don't try to build everything at once. Build one piece, test it, then add the next.

### **Tip 3: Use Sample Files as Templates**

Copy the structure from sample files and modify for your needs.

### **Tip 4: Leverage IDE Features**

Use autocomplete, hover documentation, and go-to-definition extensively.

### **Tip 5: Validate Early**

Use CDM validation to check if your objects are correctly formed.

## 🔧 Troubleshooting Common Issues

### **Problem: "Cannot resolve method"**

**Solution**: Check if you're calling the right builder method. Use Rosetta UI to see available methods.

### **Problem: "Incompatible types"**

**Solution**: Check the parameter type. You might need to create a different object first.

### **Problem: "Missing required field"**

**Solution**: Use Rosetta UI to see which fields are required and set them.

### **Problem: "Builder not found"**

**Solution**: Make sure you're calling `.builder()` on the right class, not on an instance.

## 📖 Next Steps

1. **Practice with Rosetta UI**: Explore different classes
2. **Study Sample Files**: Understand the XML structure
3. **Build Simple Objects**: Start with basic objects like `Date`, `Reference`
4. **Gradually Add Complexity**: Add more complex objects step by step
5. **Use Validation**: Always validate your objects to ensure correctness

## 🎉 Remember

The CDM builder pattern is complex, but it's designed to be **flexible and comprehensive**. The key is to:

- **Start simple** and build incrementally
- **Use the tools** (Rosetta UI, samples, IDE features)
- **Think in terms of domain concepts** rather than just code
- **Practice regularly** with different product types

Happy building! 🚀
