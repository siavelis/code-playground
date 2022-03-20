package org.competitions.kickstart.rounda.speedtyping;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.competitions.kickstart.utils.FileUtils.readResourceContents;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolverTest {

    @Test
    void testSampleInput() throws IOException {
        var input = readResourceContents("data/speed-typing/sample_input.txt");
        var expectedOutput = readResourceContents("data/speed-typing/sample_output.txt");
        var solver = new Solution.Solver();

        assertEquals(expectedOutput, solver.solveProblem(input));
    }

    @Test
    void testSecondSampleInput() throws IOException {
        var input = readResourceContents("data/speed-typing/sample2_input.txt");
        var expectedOutput = readResourceContents("data/speed-typing/sample2_output.txt");
        var solver = new Solution.Solver();

        assertEquals(expectedOutput, solver.solveProblem(input));
    }

}
