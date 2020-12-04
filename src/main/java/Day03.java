import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day03 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        var map = InputFileReader.readInputAsList("day03-puzzle.txt");

        var count31 = treeInPath(map, 3, 1);

        System.out.println("Tree in path 1 down 3 right: " + count31);

        var paths = Stream.of("3,1", "1,1", "5,1", "7,1", "1,2");

        var result = paths.map(
                path -> {
                    String[] slope = path.split(",");
                    return treeInPath(map, Integer.parseInt(slope[0]), Integer.parseInt(slope[1]));
                }
        ).reduce((a, b) -> a * b);

        System.out.println("Multiplied: " + result.get());

    }

    private static long treeInPath(List<String> map, int colInc, int lineInc) {
        var roadLength = map.get(0).length();
        return IntStream.range(0, map.size()).filter(
                i -> i % lineInc == 0
        ).filter(
                i -> map.get(i).charAt(colInc * i / lineInc % roadLength) == '#'
        ).count();
    }
}
