package com.example.playground;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Problem formulation in: <a href="https://leetcode.com/problems/remove-duplicates-from-sorted-array/?envType=study-plan-v2&envId=top-interview-150">...</a>
 */
public class RemoveDuplicatesTests {


    @ParameterizedTest
    @MethodSource("source")
    void givenInputWhenRemovingDuplicatesExpectMatchedResults(int[] inputNums, int[] outputNums) {
        var result = removeDuplicates(inputNums);
        assertEquals(outputNums.length, result);
        for (int i = 0; i < outputNums.length; i++) {
            assertEquals(outputNums[i], inputNums[i]);
        }
    }

    private static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of(new int[]{1, 2}, new int[]{1, 2}),
                Arguments.of(new int[]{1, 1, 2, 2}, new int[]{1, 2}),
                Arguments.of(new int[]{1, 1, 1, 2, 2}, new int[]{1, 2}),
                Arguments.of(new int[]{1, 1, 1, 2, 2, 3}, new int[]{1, 2, 3}),
                Arguments.of(new int[]{0, 0, 1, 1, 1, 1, 2, 3, 3}, new int[]{0, 1, 2, 3})
        );
    }

    public int removeDuplicates(int[] nums) {

        int maxNumber = -101;
        int uniqueIndex = 0;
        int duplicateIndex = 0;
        while (duplicateIndex != nums.length
                && uniqueIndex != nums.length) {

            if (nums[duplicateIndex] > maxNumber) {
                maxNumber = nums[duplicateIndex];
                nums[uniqueIndex] = maxNumber;
                uniqueIndex++;
            }
            duplicateIndex++;
        }
        return uniqueIndex;
    }

}
