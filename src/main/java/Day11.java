import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day11 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsStream("day11-puzzle.txt");
        var currentMap = input.map(s -> s.toCharArray()).toArray(char[][]::new);

        SeatInDirection nextOne = (seatMap, x, y, x_direction, y_direction) -> {
            var xpos = x + x_direction;
            var ypos = y + y_direction;
            return ( xpos >= 0 && xpos < seatMap.length && ypos >= 0 && ypos < seatMap[xpos].length ) ? seatMap[x+x_direction][y+y_direction] : '.';
        };

        SeatInDirection nextInDirection = (seatMap, x, y, x_direction, y_direction) -> {
            var xpos = x + x_direction;
            var ypos = y + y_direction;
            while (xpos >= 0 && xpos < seatMap.length && ypos >= 0 && ypos < seatMap[xpos].length && seatMap[xpos][ypos] == '.') {
                xpos += x_direction;
                ypos += y_direction;
            }
            return ( xpos >= 0 && xpos < seatMap.length && ypos >= 0 && ypos < seatMap[xpos].length ) ? seatMap[xpos][ypos] : '.';
        };

        var result = stableSeats(currentMap, 4, nextOne);

        System.out.println("Occupied seats: " + result);

        result = stableSeats(currentMap, 5, nextInDirection);

        System.out.println("Occupied seats: " + result);
    }

    private static long stableSeats(char[][] currentMap, int moveCount, SeatInDirection nextOne) {
        var rounds = 0;
        var nextRound = new char[currentMap.length][currentMap[0].length];
        var changed = true;
        while(changed) {
            changed = false;
            for(int i = 0; i < currentMap.length; i++) {
                for(int j = 0; j < currentMap[i].length; j++) {
                    nextRound[i][j] = calculateNextRound(currentMap, i, j, moveCount, nextOne);
                    if(nextRound[i][j] != currentMap[i][j]) {
                        changed = true;
                    }
                }
            }
            currentMap = nextRound;
            nextRound = new char[currentMap.length][currentMap[0].length];
            rounds ++;
        }

        System.out.println("Stable after rounds: " + rounds);
//        Arrays.stream(currentMap).sequential().forEach(line -> System.out.println(Arrays.toString(line)));
        return  Arrays.stream(currentMap).flatMap(
               row ->
                       IntStream.range(0, row.length).mapToObj(i-> row[i])
        ).filter( c -> c.charValue() == '#' ).count();

    }

    private static char calculateNextRound(char[][] seatMap, int i, int j, int moveCount, SeatInDirection seatInDirection) {
        var currentSeatState = seatMap[i][j];
        var nextSeatState = currentSeatState;

        switch (currentSeatState) {
            case '.':
                break;
            case 'L':
                if (getNeighborStream(seatMap, i, j, seatInDirection).filter( c -> c.charValue() == '#').count() == 0){
                    nextSeatState = '#';
                }
                break;
            case '#':
                if(getNeighborStream(seatMap, i, j, seatInDirection).filter( c -> c.charValue() == '#').count() >= moveCount) {
                    nextSeatState = 'L';
                }
                break;
        }
        return nextSeatState;
    }

    private static Stream<Character> getNeighborStream(char[][] seatMap, int i, int j, SeatInDirection seatInDirection) {
        return Stream.of(
                getSeat(seatMap, i, j, -1, -1, seatInDirection),
                getSeat(seatMap, i , j, -1, -0, seatInDirection),
                getSeat(seatMap, i, j, -1, 1, seatInDirection),
                getSeat(seatMap, i, j, 0, -1, seatInDirection),
                getSeat(seatMap, i, j, 0, 1, seatInDirection),
                getSeat(seatMap, i, j, 1, -1, seatInDirection),
                getSeat(seatMap, i, j, 1, 0, seatInDirection),
                getSeat(seatMap, i, j, 1, 1, seatInDirection)
        );
    }

    private static Character getSeat(char[][] seatMap, int x, int y, int x_direction, int y_direction, SeatInDirection seatInDirection) {
        return seatInDirection.getSeat(seatMap, x, y, x_direction, y_direction);
    }
}

interface SeatInDirection{
    char getSeat(char[][] seatMap, int x, int y, int x_direction, int y_direction);
}
