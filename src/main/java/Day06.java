import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day06 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsStream("day06-puzzle.txt");

        var groupAnswers = input.reduce(
                (s1, s2) -> {
                    if (!s2.isEmpty()) {
                        return s1 +" "+ s2;
                    } else {
                        return s1 + "\n";
                    }
                }
        );

        var result = groupAnswers.get().lines().mapToLong(
                groupAnswer -> groupAnswer.chars().distinct().count() - 1
        ).sum();

        System.out.println("Sum of counts distinct answers: " + result);

        result = groupAnswers.get().lines().mapToLong(
                groupAnswer -> {
                    var allQuestions = Collections.synchronizedSet("abcdefghijklmnopqrstuvwxyz".chars().mapToObj(Integer::new).collect(Collectors.toSet()));

                    var individualAnswers = groupAnswer.split(" ");
                    Arrays.stream(individualAnswers).filter(Predicate.not(String::isEmpty)).forEach(
                            individualAnswer -> allQuestions.retainAll(individualAnswer.chars().mapToObj(Integer::new).collect(Collectors.toSet()))
                    );
                    return allQuestions.size();
                }
        ).sum();

        System.out.println("sum of counts all yes answers: " + result);
    }
}
