package ca.cmpt276.as2.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * GameManager handles all of the Games in the program. It can give basic information of the
 * number of games so TextUI knows how much to print. Also can delete and add new games. It's also
 * iterable.
 */
public class GameManager implements Iterable<Game> {
    private ArrayList<Game> allGames = new ArrayList<>();
    private static GameManager instance;

    /**
     * Empty constructor for singleton support
     */
    private GameManager() {
    }

    /**
     * Creates an instance of GameManager if one does not exist.
     * @return GameManager instance that will be used across the app
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Overwrites gameList with the shared preference storage.
     * @param allGames given arrayList to overwrite from shared preference.
     */
    public void setAllGames(ArrayList<Game> allGames) {
        this.allGames = allGames;
    }

    /**
     * Gets all games in GameManager.
     * @return ArrayList containing all the Games.
     */
    public ArrayList<Game> getAllGames() {
        return allGames;
    }

    /**
     * Return Game at given index.
     * @param index address of the Game required.
     * @return Game object at the index in the ArrayList.
     */
    public Game getGameAt(int index) {
        return this.allGames.get(index);
    }

    /**
     * Add a game to the end of the list.
     * @param newGame the game to be added.
     */
    public void addGame(Game newGame) {
        this.allGames.add(newGame);
    }

    /**
     * Replaces game at index with given game. Used for editing games.
     * @param index the index of the game you want to replace.
     * @param editedGame the replacement Game object.
     */
    public void replaceGame(int index, Game editedGame) {
        this.allGames.set(index, editedGame);
    }

    /**
     * Delete a game from the array.
     * @param index array index of the game to be deleted
     */
    public void deleteGameAt(int index) {
        this.allGames.remove(index);
    }

    /**
     * Makes the class iterable
     * @return iterator of Game.
     */
    @NonNull
    @Override
    public Iterator<Game> iterator() {
        return allGames.iterator();
    }
}
