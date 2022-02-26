package org.competitions.kickstart.sample;

import org.competitions.kickstart.practice.sample.Solution;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.competitions.kickstart.utils.FileUtils.readResourceContents;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolverTest {

    @Test
    void testSampleInput() throws IOException {
        var input = readResourceContents("data/sample/sample_input.txt");
        var expectedOutput = readResourceContents("data/sample/sample_output.txt");
        var solver = new Solution.Solver();

        assertEquals(expectedOutput, solver.solveProblem(input));
    }

}
