package org.competitions.kickstart.rounda.challenge_nine;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.competitions.kickstart.utils.FileUtils.readResourceContents;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {

    @Test
    void testSampleInput() throws IOException {
        var input = readResourceContents("data/challenge-nine/sample_input.txt");
        var expectedOutput = readResourceContents("data/challenge-nine/sample_output.txt");
        var solver = new Solution.Solver();

        assertEquals(expectedOutput, solver.solveProblem(input));
    }

    @Test
    void testSample2Input() throws IOException {
        var input = readResourceContents("data/challenge-nine/sample2_input.txt");
        var expectedOutput = readResourceContents("data/challenge-nine/sample2_output.txt");
        var solver = new Solution.Solver();

        assertEquals(expectedOutput, solver.solveProblem(input));
    }

}