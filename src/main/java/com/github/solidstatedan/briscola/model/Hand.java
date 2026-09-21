package com.github.solidstatedan.briscola.model;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    // FIELDS
    private final List<Card> cards;

    // CONSTRUCTOR
    /**
     * Initialize an empty hand.
     */
    public Hand() {
        this.cards = new ArrayList<>(3);
    }

    // METHODS
    /**
     * Add card to the hand.
     * @param card The card instance to add.
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Remove and return one card from hand at the specified index.
     * @param index of the card to play.
     * @return the card instance at the corresponding index.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public Card play(int index) {
        return cards.remove(index);
    }

    /**
     * Get the current number of cards in hand.
     */
    public int getSize() {
        return cards.size();
    }
}
