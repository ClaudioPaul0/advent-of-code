package pt.timend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Day01 {

    public static void main(String[] args) throws IOException {
        System.out.println("\nTest");
        run(Files.readAllLines(Path.of("inputs/input_day01_test.txt")));

        System.out.println("\nPuzzle");
        run(Files.readAllLines(Path.of("inputs/input_day01.txt")));
    }

    public static void run(List<String> lines) {

        List<Integer> lefts = new ArrayList<>();
        List<Integer> rights = new ArrayList<>();

        lines.stream()
                .map(line -> line.split("\\W+"))
                .map(lineItems -> Arrays.stream(lineItems).map(Integer::parseInt).toArray(Integer[]::new))
                .forEach(entry -> {
                    lefts.add(entry[0]);
                    rights.add(entry[1]);
                });

        Collections.sort(lefts);
        Collections.sort(rights);

        int total = 0;
        for (int i = 0; i < lefts.size(); i++) {
            total += Math.abs(lefts.get(i) - rights.get(i));
        }
        System.out.println("Part 1: " + total);

        Map<Integer, Integer> frequencies = new TreeMap<>();
        rights.forEach(rightItem -> frequencies.merge(rightItem, 1, Integer::sum));

        Integer similarityScore = lefts.stream().reduce(0, (acc, item) -> frequencies.getOrDefault(item, 0) * item + acc);
        System.out.println("Part 2: " + similarityScore);
    }
}
