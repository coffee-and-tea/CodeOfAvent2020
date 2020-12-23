import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day21 {

    static List<String> foods = new ArrayList<>();
    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsStream("day21-puzzle.txt");

        input.sequential().forEach(
                line -> processAllergen(line)
        );

        var allCandidates = Allergen.getAllCandidates();

        var result = foods.stream().filter(
                food -> !allCandidates.contains(food)
        ).count();

        System.out.println(result);

        var dangerousList = Allergen.buildList();
        System.out.println(dangerousList);
    }

    private static void processAllergen(String line) {
        var candidates = Arrays.stream(line.substring(0, line.indexOf('(') - 1).split(" ")).collect(Collectors.toSet());

        foods.addAll(candidates);

        var allergens = line.substring(line.indexOf('(') + 1, line.indexOf(')')).replace("contains ", "").split(", ");

        Arrays.stream(allergens).forEach(
                allergen -> Allergen.updateAllergen(allergen, candidates)
        );
    }
}

class Allergen {

    static HashMap<String, Allergen> allergens = new HashMap<>();

    String name;
    Set<String> candidates;

    public static void updateAllergen(String allergenName, Set<String> candidates) {
        var allergen = allergens.computeIfAbsent(allergenName,
                name -> new Allergen(name));

        allergen.updateCandidates(candidates);
    }

    public static Set<String> getAllCandidates() {
        return allergens.values().stream().map(
                allergen -> allergen.candidates
        ).flatMap(s -> s.stream()).collect(Collectors.toSet());
    }

    public static String buildList() {
        return allergens.entrySet().stream().sorted(
                (e1, e2) ->
                    e1.getKey().compareTo(e2.getKey())

        ).map( entry -> entry.getValue().candidates.stream().findFirst().get() ).collect(Collectors.joining(","));
    }

    private void updateCandidates(Set<String> candidates) {

        if(this.candidates.isEmpty()) {
            this.candidates = candidates.stream().collect(Collectors.toSet());
        } else {
            this.candidates.retainAll(candidates);
        }

        if(this.candidates.size() == 1 ) {
            Allergen.removeCandidate(this, this.candidates);
        }

    }

    private static void removeCandidate(Allergen allergen, Set<String> candidates) {
        allergens.values().stream().forEach(
                al -> {
                    if( !al.equals(allergen)) {
                        al.remove(candidates);
                    }
                }
        );
    }

    private void remove(Set<String> candidates) {
        var beforeSize = this.candidates.size();

        this.candidates.removeAll(candidates);

        if(beforeSize != 1 && this.candidates.size() == 1) {
            this.updateCandidates(this.candidates);
        }
    }

    public Allergen(String name) {
        this.name = name;
        this.candidates = new HashSet<>();
    }
}
