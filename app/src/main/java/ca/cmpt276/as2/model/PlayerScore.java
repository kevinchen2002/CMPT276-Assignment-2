package ca.cmpt276.as2.model;
/**
 * Stores player number and score information of a player in a Game. Calculates the score of a
 * player when given the cards played, sum of points, and number of wagers. Throws
 * IllegalArgumentException when given score information is negative or if the cards played is 0
 * but sum of points or number of wagers is greater than 0.
 */
public class PlayerScore {
    public static final int WAGER_MULTIPLIER_OFFSET = 1;
    public static final int SUM_CARD_OFFSET = 20;
    public static final int THRESHOLD_FOR_BONUS = 8;

    private final int playerNumber;
    private final int cardPlayed;
    private final int sumOfCards;
    private final int numOfWagers;

    /**
     * Creates new PlayerScore and checks for invalid input.
     * @param givenCardPlayed the number of cards a player played
     * @param givenSumOfCards the sum of points from the player's played point cards
     * @param givenNumOfWagers the number of wager cards the players played
     * @throws IllegalArgumentException if any given values are negative or if cards is 0 but wagers
     *                                  or sum of card points is greater than 0.
     */
    public PlayerScore(int givenPlayerNumber,
                       int givenCardPlayed,
                       int givenSumOfCards,
                       int givenNumOfWagers) throws IllegalArgumentException {
        if (givenCardPlayed < 0) {
            throw new IllegalArgumentException("Cards played must not be negative!");
        }

        if (givenSumOfCards < 0) {
            throw new IllegalArgumentException("The sum of cards played must not be negative!");
        }

        if (givenNumOfWagers < 0) {
            throw new IllegalArgumentException("The number of wagers must not be negative!");
        }
        //add one to input to make making players from loop earlier
        this.playerNumber = givenPlayerNumber + 1;
        this.cardPlayed = givenCardPlayed;
        this.sumOfCards = givenSumOfCards;
        this.numOfWagers = givenNumOfWagers;

        if ((cardPlayed == 0) && ((sumOfCards != 0) ||(numOfWagers != 0))) {
            throw new IllegalArgumentException("If the number of cards played is 0 then sum of cards and wager cards need to be 0!");
        }
    }

    /**
     * Calculates and returns the player's score based off the cards they played. Returns 0 if
     * no cards have been played.
     * @return integer of the player's calculated score
     */
    public int getScore() {
        if (cardPlayed == 0) {
            return 0;
        }
        int score = sumOfCards - SUM_CARD_OFFSET;
        score *= (numOfWagers + WAGER_MULTIPLIER_OFFSET);
        if (cardPlayed >= THRESHOLD_FOR_BONUS) {
            score += SUM_CARD_OFFSET;
        }
        return score;
    }

    public static int calculatePlayerScore(int cardPlayed, int sumOfCards, int numOfWagers) {
        if (cardPlayed == 0) {
            return 0;
        }
        int score = sumOfCards - SUM_CARD_OFFSET;
        score *= (numOfWagers + WAGER_MULTIPLIER_OFFSET);
        if (cardPlayed >= THRESHOLD_FOR_BONUS) {
            score += SUM_CARD_OFFSET;
        }
        return score;
    }

    /**
     * Gets the player number starting at 1 for humans to understand
     * @return the player number + 1
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Gets the cards played by the player.
     * @return int of the cards played by player.
     */
    public int getCardPlayed() {
        return cardPlayed;
    }

    /**
     * Gets the sum of the points of the player's cards.
     * @return int of the player's points.
     */
    public int getSumOfCards() {
        return sumOfCards;
    }

    /**
     * Gets the the wagers the player played.
     * @return int of number of wager cards of a player.
     */
    public int getNumOfWagers() {
        return numOfWagers;
    }
}
