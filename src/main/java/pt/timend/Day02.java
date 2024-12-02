package pt.timend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day02 {

    public static void main(String[] args) throws IOException {
        System.out.println("\nTest");
        run(Files.readAllLines(Path.of("inputs/input_day02_test.txt")));

        System.out.println("\nPuzzle");
        run(Files.readAllLines(Path.of("inputs/input_day02.txt")));
    }

    private static void run(List<String> lines) {
        List<ArrayList<Integer>> reports = lines.stream()
                .map(line -> line.split("\\W+"))
                .map(lineItems -> Arrays.stream(lineItems).map(Integer::parseInt).collect(Collectors.toCollection(ArrayList::new)))
                .toList();

        int safe = 0;
        for (List<Integer> report : reports) {
            if (isSafe(report).safe) safe++;
        }
        System.out.println("Part 1: " + safe);

        safe = 0;
        outerLoop:
        for (List<Integer> report : reports) {
            SafeReport safeReport = isSafe(report);

            if (safeReport.safe) {
                safe++;
                System.out.println("safe     = " + report);
                continue;
            }

            for (Integer unsafeIndex : safeReport.unsafeIndexes) {
                List<Integer> unsafeX = new ArrayList<>(report);
                unsafeX.remove((int) unsafeIndex);

                if (isSafe(unsafeX).safe) {
                    safe++;
                    System.out.println("dampened = " + report);
                    continue outerLoop;
                }
            }
            System.out.println("unsafe   = " + report);
        }
        System.out.println("Part 2: " + safe);
    }

    private record SafeReport(boolean safe, Set<Integer> unsafeIndexes) {}

    private static SafeReport isSafe(List<Integer> report) {
        int ascending = report.get(0) < report.get(1) ? 1 : -1;

        for (int i = 0; i < report.size() - 1; i++) {
            int delta = (report.get(i + 1) - report.get(i)) * ascending;
            if (1 > delta || delta > 3) {
                TreeSet<Integer> unsafeIndexes = new TreeSet<>();
                unsafeIndexes.add(0);
                unsafeIndexes.add(1);
                unsafeIndexes.add(i);
                unsafeIndexes.add(i + 1);
                return new SafeReport(false, unsafeIndexes);
            }
        }
        return new SafeReport(true, Set.of());
    }

}
