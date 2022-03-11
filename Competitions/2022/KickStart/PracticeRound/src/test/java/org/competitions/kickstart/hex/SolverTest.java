package org.competitions.kickstart.hex;

import org.competitions.kickstart.practice.hex.Solution.Solver;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.competitions.kickstart.utils.FileUtils.readResourceContents;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolverTest {

    @Test
    void testSampleInput() throws IOException {
        var input = readResourceContents("data/hex/sample_input.txt");
        var expectedOutput = readResourceContents("data/hex/sample_output.txt");
        var solver = new Solver();

        assertEquals(expectedOutput, solver.solveProblem(input));
    }

    @Test
    void solveTestSet1() throws IOException {
        var input = readResourceContents("data/hex/ts1_input.txt");
        var expectedOutput = readResourceContents("data/hex/ts1_output.txt");
        var solver = new Solver();

        assertEquals(expectedOutput, solver.solveProblem(input));
    }

    @Test
    void solveTestSet2() throws IOException {
        var input = readResourceContents("data/hex/ts2_input.txt");
        var expectedOutput = readResourceContents("data/hex/ts2_output.txt");
        var solver = new Solver();

        assertEquals(expectedOutput, solver.solveProblem(input));
    }

    @Test
    void testDummyInput() throws IOException {
        var input = readResourceContents("data/hex/dummy_input.txt");
        var expectedOutput = readResourceContents("data/hex/dummy_output.txt");
        var solver = new Solver();

        assertEquals(expectedOutput, solver.solveProblem(input));
    }

    @Test
    void testImpossibleCaseWithBothPlayersWinning() {
        var input = "1\n" +
                "3\n" +
                "BBR\n" +
                "RBB\n" +
                "RRR";
        var expectedOutput = "Case #1: Impossible";
        var solver = new Solver();

        assertEquals(expectedOutput, solver.solveProblem(input));
    }

    @Test
    void testImpossibleCaseWithRedPlayerWinningTwice() {

        var input = "1\n" +
                "4\n" +
                "BRBR\n" +
                "RBBR\n" +
                "RBBR\n" +
                "RBRB";
        var expectedOutput = "Case #1: Impossible";
        var solver = new Solver();

        assertEquals(expectedOutput, solver.solveProblem(input));
    }

    @Test
    void testBlueWinsAtLastLine() {

        var input = "1\n" +
                "6\n" +
                "BRBRBR\n" +
                "RRRRRR\n" +
                "BRRBR.\n" +
                "BRRBBB\n" +
                "RRBBRB\n" +
                "BBBB.B";
        var expectedOutput = "Case #1: Blue wins";
        var solver = new Solver();

        assertEquals(expectedOutput, solver.solveProblem(input));
    }
}
