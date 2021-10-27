import java.util.*;

public class Player {

    // Properties
    private int playerId;
    private ArrayList<Stone> stones;
    private int score;

    private Set<List<Stone>> consecutives;
    private List<Stone>[] repetitives;

    // Constructor
    public Player(int playerNo) {
        this.playerId = playerNo;
        stones = new ArrayList<>();
        consecutives = new HashSet<>();
        repetitives = new ArrayList[Stone.NUMBER_OF_EACH_TYPE];
        for (int i = 0; i < repetitives.length; i++) {
            repetitives[i] = new ArrayList<>();
        }
    }

    // Method
    public void addStone(Stone stone) {
        stones.add(stone);
    }

    public void placeStones() {
        Stone joker = getJoker();

        if (joker == null) {
            placeStonesHelper();
            return;
        }

        int maxScore = 0;
        List<Stone> bestHand = stones;
        ArrayList<Stone> origHand = (ArrayList<Stone>) stones.clone();

        for (int i = 0; i < Stone.NUMBER_OF_STONES - 1; i++) {
            int colorId = i / Stone.NUMBER_OF_EACH_TYPE;
            int stoneNo = i % Stone.NUMBER_OF_EACH_TYPE + 1;
            joker.setColorId(colorId);
            joker.setStoneNo(stoneNo);
            placeStonesHelper();
            if (score > maxScore) {
                maxScore = score;
                bestHand = (ArrayList<Stone>) stones.clone();
            }
            consecutives = new HashSet<>();
            repetitives = new ArrayList[Stone.NUMBER_OF_EACH_TYPE];
            for (int j = 0; j < repetitives.length; j++) {
                repetitives[j] = new ArrayList<>();
            }
        }

        stones = (ArrayList<Stone>) bestHand;
        score = maxScore;
    }

    private void placeStonesHelper() {

        score = stones.size();

        generateConsecutives();
        generateRepetitives();

        // Place the sequences back to the board.
        for (List<Stone> sequence : repetitives) {
            int sequenceSize = sequence.size();
            if (sequenceSize > 1) {
                stones.addAll(sequence);
            }

            if (sequenceSize < 3) {
                score -= sequenceSize;
            }
        }

        for (List<Stone> sequence : consecutives) {
            stones.addAll(sequence);
        }

    }

    private Stone getJoker() {

        for (Stone stone : stones) {
            if (stone.isJoker()) {
                return stone;
            }
        }

        return null;
    }

    private void generateConsecutives() {
        Collections.sort(stones);

        for (int pivot = 0, currPointer = 0, nextPointer = 1; nextPointer < stones.size(); nextPointer++) {
            Stone currStone = stones.get(currPointer);
            Stone nextStone = stones.get(nextPointer);
            if (currStone.getNextStoneId() != nextStone.getStoneId()) {
                int sequenceSize = nextPointer - pivot;
                if (sequenceSize >= 3) {
                    List<Stone> subList = stones.subList(pivot, nextPointer);
                    List<Stone> sequence = new ArrayList<>(subList);
                    subList.clear();
                    consecutives.add(sequence);
                } else {
                    pivot += sequenceSize;
                }
                currPointer = pivot;
                nextPointer = pivot;
            } else {
                currPointer++;
            }
        }
    }

    private void generateRepetitives() {

        List<Stone> toRemove = new ArrayList<>();

        // If not identical, move the stones with the same number to a sequence.
        // If an identical one is encountered, keep it in stones.
        for (Stone stone : stones) {
            int stoneIdx = stone.getStoneNo() - 1;
            if (!repetitives[stoneIdx].contains(stone)) {
                repetitives[stoneIdx].add(stone);
                toRemove.add(stone);
            }
            // Decrement the score for each ungrouped identical stone.
            else {
                score--;
            }
        }

        // Find the size of each repetitive.
        // If less than 2, preserve its location.
        for (List<Stone> sequence : repetitives) {
            if (sequence.size() < 2) {
                toRemove.removeAll(sequence);
            }
        }

        for (Stone stoneToRemove : toRemove) {
            stones.remove(stoneToRemove);
        }

        // Now we only have remainders on the board.
        // Complete some partial repetitives.
        completeRepetitives();

        // Sort repetitives according to their size
        Arrays.sort(repetitives, new RepetitiveComparator());
    }

    private void completeRepetitives() {

        // Create available sets
        HashSet<Stone> availableStones = new HashSet<>();
        HashSet<Integer> availableNumbers = new HashSet<>();

        for (List<Stone> sequence : repetitives) {
            if (sequence.size() == 2) {
                availableNumbers.add(sequence.get(0).getStoneNo());
                availableStones.addAll(sequence);
            }
        }

        for (List<Stone> sequence : consecutives) {
            if (sequence.size() > 3) {
                int numMovedStones = 0;
                for (int i = 0; i < sequence.size(); i++) {
                    Stone stone = sequence.get(i);
                    int stoneNo = sequence.get(i).getStoneNo();
                    if (availableNumbers.contains(stoneNo)
                            && !availableStones.contains(stone)) {
                        availableNumbers.remove(stoneNo);
                        repetitives[stoneNo].add(stone);
                        numMovedStones++;
                        if (sequence.size() - numMovedStones <= 3) {
                            break;
                        }
                    }
                }
                if (numMovedStones > 0) {
                    sequence.subList(0, numMovedStones).clear();
                }

                if (sequence.size() <= 3) {
                    continue;
                }

                numMovedStones = 0;

                // Traverse the remaining sequence in a reverse order
                for (int j = sequence.size() - 1; j >= 0; j--) {
                    Stone stone = sequence.get(j);
                    int stoneNo = sequence.get(j).getStoneNo();
                    if (availableNumbers.contains(stoneNo)
                            && !availableStones.contains(stone)) {
                        availableNumbers.remove(stoneNo);
                        repetitives[stoneNo].add(stone);
                        numMovedStones++;
                        if (sequence.size() - numMovedStones <= 3) {
                            break;
                        }
                    }
                }

                if (numMovedStones > 0) {
                    sequence.subList(sequence.size() - numMovedStones, sequence.size()).clear();
                }
            }
        }

    }

    public List<Stone> getHand() {
        return stones;
    }

    public int getScore() {
        return score;
    }

    public int getPlayerId() {
        return playerId;
    }

    class RepetitiveComparator implements Comparator<List<Stone>> {

        @Override
        public int compare(List<Stone> seq1, List<Stone> seq2) {
            return Integer.compare(seq1.size(), seq2.size());
        }
    }
}
