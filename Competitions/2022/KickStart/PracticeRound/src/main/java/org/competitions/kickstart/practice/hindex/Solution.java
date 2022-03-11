package org.competitions.kickstart.practice.hindex;

import java.util.*;
import java.util.stream.Collectors;

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

        private final HIndexCalculator hIndexCalculator = new HIndexCalculator();
        private int currentLine;
        private String[] lines;
        private int numberOfTestCases;
        private List<List<Integer>> hIndicesPerTestCase;
        private int numberOfCitations;
        private int[] citationsPerPaper;

        public String solveProblem(String input) {
            currentLine = 0;
            lines = input.lines().toArray(String[]::new);

            setup();

            while (hasRemainingLines()) {
                setupTestCase();

                hIndicesPerTestCase.add(hIndexCalculator.calculateHIndices(citationsPerPaper, numberOfCitations));
            }

            return buildOutput();
        }

        private void setup() {

            numberOfTestCases = Integer.parseInt(readLine());
            hIndicesPerTestCase = new ArrayList<>(numberOfTestCases);
        }

        private void setupTestCase() {

            numberOfCitations = readLineAndReturnTokens()[0];
            citationsPerPaper = readLineAndReturnTokens();
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
            return numberOfTestCases > hIndicesPerTestCase.size();
        }

        private String buildOutput() {
            var sb = new StringBuilder();
            for (int i = 0; i < hIndicesPerTestCase.size(); i++) {
                sb.append(
                        format("Case #%d: %s",
                                i + 1,
                                hIndicesPerTestCase.get(i)
                                        .stream()
                                        .map(String::valueOf)
                                        .collect(Collectors.joining(" "))
                        ));
                if (i != hIndicesPerTestCase.size() - 1) {
                    sb.append(System.lineSeparator());
                }
            }
            return sb.toString();
        }
    }

    public static class HIndexCalculator {

        private TreeMap<Integer, Integer> citationsCount;

        public List<Integer> calculateHIndices(int[] citationsPerPaper, int numberOfPapers) {
            // sort ascending
            citationsCount = new TreeMap<>();

            var result = new ArrayList<Integer>(numberOfPapers);
            var previousHIndex = 0;
            for (int i = 0; i < numberOfPapers; i++) {

                result.add(calculateHIndex(citationsPerPaper[i], previousHIndex, numberOfPapers));

                previousHIndex = result.get(i);
            }
            return result;
        }

        private boolean hIndexExists(int hindex) {
            var papersSum = 0;
            var entrySet = citationsCount.tailMap(hindex)
                    .entrySet();
            for (var pair : entrySet) {
                papersSum += pair.getValue();
                if (papersSum >= hindex) {
                    return true;
                }
            }
            return false;
        }

        private int calculateHIndex(int citations, int previousHIndex, int numberOfPapers) {
            citationsCount.compute(citations, (x, y) -> y != null ? y + 1 : 1);

            if (numberOfPapers == 1) {
                return 1;
            }

            int hIndex = previousHIndex;
            for (int i = previousHIndex + 1; i <= numberOfPapers; i++) {

                if (hIndexExists(i)) {
                    hIndex = i;
                    continue;
                }

                break;
            }

            if (hIndex != previousHIndex) {
                // remove keys under h-index
                var keysToRemove = new ArrayList<>(citationsCount.headMap(1).keySet());
                keysToRemove.forEach(key -> citationsCount.remove(key));
            }

            return hIndex;
        }
    }

}
