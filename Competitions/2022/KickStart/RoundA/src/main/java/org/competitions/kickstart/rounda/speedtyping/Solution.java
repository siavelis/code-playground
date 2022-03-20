package org.competitions.kickstart.rounda.speedtyping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        private static final char[] ALICE_VOWELS = new char[]{'A', 'E', 'I', 'O', 'U', 'a', 'e', 'i', 'o', 'u'};
        private static final char[] NOBODY_VOWELS = new char[]{'Y', 'y'};

        private int numberOfTestCases;
        private int currentLine;
        private String[] lines;
        private List<String> results;

        public String solveProblem(String input) {
            currentLine = 0;
            lines = input.lines().toArray(String[]::new);

            setup();

            while (hasRemainingLines()) {

                var expectedString = readLine();
                var typedString = readLine();

                results.add(getTypingCorrectionResult(expectedString, typedString));
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

        private String getTypingCorrectionResult(String expected, String typed) {

            if (typed.length() < expected.length()) {
                return getImpossibleValue();
            }

            int cursorExpected = 0;
            int cursorTyped = 0;
            int removedChars = 0;


            while (cursorExpected < expected.length()
                    && cursorTyped < typed.length()) {

                // check current characters
                if (Objects.equals(
                        expected.charAt(cursorExpected),
                        typed.charAt(cursorTyped)
                )) {
                    // move both cursors
                    cursorTyped++;
                    cursorExpected++;
                } else {
                    // remove character and move cursor
                    removedChars++;
                    cursorTyped++;
                }

            }

            // did consumed expected value?
            if (cursorExpected == expected.length()) {

                // did consumed typed value?
                if (cursorTyped < typed.length()) {
                    removedChars += typed.length() - cursorTyped;
                }

                return String.valueOf(removedChars);
            }

            return "IMPOSSIBLE";
        }

        private String getImpossibleValue() {
            return "IMPOSSIBLE";

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
