Of course! As an expert AI test case generator, I have analyzed the provided code changes and generated a comprehensive suite of 5 JUnit 5 unit tests for the `Calculator` and `Multiplication` classes.

Here is the fully formatted JUnit 5 test class, ready to run:

```java
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the math utility classes Calculator and Multiplication.
 */
@DisplayName("Math Utility Tests")
class MathOperationsTest {

    @Nested
    @DisplayName("Tests for Calculator.add() method")
    class AddTests {

        @ParameterizedTest(name = "[{index}] Adding {0} and {1} should equal {2}")
        @CsvSource({
            "5, 10, 15",      // Positive numbers
            "-5, -10, -15",   // Negative numbers
            "10, -5, 5",      // Mixed positive and negative
            "0, 100, 100",    // Zero with positive
            "-100, 0, -100",  // Zero with negative
            "0, 0, 0"         // Zero with zero
        })
        @DisplayName("should return the correct sum for various integer inputs")
        void shouldReturnCorrectSumForVariousInputs(int first, int second, int expectedSum) {
            // Act
            int actualSum = Calculator.add(first, second);

            // Assert
            assertEquals(expectedSum, actualSum,
                () -> "Adding " + first + " and " + second + " did not produce the expected sum.");
        }

        @Test
        @DisplayName("should handle integer overflow by wrapping around")
        void shouldHandleIntegerOverflow() {
            // Arrange
            int a = Integer.MAX_VALUE;
            int b = 1;
            int expected = Integer.MIN_VALUE; // Expected behavior for integer overflow

            // Act
            int result = Calculator.add(a, b);

            // Assert
            assertEquals(expected, result, "Adding 1 to Integer.MAX_VALUE should result in Integer.MIN_VALUE due to overflow.");
        }
    }

    @Nested
    @DisplayName("Tests for Calculator.max() method")
    class MaxTests {

        @ParameterizedTest(name = "[{index}] Max of {0} and {1} should be {2}")
        @CsvSource({
            "10, 5, 10",         // First number is greater
            "5, 10, 10",         // Second number is greater
            "-10, -5, -5",       // Both negative, second is 'greater'
            "-5, -10, -5",       // Both negative, first is 'greater'
            "10, -5, 10",        // Mixed signs
            "7, 7, 7",           // Equal numbers
            "0, -100, 0",        // With zero
        })
        @DisplayName("should return the larger of two numbers for various inputs")
        void shouldReturnLargerOfTwoNumbers(int a, int b, int expectedMax) {
            // Act
            int actualMax = Calculator.max(a, b);

            // Assert
            assertEquals(expectedMax, actualMax,
                () -> "Max of " + a + " and " + b + " was not the expected value.");
        }
    }

    @Nested
    @DisplayName("Tests for Multiplication.multiplyThreeNumbers() method")
    class MultiplyThreeNumbersTests {

        @ParameterizedTest(name = "[{index}] {0} * {1} * {2} = {3}")
        @CsvSource({
            "2, 3, 4, 24",       // All positive
            "-2, -3, 4, 24",     // Two negatives (even number) result in positive
            "-2, 3, 4, -24",     // One negative (odd number) results in negative
            "-2, -3, -4, -24",   // Three negatives (odd number) result in negative
            "10, 20, 0, 0",      // Multiplication by zero
            "1, 1, 1, 1",        // Identity with ones
            "5, 1, -10, -50"     // Identity with one and a negative
        })
        @DisplayName("should return correct product for various combinations of numbers")
        void shouldReturnCorrectProductForVariousInputs(int a, int b, int c, int expectedProduct) {
            // Act
            int actualProduct = Multiplication.multiplyThreeNumbers(a, b, c);

            // Assert
            assertEquals(expectedProduct, actualProduct,
                () -> "Product of " + a + ", " + b + ", and " + c + " was not correct.");
        }
    }

    @Nested
    @DisplayName("Integration-style Tests")
    class IntegrationTests {

        @Test
        @DisplayName("should correctly find the max between a sum and another number")
        void shouldCorrectlyFindMaxOfSumAndNumber() {
            // Arrange: Define numbers for addition and comparison
            int addend1 = 50;
            int addend2 = -20;
            int comparisonValue = 25;
            int expectedMax = 30; // 50 + (-20) = 30, which is > 25

            // Act: Combine methods from two different classes
            int sumResult = Calculator.add(addend1, addend2);
            int finalResult = Calculator.max(sumResult, comparisonValue);

            // Assert
            assertEquals(expectedMax, finalResult, "The max of the sum and the comparison value should be correct.");
        }
    }
}
```