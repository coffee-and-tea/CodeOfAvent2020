import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class Day13 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsList("day13-puzzle.txt");

        var arrivalTime = Integer.parseInt(input.get(0));

        var busses = Arrays.stream(input.get(1).split(",")).mapToInt(
                bus -> {
                    if(!bus.equals("x")) {
                        return Integer.parseInt(bus);
                    }else{
                        return 0;
                    }
                }
        ).toArray();

        arrivalTime(busses, arrivalTime);

        startTime(busses);
    }

    private static void startTime(int[] busses) {
        long res = 1;
        long step = 1;

        // input is all prime number so don't have to find common factors,
        // for 2 busses a and b, between 0 and a*b the right gap will be there
        // each time increment a and find x that x + gap % b == 0, and to keep this
        // x needs to increment a*b every time
        for (int i = 0; i < busses.length; i++) {
            if (busses[i] != 0) {
                var factor = busses[i];
                while ((res + i) % factor != 0) {
                    res += step;
                }
                step = factor * step;
            }
        }
        System.out.println(res);
    }

    private static void arrivalTime(int[] input, int arrivalTime) {

        var result = Arrays.stream(input).filter(
                bus -> bus != 0
        ).mapToObj(
                bus -> {
                    return new Pair(bus - arrivalTime % bus, bus);
                }
        ).min(Pair::compareTo).get();
        System.out.println("Bus: " + (result.getX() * result.getY()));
    }


}

class Pair implements Comparable {

    private int x;
    private int y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int compareTo(Object o) {

        return Integer.compare(x, ((Pair) o).getX());
    }
}