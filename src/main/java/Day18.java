import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


interface Operation {
    long compute(long a, long b);
}

public class Day18 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsStream("day18-puzzle.txt");

        var result = input.mapToLong(
                line -> Expression.parseEqualPriority(line).eval()
        ).sum();

        System.out.println("Equal priority: " + result);

        input = InputFileReader.readInputAsStream("day18-puzzle.txt");

        result = input.mapToLong(line -> Expression.parsePriorityAddition(line).eval()).sum();

        System.out.println("Priority addition: " + result);
    }

}

class Expression {

    static final Operation addition = (a, b) -> a + b;
    static final Operation multiply = (a, b) -> a * b;

    Operation operation;
    Expression left;
    Expression right;
    Long value;

    public Expression(Long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Expression{" +
                "operation=" + operation +
                ", left=" + left +
                ", right=" + right +
                ", value=" + value +
                '}';
    }

    public Expression(Operation operation, Expression left, Expression right) {
        this.operation = operation;
        this.left = left;
        this.right = right;
    }

    static Expression parsePriorityAddition(String input) {
        var priority = new HashMap<Integer, Set<Character>>();
        priority.put(0, Set.of('+'));
        priority.put(1, Set.of('*'));

        return parse(input, priority);
    }

    static Expression parseEqualPriority(String input) {
        var priority = new HashMap<Integer, Set<Character>>();
        priority.put(0, Set.of('+', '*'));

        return parse(input, priority);
    }

    static Expression parse(String input, Map<Integer, Set<Character>> priority) {

        if(input.length() == 1) {
            return new Expression(Long.parseLong(input));
        }

        var lastOperatorIndex = priority.keySet().stream().sorted(Comparator.reverseOrder()).mapToInt(
                p ->
                    lastOperatorIndex(input, priority.get(p))
        ).filter(i -> i != -1).findFirst().orElseGet(() -> -1);

        // input in () remove () then parse again
        if (lastOperatorIndex == -1) {
            return parse(input.substring(1, input.length() - 1), priority);
        } else {
            var operation = input.charAt(lastOperatorIndex) == '*' ? multiply : addition;
            var left = parse(input.substring(0, lastOperatorIndex - 1), priority);
            var right = parse(input.substring(lastOperatorIndex + 2), priority);
            return new Expression(operation, left, right);
        }
    }

    private static int lastOperatorIndex(String input, Set<Character> operators) {
        var parCount = 0;
        for (int i = input.length() - 1; i >= 0; i--) {
            var currentChar = input.charAt(i);
            if (operators.contains(currentChar) && parCount == 0) {
                return i;
            }
            if (currentChar == ')') {
                parCount++;
            }
            if (currentChar == '(') {
                parCount--;
            }
        }
        return -1;
    }

    long eval() {
        if (value != null) {
            return value.longValue();
        } else {
            return operation.compute(left.eval(), right.eval());
        }
    }

}