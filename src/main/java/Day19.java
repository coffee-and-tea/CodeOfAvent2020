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

public class Day19 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsStream("day19-puzzle.txt");

        var result = input.mapToInt(
                line -> {
                    if (line.contains(":")) {
                        Rule.processRule(line);
                    } else if (line.length() > 0) {
                        if (Rule.matchLoopingZero(line)) {
                            return 1;
                        }
                    }
                    return 0;
                }
        ).sum();

        System.out.println(result);

        input = InputFileReader.readInputAsStream("day19-puzzle.txt");

        createLoop();

        result = input.mapToInt(
                line -> {
                    if (!line.contains(":") && line.length() > 0) {
                        if (Rule.matchLoopingZero(line)) {
                            return 1;
                        }
                    }
                    return 0;
                }
        ).sum();

        System.out.println(result);

    }

    private static void createLoop() {
        var rule8change = new ArrayList<Rule>();
        rule8change.add(Rule.allRules.get("42"));
        rule8change.add(Rule.allRules.get("8"));
        Rule.allRules.get("8").details.add(rule8change);

        var rule11change = new ArrayList<Rule>();
        rule11change.add(Rule.allRules.get("42"));
        rule11change.add(Rule.allRules.get("11"));
        rule11change.add(Rule.allRules.get("31"));
        Rule.allRules.get("11").details.add(rule11change);
    }

}

class SearchOption {

    List<Rule> rules;

    String searchTarget;

    public SearchOption(List<Rule> rules, String searchTarget) {
        this.rules = rules;
        this.searchTarget = searchTarget;
    }
}

class Rule {

    static Map<String, Rule> allRules = new HashMap<>();

    String id;
    Character ch;
    Set<List<Rule>> details = new HashSet<>();

    public Rule(String id) {
        this.id = id;
    }

    public static boolean matchLooping(List<Rule> rules, String input) {

        var options = new LinkedList<SearchOption>();

        options.push(new SearchOption(rules, input));

        while (!options.isEmpty()) {

            var currentSearch = options.pop();

            if(currentSearch.rules.size() == 0 && currentSearch.searchTarget.length() > 0){
                continue;
            }

            if(currentSearch.rules.size() == 0 && currentSearch.searchTarget.length() == 0){
                return true;
            }

            if(currentSearch.rules.size() > currentSearch.searchTarget.length()) {
                continue;
            }

            var searchHead = currentSearch.rules.remove(0);

            if(searchHead.ch != null ) {
                if(searchHead.ch.equals(currentSearch.searchTarget.charAt(0))) {
                    if(currentSearch.rules.size() == 0 && currentSearch.searchTarget.length() == 1) {
                        return true;
                    }
                    options.push(new SearchOption(currentSearch.rules, currentSearch.searchTarget.substring(1)));
                } else {
                    continue;
                }
            } else {

                // bug modify details here
                var opts = searchHead.details.stream()
                        .map( l -> l.stream().collect(Collectors.toList()))
                        .collect(Collectors.toList());
                opts.stream().forEach(
                        headRules -> {
                            headRules.addAll(currentSearch.rules);
                            var ruleString = headRules.stream().map(
                                    rule -> rule.id
                            ).collect(Collectors.toList());
                            options.push(new SearchOption(headRules, currentSearch.searchTarget));
                        }
                );
            }

        }

        return false;
    }

    public static void processRule(String line) {

        var defineRuleId = line.substring(0, line.indexOf(':'));
        var defineRule = allRules.computeIfAbsent(defineRuleId, (id) -> new Rule(id));
        if (line.contains("\"")) {
            defineRule.ch = line.charAt(line.indexOf('"') + 1);
        } else {
            var subRules = line.substring(line.indexOf(':') + 2).split(" ");
            ArrayList<Rule> ruleLinkedList = new ArrayList<>();
            for (String ruleId : subRules) {
                if (ruleId.equals("|")) {
                    defineRule.details.add(ruleLinkedList);
                    ruleLinkedList = new ArrayList<>();
                } else {
                    var subRule = allRules.computeIfAbsent(ruleId, (id) -> new Rule(id));
                    ruleLinkedList.add(subRule);
                }
            }
            defineRule.details.add(ruleLinkedList);
        }
    }

    public static boolean matchLoopingZero(String line) {
        var initialList = new ArrayList<Rule>();
        initialList.add(allRules.get("0"));
        return Rule.matchLooping(initialList, line);
    }
}
