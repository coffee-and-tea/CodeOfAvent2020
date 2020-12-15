import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;

public class Day15 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsList("day15-puzzle.txt");

        var inputs = Arrays.stream(input.get(0).split(",")).mapToInt(
                Integer::parseInt
        ).toArray();

        var lastAppearance = new HashMap<Integer, Integer>();
        var beforeLastAppearance = new HashMap<Integer, Integer>();

        var turn = 1;
        var lastNumber = 0;
        var currentNumber = 0;
        for (int number: inputs) {
            lastAppearance.put(number, turn);
            lastNumber = number;
            turn++;
        }

        while (turn < 30000001) {
            if ( !beforeLastAppearance.containsKey(lastNumber) ) {
                currentNumber = 0;
            } else {
                currentNumber = lastAppearance.get(lastNumber) - beforeLastAppearance.get(lastNumber);
            }
            if(lastAppearance.containsKey(currentNumber)) {
                var beforeLast = lastAppearance.get(currentNumber);
                beforeLastAppearance.put(currentNumber, beforeLast);
            }
            lastAppearance.put(currentNumber, turn);

            lastNumber = currentNumber;
            turn++;
        }
        System.out.println("Turn: " + --turn + " , number: " + currentNumber);
    }
}
