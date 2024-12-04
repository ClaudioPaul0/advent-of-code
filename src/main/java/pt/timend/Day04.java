package pt.timend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.EnumSet;

public class Day04 {

    public static void main(String[] args) throws IOException {
        System.out.println("\nTest");
        run(Files.readString(Path.of("inputs/input_day04_test.txt")));

        System.out.println("\nPuzzle");
        run(Files.readString(Path.of("inputs/input_day04.txt")));
    }

    private enum Letter {
        X, M, A, S;

        private boolean isContinuation(Letter otherLetter) {
            return switch (this) {
                case M -> otherLetter == X;
                case A -> otherLetter == M;
                case S -> otherLetter == A;
                default -> false;
            };
        }

        private boolean isStart() {
            return this == X;
        }

        private boolean isEnd() {
            return this == S;
        }

        private boolean isCrossCenter() {
            return this == A;
        }

        private static boolean isCrossOpposite(Letter letterA, Letter letterB) {
            return switch (letterA) {
                case S -> letterB == M;
                case M -> letterB == S;
                default -> false;
            };
        }
    }

    private enum Direction {
        TL(-1, -1),
        T(0, -1),
        TR(1, -1),
        R(1, 0),
        BR(1, 1),
        B(0, 1),
        BL(-1, 1),
        L(-1, 0);

        public final int dx;
        public final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public boolean isOutOfBounds(int x, int y, int width, int height) {
            x = x + dx;
            y = y + dy;
            return x < 0
                    || x >= width
                    || y < 0
                    || y >= height;
        }
    }

    static EnumSet<Direction> DIRECTIONS = EnumSet.allOf(Direction.class);

    private static void run(String input) {
        String[] lines = input.split("\n");

        int height = lines.length;
        int width = lines[0].length();

        Letter[][] matrix = new Letter[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                matrix[y][x] = switch (lines[y].charAt(x)) {
                    case 'X' -> Letter.X;
                    case 'M' -> Letter.M;
                    case 'A' -> Letter.A;
                    case 'S' -> Letter.S;
                    default -> throw new IllegalStateException("Unexpected value: " + lines[y].charAt(x));
                };
            }
        }

        System.out.println("matrix = [");
        for (Letter[] row : matrix) {
            System.out.println("    " + Arrays.deepToString(row));
        }
        System.out.println("]");

        int total = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix[y][x].isStart()) {
                    for (Direction direction : DIRECTIONS) {
                        if (isCompleteWord(matrix, x, y, direction)) {
                            System.out.printf("[%d, %d, %s]\n", x, y, direction);
                            total++;
                        }
                    }
                }
            }
        }
        System.out.println("Part 1: " + total);

        total = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix[y][x].isCrossCenter()) {
                    if (isCrossMas(matrix, x, y)) {
                        System.out.printf("[%d, %d]\n", x, y);
                        total++;
                    }
                }
            }
        }
        System.out.println("Part 2: " + total);
    }

    private static boolean isCompleteWord(Letter[][] matrix, int x, int y, Direction direction) {
        Letter letter = matrix[y][x];

        while (!letter.isEnd()) {
            if (direction.isOutOfBounds(x, y, matrix[0].length, matrix.length)) return false;
            x = x + direction.dx;
            y = y + direction.dy;
            Letter step = matrix[y][x];

            if (step == null || !step.isContinuation(letter)) {
                return false;
            }
            letter = step;
        }
        return true;
    }

    private static boolean isCrossMas(Letter[][] matrix, int x, int y) {
        int width = matrix[0].length, height = matrix.length;
        return (!Direction.TL.isOutOfBounds(x, y, width, height)
                && !Direction.BR.isOutOfBounds(x, y, width, height)
                && Letter.isCrossOpposite(matrix[y + Direction.TL.dy][x + Direction.TL.dx], matrix[y + Direction.BR.dy][x + Direction.BR.dx])

                && !Direction.TR.isOutOfBounds(x, y, width, height)
                && !Direction.BL.isOutOfBounds(x, y, width, height)
                && Letter.isCrossOpposite(matrix[y + Direction.TR.dy][x + Direction.TR.dx], matrix[y + Direction.BL.dy][x + Direction.BL.dx]));
    }

}
