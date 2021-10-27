import java.util.*;

public class OkeyGameController {
    public static final int NUMBER_OF_PLAYERS = 4;
    public static final int STONE_PER_PLAYER = 14;

    private final int FULL_PACK_OF_STONES = 2 * Stone.NUMBER_OF_STONES;
    private final int FALSE_JOKER_ID = 52;

    private final Random random;

    // Properties
    private Stone[] stones;
    private Player[] players;
    private Stone jokerStone;

    // Constructor
    public OkeyGameController() {

        stones = new Stone[FULL_PACK_OF_STONES];
        players = new Player[NUMBER_OF_PLAYERS];
        random = new Random();

        for (int i = 0; i < stones.length; i++) {
            stones[i] = new Stone(i % Stone.NUMBER_OF_STONES);
        }

        shuffleStones();
        pickJokerStone();
        distributeStones();
        placeStones();
    }

    // Methods
    public Stone getJokerStone() {
        return jokerStone;
    }

    private void shuffleStones() {

        for (int i = 0; i < stones.length; i++) {
            int randomIndex = random.nextInt(stones.length);
            Stone temp = stones[i];
            stones[i] = stones[randomIndex];
            stones[randomIndex] = temp;
        }
    }

    private void pickJokerStone() {

        int randomIndex = random.nextInt(stones.length);
        Stone randomStone = stones[randomIndex];

        while (randomStone.getStoneId() == FALSE_JOKER_ID) {
            randomIndex = random.nextInt(stones.length);
            randomStone = stones[randomIndex];
        }

        int jokerId = randomStone.getNextStoneId();
        jokerStone = null;

        for (Stone stone : stones) {
            if (stone.getStoneId() == jokerId) {
                stone.setJoker(true);
                jokerStone = stone;
            }
        }

        // False jokers get the number and color of the real jokers.
        for (Stone stone : stones) {
            if (stone.getStoneId() == FALSE_JOKER_ID) {
                stone.setStoneNo(jokerStone.getStoneNo());
                stone.setColorId(jokerStone.getColorId());
            }
        }

    }

    private void distributeStones() {

        int stoneIndex = 0;

        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            players[i] = new Player(i);

            for (int j = 0; j < STONE_PER_PLAYER; j++) {
                players[i].addStone(stones[stoneIndex++]);
            }
        }

        int randomIndex = random.nextInt(players.length);
        players[randomIndex].addStone(stones[stoneIndex]);
    }

    private void placeStones() {

        for (Player player : players) {
            player.placeStones();
        }
    }

    public List<Integer> getWinnerIds() {
        int maxScore = 0;
        List<Integer> winners = new ArrayList<>();

        for (Player player : players) {
            int score = player.getScore();
            System.out.println("Score of Player " + (player.getPlayerId() + 1) + ": " + score);
            if (score > maxScore) {
                maxScore = score;
            }
        }

        for (int i = 0; i < players.length; i++) {
            int score = players[i].getScore();
            if (score == maxScore) {
                winners.add(i);
            }
        }

        return winners;
    }

    public List<Stone> getPlayerHand(int playerId) {
        return players[playerId].getHand();
    }
}




