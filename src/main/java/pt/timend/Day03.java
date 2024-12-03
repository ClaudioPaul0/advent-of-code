package pt.timend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class Day03 {

    public static void main(String[] args) throws IOException {
        System.out.println("\nTestA");
        run(Files.readString(Path.of("inputs/input_day03_testA.txt")));

        System.out.println("\nTestB");
        run(Files.readString(Path.of("inputs/input_day03_testB.txt")));

        System.out.println("\nPuzzle");
        run(Files.readString(Path.of("inputs/input_day03.txt")));
    }

    private static void run(String program) {
        Pattern pattern = Pattern.compile("mul\\(([1-9][0-9]{0,2}),([1-9][0-9]{0,2})\\)");

        long sum = pattern.matcher(program).results()
                .mapToLong(result -> Long.parseLong(result.group(1)) * Long.parseLong(result.group(2)))
                .sum();
        System.out.println("Part 1: " + sum);

        program = program.replaceAll("don't\\(\\)([\\s\\S]+?)(do\\(\\)|$)", "");

        sum = pattern.matcher(program).results()
                .mapToLong(result -> Long.parseLong(result.group(1)) * Long.parseLong(result.group(2)))
                .sum();
        System.out.println("Part 2: " + sum);
    }

}
