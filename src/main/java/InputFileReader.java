import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InputFileReader {

    public static Stream<String> readInputAsStream(String fileName) throws URISyntaxException, IOException {

        var resource = Day02.class.getClassLoader().getResource(fileName);
        var inputFile = Path.of(resource.toURI());

        BufferedReader inputReader = Files.newBufferedReader(inputFile);

        return inputReader.lines();
    }

    public static List<String> readInputAsList(String fileName) throws URISyntaxException, IOException {
        var resource = Day03.class.getClassLoader().getResource(fileName);
        var inputFile = Path.of(resource.toURI());

        try (BufferedReader inputReader = Files.newBufferedReader(inputFile)) {

            return inputReader.lines().collect(Collectors.toList());

        }
    }

    public static int[] readInputAsIntArray(String fileName) throws URISyntaxException, IOException {
        var resource = Day01.class.getClassLoader().getResource(fileName);
        var inputFile = Path.of(resource.toURI());

        try (BufferedReader inputReader = Files.newBufferedReader(inputFile)) {

            var integerList = new ArrayList<Integer>();
            String inputLine;
            while ((inputLine = inputReader.readLine()) != null) {
                integerList.add(Integer.valueOf(inputLine));
            }

            return integerList.stream().mapToInt(i -> i).toArray();
        }
    }
}
