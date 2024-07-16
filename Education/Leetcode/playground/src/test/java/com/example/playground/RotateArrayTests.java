package com.example.playground;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Problem formulation in: <a href="https://leetcode.com/problems/rotate-array/">...</a>
 */
public class RotateArrayTests {

    @ParameterizedTest
    @MethodSource("source")
    void givenInputWhenRotatingArrayThenExpectMatchedResults(int[] inputNums, int k, int[] outputNums) {
        rotate(inputNums, k);
        assertArrayEquals(outputNums, inputNums);
    }

    private static Stream<Arguments> source() {
        return Stream.of(
//                Arguments.of(new int[]{1, 2, 3, 4, 5, 6, 7}, 3, new int[]{5, 6, 7, 1, 2, 3, 4}),
//                Arguments.of(new int[]{1, 2, 3, 4, 5, 6, 7}, 2, new int[]{6, 7, 1, 2, 3, 4, 5}),
                Arguments.of(new int[]{1, 2, 3, 4}, 1, new int[]{4, 1, 2, 3})
//                ,
//                Arguments.of(new int[]{1, 2, 3, 4}, 2, new int[]{3, 4, 1, 2})
//                ,
//                Arguments.of(new int[]{-1, -100, 3, 99}, 2, new int[]{3, 99, -1, -100})
        );
    }

    private void swapElements(int[] nums, int left, int right) {
        int leftNumber = nums[left];
        nums[left] = nums[right];
        nums[right] = leftNumber;
    }

    public void rotate(int[] nums, int k) {

        for (int i = 0; i < k; i++) {
            swapElements(nums, i, i + k);
            System.out.println(Arrays.toString(nums));

        }
//
//        k = k % nums.length;
//        int count = 0;
//        for (int i = k; i < nums.length; i++) {
//            swapElements(nums, count++, i);
//            System.out.println(Arrays.toString(nums));
//
//        }
//        int currentNumber = nums[0];
//        for (int i = 0; i < nums.length; i++) {
//            int newIndex = (i + k) % nums.length;
//            int temporaryNum = nums[newIndex];
//
//            nums[newIndex] = currentNumber;
//            currentNumber = temporaryNum;
//            System.out.println(Arrays.toString(nums));
//
//        }


        // 1, 2, 3, 4 -> 2
        // 3, 4, 1, 2
//        int length = nums.length;
//        int negativeBase = nums.length - k;
//        for (int i = 0; i < nums.length ; i++) {
//            int newIndex = (negativeBase - i) % length;
//
//            int temporaryNum = nums[i];
//
//            nums[i] = nums[newIndex];
//            nums[newIndex] = temporaryNum;
//            System.out.println("length = " + length + " k = " + k + " i = " + i + " newIndex = " + newIndex);
//            System.out.println(Arrays.toString(nums));
//        }
//
//        int currentIndex = 0;
//        int currentNumber = nums[currentIndex];
//        for (int count = 0; count < nums.length; count++) {
//            int newIndex = (currentIndex + k) % nums.length;
//            int temporaryNum = nums[newIndex];
//
//            nums[newIndex] = currentNumber;
//            System.out.println("currentNumber = " + currentNumber + " k = " + k + " currentIndex = " + currentIndex + " newIndex = " + newIndex);
//
//            currentIndex = newIndex;
//            currentNumber = temporaryNum;
//
//
//        }

    }


}
