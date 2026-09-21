package com.github.solidstatedan.briscola.model;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Deck {

    // FIELDS
    private final List<Card> cards;

    // CONSTRUCTOR
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
    // Draw a card.
    public Card draw() {

        // If deck is empty:
        if (cards.isEmpty()) {
            throw new NoSuchElementException("Deck has no cards left");
        }

        // Otherwise (deck still has cards):
        return cards.removeLast();
    }

    // How many cards left in the deck.
    public int getSize() {
        return cards.size();
    }
}
