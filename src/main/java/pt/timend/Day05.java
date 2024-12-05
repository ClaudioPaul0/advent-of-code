package pt.timend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day05 {

    public static void main(String[] args) throws IOException {
        System.out.println("\nTest");
        run(Files.readString(Path.of("inputs/input_day05_test.txt")));

        System.out.println("\nPuzzle");
        run(Files.readString(Path.of("inputs/input_day05.txt")));
    }

    private static void run(String input) {
        Iterator<String> lineIterator = Arrays.stream(input.split("\n")).iterator();

        Map<Byte, List<Byte>> rules = new IdentityHashMap<>();
        while (lineIterator.hasNext()) {
            String currentLine = lineIterator.next();
            if (currentLine == null || currentLine.isBlank()) break;

            String[] parts = currentLine.split("\\|");

            Byte left = Byte.valueOf(parts[0]);
            Byte right = Byte.valueOf(parts[1]);

            List<Byte> pageRules = rules.computeIfAbsent(right, k -> new ArrayList<>());
            pageRules.add(left);
        }

        int totalCorrect = 0;
        int totalIncorrect = 0;

        while (lineIterator.hasNext()) {
            String currentLine = lineIterator.next();
            String[] pages = currentLine.split(",");

            if (isIncorrect(pages, rules)) {
                Arrays.sort(pages, (o1, o2) -> sortByRules(rules, o1, o2));
                totalIncorrect += Integer.parseInt(pages[pages.length / 2]);
            } else {
                totalCorrect += Integer.parseInt(pages[pages.length / 2]);
            }
        }

        System.out.println("Part 1: " + totalCorrect);
        System.out.println("Part 2: " + totalIncorrect);
    }

    private static boolean isIncorrect(String[] pages, Map<Byte, List<Byte>> rules) {
        Set<Byte> invalidPages = Collections.newSetFromMap(new IdentityHashMap<>());
        for (String page : pages) {
            Byte pageValue = Byte.valueOf(page);
            if (invalidPages.contains(pageValue)) {
                return true;
            }
            invalidPages.addAll(rules.getOrDefault(pageValue, List.of()));
        }
        return false;
    }

    /**
     * returning  1 means page1 must be after page2
     * returning -1 means page1 must be before page2
     * returning  0 means there is no rule
     */
    private static int sortByRules(Map<Byte, List<Byte>> rules, String page1, String page2) {
        Byte pageValue1 = Byte.valueOf(page1);
        Byte pageValue2 = Byte.valueOf(page2);
        if (rules.containsKey(pageValue1) && rules.get(pageValue1).contains(pageValue2)) return 1;
        if (rules.containsKey(pageValue2) && rules.get(pageValue2).contains(pageValue1)) return -1;
        return 0;
    }

}
