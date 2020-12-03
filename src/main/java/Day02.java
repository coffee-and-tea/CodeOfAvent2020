import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Day02 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        var checkList = new ArrayList<PolicyCheck>();

        InputFileReader.readInputAsStream("day02-puzzle.txt").forEach(
                line -> {
                    var params = line.split(" ");
                    checkList.add(
                            new PolicyCheck(
                                    params[1].charAt(0),
                                    Integer.parseInt(params[0].substring(0, params[0].indexOf('-'))),
                                    Integer.parseInt(params[0].substring(params[0].indexOf('-') + 1)),
                                    params[2]
                            )
                    );
                }
        );

        var validPasswordCount = checkList.stream().filter(PolicyCheck::validOccurrenceBased).count();

        System.out.println("Valid passwords base on occurrence: " + validPasswordCount);

        validPasswordCount = checkList.stream().filter(PolicyCheck::validPositionBased).count();

        System.out.println("Valid passwords base on position: " + validPasswordCount);
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
                    c -> (int) key == c
            ).count();
            return count >= minOccurrence && count <= maxOccurrence;
        }

        public boolean validPositionBased() {
            return password.charAt(minOccurrence - 1) == key ^ password.charAt(maxOccurrence - 1) == key;
        }

    }
}
