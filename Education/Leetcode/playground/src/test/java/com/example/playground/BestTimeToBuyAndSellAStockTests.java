package com.example.playground;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Problem formulation in: <a href="https://leetcode.com/problems/best-time-to-buy-and-sell-stock/">...</a>
 */
public class BestTimeToBuyAndSellAStockTests {


    @ParameterizedTest
    @MethodSource("source")
    void givenInputWhenMaxProfitTheExpectMatchedResult(int[] inputNums, int expectedResult) {
        var result = maxProfit(inputNums);
        assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> source() {
        return Stream.of(
//                Arguments.of(new int[]{7, 1, 5, 3, 6, 4}, 5),
                Arguments.of(new int[]{2, 4, 1}, 2)
        );
    }

    public int maxProfit(int[] prices) {
        int minimumIndex = indexOfMinimum(prices);
        int maximumIndex = indexOfMaximum(prices, minimumIndex);
        if (minimumIndex == maximumIndex) {
            return 0;
        }
        int profit = prices[maximumIndex] - prices[minimumIndex];
        return profit < 0 ? 0 : profit;
    }

    private int indexOfMinimum(int[] prices) {
        int minimumIndex = 0;
        int min = prices[minimumIndex];
        for (int i = 0; i < prices.length; i++) {
            if (min > prices[i]) {
                min = prices[i];
                minimumIndex = i;
            }
        }
        return minimumIndex;
    }

    private int indexOfMaximum(int[] prices, int startIndex) {
        int maximumIndex = startIndex;
        int max = prices[startIndex];
        for (int i = startIndex; i < prices.length; i++) {
            if (max < prices[i]) {
                max = prices[i];
                maximumIndex = i;
            }
        }
        return maximumIndex;
    }

}
