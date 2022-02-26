package org.competitions.kickstart.practice.centauri;

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
        private static final char[] ALICE_VOWELS = new char[]{'A', 'E', 'I', 'O', 'U', 'a', 'e', 'i', 'o', 'u'};
        private static final char[] NOBODY_VOWELS = new char[]{'Y', 'y'};

        private int numberOfTestCases;
        private int currentLine;
        private String[] lines;
        private List<String> rulers;

        public String solveProblem(String input) {
            currentLine = 0;
            lines = input.lines().toArray(String[]::new);

            setup();

            while (hasRemainingLines()) {

                var line = readLine();
                var lastChar = line.charAt(line.length() - 1);

                rulers.add(format("%s is ruled by %s.", line, getRuler(lastChar)));
            }

            return buildOutput();
        }

        private void setup() {
            numberOfTestCases = Integer.parseInt(readLine());

            rulers = new ArrayList<>(numberOfTestCases);
        }

        private String readLine() {
            return lines[currentLine++];
        }

        private String getRuler(char lastChar) {
            if (indexOf(NOBODY_VOWELS, lastChar) != -1) {
                return "nobody";
            } else if (indexOf(ALICE_VOWELS, lastChar) != -1) {
                return "Alice";
            } else {
                return "Bob";
            }
        }

        private int indexOf(char[] chars, char character) {
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == character) {
                    return i;
                }
            }
            return -1;
        }

        private boolean hasRemainingLines() {
            return numberOfTestCases > rulers.size();
        }

        private String buildOutput() {
            var sb = new StringBuilder();
            for (int i = 0; i < rulers.size(); i++) {
                sb.append(format("Case #%d: %s", i + 1, rulers.get(i)));
                if (i != rulers.size() - 1) {
                    sb.append(System.lineSeparator());
                }
            }
            return sb.toString();
        }
    }
}
