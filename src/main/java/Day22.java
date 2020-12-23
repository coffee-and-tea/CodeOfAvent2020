import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day22 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var input = InputFileReader.readInputAsIterator("day22-puzzle.txt");

        var player1 = new Player("unassigned");
        var player2 = new Player("unassigned");
        var currentPlayer = player1;

        while (input.hasNext()) {
            var line = input.next();
            if (line.contains(":")) {

                currentPlayer = new Player(line.substring(0, line.indexOf(":")));
                if (player1.name.equals("unassigned")) {
                    player1 = currentPlayer;
                } else {
                    player2 = currentPlayer;
                }
            } else if (!line.isEmpty()) {
                currentPlayer.addCard(Integer.parseInt(line));
            }
        }

//        var result = Player.play(player1, player2);
//        System.out.println(result);

        if(Player.playRulePartTwo(player1, player2)) {
            System.out.println(player1.calculateScore());
        } else {
            System.out.println(player2.calculateScore());
        }

    }
}

class Player {

    String name;
    Deque<Integer> deck = new LinkedList<>();

    static HashMap<Integer, Set<List<Integer>>> gamesPlayed = new HashMap<>();

    static int game = 1;
    static int round = 0;

    public Player(String name) {
        this.name = name;
    }

    public static long play(Player player1, Player player2) {

        while (player1.deck.size() > 0 && player2.deck.size() > 0) {
            var player1Card = player1.deck.poll();
            var player2Card = player2.deck.poll();
            if (player1Card > player2Card) {
                player1.addCard(player1Card);
                player1.addCard(player2Card);
            } else {
                player2.addCard(player2Card);
                player2.addCard(player1Card);
            }
        }

        System.out.println("== Post-game results ==");
        System.out.println(player1);
        System.out.println(player2);

        if (player1.deck.size() > 0) {
            return player1.calculateScore();
        } else {
            return player2.calculateScore();
        }
    }

    public static boolean playRulePartTwo(Player player1, Player player2) {

        System.out.println("=== Game " + game + " ===" );

        while (player1.deck.size() > 0 && player2.deck.size() > 0) {
            round++;
            System.out.println("-- Round " + round + " (Game " + game + ") --");
            System.out.println(player1);
            System.out.println(player2);

            if(gamesPlayed.computeIfAbsent(game, g -> new HashSet<>()).contains(player1.deck.stream().collect(Collectors.toList()))){
                break;
            } else {
                gamesPlayed.get(game).add(player1.deck.stream().collect(Collectors.toList()));
            }

            var player1Card = player1.deck.poll();
            var player2Card = player2.deck.poll();

            if(player1.deck.size() >= player1Card && player2.deck.size() >= player2Card) {
                System.out.println("Playing a sub-game to determine the winner...");
                if(subGame(player1, player1Card, player2, player2Card)) {
                    System.out.println("Player 1 wins");
                    player1.addCard(player1Card);
                    player1.addCard(player2Card);
                } else {
                    System.out.println("Player 2 wins");
                    player2.addCard(player2Card);
                    player2.addCard(player1Card);
                }
                game--;
            }else {
                if (player1Card > player2Card) {
                    System.out.println("Player 1 wins");
                    player1.addCard(player1Card);
                    player1.addCard(player2Card);
                } else {
                    System.out.println("Player 2 wins");
                    player2.addCard(player2Card);
                    player2.addCard(player1Card);
                }
            }
        }

        System.out.println("== Post-game results ==");
        System.out.println(player1);
        System.out.println(player2);

        if (player1.deck.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean subGame(Player player1, Integer player1Size, Player player2, Integer player2Size) {
        game++;
        gamesPlayed.put(game, new HashSet<>());
        var player3 = player1.clone(player1Size);
        var player4 = player2.clone(player2Size);
        return playRulePartTwo(player3, player4);
    }

    private Player clone(Integer player1size) {
        var player = new Player(this.name);
        player.deck.addAll(this.deck.stream().collect(Collectors.toList()).subList(0, player1size));
        return player;
    }

    public long calculateScore() {
        long result = 0;
        int count = 1;
        while (deck.size() > 0) {
            result += deck.pollLast() * count;
            count++;
        }
        return result;
    }

    public void addCard(int card) {
        deck.offer(card);
    }

    @Override
    public String toString() {
        return "Player{" + name + "\'s deck" +
                ", deck=" + deck +
                '}';
    }
}
