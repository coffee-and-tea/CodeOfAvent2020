import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day07 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsStream("day07-puzzle.txt");

        BagRules.initialize(input);

        var result = BagRules.insideBags("shiny gold");

        System.out.println("Shiny gold bag count: " + (result - 1));

        result = BagRules.countInside("shiny gold");

        System.out.println("Inside shiny gold bag: " + result);
    }
}

class BagRules {
    private final static String bagRulePatternString = "^(\\w+\\s\\w+)\\sbags\\scontain(((\\s(\\d+)\\s(\\w+\\s\\w+)\\s(bag|bags)(\\,?))+)|(\\sno other bags))\\.";
    private final static String bagInclusionString = "\\s(\\d+)\\s(\\w+\\s\\w+)\\s(bag|bags)(\\,?)";

    private final static Map<String, Map<String, Integer>> bagRules = new HashMap<>();

    private static ConcurrentMap<String, Boolean> includes = new ConcurrentHashMap<>();


    public static long countInside(String color) {
        return bagRules.get(color).isEmpty() ? 0 : bagRules.get(color).entrySet().stream().mapToLong(
                bagRule ->
                        bagRule.getValue() * (BagRules.countInside(bagRule.getKey()) + 1)
        ).sum();
    }

    private static boolean includedInBag(String color, String includesColor) {

        if (includes.containsKey(color)) {
            return includes.get(color);
        } else {
            if (color.equals(includesColor)) {
                includes.put(color, true);
                return true;
            } else {
                var result = bagRules.get(color).keySet().stream().anyMatch(
                        bag -> BagRules.includedInBag(bag, includesColor));
                includes.put(color, result);
                return result;
            }
        }
    }

    public static long insideBags(String color) {
        includes = new ConcurrentHashMap<>();
        return bagRules.keySet().stream().filter(
                bag ->
                        BagRules.includedInBag(bag, color)
        ).count();
    }


    public static void initialize(Stream<String> input) {
        Pattern bagRulePattern = Pattern.compile(bagRulePatternString);
        Pattern bagInclusionPatter = Pattern.compile(bagInclusionString);
        input.forEach(
                line -> {
                    Matcher bagRuleMatcher = bagRulePattern.matcher(line);
                    if (bagRuleMatcher.find()) {
                        var color = bagRuleMatcher.group(1);
                        var insideBagRules = bagRuleMatcher.group(2);
                        var insideBags = new HashMap<String, Integer>();
                        if (!insideBagRules.contains("no other bags")) {
                            var rules = insideBagRules.split(",");
                            Arrays.stream(rules).forEach(
                                    rule -> {
                                        var ruleMatcher = bagInclusionPatter.matcher(rule);
                                        if (ruleMatcher.find()) {
                                            var nbrOfBags = Integer.valueOf(ruleMatcher.group(1));
                                            var bagInside = ruleMatcher.group(2);
                                            insideBags.put(bagInside, nbrOfBags);
                                        } else {
                                            System.out.println("Not supported rule: " + rule);
                                        }

                                    }
                            );
                        }
                        bagRules.put(color, insideBags);
                    } else {
                        System.out.println(line);
                    }
                }
        );
    }

}