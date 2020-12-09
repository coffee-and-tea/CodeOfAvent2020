import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class Day09 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        var input = InputFileReader.readInputAsIterator("day09-puzzle.txt");

        long result = sumNotMatch(input, 25);

        System.out.println("Not matching previous 25: " + result);

        var streamInput = InputFileReader.readInputAsIterator("day09-puzzle.txt");

        var resultList= sumToResult(streamInput, result);

        resultList.sort(Comparator.naturalOrder());
        System.out.println("Min and Max sum result: " + (resultList.getFirst() + resultList.getLast()));
    }

    private static LinkedList<Long> sumToResult(Iterator<String> input, long target) throws URISyntaxException, IOException {

        var linkedList = new LinkedList<Long>();
        long sum = 0;

        while(input.hasNext()){
            var entry = Long.parseLong(input.next());
            linkedList.offer(entry);
            sum += entry;
            while (sum > target) {
                var removed =  linkedList.poll();
                sum -= removed;
            }
            if (sum == target) {
                return linkedList;
            }
        }
        return  null;
    }

    private static long sumNotMatch(java.util.Iterator<String> input, int listSize) {
        var list25 = new LinkedList<Long>();
        var result = Long.MAX_VALUE;

        while (input.hasNext()) {
            var nextNbr = Long.parseLong(input.next());

            if (list25.size() < listSize) {
                list25.offer(nextNbr);
            } else {
                if (!valid(list25, nextNbr)) {
                    result = nextNbr;
                    break;
                } else {
                    list25.poll();
                    list25.offer(nextNbr);
                }
            }
        }
        return result;
    }

    private static boolean valid(LinkedList<Long> list25, long nextNbr) {
        var set25 = Set.copyOf(list25);
        return list25.stream().anyMatch(
                l ->
                    set25.contains(nextNbr - l)

        );
    }
}
