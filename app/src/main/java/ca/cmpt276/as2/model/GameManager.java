/*
//TODO: describe class better
Game Manager stores the games as an ArrayList
add new game, retrieve specific game from index, and removing game by index
 */

package com.example.gamemodule.model;

import java.util.ArrayList;
import java.util.Iterator;

public class GameManager implements Iterable<Game> {
    private final ArrayList<Game> allGames = new ArrayList<>();

    /**
     * Add a game to the end of the list
     * @param newGame the game to be added
     */
    public void addGame(Game newGame) {
        this.allGames.add(newGame);
    }

    /**
     * Delete a game from the array.
     * @param indexToDelete array index of the game to be deleted
     */
    public void deleteGameAt(int indexToDelete) {
        this.allGames.remove(indexToDelete);
    }

    /**
     * Gives the number of games stored.
     * @return integer of the size of the Game arraylist
     */
    public int getSize(){
        return this.allGames.size();
    }

    /**
     * Return Game at given index
     * @param indexToGet address of the Game required
     * @return Game object at the index in the ArrayList
     */
    public Game getGameAt(int indexToGet) {
        return this.allGames.remove(indexToGet);
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return !this.allGames.isEmpty();
    }

    //makes the class iterable
    @Override
    public Iterator<Game> iterator() {
        return allGames.iterator();
    }
}
