package org.competitions.kickstart.rounda.challenge_nine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SolverTest {

    @Test
    void test_isDividableByNine() {

        assertTrue(new Solution.Solver().isDividableByNine("45"));
        assertTrue(new Solution.Solver().isDividableByNine("9"));
        assertTrue(new Solution.Solver().isDividableByNine("18"));
        assertTrue(new Solution.Solver().isDividableByNine("351"));
        assertFalse(new Solution.Solver().isDividableByNine("352"));
    }
}
