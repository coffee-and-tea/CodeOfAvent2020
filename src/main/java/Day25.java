import java.io.IOException;
import java.net.URISyntaxException;

public class Day25 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsIntArray("day25-puzzle.txt");

        var cardPublicKey = input[0];
        var doorPublicKey = input[1];

        var cardValue = (1 * 7) % 20201227;

        var cardLoopSize = 1;

        while (cardValue != cardPublicKey) {
            cardValue = (cardValue * 7) % 20201227;
            cardLoopSize++;
        }

        var doorValue = (1 * 7) % 20201227;

        var doorLoopSize = 1;

        while (doorValue != doorPublicKey) {
            doorValue = (doorValue * 7) % 20201227;
            doorLoopSize++;
        }

        long encryptionKey = 1;
        var encryptionLoop = doorLoopSize;
        while (encryptionLoop > 0) {
            encryptionLoop--;
            encryptionKey = (encryptionKey * cardPublicKey) % 20201227;
        }

        System.out.println(encryptionKey);

        encryptionKey = 1;
        encryptionLoop = cardLoopSize;
        while (encryptionLoop > 0) {
            encryptionLoop--;
            encryptionKey = (encryptionKey * doorPublicKey) % 20201227;
        }

        System.out.println(encryptionKey);
    }
}
