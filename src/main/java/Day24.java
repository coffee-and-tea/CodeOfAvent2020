import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day24 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsStream("day24-puzzle.txt");

        input.forEach(
                s -> Floor.flip(s)
        );

        Floor.countFlipped();

        Floor.allFlippedTiles.stream().forEach(System.out::println);

        Floor.flipPerDay(100);

        Floor.countFlipped();
    }
}

class Floor {

    static List<Floor> allFlippedTiles = new ArrayList<>();
    int ylog3Variant;
    int xVariant;
    boolean nextStepFlip;

    public Floor() {
    }

    public Floor(int ylog3Variant, int xVariant) {
        this.ylog3Variant = ylog3Variant;
        this.xVariant = xVariant;
    }

    public static void flip(String steps) {
        Floor floor = new Floor();
        floor.identify(steps);

        if (allFlippedTiles.contains(floor)) {
            var existing = allFlippedTiles.get(allFlippedTiles.indexOf(floor));
            allFlippedTiles.remove(existing);
        } else {
            allFlippedTiles.add(floor);
        }
    }

    public static void countFlipped() {
        System.out.println(allFlippedTiles.size());
    }

    public static void flipPerDay(int days) {
        while(days > 0) {
            System.out.println("Day " + (101 - days));
            dayFlip();
            Floor.countFlipped();
            days--;
        }


    }

    private static void dayFlip() {
        int xMax, xMin, yMax, yMin;
        var x = allFlippedTiles.stream().mapToInt(
                f -> f.xVariant
        ).summaryStatistics();

        xMax = x.getMax() + 1;
        xMin = x.getMin() - 1;

        var y = allFlippedTiles.stream().mapToInt(
                f -> f.ylog3Variant
        ).summaryStatistics();

        yMax = y.getMax() + 1;
        yMin = y.getMin() - 1;

        allFlippedTiles = IntStream.range(yMin, yMax + 1).mapToObj(
                yRange -> {
                    if(yRange % 2 == 0) {
                        return IntStream.range(xMin, xMax + 1).filter(
                                xRange -> xRange % 2 == 0
                        ).mapToObj(
                                xRange -> {
                                    Floor currentFloor = new Floor();
                                    currentFloor.xVariant = xRange;
                                    currentFloor.ylog3Variant = yRange;
                                    currentFloor.decideFlip();
                                    return currentFloor;
                                }
                        ).collect(Collectors.toList());
                    } else {
                        return IntStream.range(xMin, xMax + 1).filter(
                                xRange -> xRange % 2 != 0
                        ).mapToObj(
                                xRange -> {
                                    Floor currentFloor = new Floor();
                                    currentFloor.xVariant = xRange;
                                    currentFloor.ylog3Variant = yRange;
                                    currentFloor.decideFlip();
                                    return currentFloor;
                                }
                        ).collect(Collectors.toList());
                    }
                }
        ).flatMap(floors -> floors.stream()).filter(
                floor -> floor.nextStepFlip
        ).collect(Collectors.toList());

    }

    private void decideFlip() {
        var countFlipNeighbor = countFlippedNeighbor();
        if (allFlippedTiles.contains(this)) {
            if(countFlipNeighbor == 0 || countFlipNeighbor > 2){
                nextStepFlip = false;
            } else {
                nextStepFlip = true;
            }
        } else {
            if(countFlipNeighbor == 2) {
                nextStepFlip = true;
            } else {
                nextStepFlip = false;
            }
        }
    }

    private long countFlippedNeighbor() {
        return Stream.of(
                new Floor(ylog3Variant + 1, xVariant - 1),
                new Floor(ylog3Variant + 1, xVariant + 1),
                new Floor( ylog3Variant, xVariant - 2),
                new Floor(ylog3Variant, xVariant + 2),
                new Floor(ylog3Variant - 1, xVariant - 1),
                new Floor(ylog3Variant - 1, xVariant + 1)
        ).filter(
                f -> allFlippedTiles.contains(f)
        ).count();
    }

    @Override
    public String toString() {
        return "Floor{" +
                "ylog3Variant=" + ylog3Variant +
                ", xVariant=" + xVariant +
                '}';
    }

    private void identify(String steps) {
        int ylog3V = 0;
        int xV = 0;
        while (steps.length() > 0) {
            if (steps.startsWith("se")) {
                ylog3V--;
                xV++;
                steps = steps.substring(2);
            } else if (steps.startsWith("sw")) {
                ylog3V--;
                xV--;
                steps = steps.substring(2);
            } else if (steps.startsWith("e")) {
                xV += 2;
                steps = steps.substring(1);
            } else if (steps.startsWith("nw")) {
                ylog3V++;
                xV--;
                steps = steps.substring(2);
            } else if (steps.startsWith("ne")) {
                ylog3V++;
                xV++;
                steps = steps.substring(2);
            } else if (steps.startsWith("w")) {
                xV -= 2;
                steps = steps.substring(1);
            }
        }

        this.ylog3Variant = ylog3V;
        this.xVariant = xV;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Floor floor = (Floor) o;

        if (ylog3Variant != floor.ylog3Variant) return false;
        return xVariant == floor.xVariant;
    }

    @Override
    public int hashCode() {
        int result = ylog3Variant;
        result = 31 * result + xVariant;
        return result;
    }
}



