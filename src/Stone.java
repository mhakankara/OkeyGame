import java.util.Objects;

public class Stone implements Comparable<Stone> {

    public static final int NUMBER_OF_EACH_TYPE = 13;
    public static final int NUMBER_OF_STONES = 53;

    // Properties
    private int colorId;
    private int stoneId;
    private int stoneNo;
    private boolean isJoker;

    // Constructor
    public Stone(int stoneId) {

        this.stoneId = stoneId;
        this.colorId = stoneId / NUMBER_OF_EACH_TYPE;
        this.stoneNo = stoneId % NUMBER_OF_EACH_TYPE + 1;
    }

    // Methods
    public int getColorId() {

        return colorId;
    }

    public int getStoneId() {
        return stoneId;
    }

    public int getStoneNo() {
        return stoneNo;
    }

    @Override
    public int compareTo(Stone stone) {

        if (this.colorId == stone.colorId) {
            return Integer.compare(this.stoneNo, stone.stoneNo);
        }

        return Integer.compare(this.colorId, stone.colorId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stone stone = (Stone) o;
        return colorId == stone.colorId && stoneNo == stone.stoneNo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(colorId, stoneNo);
    }

    public boolean isJoker() {
        return isJoker;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public void setStoneNo(int stoneNo) {
        this.stoneNo = stoneNo;
    }

    public void setJoker(boolean predicate) {
        isJoker = predicate;
    }

    public int getNextStoneId() {

        int next = stoneId + 1;

        // Design decision: If the next stone is a different color,
        // pick the first stone with the same color.

        if (next % NUMBER_OF_EACH_TYPE == 0) {
            return next - NUMBER_OF_EACH_TYPE;
        }

        return next;
    }
}




