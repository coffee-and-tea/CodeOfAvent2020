import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day08 {

    public static void main(String[] args) throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        var input = InputFileReader.readInputAsList("day08-puzzle.txt");

        var program = new Program(input);

        var accumulator = program.loopDetectionExecute();

        System.out.println("Accumulator value before loop: " + accumulator);

        var programWithState = new ProgramWithState(input);

        accumulator = programWithState.terminateSeekingExecution();

        System.out.println("Solution for termination: " + accumulator);

    }
}

class Program {

    protected List<String> codes;
    protected int accumulator;
    protected int executionLine;
    protected Set<Integer> executedLines;
    protected boolean altered;

    public Program(List<String> codes, int accumulator, int executionLine, Set<Integer> executedLines) {
        this.codes = codes;
        this.accumulator = accumulator;
        this.executionLine = executionLine;
        this.executedLines = executedLines;
    }

    public Program(List<String> codes) {
        this.codes = codes;
        accumulator = 0;
        executionLine = 0;
        executedLines = new HashSet<>();
    }

    public int loopDetectionExecute() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        while (executionLine >= 0 && executionLine < codes.size()) {
            if (executedLines.contains(executionLine)) {
                break;
            } else {
                executedLines.add(executionLine);
            }
            execute();
        }
        return accumulator;
    }

    protected void execute() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var instruction = codes.get(executionLine).split(" ");
        var ops = instruction[0];
        var operand = Integer.parseInt(instruction[1]);

        var method = this.getClass().getMethod(ops, int.class);
        method.invoke(this, operand);
    }

    public void nop(int operand) {
        executionLine++;
    }

    public void acc(int operand) {
        accumulator += operand;
        executionLine++;
    }

    public void jmp(int operand) {
        executionLine += operand;
    }
}


class ProgramWithState extends Program {

    private List<Program> programStates;

    public ProgramWithState(List<String> codes) {
        super(codes);
        programStates = new ArrayList<>();
        programStates.add(this);
    }

    public int terminateSeekingExecution() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        while (programStates.size() > 0) {
            loadProgram();
            while (executionLine >= 0 && executionLine < codes.size()) {
                if (executedLines.contains(executionLine)) {
                    break;
                } else {
                    executedLines.add(executionLine);
                }
                execute();
            }

            if (executionLine == codes.size())
                return accumulator;
        }
        return -1;
    }

    @Override
    public void nop(int operand) {
        if (!altered) {
            // run current instruction without alter and memorize
            executionLine++;
            memorizeExecution();

            // restore execution and run with altered solution
            executionLine--;
            this.altered = true;
            jmp(operand);
        } else {
            executionLine++;
        }
    }

    @Override
    public void jmp(int operand) {
        if (!altered) {
            // run current instruction without alter and memorize
            executionLine += operand;
            memorizeExecution();

            // restore execution and run with altered solution
            executionLine -= operand;
            this.altered = true;
            memorizeExecution();
            nop(operand);
        } else {
            executionLine += operand;
        }
    }

    private void memorizeExecution() {
        this.programStates.add(new Program(this.codes, this.accumulator, this.executionLine, new HashSet<>(this.executedLines)));
    }

    private void loadProgram() {
        var programToRun = this.programStates.remove(0);
        this.executionLine = programToRun.executionLine;
        this.accumulator = programToRun.accumulator;
        this.executedLines = programToRun.executedLines;
        this.altered = programToRun.altered;
    }
}