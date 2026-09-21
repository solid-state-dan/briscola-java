package com.github.solidstatedan.briscola.model;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Manages the sequential collection of cards of a traditional deck (40-card deck).
 */
public class Deck {

    // FIELDS
    private final List<Card> cards;

    // CONSTRUCTOR
    /**
     * Initializes a standard briscola deck.
     */
    public Deck() {
        // A new deck should have 40 cards.
        this.cards = new ArrayList<>(40);

        // Fill up the deck with all 40 unique cards.
        for (Suit suit : Suit.values()) {
            for (int rank = 1; rank <= 10; rank++) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    // METHODS
    /**
     * Draws and removes the top card from deck.
     * @return the drawn Card instance.
     */
    public Card draw() {

        // If deck is empty:
        if (cards.isEmpty()) {
            throw new NoSuchElementException("Deck has no cards left");
        }

        // Otherwise (deck still has cards):
        return cards.removeLast();
    }

    /**
     * Gets the remaining number of cards left in the deck.
     */
    public int getSize() {
        return cards.size();
    }
}
