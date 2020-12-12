import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;

public class Day12 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsIterator("day12-puzzle.txt");

        int[] position = navigate(input);

        System.out.println("Result: " + (Math.abs(position[0]) + Math.abs(position[1])));

        input = InputFileReader.readInputAsIterator("day12-puzzle.txt");

        int[] wayPointPosition = navigateByWayPoint(input);

        System.out.println("Result: " + (Math.abs(wayPointPosition[0]) + Math.abs(wayPointPosition[1])));
    }

    private static int[] navigateByWayPoint(Iterator<String> input) {
        // char[] directions = {'E', 'N', 'W', 'S'};

        // index 0 x, > 0 E, < 0 W, index 1, y, > 0 N, < 0 S
        int [] startPosition = {0, 0};

        int [] waypoint = {10, 1};

        while (input.hasNext()) {

            var line = input.next();
            var action = line.charAt(0);
            var value = Integer.parseInt(line.substring(1));

            moveByWayPoint(startPosition, waypoint, action, value);
        }
        return startPosition;
    }

    private static int[] navigate(Iterator<String> input) {
        // char[] directions = {'E', 'N', 'W', 'S'};
        var directionIdx = 0;

        // index 0 x, > 0 E, < 0 W, index 1, y, > 0 N, < 0 S
        int [] position = {0, 0};

        while (input.hasNext()) {

            var line = input.next();
            var action = line.charAt(0);
            var value = Integer.parseInt(line.substring(1));

            directionIdx = moveByDirection(position, directionIdx, action, value);
        }
        return position;
    }

    private static void moveByWayPoint(int[] position, int[] waypoint, char action, int value) {
        switch (action) {
            case 'N':
                waypoint[1] += value;
                break;
            case 'S':
                waypoint[1] -= value;
                break;
            case 'E':
                waypoint[0] += value;
                break;
            case 'W':
                waypoint[0] -= value;
                break;
            case 'L':
                // E, N, W, S
                var turnL = (value/90) % 4;
                while (turnL > 0) {
                    var tmp = waypoint[0];
                    waypoint[0] = -waypoint[1];
                    waypoint[1] = tmp;
                    turnL--;
                }
                break;
            case 'R':
                var turn = (value/90) % 4;
                while (turn > 0) {
                    var tmp = waypoint[0];
                    waypoint[0] = waypoint[1];
                    waypoint[1] = -tmp;
                    turn--;
                }
                break;
            case 'F':
                position[0] += waypoint[0]*value;
                position[1] += waypoint[1]*value;
        }
    }

    private static int moveByDirection(int[] position, int directionIdx, char action, int value) {
        if(action == 'F') {
            switch (directionIdx) {
                case 0:
                    action = 'E';
                    break;
                case 1:
                    action = 'N';
                    break;
                case 2:
                    action = 'W';
                    break;
                case 3:
                    action = 'S';
                    break;
            }
        }

        switch (action) {
            case 'N':
                position[1] += value;
                break;
            case 'S':
                position[1] -= value;
                break;
            case 'E':
                position[0] += value;
                break;
            case 'W':
                position[0] -= value;
                break;
            case 'L':
                // E, N, W, S
                directionIdx = (directionIdx + value/90) % 4;
                break;
            case 'R':
                directionIdx = ((directionIdx - value/90) % 4 + 4) % 4 ;
                break;
        }
        return directionIdx;
    }
}
