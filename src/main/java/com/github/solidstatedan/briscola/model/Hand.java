package com.github.solidstatedan.briscola.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the collection of cards currently held by a player (3 cards maximum).
 */
public class Hand {

    // FIELDS
    private final List<Card> cards;

    // CONSTRUCTOR
    /**
     * Initializes an empty hand.
     */
    public Hand() {
        this.cards = new ArrayList<>(3);
    }

    // METHODS
    /**
     * Adds one card to the hand.
     * @param card The Card instance to add.
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Removes and returns one card from hand at the specified index.
     * @param index of the card to play.
     * @return the Card instance at the corresponding index.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public Card playCard(int index) {
        return cards.remove(index);
    }

    /**
     * Removes all cards currently held in this hand.
     * <p>
     * Typically called when starting a new game.
     */
    public void clear() {
        cards.clear();
    }

    /**
     * Gets the current number of cards in hand.
     */
    public int getSize() {
        return cards.size();
    }

    /**
     * Gets an unmodifiable read-only view of the cards currently in hand.
     *
     * @return Read-only list of {@link Card} instances.
     */
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }
}
