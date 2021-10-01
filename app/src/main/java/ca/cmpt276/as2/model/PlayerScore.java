
package ca.cmpt276.as2.model;
/**
 PlayerScore represents the score of a single player in a Game.
 take in number of cards, sum of points, and number of wager cards
 calculate's a player's score
 */
public class PlayerScore {
    public static final int WAGER_MULTIPLIER_OFFSET = 1;
    public static final int SUM_CARD_OFFSET = 20;
    public static final int THRESHOLD_FOR_BONUS = 8;

    private final int playerNumber;
    private final int cardPlayed;
    private int sumOfCards;
    private int numOfWagers;

    /**
     * Constructs the player score class with given parameters
     * @param givenCardPlayed the number of cards a player played
     * @param givenSumOfCards the sum of points from the cards the players played
     * @param givenNumOfWagers the number of wager cards the players played
     * @throws IllegalArgumentException if any given values are negative
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
     * Returns the calculated score of the player
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
     * Returns a player's number in a Game
     * @return int value from 1 - 4
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    public int getCardPlayed() {
        return cardPlayed;
    }

    public int getSumOfCards() {
        return sumOfCards;
    }

    public int getNumOfWagers() {
        return numOfWagers;
    }
}
