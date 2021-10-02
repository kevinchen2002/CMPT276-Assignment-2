/*
Game Manager stores the games as an ArrayList
add new game, retrieve specific game from index, and removing game by index
 */

package ca.cmpt276.as2.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;

public class GameManager implements Iterable<Game> {
    private ArrayList<Game> allGames = new ArrayList<>();

    /*
    Singleton Support
    Taken from Brian Fraser's Singleton Model video
     */
    private static GameManager instance;
    private GameManager(){
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void setAllGames(ArrayList<Game> allGames) {
        this.allGames = allGames;
    }

    public ArrayList<Game> getAllGames() {
        return allGames;
    }

    /**
     * Add a game to the end of the list
     * @param newGame the game to be added
     */
    public void addGame(Game newGame) {
        this.allGames.add(newGame);
    }

    public void replaceGame(int gameIndex, Game editedGame) {
        this.allGames.set(gameIndex, editedGame);
    }

    /**
     * Delete a game from the array.
     * @param indexToDelete array index of the game to be deleted
     */
    public void deleteGameAt(int indexToDelete) {
        this.allGames.remove(indexToDelete);
    }

    /**
     * Return Game at given index
     * @param indexToGet address of the Game required
     * @return Game object at the index in the ArrayList
     */
    public Game getGameAt(int indexToGet) {
        return this.allGames.get(indexToGet);
    }

    //makes the class iterable
    @NonNull
    @Override
    public Iterator<Game> iterator() {
        return allGames.iterator();
    }
}
