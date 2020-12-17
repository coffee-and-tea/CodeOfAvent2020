import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day16 {

    static ArrayList<FieldRange> fieldRanges = new ArrayList<>();
    static List<Ticket> tickets = new ArrayList<>();


    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsStream("day16-puzzle.txt");

        var errorRate = input.mapToLong(
                line -> {
                    if (line.contains("or")) {
                        return processFieldRanges(line);
                    } else if (line.contains(",")) {
                        return processTickets(line);
                    }
                    return 0;
                }
        ).sum();

        System.out.println("Error rate: " + errorRate);

        determineIndex();

        var result = fieldRanges.stream().filter(
                fieldRange -> fieldRange.name.startsWith("departure")
        ).mapToLong(
                fieldRange -> tickets.get(0).getPositions()[fieldRange.index]
        ).reduce(
                (a, b) -> a * b
        );

        System.out.println(result);
    }

    private static void determineIndex() {

        var fieldPotentialIndex = new HashMap<FieldRange, List<Integer>>();

        fieldRanges.stream().forEach(
                fieldRange ->
                        fieldPotentialIndex.put(fieldRange, IntStream.range(0, fieldRanges.size()).filter(
                                i ->
                                        tickets.stream().allMatch(
                                                ticket ->
                                                        fieldRange.valid(ticket.getPositions()[i])
                                        )
                        ).mapToObj(Integer::valueOf).collect(Collectors.toList()))
        );

        Comparator<FieldRange> fieldRangeComparator = (s1, s2) -> Integer.compare(fieldPotentialIndex.get(s1).size(), fieldPotentialIndex.get(s2).size());
        List<FieldRange> sorted = fieldPotentialIndex.keySet().stream().sorted(
                fieldRangeComparator
        ).collect(Collectors.toList());

        while (sorted.size() > 0) {
            FieldRange determinedField = sorted.remove(0);
            determinedField.index = fieldPotentialIndex.get(determinedField).get(0);
            fieldPotentialIndex.values().stream().forEach(
                    list -> list.remove(Integer.valueOf(determinedField.index))
            );
            Collections.sort(sorted, fieldRangeComparator);
        }
    }

    private static long processTickets(String line) {
        var input = Arrays.stream(line.split(",")).sequential().mapToInt(Integer::parseInt).toArray();

        var statistics = Arrays.stream(input).sequential().filter(
                fieldValue -> !validate(fieldValue)
        ).summaryStatistics();

        if (statistics.getCount() == 0) {
            tickets.add(new Ticket(input));
        }

        return statistics.getSum();
    }

    private static boolean validate(int value) {
        return fieldRanges.stream().anyMatch(
                fieldRange -> fieldRange.valid(value)
        );
    }

    private static long processFieldRanges(String line) {
        var name = line.substring(0, line.indexOf(':'));
        var ranges = line.substring(line.indexOf(':') + 2).split(" or ");

        fieldRanges.add(new FieldRange(name, parseRange(ranges[0]), parseRange(ranges[1])));

        return 0;
    }

    private static Range parseRange(String range) {
        var minMax = range.split("-");
        return new Range(Integer.parseInt(minMax[0]), Integer.parseInt(minMax[1]));
    }
}

class FieldRange {
    String name;
    Range range1;
    Range range2;
    int index;

    public FieldRange(String name, Range range1, Range range2) {
        this.name = name;
        this.range1 = range1;
        this.range2 = range2;
    }

    public boolean valid(int value) {
        return this.range1.valid(value) || this.range2.valid(value);
    }
}

class Range {

    int min;
    int max;

    public Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public boolean valid(int value) {
        if (this.max >= value && this.min <= value) {
            return true;
        }
        return false;
    }

}

class Ticket {
    int[] positions;

    public Ticket(int[] positions) {
        this.positions = positions;
    }

    public int[] getPositions() {
        return positions;
    }

}
