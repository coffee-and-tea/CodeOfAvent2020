import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day17 {

    private static Space3D cube = new Space3D();

    private static Space4D d4 = new Space4D();

    private static int minW = 0;
    private static int maxW = 1;
    private static int minZ = 0;
    private static int maxZ = 1;
    private static int minY = 0;
    private static int maxY = 1;
    private static int minX = 0;
    private static int maxX = 1;

    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsList("day17-puzzle.txt");

        init3DCube(input);

        propagate3D(6);

        var count = cube.surfaces.values().stream()
                .flatMap(surface -> surface.lines.values().stream())
                .flatMap(line -> line.points.values().stream())
                .filter(
                        c -> c.equals('#')
                ).count();

        System.out.println(count);

        init4DCube(input);

        propagate4D(6);

        count = d4.spaces.values().stream().flatMap(
                space -> space.surfaces.values().stream())
                .flatMap(surface -> surface.lines.values().stream())
                .flatMap(line -> line.points.values().stream())
                .filter(
                        c -> c.equals('#')
                ).count();

        System.out.println(count);
    }


    private static void init4DCube(List<String> input) {
        maxY = input.size();
        maxX = input.get(0).length();
        var z = 0;
        var w = 0;

        init3DCube(input);

        d4.spaces.put(w, cube);
    }

    private static void init3DCube(List<String> input) {
        cube = new Space3D();
        cube.min = 0;
        cube.max = 1;

        maxY = input.size();
        maxX = input.get(0).length();
        var z = 0;
        IntStream.range(0, maxY).forEach(
                y -> {
                    IntStream.range(0, maxX).forEach(
                            x -> {
                                addIn4D(input.get(y).charAt(x), z, y, x, cube);
                            }
                    );
                }
        );
    }

    private static void addIn4D(Character status, int w, int z, int y, int x, Space4D d4) {
        var line = d4.spaces.computeIfAbsent(w,
                (w1) -> new Space3D()
        ).surfaces.computeIfAbsent(z,
                (z1) -> new Surface()
        ).lines.computeIfAbsent(y, (y1) -> new Line());
        line.points.put(x, status);
    }



    private static void addIn4D(Character status, int z, int y, int x,
                                Space3D cube) {
        var line =
                cube.surfaces.computeIfAbsent(z,
                        (z1) -> new Surface()
                ).lines.computeIfAbsent(y, (y1) -> new Line());
        line.points.put(x, status);
    }

    private static void propagate4D(int cycle) {
        while (cycle > 0) {
            cycle--;
            propagate4D();
        }
    }


    private static void propagate3D(int cycle) {
        while (cycle > 0) {
            cycle--;
            propagate3D();
        }
    }

    private static void propagate4D() {
        minW--;
        maxW++;
        minZ--;
        maxZ++;
        minY--;
        maxY++;
        minX--;
        maxX++;

        Space4D next4D = new Space4D();

        IntStream.range(minW, maxW).forEach(
                w ->
                    IntStream.range(minZ, maxZ).forEach(
                            z ->
                                IntStream.range(minY, maxY).forEach(
                                        y ->
                                            IntStream.range(minX, maxX).forEach(
                                                    x -> getNextStatus(w, z, y, x, next4D)
                                            )
                                )
                    )
        );

        d4 = next4D;
    }


    private static void propagate3D() {
        minZ--;
        maxZ++;
        minY--;
        maxY++;
        minX--;
        maxX++;

        Space3D nextCube
                = new Space3D();

        IntStream.range(minZ, maxZ).forEach(
                z -> {
                    IntStream.range(minY, maxY).forEach(
                            y -> {
                                IntStream.range(minX, maxX).forEach(
                                        x -> getNextStatus(z, y, x, nextCube)
                                );
                            }
                    );
                }
        );

        cube = nextCube;
//        printCube();
    }

    private static void printCube() {
        IntStream.range(minZ, maxZ).forEach(
                z -> {
                    System.out.println("z=" + z);
                    IntStream.range(minY, maxY).forEach(
                            y -> {
                                IntStream.range(minX, maxX).forEach(
                                        x ->
                                                System.out.print(cube.surfaces.get(z).lines.get(y).points.get(x))
                                );
                                System.out.println();
                            }
                    );
                    System.out.println();
                }
        );
    }

    private static void getNextStatus(int w, int z, int y, int x, Space4D next4D) {

        var currentStatus = getCurrentStatus(w, z, y, x);

        var activeCount = getCurrentNeighborStatus(w, z, y, x).filter(
                c -> c.equals('#')
        ).count();
        char nextStatus = calculateStatus(currentStatus, activeCount);

        addIn4D(nextStatus, w, z, y, x, next4D);
    }

    private static char calculateStatus(Character currentStatus, long activeCount) {
        return currentStatus.equals('#') ?
                ((activeCount == 4 || activeCount == 3) ? '#' : '.')
                :
                activeCount == 3 ? '#' : '.';
    }


    private static void getNextStatus(int z, int y, int x,
                                      Space3D nextCube) {
        var currentStatus = getCurrentStatus(z, y, x, cube);
        var activeCount = getCurrentNeighborStatus(z, y, x).filter(
                c -> c.equals('#')
        ).count();
        char nextStatus = calculateStatus(currentStatus, activeCount);

        addIn4D(nextStatus, z, y, x, nextCube);
    }


    private static Stream<Character> getCurrentNeighborStatus(int w, int z, int y, int x) {
        return IntStream.range(w - 1, w + 2).mapToObj(
                wIndex ->
                        IntStream.range(z - 1, z + 2).mapToObj(
                                zIndex ->
                                        IntStream.range(y - 1, y + 2).mapToObj(
                                                yIndex ->
                                                        IntStream.range(x - 1, x + 2).mapToObj(
                                                                xIndex ->
                                                                        getCurrentStatus(wIndex, zIndex, yIndex, xIndex)
                                                        )
                                        ).flatMap(s -> s)
                        ).flatMap(s -> s)
        ).flatMap(s -> s);
    }

    private static Stream<Character> getCurrentNeighborStatus(int z, int y, int x) {

        return IntStream.range(z - 1, z + 2).mapToObj(
                zIndex ->
                        IntStream.range(y - 1, y + 2).mapToObj(
                                yIndex ->
                                        IntStream.range(x - 1, x + 2).mapToObj(
                                                xIndex ->
                                                        getCurrentStatus(zIndex, yIndex, xIndex, cube)
                                        )

                        ).flatMap(s -> s)
        ).flatMap(s -> s);
    }

    private static Character getCurrentStatus(int w, int z, int y, int x) {
        if (!d4.spaces.containsKey(w)) {
            return '.';
        }
        return getCurrentStatus(z, y, x, d4.spaces.get(w));
    }

    private static Character getCurrentStatus(int z, int y, int x, Space3D cube) {
        if (!cube.surfaces.containsKey(z)) {
            return '.';
        } else if (!cube.surfaces.get(z).lines.containsKey(y)) {
            return '.';
        }
        return cube.surfaces.get(z).lines.get(y).points.getOrDefault(x, '.');
    }
}

class Space4D {
    int min;
    int max;
    HashMap<Integer, Space3D> spaces = new HashMap<>();
}

class Space3D {
    int min;
    int max;
    HashMap<Integer, Surface> surfaces = new HashMap<>();
}

class Surface {
    int min;
    int max;
    HashMap<Integer, Line> lines = new HashMap<>();
}

class Line {
    int min;
    int max;
    HashMap<Integer, Character> points = new HashMap<>();
}
