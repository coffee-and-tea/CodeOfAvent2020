import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day05 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsStream("day05-puzzle.txt");

        var max = input.mapToInt(
                s ->
                        Integer.parseInt(
                                s
                                        .replaceAll("B", "1")
                                        .replaceAll("F", "0")
                                        .replaceAll("R", "1")
                                        .replaceAll("L", "0"),
                                2)

        ).max();

        System.out.println("Max seat number: " + max.getAsInt());

        input = InputFileReader.readInputAsStream("day05-puzzle.txt");

        var allSeats = IntStream.range(0, max.getAsInt()+1).mapToObj(Integer::new).collect(Collectors.toSet());

        input.mapToInt(
                s ->
                        Integer.parseInt(
                                s
                                        .replaceAll("B", "1")
                                        .replaceAll("F", "0")
                                        .replaceAll("R", "1")
                                        .replaceAll("L", "0"),
                                2)

        ).forEach(seat -> allSeats.remove(seat));

        var mySeat = allSeats.stream().mapToInt(Integer::intValue).max();

        System.out.println("My seat: " + mySeat.getAsInt());

    }
}
