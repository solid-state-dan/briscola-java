package com.github.solidstatedan.briscola.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

public class DeckTest {

    private Deck deck;

    @BeforeEach
    public void setUp() {
        deck = new Deck();
    }

    @Test
    public void testValidDeckInitialization() {

        assertEquals(40, deck.getSize(), "A new deck should have 40 cards.");
    }

    @Test
    public void testDraw() {
        Card drawnCard = deck.draw();

        assertEquals(39, deck.getSize(), "Deck size should decrease by 1.");
        assertInstanceOf(Card.class, drawnCard, "Drawn card should be of type Card.");
    }

    @Test
    public void testDrawEmptyDeckThrowsException() {

        // Empty the deck.
        for (int i = 0; i < 40; i++) {
            deck.draw();
        }

        assertThrows(NoSuchElementException.class, deck::draw, "Can't draw a card from an empty deck.");
    }
}
