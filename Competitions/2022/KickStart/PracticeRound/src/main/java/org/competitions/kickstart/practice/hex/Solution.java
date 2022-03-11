package org.competitions.kickstart.practice.hex;

import java.util.*;
import java.util.function.Function;
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

        int currentLine;
        String[] lines;
        int numberOfTestCases;

        List<WinnerType> winners;
        Board board;
        int boardSize;

        public String solveProblem(String input) {
            currentLine = 0;
            lines = input.lines().toArray(String[]::new);

            setup();

            while (hasRemainingLines()) {
                setupTestCase();

                System.out.println("Test case #: " + (winners.size() + 1));

                for (int row = 0; row < boardSize; row++) {
                    var line = readLineAndReturnTokens();
                    System.out.println(Arrays.toString(line));
                    for (int column = 0; column < boardSize; column++) {
                        var value = line[column];
                        CellColor cellColor;
                        if (Objects.equals(".", value)) {
                            cellColor = CellColor.NO_COLOR;
                        } else if (Objects.equals("R", value)) {
                            cellColor = CellColor.RED;
                        } else {
                            cellColor = CellColor.BLUE;
                        }

                        board.addCell(cellColor, new CellPosition(row, column));
                    }
                }

                winners.add(board.getWinnerType());
                System.out.println("Winner: " + winners.get(winners.size() - 1));
            }

            return buildOutput();
        }

        void setup() {
            numberOfTestCases = Integer.parseInt(readLine());
            winners = new ArrayList<>(numberOfTestCases);
        }

        void setupTestCase() {
            boardSize = Integer.parseInt(readLine());

            board = new Board(boardSize);
        }

        String readLine() {
            return lines[currentLine++];
        }

        String[] readLineAndReturnTokens() {
            return readLine().split("");
        }

        boolean hasRemainingLines() {
            return numberOfTestCases > winners.size();
        }

        String buildOutput() {
            var sb = new StringBuilder();
            for (int i = 0; i < winners.size(); i++) {
                sb.append(format("Case #%d: %s", i + 1, winners.get(i).value));
                if (i != winners.size() - 1) {
                    sb.append(System.lineSeparator());
                }
            }
            return sb.toString();
        }
    }

    public static class Board {

        final int boardSize;
        final Set<CellPosition> positions;
        final CellGroup blueCells;
        final CellGroup redCells;
        final List<CellPosition> noColoredCells;
        boolean gotDuplicatePosition;


        public Board(int boardSize) {
            this.boardSize = boardSize;
            positions = new HashSet<>();
            blueCells = new CellGroup(CellColor.BLUE, boardSize);
            redCells = new CellGroup(CellColor.RED, boardSize);
            noColoredCells = new ArrayList<>();
        }

        public void addCell(CellColor cellColor, CellPosition cellPosition) {

            if (!positions.add(cellPosition)) {
                gotDuplicatePosition = true;
                return;
            }

            if (CellColor.NO_COLOR == cellColor) {
                noColoredCells.add(cellPosition);
            } else if (CellColor.BLUE == cellColor) {
                blueCells.addCell(cellPosition);
            } else {
                redCells.addCell(cellPosition);
            }
        }

        public WinnerType getWinnerType() {

            if (gotDuplicatePosition) {
                return WinnerType.IMPOSSIBLE;
            }

            if (Math.abs(blueCells.getCellCounts() - redCells.getCellCounts()) > 1) {
                return WinnerType.IMPOSSIBLE;
            }

            var redCellWinCount = redCells.countWinningPaths();
            var blueCellWinCount = blueCells.countWinningPaths();
            var totalWins = redCellWinCount + blueCellWinCount;

            if (totalWins > 1) {
                return WinnerType.IMPOSSIBLE;
            } else if (redCellWinCount == 1) {
                return WinnerType.RED;
            } else if (blueCellWinCount == 1) {
                return WinnerType.BLUE;
            } else {
                return WinnerType.NOBODY;
            }
        }

    }

    public static class CellGroup {

        final int boardSize;
        final CellColor cellColor;
        final List<CellPosition> cells;

        public CellGroup(CellColor cellColor, int boardSize) {
            this.cellColor = cellColor;
            this.boardSize = boardSize;
            cells = new ArrayList<>();
        }

        public void addCell(CellPosition cellPosition) {
            cells.add(cellPosition);
        }

        private int getMaxDistance(Collection<CellPosition> cells) {
            return findDistance(cells, getDistanceSelector());
        }

        private Function<CellPosition, Integer> getDistanceSelector() {
            if (CellColor.BLUE == cellColor) {
                return CellPosition::getColumn;
            }
            return CellPosition::getRow;
        }

        private int findDistance(Collection<CellPosition> cells, Function<CellPosition, Integer> mapper) {
            if (cells.isEmpty()) {
                return 0;
            }

            var min = cells.stream()
                    .map(mapper)
                    .min(Integer::compare)
                    .orElse(0);

            var max = cells.stream()
                    .map(mapper)
                    .max(Integer::compare)
                    .orElse(0);

            return max - min + 1;
        }

        public int getCellCounts() {
            return cells.size();
        }

        public int countWinningPaths() {

            List<CellPosition> cellPool = new ArrayList<>(cells);
            var finder = new CellGroupPathFinder(boardSize, cellColor);

            var maxPath = finder.getWinningPath(cellPool);
            var maxDistance = getMaxDistance(maxPath);
            if (maxDistance >= boardSize) {

                var distanceMapper = getDistanceSelector();
                var cellsAtMaxDistance = maxPath.stream()
                        .filter(x -> distanceMapper.apply(x) == maxDistance - 1)
                        .collect(Collectors.toList());
                if (cellsAtMaxDistance.size() == 1) {
                    return 1;
                }
                // try get cells at previous level
                if (boardSize > 1) {
                    var gridUtils = new CellGridUtils(boardSize);
                    var cellsAtPenultimateDistance = maxPath
                            .stream()
                            .filter(x -> distanceMapper.apply(x) == maxDistance - 1 - 1)
                            .collect(Collectors.toList());
                    var countCellsAtPenultimateDistanceThatAreConnectedToMaxDistance = cellsAtPenultimateDistance.stream()
                            .mapToLong(penultimateCell -> cellsAtMaxDistance.stream()
                                    .filter(lastCell -> gridUtils.isLinked(penultimateCell, List.of(lastCell)))
                                    .count()
                            )
                            .min()
                            .orElse(0);
                    return (int) countCellsAtPenultimateDistanceThatAreConnectedToMaxDistance;
                }

                return 10;
            }

            return 0;
        }
    }

    public static class CellGroupPathFinder {

        private static final Comparator<CellPosition> TOP_LEFT_CELL_FIRST_COMPARATOR =
                Comparator.comparingInt((CellPosition o) -> o.row)
                        .thenComparingInt(o -> o.column);

        private static final Comparator<CellPosition> BOTTOM_RIGHT_CELL_FIRST_COMPARATOR =
                TOP_LEFT_CELL_FIRST_COMPARATOR.reversed();


        final int boardSize;
        final CellColor cellColor;
        final CellGridUtils gridUtils;
        Set<CellPosition> linkedCellPositions;
        Set<CellPosition> nonLinkedCellPositions;
        Set<CellPosition> edgeCells;

        public CellGroupPathFinder(int boardSize, CellColor cellColor) {
            this.boardSize = boardSize;
            this.cellColor = cellColor;

            gridUtils = new CellGridUtils(boardSize);

            linkedCellPositions = null;
            nonLinkedCellPositions = null;
            edgeCells = null;
        }

        private Comparator<CellPosition> getCellComparator(CellGroupDirection cellGroupDirection) {
            switch (cellGroupDirection) {
                case LEFT_TO_RIGHT:
                case TOP_TO_BOTTOM:
                    return TOP_LEFT_CELL_FIRST_COMPARATOR;
                default:
                    return BOTTOM_RIGHT_CELL_FIRST_COMPARATOR;
            }
        }

        private SortedSet<CellPosition> getEdgeCells(CellGroupDirection cellGroupDirection) {
            var cells = new TreeSet<>(getCellComparator(cellGroupDirection));

            MoveDirectionEnum direction;
            switch (cellGroupDirection) {
                case LEFT_TO_RIGHT:
                    direction = MoveDirectionEnum.DOWNRIGHT;
                    cells.add(new CellPosition(0, 0));
                    break;
                case TOP_TO_BOTTOM:
                    direction = MoveDirectionEnum.RIGHT;
                    cells.add(new CellPosition(0, 0));
                    break;
                case RIGHT_TO_LEFT:
                    direction = MoveDirectionEnum.UPLEFT;
                    cells.add(new CellPosition(boardSize - 1, boardSize - 1));
                    break;

                case BOTTOM_TO_TOP:
                default:
                    direction = MoveDirectionEnum.LEFT;
                    cells.add(new CellPosition(boardSize - 1, boardSize - 1));
                    break;
            }

            for (int i = 0; i < boardSize - 1; i++) {
                cells
                        .last()
                        .move(new CellMove(boardSize, boardSize, direction))
                        .ifPresent(cells::add);
            }

            return cells;
        }

        public List<CellPosition> getWinningPath(List<CellPosition> cells) {

            Map<CellGroupDirection, List<CellPosition>> winningPathByDirection = new HashMap<>();

            if (CellColor.BLUE == cellColor) {
                winningPathByDirection.put(CellGroupDirection.LEFT_TO_RIGHT, getWinningPathByDirection(cells, CellGroupDirection.LEFT_TO_RIGHT));
                winningPathByDirection.put(CellGroupDirection.RIGHT_TO_LEFT, getWinningPathByDirection(cells, CellGroupDirection.RIGHT_TO_LEFT));
            } else {

                winningPathByDirection.put(CellGroupDirection.TOP_TO_BOTTOM, getWinningPathByDirection(cells, CellGroupDirection.TOP_TO_BOTTOM));
                winningPathByDirection.put(CellGroupDirection.BOTTOM_TO_TOP, getWinningPathByDirection(cells, CellGroupDirection.BOTTOM_TO_TOP));
            }

            return winningPathByDirection.entrySet()
                    .stream()
                    .max(Comparator.comparingInt(x -> x.getValue().size()))
                    .map(Map.Entry::getValue)
                    .orElse(Collections.emptyList());

        }

        private List<CellPosition> getWinningPathByDirection(List<CellPosition> cells, CellGroupDirection direction) {
            linkedCellPositions = new HashSet<>();
            nonLinkedCellPositions = new HashSet<>();
            edgeCells = getEdgeCells(direction);

            for (var cell : cells) {
                addCell(cell);
            }

            return new ArrayList<>(linkedCellPositions);
        }

        private void addCell(CellPosition cellPosition) {
            if (isLinkedToSomeEdge(cellPosition) || isLinked(cellPosition, linkedCellPositions)) {
                linkedCellPositions.add(cellPosition);
                tryLinkUnconnectedCellPool();
            } else {
                nonLinkedCellPositions.add(cellPosition);
            }
        }

        private void tryLinkUnconnectedCellPool() {
            while (true) {
                var newLinkedCells = new ArrayList<CellPosition>();
                for (var cell : nonLinkedCellPositions) {
                    if (isLinked(cell, linkedCellPositions)) {
                        newLinkedCells.add(cell);
                    }
                }

                for (var cell : newLinkedCells) {
                    linkedCellPositions.add(cell);
                    nonLinkedCellPositions.remove(cell);
                }

                if (newLinkedCells.size() == 0) {
                    break;
                }
            }
        }

        private boolean isLinkedToSomeEdge(CellPosition cellPosition) {
            return edgeCells.contains(cellPosition);
        }

        private boolean isLinked(CellPosition cellPosition, Collection<CellPosition> cells) {
            return gridUtils.isLinked(cellPosition, cells);
        }

    }

    public static class CellGridUtils {
        final int boardSize;

        public CellGridUtils(int boardSize) {
            this.boardSize = boardSize;
        }

        public boolean isLinked(CellPosition cellPosition, Collection<CellPosition> cells) {
            return Arrays.stream(MoveDirectionEnum.values())
                    .anyMatch(direction ->
                            cellPosition.move(new CellMove(boardSize, boardSize, direction))
                                    .map(cells::contains)
                                    .orElse(false)
                    );
        }

    }

    //    public static class DuplicatePathFinder
    public enum WinnerType {
        NOBODY("Nobody wins"),
        RED("Red wins"),
        BLUE("Blue wins"),
        IMPOSSIBLE("Impossible");

        private final String value;

        WinnerType(String value) {
            this.value = value;
        }
    }

    public enum CellColor {
        NO_COLOR,
        RED,
        BLUE
    }

    public enum CellGroupDirection {
        LEFT_TO_RIGHT,
        TOP_TO_BOTTOM,

        RIGHT_TO_LEFT,
        BOTTOM_TO_TOP
    }

    public enum MoveDirectionEnum {
        UPRIGHT,
        UPLEFT,
        DOWNRIGHT,
        DOWNLEFT,
        RIGHT,
        LEFT
    }

    public static class CellMove {

        final int numberOfRows;
        final int numberOfColumns;
        final MoveDirectionEnum direction;

        public CellMove(int numberOfRows, int numberOfColumns, MoveDirectionEnum direction) {
            this.numberOfRows = numberOfRows;
            this.numberOfColumns = numberOfColumns;
            this.direction = direction;
        }

    }

    public static class CellPosition {

        final int row;
        final int column;

        public CellPosition(int row, int column) {
            this.row = row;
            this.column = column;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CellPosition that = (CellPosition) o;
            return row == that.row && column == that.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }

        public Optional<CellPosition> move(CellMove cellMove) {
            int newRow = row;
            int newColumn = column;

            switch (cellMove.direction) {

                case DOWNRIGHT:
                    newRow++;
                    break;

                case DOWNLEFT:
                    newRow++;
                    newColumn--;
                    break;

                case UPRIGHT:
                    newRow--;
                    newColumn++;
                    break;

                case UPLEFT:
                    newRow--;
                    break;

                case RIGHT:
                    newColumn++;
                    break;

                case LEFT:
                    newColumn--;
                    break;

                default:
                    return Optional.empty();
            }

            if (!canMove(newRow, newColumn, cellMove)) {
                return Optional.empty();
            }

            return Optional.of(new CellPosition(newRow, newColumn));
        }

        boolean canMove(int newRow, int newFila, CellMove cellMove) {
            return newRow >= 0
                    && newRow < cellMove.numberOfRows
                    && newFila >= 0
                    && newFila < cellMove.numberOfColumns;
        }
    }

}
