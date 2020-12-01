import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day1 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        int[] inputs = readInput();


        // O(nlogn) complexity, space O(1)
        Arrays.sort(inputs);

        int result = produceOfTwoSumToTarget(2020, 0, inputs);
        System.out.println("Solution for 2 sum: " + result);

        result = produceOf3SumToTarget(2020, inputs);
        System.out.println("Solution for 3 sum: " + result);
    }

    private static int[] readInput() throws URISyntaxException, IOException {
        URL resource = Day1.class.getClassLoader().getResource("puzzle.txt");
        Path inputFile = Path.of(resource.toURI());

        try (BufferedReader inputReader = Files.newBufferedReader(inputFile)) {

            List<Integer> integerList = new ArrayList<>();
            String inputLine;
            while ((inputLine = inputReader.readLine()) != null) {
                integerList.add(Integer.valueOf(inputLine));
            }

            return integerList.stream().mapToInt(i -> i).toArray();
        }
    }

    /**
     * Day1 puzzle produce of 2 numbers sum up to 2020
     * <p>
     * With a sorted array, then pick from left to right, perform binary search to sum up to 2020
     * If match found, then produce it
     * Complexity O(nlogn)
     * <p>
     * @param target   target number to sum up to
     * @param startIdx start position for search
     * @param intArray sorted array of integers
     * @return produce of 2 number sum up to target
     */
    private static int produceOfTwoSumToTarget(int target, int startIdx, int[] intArray) {
        int result = -1;
        for (int i = startIdx; i < intArray.length; i++) {
            if (searchTargetSum(intArray, i, target)) {
                result = i;
                break;
            }
        }
        if (result != -1) {
            System.out.println("Found pair: " + intArray[result] + " and " + (target - intArray[result]));
            return intArray[result] * (target - intArray[result]);
        } else {
            return -1;
        }
    }


    /**
     * Binary search for target sum, complexity O(logn)
     */
    private static boolean searchTargetSum(int[] intArray, int searchIdx, int target) {
        int startIdx = searchIdx + 1;
        int endIdx = intArray.length - 1;
        int matchPair = intArray[searchIdx];
        int median;

        while (startIdx <= endIdx) {
            median = startIdx + (endIdx - startIdx) / 2;
            if (target - matchPair == intArray[median]) {
                return true;
            } else if (target - matchPair > intArray[median]) {
                startIdx = median + 1;
            } else {
                endIdx = median - 1;
            }

        }
        return false;
    }


    private static int produceOf3SumToTarget(int target, int[] inputs) {
        int result = 0;
        int produceOf2 = 0;
        for (int i = 0; i < inputs.length; i++) {
            int match1 = inputs[i];
            int produce2 = produceOfTwoSumToTarget(target - match1, i + 1, inputs);
            if (produce2 != -1) {
                System.out.println("Find match: " + match1);
                return match1 * produce2;
            }
        }
        return -1;
    }
}
