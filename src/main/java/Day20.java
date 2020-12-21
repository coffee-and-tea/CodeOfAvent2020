import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day20 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsIterator("day20-puzzle.txt");

        Tile.processInput(input);

        Tile.buildSides();

        Tile.identifyNeighbors();

        Tile.repositionAllTiles();

        var partOneResult = Tile.tiles.values().stream().filter(
                tile -> tile.neighbor.size() < 3
        ).mapToLong(
                tile -> tile.id
        ).reduce(1, (a, b) -> a * b);

        System.out.println(partOneResult);

        Tile graph = Tile.buildImage();

        Map<Integer, List<Integer>> monsterChecks = new HashMap<>();
        monsterChecks.put(0, List.of(18));
        monsterChecks.put(1, List.of(0, 5, 6, 11, 12, 17, 18, 19));
        monsterChecks.put(2, List.of(1, 4, 7, 10, 13, 16));

        var monsterLength = 20;
        var monsterHeight = 3;

        graph.findMonster(monsterChecks, monsterLength, monsterHeight);
    }
}

class Tile {

    static Map<Integer, Tile> tiles = new HashMap<>();

    //  clock wise current sides
    int id;
    int x;
    int y;
    List<String> sides;
    List<String> lines;
    Map<Integer, Tile> neighbor = new HashMap<>();

    public Tile(int id) {
        this.id = id;
    }

    public static void identifyNeighbors() {

        var allTiles = tiles.values().stream().collect(Collectors.toList());

        while (allTiles.size() > 1) {
            var currentTile = allTiles.remove(0);
            allTiles.stream().forEach(
                    tile -> tile.matchSide(currentTile));
        }
    }

    /**
     * locate first tile then reposition rest of the tiles base on this one
     */
    public static void repositionAllTiles() {

        LinkedList<Tile> listToMerge = new LinkedList<>();
        Set<Tile> alreadyMerged = new HashSet<>();
        listToMerge.offer(tiles.values().stream().findFirst().get());

        // propagate while still anything not repositioned
        while (listToMerge.size() > 0) {

            var currentTile = listToMerge.pop();
            alreadyMerged.add(currentTile);
            currentTile.repositionNeighbors();
            currentTile.neighbor.values().stream().filter(
                    tile -> !alreadyMerged.contains(tile)
            ).forEach(
                    tile -> listToMerge.add(tile)
            );
        }

    }

    public static void buildSides() {
        tiles.values().forEach(
                tile -> tile.buildTileSides()
        );
    }

    public static Tile buildImage() {
        var topLeftTile = tiles.values().stream().filter(
                tile -> tile.neighbor.get(0) == null && tile.neighbor.get(3) == null
        ).findFirst().get();

        topLeftTile.x = 0;
        topLeftTile.y = 0;

        topLeftTile.orderNeighbor();

        List<String> graph = topLeftTile.constructGraph();

        Tile graphTile = new Tile(-1);
        graphTile.lines = graph;
        return graphTile;
    }

    public static void processInput(java.util.Iterator<String> input) {

        Tile tile = new Tile(-1);
        var lines = new ArrayList<String>();

        while (input.hasNext()) {
            var line = input.next();
            if (line.startsWith("Tile")) {
                tile = new Tile(Integer.parseInt(line.substring(line.indexOf('e') + 2, line.indexOf(':'))));
                lines = new ArrayList<>();
                Tile.tiles.put(tile.id, tile);
            } else if (line.trim().isEmpty()) {
                tile.lines = lines;
            } else {
                lines.add(line);
            }
        }
        tile.lines = lines;
    }

    private List<String> constructGraph() {
        List<String> graphLines = new ArrayList<>();

        if (neighbor.get(3) == null) {
            graphLines.addAll(IntStream.range(1, lines.size() - 1).mapToObj(
                    i -> this.getLine(i)
            ).collect(Collectors.toList()));
        }

        if (neighbor.get(2) != null) {
            graphLines.addAll(neighbor.get(2).constructGraph());
        }
        return graphLines;
    }

    private String getLine(int i) {
        if (neighbor.get(1) != null) {
            return lines.get(i).substring(1, lines.size() - 1) + neighbor.get(1).getLine(i);
        } else {
            return lines.get(i).substring(1, lines.size() - 1);
        }
    }

