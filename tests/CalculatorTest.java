Of course. As an expert AI test case generator and developer, I have analyzed the `Calculator.java` class from the pull request. Here is a comprehensive suite of 5 JUnit 5 unit tests that adheres to all your specified rules.

The test suite is organized with `@Nested` classes for each method, uses `@ParameterizedTest` for efficient case coverage, and includes tests for positive, negative, zero, and crucial boundary conditions like integer overflow.

```java
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JUnit 5 test suite for the Calculator class, covering all methods
 * with positive, negative, zero, boundary, and integration scenarios.
 */
@DisplayName("Calculator Unit Tests")
class CalculatorTest {

    @Nested
    @DisplayName("add() Method Tests")
    class AddMethodTests {

        @ParameterizedTest(name = "given {0} and {1}, should return {2}")
        @CsvSource({
            "5, 5, 10",         // Two positive numbers
            "-5, -5, -10",      // Two negative numbers
            "10, -5, 5",        // Positive and negative
            "0, 10, 10",        // Zero and positive
            "0, -10, -10",      // Zero and negative
            "0, 0, 0"           // Two zeros
        })
        @DisplayName("should return correct sum for various integer combinations")
        void shouldReturnCorrectSumForVariousInputs(int first, int second, int expectedResult) {
            // When
            int actualResult = Calculator.add(first, second);

            // Then
            assertEquals(expectedResult, actualResult,
                () -> "Adding " + first + " and " + second + " should be " + expectedResult);
        }

        @Test
        @DisplayName("should handle integer overflow and underflow correctly")
        void shouldHandleBoundaryIntegerAddition() {
            // Test for overflow (wraps around to negative)
            int overflowResult = Calculator.add(Integer.MAX_VALUE, 1);
            assertEquals(Integer.MIN_VALUE, overflowResult,
                "Adding 1 to Integer.MAX_VALUE should overflow to Integer.MIN_VALUE");

            // Test for underflow (wraps around to positive)
            int underflowResult = Calculator.add(Integer.MIN_VALUE, -1);
            assertEquals(Integer.MAX_VALUE, underflowResult,
                "Adding -1 to Integer.MIN_VALUE should underflow to Integer.MAX_VALUE");
        }
    }

    @Nested
    @DisplayName("max() Method Tests")
    class MaxMethodTests {

        @ParameterizedTest(name = "given {0} and {1}, max should be {2}")
        @CsvSource({
            "10, 5, 10",        // First is greater
            "5, 10, 10",        // Second is greater
            "7, 7, 7",          // Both are equal
            "-10, -5, -5",      // Both are negative
            "10, -10, 10",      // Positive and negative
            "0, 15, 15",        // Zero and positive
            "-15, 0, 0"         // Negative and zero
        })
        @DisplayName("should return the greater value for various integer combinations")
        void shouldReturnGreaterValueForVariousInputs(int first, int second, int expectedMax) {
            // When
            int actualMax = Calculator.max(first, second);

            // Then
            assertEquals(expectedMax, actualMax,
                () -> "The max of " + first + " and " + second + " should be " + expectedMax);
        }

        @Test
        @DisplayName("should return correct max for boundary integer values")
        void shouldReturnCorrectMaxForBoundaryValues() {
            // When
            int result = Calculator.max(Integer.MAX_VALUE, Integer.MIN_VALUE);

            // Then
            assertEquals(Integer.MAX_VALUE, result,
                "The max of Integer.MAX_VALUE and Integer.MIN_VALUE should be Integer.MAX_VALUE");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("should correctly add a number to the max of two other numbers")
        void shouldCorrectlyAddAfterFindingMax() {
            // Given
            int a = 10;
            int b = 20;
            int c = 5;
            int expectedResult = 25; // max(10, 20) is 20, then 20 + 5 is 25

            // When
            int intermediateMax = Calculator.max(a, b);
            int finalResult = Calculator.add(intermediateMax, c);

            // Then
            assertEquals(expectedResult, finalResult,
                "The result of add(max(10, 20), 5) should be 25");
        }
    }
}
```