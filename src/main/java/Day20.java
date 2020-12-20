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

        processInput(input);

        Tile.buildSides();

        Tile.buildImage();

        var result = Tile.tiles.values().stream().filter(
                tile -> tile.neighbor.size() < 3
        ).mapToLong(
                tile -> tile.id
        ).reduce(1, (a, b) -> a * b);

        System.out.println(result);

        Tile graph = printImage();

        Map<Integer, List<Integer>> monsterChecks = new HashMap<>();
        monsterChecks.put(0, List.of(18));
        monsterChecks.put(1, List.of(0, 5, 6, 11, 12, 17, 18, 19));
        monsterChecks.put(2, List.of(1, 4, 7, 10, 13, 16));

        var monsterLength = 20;
        var monsterHeight = 3;

        graph.findMonster(monsterChecks, monsterLength, monsterHeight);
    }

    private static Tile printImage() {
        return Tile.order();
    }

    private static void processInput(java.util.Iterator<String> input) {
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

    public static void buildImage() {

        int imageLength = (int) Math.sqrt(tiles.size());

        var allTiles = tiles.values().stream().collect(Collectors.toList());

        while (allTiles.size() > 1) {
            var currentTile = allTiles.remove(0);
            allTiles.stream().forEach(
                    tile -> tile.matchSide(currentTile));
        }

        mergeTiles();
    }

    private static void mergeTiles() {

        LinkedList<Tile> listToMerge = new LinkedList<>();
        Set<Tile> alreadyMerged = new HashSet<>();
        listToMerge.offer(tiles.values().stream().findFirst().get());

        // propagate while still anything not corrected
        while (listToMerge.size() > 0) {

            var currentTile = listToMerge.pop();
            alreadyMerged.add(currentTile);
            currentTile.mergeWithNeighbors();
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

    public static Tile order() {
        var topLeftTile = tiles.values().stream().filter(
                tile -> tile.neighbor.get(0) == null && tile.neighbor.get(3) == null
        ).findFirst().get();

        topLeftTile.x = 0;
        topLeftTile.y = 0;

        topLeftTile.orderNeighbor();

        topLeftTile.printTile();

        List<String> graph = topLeftTile.constructGraph();
        System.out.println("-----------------------------");
        graph.stream().forEach(System.out::println);

        Tile graphTile = new Tile(-1);
        graphTile.lines = graph;
        return graphTile;
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


        if (id == 2617 || id == 2273) {
            lines.stream().forEach(System.out::println);
        }

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
                System.out.println("Order neighbor bottom: " + this.id);
                bottomNeighbor.x = this.x;
                bottomNeighbor.y = this.y + 1;
                bottomNeighbor.orderNeighbor();
            }
        }

    }

    private void mergeWithNeighbors() {
        // merging partner: 0 match 2, 1 match 3, 2 match 0, 3 match 1

        // merge side 0:
        if (neighbor.get(0) != null) {
            topLink(neighbor.get(0));
        }
        if (neighbor.get(1) != null) {
            rightLink(neighbor.get(1));
        }
        if (neighbor.get(2) != null) {
            bottomLink(neighbor.get(2));
        }
        if (neighbor.get(3) != null) {
            leftLink(neighbor.get(3));
        }
    }

    private void leftLink(Tile anotherTile) {
        anotherTile.rotateToRightMatch(sides.get(3));
    }

    private void rotateToRightMatch(String rightSide) {
        var currentMatchIndex = sides.indexOf(
                sides.stream().filter(
                        side -> matchString(side, rightSide)
                ).findFirst().get()
        );
        int targetIndex = 1;

        rotate(currentMatchIndex, targetIndex, rightSide);
    }

    private void bottomLink(Tile anotherTile) {
        anotherTile.rotateToTopMatch(sides.get(2));
    }

    private void rotateToTopMatch(String topSide) {
        var currentMatchIndex = sides.indexOf(
                sides.stream().filter(
                        side -> matchString(side, topSide)
                ).findFirst().get()
        );
        int targetIndex = 0;

        rotate(currentMatchIndex, targetIndex, topSide);
    }

    private void rightLink(Tile anotherTile) {
        anotherTile.rotateToLeftMatch(sides.get(1));
    }

    private void rotateToLeftMatch(String leftSide) {

        var currentMatchIndex = sides.indexOf(
                sides.stream().filter(
                        side -> matchString(side, leftSide)
                ).findFirst().get()
        );
        int targetIndex = 3;

        rotate(currentMatchIndex, targetIndex, leftSide);
    }

    private void topLink(Tile anotherTile) {
        anotherTile.rotateToBottomMatch(sides.get(0));
    }

    private void rotateToBottomMatch(String bottomSide) {

        var currentMatchIndex = sides.indexOf(
                sides.stream().filter(
                        side -> matchString(side, bottomSide)
                ).findFirst().get()
        );
        int targetIndex = 2;

        rotate(currentMatchIndex, targetIndex, bottomSide);
    }

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

    private boolean matchString(String side, String anotherSide) {
        return side.equals(anotherSide) || reverse(side).equals(anotherSide);
    }

    @Override
    public String toString() {
        return "Tile{" +
                "id=" + id +
                "x=" + x +
                "y=" + y +
                '}';
    }

    private void matchSide(Tile anotherTile) {
        sides.stream().forEach(
                thisSide ->
                        anotherTile.sides.stream().forEach(
                                anotherSide -> {
                                    if (matchString(thisSide, anotherSide)) {
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

    public void printTile() {
        if (neighbor.get(3) == null) {
            IntStream.range(0, lines.size()).forEach(
                    i -> this.printLine(i)
            );
        }

        if (neighbor.get(2) != null) {
            neighbor.get(2).printTile();
        }
    }

    private void printLine(int i) {
        System.out.print(lines.get(i));
        if (neighbor.get(1) != null) {
            neighbor.get(1).printLine(i);
        } else {
            System.out.println();
        }
    }

    public void findMonster(Map<Integer, List<Integer>> monsterChecks, int monsterLength, int monsterHeight) {

        findMonsterWithoutRotate(monsterChecks, monsterLength, monsterHeight);

        rotateLeft();

        findMonsterWithoutRotate(monsterChecks, monsterLength, monsterHeight);

        sideFlip();

        findMonsterWithoutRotate(monsterChecks, monsterLength, monsterHeight);

        rotateLeft();

        findMonsterWithoutRotate(monsterChecks, monsterLength, monsterHeight);

        rotateLeft();

        findMonsterWithoutRotate(monsterChecks, monsterLength, monsterHeight);

        System.out.println("=================================");
        this.lines.stream().forEach(System.out::println);
        var result = this.lines.stream().mapToInt(
                line -> {
                    int count = 0;
                    for(char ch : line.toCharArray() ) {
                        if (ch == '#') {
                            count ++;
                        }
                    }
                    return count;
                }
        ).sum();

        System.out.println(result);
    }

    private void findMonsterWithoutRotate(Map<Integer, List<Integer>> monsterChecks, int monsterLength, int monsterHeight) {
        System.out.println("Find monster in grid: " + lines.size() + "," + lines.get(0).length());
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
                            xVariance -> lines.get(y+yVariance).charAt(x+xVariance) == '#'
                    );
                }
        );

        if(found) {
            monsterChecks.entrySet().stream().forEach(
            entry -> {
                var yVariance = entry.getKey();
                entry.getValue().stream().forEach(
                        xVariance -> {
                            var line = lines.get(y+yVariance);
                            line = line.substring(0, x+xVariance) + "0" + line.substring(x+xVariance+1);
                            lines.remove(y+yVariance);
                            lines.add(y+yVariance, line);
                        }
                );
            });
        }
    }
}
