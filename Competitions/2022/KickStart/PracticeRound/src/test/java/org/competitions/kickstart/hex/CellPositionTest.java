package org.competitions.kickstart.hex;

import org.competitions.kickstart.practice.hex.Solution;
import org.competitions.kickstart.practice.hex.Solution.CellPosition;
import org.competitions.kickstart.practice.hex.Solution.CellMove;
import org.competitions.kickstart.practice.hex.Solution.MoveDirectionEnum;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class CellPositionTest {

    @Test
    void testAllowedDirectionsFromTopLeftCorner() {

        var boardSize = 3;
        var cellPosition = new CellPosition(0, 0);
        var allowedDirections = List.of(
                MoveDirectionEnum.DOWNRIGHT,
                MoveDirectionEnum.RIGHT
        );
        var forbiddenDirections = Arrays.stream(MoveDirectionEnum.values())
                .filter(x -> !allowedDirections.contains(x))
                .collect(Collectors.toList());

        for (var direction : allowedDirections) {

            var cellMove = new CellMove(boardSize, boardSize, direction);
            assertTrue(
                    cellPosition.move(cellMove).isPresent(),
                    () -> "Direction: " + direction
            );
        }

        for (var direction : forbiddenDirections) {
            var cellMove = new CellMove(boardSize, boardSize, direction);
            assertTrue(
                    cellPosition.move(cellMove).isEmpty(),
                    () -> "Direction: " + direction
            );
        }
    }

    @Test
    void testHexagonalPositioning() {
        var centerCell = new CellPosition(1, 1);

        assertEquals(new CellPosition(0, 1), getNewCell(centerCell, MoveDirectionEnum.UPLEFT));
        assertEquals(new CellPosition(0, 2), getNewCell(centerCell, MoveDirectionEnum.UPRIGHT));

        assertEquals(new CellPosition(1, 0), getNewCell(centerCell, MoveDirectionEnum.LEFT));
        assertEquals(new CellPosition(1, 2), getNewCell(centerCell, MoveDirectionEnum.RIGHT));

        assertEquals(new CellPosition(2, 0), getNewCell(centerCell, MoveDirectionEnum.DOWNLEFT));
        assertEquals(new CellPosition(2, 1), getNewCell(centerCell, MoveDirectionEnum.DOWNRIGHT));

    }

    private CellPosition getNewCell(CellPosition cellPosition, MoveDirectionEnum directionEnum) {
        return cellPosition.move(
                new CellMove(3, 3, directionEnum)
        ).orElse(null);
    }
}
