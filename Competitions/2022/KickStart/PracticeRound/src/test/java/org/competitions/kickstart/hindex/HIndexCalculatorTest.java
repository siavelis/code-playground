package org.competitions.kickstart.hindex;

import org.competitions.kickstart.practice.hindex.Solution;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class HIndexCalculatorTest {

    private final Solution.HIndexCalculator calculator = new Solution.HIndexCalculator();

    @Test
    void testWithSameBigNumber() {
        var size = 10_000;
        var citationsPerPaper = IntStream.range(1, size).map(x -> size).toArray();
        var expectedResult = IntStream.range(1, size).toArray();

        var result = calculator.calculateHIndices(citationsPerPaper, citationsPerPaper.length);

        assertArrayEquals(expectedResult, result.stream().mapToInt(x -> x).toArray());
    }

    @Test
    void testWithMonotonicallyIncreasingNumber() {
        var size = 30_000;
        var citationsPerPaper = IntStream.range(1, size).toArray();
        var expectedResult = IntStream.range(1, size)
                .map(x -> (int) (x + 1 + (x % 2 == 0 ? 0 : 0.5)) / 2)
                .toArray();

        var result = calculator.calculateHIndices(citationsPerPaper, citationsPerPaper.length);

        assertArrayEquals(expectedResult, result.stream().mapToInt(x -> x).toArray());
    }
}
