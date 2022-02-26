package org.competitions.kickstart.practice.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.String.format;

public class Solution {

    public static void main(String[] args) {
        var sb = new StringBuilder();

        try (Scanner in = new Scanner(System.in)) {
            while (in.hasNext()) {
                sb
                        .append(in.nextLine())
                        .append(System.lineSeparator());
            }
        }

        System.out.println(getSolution(sb.toString()));
    }

    public static String getSolution(String input) {
        return new Solver().solveProblem(input);
    }

    public static class Solver {
        private int numberOfTestCases;
        private int numberOfKids;

        private int currentLine;
        private String[] lines;
        private List<Integer> results;

        public String solveProblem(String input) {
            currentLine = 0;
            lines = input.lines().toArray(String[]::new);

            setup();

            while (hasRemainingLines()) {
                setupTestCase();

                var sum = Arrays.stream(readLineAndReturnTokens())
                        .sum();

                results.add(sum % numberOfKids);
            }

            return buildOutput();
        }

        private void setup() {
            numberOfTestCases = Integer.parseInt(readLine());

            results = new ArrayList<>(numberOfTestCases);
        }

        private void setupTestCase() {

            var secondLineTokens = readLineAndReturnTokens();
            int numberOfCandyBags = secondLineTokens[0];
            numberOfKids = secondLineTokens[1];
        }

        private String readLine() {
            return lines[currentLine++];
        }

        private int[] readLineAndReturnTokens() {
            return Arrays.stream(readLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }

        private boolean hasRemainingLines() {
            return numberOfTestCases > results.size();
        }

        private String buildOutput() {
            var sb = new StringBuilder();
            for (int i = 0; i < results.size(); i++) {
                sb.append(format("Case #%d: %d", i + 1, results.get(i)));
                if (i != results.size() - 1) {
                    sb.append(System.lineSeparator());
                }
            }
            return sb.toString();
        }
    }
}
