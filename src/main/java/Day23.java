import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class Day23 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsIterator("day23-puzzle.txt");

        partOne(input);

        input = InputFileReader.readInputAsIterator("day23-puzzle.txt");

        partTwo(input);
    }

    private static void partTwo(Iterator<String> input) {

        var cups = input.next().split("");
        var head = new LinkedCup(Integer.valueOf(cups[0]));
        var currentCup = head;
        var maxValue = 9;

        Map<Integer, LinkedCup> allCups = new HashMap<>();

        allCups.put(Integer.valueOf(cups[0]), currentCup);

        for(int i = 1; i < cups.length; i++) {
            var nextCup = new LinkedCup(Integer.valueOf(cups[i]));
            currentCup.next = nextCup;
            allCups.put(Integer.valueOf(cups[i]), nextCup);
            currentCup = nextCup;
        }

        IntStream.range(1, 10).forEach(
                i -> {
                    var previousValue = i - 1 == 0 ? maxValue : i - 1;
                    allCups.get(i).valueLessCup = allCups.get(previousValue);
                }
        );

        var cup10 = new LinkedCup(10);
        cup10.valueLessCup = allCups.get(9);
        currentCup.next = cup10;
        currentCup = cup10;

        for(int i = 11; i < 1000001; i ++ ) {
            var nextCup = new LinkedCup(i);
            nextCup.valueLessCup = currentCup;
            currentCup.next = nextCup;
            currentCup = nextCup;
        }

        currentCup.next = head;
        allCups.get(1).valueLessCup = currentCup;

        for(int i = 0; i < 10000000; i++) {
            head = head.move();
        }

        while(head.value != 1) {
            head = head.next;
        }
        System.out.println(head.value);
        System.out.print(head.next.value );
        System.out.print("*");
        System.out.print(head.next.next.value );
        System.out.println();
        System.out.println((long)head.next.value * head.next.next.value);
    }

    private static void partOne(Iterator<String> input) {

        var cups = input.next().split("");
        var head = new LinkedCup(Integer.valueOf(cups[0]));
        var currentCup = head;
        var maxValue = 9;

        Map<Integer, LinkedCup> allCups = new HashMap<>();

        allCups.put(Integer.valueOf(cups[0]), currentCup);

        for(int i = 1; i < cups.length; i++) {
            var nextCup = new LinkedCup(Integer.valueOf(cups[i]));
            currentCup.next = nextCup;
            allCups.put(Integer.valueOf(cups[i]), nextCup);
            currentCup = nextCup;
        }

        currentCup.next = head;

        IntStream.range(1, 10).forEach(
                i -> {
                    var previousValue = i - 1 == 0 ? maxValue : i - 1;
                    allCups.get(i).valueLessCup = allCups.get(previousValue);
                }
        );

        for(int i = 0; i < 100; i++) {
            head = head.move();
        }

        head.printCurrent();


    }
}


class LinkedCup {
    int value;
    LinkedCup next;
    LinkedCup valueLessCup;
    public LinkedCup(int value) {
        this.value = value;
    }

    public LinkedCup move() {
        var currentNext = next;
        var nextTwo = next.next;
        var nextThree = next.next.next;

        next = nextThree.next;
        var destination = valueLessCup;
        while (destination == currentNext || destination == nextTwo || destination == nextThree) {
            destination = destination.valueLessCup;
        }

        var destinationNext = destination.next;
        destination.next = currentNext;
        nextThree.next = destinationNext;

        return next;
    }

    public void printCurrent() {
        System.out.print(value);
        next.printValue(value);

        System.out.println();
    }

    private void printValue(int stop) {
        if(value != stop) {
            System.out.print(value);
            next.printValue(stop);
        }
    }
}
