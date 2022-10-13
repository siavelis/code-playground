package org.competitions.kickstart.rounda.challenge_nine;

import java.util.ArrayList;
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
        private int currentLine;
        private String[] lines;
        private List<String> results;

        public String solveProblem(String input) {
            currentLine = 0;
            lines = input.lines().toArray(String[]::new);

            setup();

            while (hasRemainingLines()) {

                var line = readLine();

                results.add(getChallengeNine(line));
            }

            return buildOutput();
        }

        private void setup() {
            numberOfTestCases = Integer.parseInt(readLine());

            results = new ArrayList<>(numberOfTestCases);
        }

        private String readLine() {
            return lines[currentLine++];
        }

        private String getChallengeNine(String input) {

            if (isDividableByNine(input)) {
                return input;
            }

            var currentDigitsSum = getDigitsSum(input);
            var digitToInsert = 9 - currentDigitsSum;
            var currentMin = Long.MAX_VALUE;

            for (int i = 0; i <= input.length(); i++) {

                var currentString = "";
                if (i > 0) {

                    currentString = input.substring(0, i);
                }

                currentString += digitToInsert + input.substring(i);

                var currentNumber = Long.parseLong(currentString);
                if (currentNumber < currentMin) {
                    currentMin = currentNumber;
                }

            }

            return String.valueOf(currentMin);
        }


        public boolean isDividableByNine(String number) {
            var sum = getDigitsSum(number);
            if (sum > 9) {
                return isDividableByNine(String.valueOf(sum));
            }
            return sum == 9;
        }

        private int getDigitsSum(String number) {
            return number.chars().map(c -> c - '0').sum();
        }

        private boolean hasRemainingLines() {
            return numberOfTestCases > results.size();
        }

        private String buildOutput() {
            var sb = new StringBuilder();
            for (int i = 0; i < results.size(); i++) {
                sb.append(format("Case #%d: %s", i + 1, results.get(i)));
                if (i != results.size() - 1) {
                    sb.append(System.lineSeparator());
                }
            }
            return sb.toString();
        }
    }
}
