/*
Game represents a game played by 1 to 4 players, also stores when the game was created
stores which player won the game
//TODO: describe class better
 */

package ca.cmpt276.as2.model;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Game {
    private final ArrayList<PlayerScore> players = new ArrayList<>();
    private final ArrayList<PlayerScore> winners = new ArrayList<>();
    private final LocalDateTime creationDateTime = LocalDateTime.now();

    /**
     * Add a single player to a game.
     * @param playerToAdd a single PlayerScore class
     */
    public void addPlayer(PlayerScore playerToAdd) {
        this.players.add(playerToAdd);
        this.calculateWinner();
    }


    public int getScoresAt(int index) {
        return this.players.get(index).getScore();
    }

    /**
     * Gives the winners of the game, multiple if tied.
     */
    public void calculateWinner() {
        winners.clear();
        //keep track of the current high score
        int highestScore = this.getScoresAt(0);
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

    public int getWinnersNumAt(int index) {
        return winners.get(index).getPlayerNumber();
    }

    public int getPlayerCount(){
        return players.size();
    }

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

    @Override
    public String toString() {
        //return "Game{" +
//                "players=" + players +
//                ", winners=" + winners +
//                ", creationDateTime=" + creationDateTime +
//                '}';
        String gameString = "";
        gameString += creationDateTime.toString();
        gameString += " - Player " + winners + "won: ";
        gameString += "Insert scores here?";
        return gameString;
    }
}
