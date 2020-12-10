import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.IntStream;

public class Day10 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        var devices = InputFileReader.readInputAsIntSet("day10-puzzle.txt");

        var deviceJoltage = devices.stream().max(Comparator.naturalOrder()).get() + 3;

        var result = longestPath(devices, deviceJoltage);

        System.out.println("Longest path: " + result );

        var pathCounts = countPaths(devices, deviceJoltage);

        System.out.println("Possible path: " + pathCounts);
    }

    private static long countPaths(Set<Integer> devices, int deviceJoltage) {

        var paths = new HashMap<Integer, Long>();

        var listOfDevices = IntStream.range(0, deviceJoltage).sequential().filter(
                        i -> devices.contains(i)
                ).iterator();


        while(listOfDevices.hasNext()) {
            var device = listOfDevices.next();

            var pathCount = (long) 0;
            for(int step = 1; step < 4; step ++) {
                var previousDevice = device - step;
                if(previousDevice == 0) {
                    pathCount++;
                } else if(devices.contains(previousDevice)) {
                    pathCount += paths.get(previousDevice);
                }
                paths.put(device, pathCount);
            }
        }

        return paths.get(deviceJoltage - 3);
    }

    private static int longestPath(Set<Integer> devices, int deviceJoltage) {

        var oneSteps = 0;
        var threeSteps = 0;
        var previousDevice = 0;

        var listOfDevice = IntStream.range(0, deviceJoltage).sequential().filter(
                        i -> devices.contains(i)
                ).iterator();

        while( listOfDevice.hasNext() ){
            var device = listOfDevice.next();
            if(device - previousDevice == 1) {
                oneSteps++;
            } else if (device - previousDevice == 3) {
                threeSteps++;
            }
            previousDevice = device;
        }

        return oneSteps * ++threeSteps;

    }
}
