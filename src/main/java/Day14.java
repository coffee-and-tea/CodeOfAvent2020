import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;

public class Day14 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsIterator("day14-puzzle.txt");

        HashMap<Long, Long> memory = processValueMask(input);

        System.out.println(memory.values().stream().mapToLong(Long::longValue).sum());

        processAddressMask();
    }

    private static void processAddressMask() throws URISyntaxException, IOException {
        HashMap<Long, Long> memory;
        java.util.Iterator<String> input;
        input = InputFileReader.readInputAsIterator("day14-puzzle.txt");

        String mask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

        memory = new HashMap<Long, Long>();

        while (input.hasNext()) {
            mask = processMemLine(input.next(), mask, memory);
        }
        System.out.println(memory.values().stream().mapToLong(Long::longValue).sum());
    }

    private static HashMap<Long, Long> processValueMask(java.util.Iterator<String> input) {
        var mask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

        var memory = new HashMap<Long, Long>();

        while (input.hasNext()) {
           mask = processLine(input.next(), mask, memory);
        }
        return memory;
    }

    private static String processMemLine(String line, String mask, HashMap<Long, Long> memory) {
        if(line.startsWith("mask")){
            mask = line.substring(7);
        } else {
            var memPos = Integer.parseInt(line.substring(line.indexOf('[') + 1, line.indexOf(']')));
            var value = Long.parseLong(line.substring(line.indexOf('=')+2));
            maskAddress(memPos, mask, value, memory);
        }
        return mask;
    }

    private static void maskAddress(int memPos, String mask, long value, HashMap<Long, Long> memory) {
        var input = Long.toString(memPos, 2);

        var charArray = mask.toCharArray();

        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == '0') {
                if (35 - i < input.length()) {
                    charArray[i] = input.charAt(input.length() - 36 + i);
                } else {
                    charArray[i] = '0';
                }
            }
        }

        var floatMem = String.valueOf(charArray);

        var floatMemSet = new LinkedList<String>();

        floatMemSet.offer(floatMem);

        while (!floatMemSet.isEmpty()) {
            var mem = floatMemSet.pop();
            if (mem.contains("X")) {
                floatMemSet.offer(mem.replaceFirst("X", "0"));
                floatMemSet.offer(mem.replaceFirst("X", "1"));
            } else {
                memory.put(Long.parseLong(mem, 2), value);
            }
        }

    }

    private static String processLine(String line, String mask, HashMap<Long, Long> memory) {
        if(line.startsWith("mask")){
            mask = line.substring(7);
        } else {
            var memPos = Long.parseLong(line.substring(line.indexOf('[') + 1, line.indexOf(']')));
            var value = mask(Long.parseLong(line.substring(line.indexOf('=')+2)), mask);
            memory.put(memPos, value);
        }
        return mask;
    }

    private static long mask(long value, String mask) {
        var input = Long.toString(value, 2);

        var charArray = mask.toCharArray();

        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == 'X') {
                if (35 - i < input.length()) {
                    charArray[i] = input.charAt(input.length() - 36 + i);
                } else {
                    charArray[i] = '0';
                }
            }
        }
        return Long.parseLong(String.valueOf(charArray), 2);
    }
}
