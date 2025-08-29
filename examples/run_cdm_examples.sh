#!/bin/bash

# 🎯 CDM Examples Runner Script
# Common Domain Model (CDM) 5.26.2

echo "🎯 CDM Examples Runner"
echo "======================"
echo ""

# Check if we're in the right directory
if [ ! -f "pom.xml" ]; then
    echo "❌ Error: pom.xml not found. Please run this script from the examples directory."
    echo "   cd examples"
    echo "   ./run_cdm_examples.sh"
    exit 1
fi

# Function to run an example
run_example() {
    local class_name=$1
    local description=$2
    
    echo "🚀 Running: $description"
    echo "   Class: $class_name"
    echo "   Command: mvn exec:java -Dexec.mainClass=\"$class_name\""
    echo ""
    
    mvn exec:java -Dexec.mainClass="$class_name"
    
    echo ""
    echo "✅ Completed: $description"
    echo "========================================"
    echo ""
}

# Main menu
while true; do
    echo "📋 Available CDM Examples:"
    echo "1. Simple Interest Rate Swap (IRS)"
    echo "2. Enum Serialization"
    echo "3. Floating Rate Calculation"
    echo "4. Fixed Rate Calculation"
    echo "5. Run All Examples"
    echo "6. Exit"
    echo ""
    read -p "Select an option (1-6): " choice
    
    case $choice in
        1)
            run_example "com.regnosys.cdm.example.SimpleIRSExample" "Simple Interest Rate Swap"
            ;;
        2)
            run_example "com.regnosys.cdm.example.EnumSerialisation" "Enum Serialization"
            ;;
        3)
            run_example "com.regnosys.cdm.example.functions.FloatingRateCalculationWithFunction" "Floating Rate Calculation"
            ;;
        4)
            run_example "com.regnosys.cdm.example.functions.FixedRateCalculationWithFunction" "Fixed Rate Calculation"
            ;;
        5)
            echo "🎯 Running All CDM Examples..."
            echo "========================================"
            echo ""
            
            run_example "com.regnosys.cdm.example.SimpleIRSExample" "Simple Interest Rate Swap"
            run_example "com.regnosys.cdm.example.EnumSerialisation" "Enum Serialization"
            run_example "com.regnosys.cdm.example.functions.FloatingRateCalculationWithFunction" "Floating Rate Calculation"
            run_example "com.regnosys.cdm.example.functions.FixedRateCalculationWithFunction" "Fixed Rate Calculation"
            
            echo "🎉 All examples completed successfully!"
            ;;
        6)
            echo "👋 Goodbye! CDM examples completed."
            exit 0
            ;;
        *)
            echo "❌ Invalid option. Please select 1-6."
            echo ""
            ;;
    esac
    
    if [ $choice -ne 5 ] && [ $choice -ne 6 ]; then
        read -p "Press Enter to continue..."
    fi
done
