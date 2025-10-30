```java
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Unit Tests for New Math Utilities")
class NewMathUtilsTest {

    @Nested
    @DisplayName("Tests for Calculator Class")
    class CalculatorTests {

        @ParameterizedTest(name = "add({0}, {1}) should return {2}")
        @CsvSource({
            "5, 10, 15",        // Two positive numbers
            "-5, -10, -15",     // Two negative numbers
            "10, -5, 5",        // Positive and negative
            "0, 0, 0",          // Both zero
            "-10, 0, -10",      // One argument is zero
            "12345, 54321, 66666"
        })
        @DisplayName("should add various integer combinations correctly")
        void shouldAddVariousIntegerCombinationsCorrectly(int firstInput, int secondInput, int expectedSum) {
            int actualSum = Calculator.add(firstInput, secondInput);
            assertEquals(expectedSum, actualSum,
                () -> "Adding " + firstInput + " and " + secondInput + " should result in " + expectedSum);
        }

        @Test
        @DisplayName("should handle integer boundary values for addition")
        void shouldHandleBoundaryValuesForAddition() {
            // Test for overflow, which wraps around in Java for int
            assertEquals(Integer.MIN_VALUE, Calculator.add(Integer.MAX_VALUE, 1),
                "Adding 1 to Integer.MAX_VALUE should overflow to Integer.MIN_VALUE");

            // Test for underflow
            assertEquals(Integer.MAX_VALUE, Calculator.add(Integer.MIN_VALUE, -1),
                "Subtracting 1 from Integer.MIN_VALUE should underflow to Integer.MAX_VALUE");

            // Test adding MIN and MAX values
            assertEquals(-1, Calculator.add(Integer.MAX_VALUE, Integer.MIN_VALUE),
                "Adding Integer.MAX_VALUE and Integer.MIN_VALUE should result in -1");
        }

        @ParameterizedTest(name = "max({0}, {1}) should return {2}")
        @CsvSource({
            "100, 1, 100",        // First argument is greater
            "1, 100, 100",        // Second argument is greater
            "50, 50, 50",         // Both arguments are equal
            "-10, -5, -5",        // Both arguments are negative
            "0, -100, 0",         // One argument is zero
            "Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE" // Boundary values
        })
        @DisplayName("should return the greater of two integers for various cases")
        void shouldReturnGreaterOfTwoIntegers(int firstInput, int secondInput, int expectedMax) {
            int actualMax = Calculator.max(firstInput, secondInput);
            assertEquals(expectedMax, actualMax,
                () -> "The maximum of " + firstInput + " and " + secondInput + " should be " + expectedMax);
        }
    }

    @Nested
    @DisplayName("Tests for Multiplication Class")
    class MultiplicationTests {

        @ParameterizedTest(name = "multiply({0}, {1}, {2}) should return {3}")
        @CsvSource({
            "2, 3, 4, 24",      // All positive numbers
            "-5, 10, 2, -100",  // One negative number
            "-5, -10, 2, 100",  // Two negative numbers (even count)
            "-2, -3, -4, -24",  // Three negative numbers (odd count)
            "1, 1, 1, 1",       // Identity multiplication
            "-1, -1, -1, -1"    // Multiplying negative ones
        })
        @DisplayName("should multiply three integers with various signs correctly")
        void shouldMultiplyThreeIntegersCorrectly(int a, int b, int c, int expectedProduct) {
            int actualProduct = Multiplication.multiplyThreeNumbers(a, b, c);
            assertEquals(expectedProduct, actualProduct,
                () -> "The product of " + a + ", " + b + ", and " + c + " should be " + expectedProduct);
        }

        @Test
        @DisplayName("should return zero when any input is zero (Zero-Product Property)")
        void shouldReturnZeroWhenAnyInputIsZero() {
            assertEquals(0, Multiplication.multiplyThreeNumbers(123, 0, 456),
                "Multiplication should be zero if the second argument is zero");
            assertEquals(0, Multiplication.multiplyThreeNumbers(0, 123, 456),
                "Multiplication should be zero if the first argument is zero");
            assertEquals(0, Multiplication.multiplyThreeNumbers(123, 456, 0),
                "Multiplication should be zero if the third argument is zero");
            assertEquals(0, Multiplication.multiplyThreeNumbers(0, 0, 0),
                "Multiplication of all zeros should be zero");
        }
    }
}
```