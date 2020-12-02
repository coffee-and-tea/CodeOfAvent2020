import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day02 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var checkList = readInput();

        var validPasswordCount = checkList.stream().filter(PolicyCheck::validOccurrenceBased).count();

        System.out.println("Valid passwords base on occurrence: " + validPasswordCount);

        validPasswordCount = checkList.stream().filter(PolicyCheck::validPositionBased).count();

        System.out.println("Valid passwords base on position: " + validPasswordCount);
    }

    private static List<PolicyCheck> readInput() throws URISyntaxException, IOException {

        var resource = Day01.class.getClassLoader().getResource("day02-puzzle.txt");
        var inputFile = Path.of(resource.toURI());

        try (BufferedReader inputReader = Files.newBufferedReader(inputFile)) {

            var checkList = new ArrayList<PolicyCheck>();
            var inputLine = "";
            while ((inputLine = inputReader.readLine()) != null) {
                var input = inputLine.split(" ");
                var policyCheck = new PolicyCheck(
                        input[1].charAt(0),
                        Integer.parseInt(input[0].substring(0, input[0].indexOf('-'))),
                        Integer.parseInt(input[0].substring(input[0].indexOf('-')+1)),
                        input[2]);
                checkList.add(policyCheck);
            }

            return checkList;
        }
    }

    static class PolicyCheck {

        private char key;
        private int minOccurrence;
        private int maxOccurrence;
        private String password;

        public PolicyCheck(char key, int minOccurrence, int maxOccurrence, String password) {
            this.key = key;
            this.minOccurrence = minOccurrence;
            this.maxOccurrence = maxOccurrence;
            this.password = password;
        }

        public boolean validOccurrenceBased() {
            var count = password.chars().filter(
                    c -> {
                        return (int)key == c;
                    }
            ).count();
            return count >= minOccurrence && count <= maxOccurrence;
        }

        public boolean validPositionBased() {
            return password.charAt(minOccurrence-1) == key ^ password.charAt(maxOccurrence-1) == key;
        }

    }
}