    private void orderNeighbor() {

        // order right side neighbors
        var rightNeighbor = this.neighbor.get(1);
        if (rightNeighbor != null) {
            rightNeighbor.x = this.x + 1;
            rightNeighbor.y = this.y;
            rightNeighbor.orderNeighbor();
        }

        // propagate to bottom if no left
        if (this.neighbor.get(3) == null) {
            var bottomNeighbor = this.neighbor.get(2);
            if (bottomNeighbor != null) {
                bottomNeighbor.x = this.x;
                bottomNeighbor.y = this.y + 1;
                bottomNeighbor.orderNeighbor();
            }
        }

    }

    private void repositionNeighbors() {
        // reposition neighbors: 0 match 2, 1 match 3, 2 match 0, 3 match 1

        // side 0(top): the matching side needs to be at bottom
        if (neighbor.get(0) != null) {
            neighbor.get(0).rotateToMatchPosition(sides.get(0), 2);
        }

        // side 1(right): the matching neigbhor side needs to be at left
        if (neighbor.get(1) != null) {
            neighbor.get(1).rotateToMatchPosition(sides.get(1), 3);
        }

        // side 2(bottom): the matching neigbhor side needs to be at top
        if (neighbor.get(2) != null) {
            neighbor.get(2).rotateToMatchPosition(sides.get(2), 0);
        }

        // side 3(left): the matching neigbhor side needs to be at right
        if (neighbor.get(3) != null) {
            neighbor.get(3).rotateToMatchPosition(sides.get(3), 1);
        }
    }

    private void rotateToMatchPosition(String targetSide, int targetPosition) {
        var currentMatchIndex = sides.indexOf(
                sides.stream().filter(
                        side -> matchSides(side, targetSide)
                ).findFirst().get()
        );

        rotate(currentMatchIndex, targetPosition, targetSide);
    }

    /**
     * this can be improved, but current implementation would just rotate left until side in position
     * Then flip if needed to reposition to match the neighbor requests this
    * */
    private void rotate(int currentMatchIndex, int targetIndex, String targetResult) {
        while (currentMatchIndex != targetIndex) {
            rotateLeft();
            currentMatchIndex = ++currentMatchIndex % 4;
        }

        if (!targetResult.equals(sides.get(targetIndex))) {
            if (targetIndex == 0 || targetIndex == 2) {
                sideFlip();
            } else {
                horizontalFlip();
            }
        }
    }

    private void horizontalFlip() {
        lines = IntStream.range(0, lines.size()).mapToObj(
                i -> lines.get(lines.size() - i - 1)
        ).collect(Collectors.toList());

        this.buildTileSides();

        var topNeighbor = this.neighbor.remove(2);
        var bottomNeighbor = this.neighbor.remove(0);
        if (topNeighbor != null) {
            this.neighbor.put(0, topNeighbor);
        }
        if (bottomNeighbor != null) {
            this.neighbor.put(2, bottomNeighbor);
        }
    }

    private void sideFlip() {
        lines = lines.stream().map(this::reverse).collect(Collectors.toList());
        this.buildTileSides();
        var leftNeighbor = this.neighbor.remove(1);
        var rightNeighbor = this.neighbor.remove(3);
        if (leftNeighbor != null) {
            this.neighbor.put(3, leftNeighbor);
        }
        if (rightNeighbor != null) {
            this.neighbor.put(1, rightNeighbor);
        }
    }

    private void rotateLeft() {

        lines = IntStream.range(0, lines.size()).mapToObj(
                i -> {
                    StringBuilder sb = new StringBuilder();
                    for (int j = lines.size() - 1; j >= 0; j--) {
                        sb.append(lines.get(j).charAt(i));
                    }
                    return sb.toString();
                }
        ).collect(Collectors.toList());

        this.buildTileSides();

        var rotatedNeighbor = new HashMap<Integer, Tile>();
        this.neighbor.entrySet().stream().forEach(
                entry ->
                        rotatedNeighbor.put((entry.getKey() + 1) % 4, entry.getValue())
        );
        this.neighbor = rotatedNeighbor;
    }

