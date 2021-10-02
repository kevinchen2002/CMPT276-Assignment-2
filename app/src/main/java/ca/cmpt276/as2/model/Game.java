package ca.cmpt276.as2.model;
import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Game manages the players of a single game. It calculates the winner and records the time of its
 * creation through a parameterized constructor.
 */
public class Game {
    private final ArrayList<PlayerScore> players = new ArrayList<>();
    private final ArrayList<PlayerScore> winners = new ArrayList<>();
    private final LocalDateTime creationDateTime;

    /**
     * Creates new empty game with a given LocalDateTime object.
     * @param createTimeAndDate the time the game was created.
     */
    public Game(LocalDateTime createTimeAndDate) {
        this.creationDateTime = createTimeAndDate;
    }

    /**
     * Add a single player to a game.
     * @param playerToAdd a single PlayerScore class
     */
    public void addPlayer(PlayerScore playerToAdd) {
        this.players.add(playerToAdd);
        this.calculateWinner();
    }

    /**
     * Gets the player at the given index.
     * @param index of the player you want to see.
     * @return PlayerScore object.
     */
    public PlayerScore getPlayer(int index) {
        return players.get(index);
    }

    /**
     * Gets the score of the player at the given index.
     * @param index of the player whose score you want to see.
     * @return int representing the player's score.
     */
    public int getScoreAt(int index) {
        return this.getPlayer(index).getScore();
    }

    /**
     * Gets the ID number of the winning player at the given index.
     * @param index of the winning player (in the apps case this is either 1 or 2)
     * @return the winning player's number.
     */
    public int getWinnersNumAt(int index) {
        return winners.get(index).getPlayerNumber();
    }

    /**
     * Gets the number of players in the game.
     * @return int representing players in a game.
     */
    public int getPlayerCount(){
        return players.size();
    }

    /**
     * Gets the number of winners in a game.
     * @return int representing the number of winners.
     */
    public int getWinnerCount(){
        return winners.size();
    }

    /**
     * Gets the date and time the game was made
     * @return LocalDateTime object created on this class instantiation
     */
    public LocalDateTime getCreationDateTime() {
        return this.creationDateTime;
    }

    /**
     * Helper method to calculate the winning player (s)
     */
    private void calculateWinner() {
        winners.clear();
        //keep track of the current high score
        int highestScore = this.getScoreAt(0);
        //check all the players to see if they won
        for (PlayerScore singlePlayer : this.players) {
            //if another player has the same score, multiple winners
            if (singlePlayer.getScore() == highestScore) {
                winners.add(singlePlayer);
            }
            //if a player has a higher score, throw out the old winners add the new one
            if (singlePlayer.getScore() > highestScore) {
                winners.clear();
                winners.add(singlePlayer);
            }
        }
    }

    /**
     * Gets the points of each player and returns a nicely formatted string.
     * @return String of scores, separated by vs.
     */
    public String getPoints() {
        StringBuilder scoreDetails = new StringBuilder();
        for (int i = 0; i < this.getPlayerCount(); i++) {
            scoreDetails.append(this.getScoreAt(i));
            //only prints vs. between games and not at the end
            if (!(i ==(this.getPlayerCount() - 1))) {
                scoreDetails.append(" vs. ");
            }
        }
        return  scoreDetails.toString();
    }

    /**
     * Returns formatted string of the Game's creation date.
     * @return String of Date and Time in format yyyy-MM-dd HH:mm
     */
    public String getDateString() {
        StringBuilder dateDetails = new StringBuilder();

        //code learned from https://mkyong.com/java8/java-8-how-to-format-localdatetime/
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDatetime = this.getCreationDateTime().format(formatter);
        dateDetails.append("\n (").append(formattedDatetime).append(")\n");

        return dateDetails.toString();
    }

    /**
     * Returns all the details of this Game.
     * @return String of players score, who won, and the date of the game.
     */
    @NonNull
    @Override
    public String toString() {
        StringBuilder gameDetails = new StringBuilder();

        gameDetails.append(getPoints());

        //print winners
        int winnerCount = this.getWinnerCount();
        gameDetails.append(", Winning player" );
        if (winnerCount > 1) {
            gameDetails.append("s");
        }
        gameDetails.append(": ");
        for (int i = 0; i < winnerCount; i++) {
            gameDetails.append(this.getWinnersNumAt(i));
            if (!(i == (winnerCount - 1))) {
                gameDetails.append(", ");
            }
        }

        gameDetails.append("\n (")
                    .append(getDateString())
                    .append(")\n");

        return gameDetails.toString();
    }
}
