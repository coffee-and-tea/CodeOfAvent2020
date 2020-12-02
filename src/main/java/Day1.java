import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Day1 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        var inputs = readInput();

        // better solution with set, complexity O(n), space O(n)
        var result = produceOf2SumUseSet(2020, 0, inputs);
        System.out.println("Solution for 2 sum with set: " + result);

        // complexity O(n*n), space O(n)
        result = produceOf3SumUseSet(2020, inputs);
        System.out.println("Solution for 3 sum with set: " + result);

        // O(nlogn) complexity, space O(1)
        Arrays.sort(inputs);

        // complexity O(nlogn), space O(1)
        result = produceOfTwoSumBinarySearch(2020, 0, inputs);
        System.out.println("Solution for 2 sum with binary search: " + result);

        // complexity O(n*nlogn), space O(1)
        result = produceOf3SumBinarySearch(2020, inputs);
        System.out.println("Solution for 3 sum: " + result);
    }

    private static int produceOf3SumUseSet(int target, int[] inputs) {

        for(int i = 0; i < inputs.length; i++){
            var produceOf2 = produceOf2SumUseSet(target-inputs[i], i+1, inputs);
            if ( produceOf2 != -1) {
                return produceOf2 * inputs[i];
            }
        }
        return -1;
    }

    /**
     * Use set to determine 2 sum with complexity of O(n)
     */
    private static int produceOf2SumUseSet(int target, int startIdx, int[] inputs) {

        var intSet = new HashSet<Integer>();
        for(int i = startIdx; i < inputs.length; i ++) {
            if(intSet.contains(target-inputs[i])){
                return inputs[i] * (target-inputs[i]);
            } else {
                intSet.add(inputs[i]);
            }
        }
        return -1;
    }

    private static int[] readInput() throws URISyntaxException, IOException {
        var resource = Day1.class.getClassLoader().getResource("puzzle.txt");
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
    private static int produceOfTwoSumBinarySearch(int target, int startIdx, int[] intArray) {
        var result = -1;
        for (int i = startIdx; i < intArray.length; i++) {
            if (searchTargetSum(intArray, i, target)) {
                result = i;
                break;
            }
        }
        if (result != -1) {
            return intArray[result] * (target - intArray[result]);
        } else {
            return -1;
        }
    }


    /**
     * Binary search for target sum, complexity O(logn)
     */
    private static boolean searchTargetSum(int[] intArray, int searchIdx, int target) {
        var startIdx = searchIdx + 1;
        var endIdx = intArray.length - 1;
        var matchPair = intArray[searchIdx];
        var median = -1;

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


    private static int produceOf3SumBinarySearch(int target, int[] inputs) {
        for (int i = 0; i < inputs.length; i++) {
            var match1 = inputs[i];
            var produce2 = produceOfTwoSumBinarySearch(target - match1, i + 1, inputs);
            if (produce2 != -1) {
                return match1 * produce2;
            }
        }
        return -1;
    }
}