    private boolean matchSides(String side, String anotherSide) {
        return side.equals(anotherSide) || reverse(side).equals(anotherSide);
    }

    /**
     * sides are match with neighbors, during this process
     * neighbor 0: top neighbor
     * neighbor 1: right side neighbor
     * neighbor 2: bottom side neighbor
     * neighbor 3: left side neighbor
     */
    private void matchSide(Tile anotherTile) {
        sides.stream().forEach(
                thisSide ->
                        anotherTile.sides.stream().forEach(
                                anotherSide -> {
                                    if (matchSides(thisSide, anotherSide)) {
                                        this.neighbor.put(sides.indexOf(thisSide), anotherTile);
                                        anotherTile.neighbor.put(anotherTile.sides.indexOf(anotherSide), this);
                                    }
                                }
                        )
        );
    }

    private String reverse(String s) {
        StringBuilder sb = new StringBuilder(s);
        return sb.reverse().toString();
    }

    /**
     * 4 sides are added for a tile:
     * side 0: top one
     * side 1: right one
     * side 2: bottom one
     * side 3: left one
     */
    public void buildTileSides() {
        sides = new ArrayList<>();
        sides.add(lines.get(0));
        sides.add(
                lines.stream().map(
                        line -> line.substring(line.length() - 1)
                ).collect(Collectors.joining())
        );
        sides.add(lines.get(lines.size() - 1));
        sides.add(
                lines.stream().map(
                        line -> line.substring(0, 1)
                ).collect(Collectors.joining())
        );
    }

    public void findMonster(Map<Integer, List<Integer>> monsterChecks, int monsterLength, int monsterHeight) {

        findMonsterWithoutRotate(monsterChecks, monsterLength, monsterHeight);

        rotateLeft();

        findMonsterWithoutRotate(monsterChecks, monsterLength, monsterHeight);

        sideFlip();

        findMonsterWithoutRotate(monsterChecks, monsterLength, monsterHeight);

        horizontalFlip();

        findMonsterWithoutRotate(monsterChecks, monsterLength, monsterHeight);

        rotateLeft();

        findMonsterWithoutRotate(monsterChecks, monsterLength, monsterHeight);

        sideFlip();

        findMonsterWithoutRotate(monsterChecks, monsterLength, monsterHeight);

        horizontalFlip();

        findMonsterWithoutRotate(monsterChecks, monsterLength, monsterHeight);

        rotateLeft();

        findMonsterWithoutRotate(monsterChecks, monsterLength, monsterHeight);

        sideFlip();

        findMonsterWithoutRotate(monsterChecks, monsterLength, monsterHeight);

        horizontalFlip();

        findMonsterWithoutRotate(monsterChecks, monsterLength, monsterHeight);

        var result = this.lines.stream().mapToInt(
                line -> {
                    int count = 0;
                    for (char ch : line.toCharArray()) {
                        if (ch == '#') {
                            count++;
                        }
                    }
                    return count;
                }
        ).sum();

        System.out.println(result);
    }

    private void findMonsterWithoutRotate(Map<Integer, List<Integer>> monsterChecks, int monsterLength, int monsterHeight) {
        IntStream.range(0, lines.size() - monsterHeight).forEach(
                y ->
                        IntStream.range(0, lines.size() - monsterLength).forEach(
                                x -> markMonster(monsterChecks, x, y)
                        )

        );
    }

    private void markMonster(Map<Integer, List<Integer>> monsterChecks, int x, int y) {
        var found = monsterChecks.entrySet().stream().allMatch(
                entry -> {
                    var yVariance = entry.getKey();
                    return entry.getValue().stream().allMatch(
                            xVariance -> lines.get(y + yVariance).charAt(x + xVariance) == '#'
                    );
                }
        );

        if (found) {
            monsterChecks.entrySet().stream().forEach(
                    entry -> {
                        var yVariance = entry.getKey();
                        entry.getValue().stream().forEach(
                                xVariance -> {
                                    var line = lines.get(y + yVariance);
                                    line = line.substring(0, x + xVariance) + "0" + line.substring(x + xVariance + 1);
                                    lines.remove(y + yVariance);
                                    lines.add(y + yVariance, line);
                                }
                        );
                    });
        }
    }
}
