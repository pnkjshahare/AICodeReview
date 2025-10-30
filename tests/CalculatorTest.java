Of course! As an expert AI test case generator and developer, I have analyzed the code changes in the pull request. The changes introduce two new utility classes, `Calculator` and `Multiplication`.

Here is a comprehensive suite of 5 JUnit 5 unit tests for the newly added Java classes, following all the specified rules.

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
@DisplayName("Math Operations Tests")
class MathOperationsTest {

    @Nested
    @DisplayName("Tests for Calculator.add() method")
    class AddTests {

        @ParameterizedTest(name = "[{index}] Adding {0} and {1} should equal {2}")
        @CsvSource({
            "5, 10, 15",      // Positive numbers
            "-5, -10, -15",   // Negative numbers
            "10, -5, 5",      // Mixed positive and negative
            "0, 100, 100",    // Zero with a positive number
            "-100, 0, -100",  // Zero with a negative number
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
        void shouldHandleIntegerOverflowWhenAdding() {
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
            "10, 5, 10",                  // First number is greater
            "5, 10, 10",                  // Second number is greater
            "-10, -5, -5",                // Both negative, second is 'greater'
            "-5, -10, -5",                // Both negative, first is 'greater'
            "10, -5, 10",                 // Mixed signs
            "7, 7, 7",                    // Equal numbers
            "0, -100, 0",                 // With zero
            "2147483647, -2147483648, 2147483647" // Boundary values (Integer.MAX_VALUE, Integer.MIN_VALUE)
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
        @DisplayName("should correctly find the max between a sum and a product")
        void shouldCorrectlyFindMaxOfSumAndProduct() {
            // Arrange: Define numbers for addition, multiplication, and comparison
            int addend1 = 50;
            int addend2 = -20;
            int factor1 = 5;
            int factor2 = 3;
            int factor3 = 2;
            int expectedMax = 30; // add(50, -20) = 30; multiply(5,3,2) = 30. max(30, 30) = 30.

            // Act: Combine methods from both Calculator and Multiplication classes
            int sumResult = Calculator.add(addend1, addend2);
            int productResult = Multiplication.multiplyThreeNumbers(factor1, factor2, factor3);
            int finalResult = Calculator.max(sumResult, productResult);

            // Assert
            assertEquals(expectedMax, finalResult, "The max of the sum and the product should be correct.");
        }
    }
}
```