package com.github.solidstatedan.briscola.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HandTest {

    private Hand hand;
    private Card card1;
    private Card card2;
    private Card card3;

    @BeforeEach
    public void SetUp() {
        hand = new Hand();
        card1 = new Card(Suit.COINS, 10);
        card1 = new Card(Suit.CLUBS, 1);
        card1 = new Card(Suit.CUPS, 3);
    }

    @Test
    public void testHandStartsEmpty() {

        assertEquals(0, hand.getSize(), "Initial hand should be empty.");
    }

    @Test
    public void testDrawCardIncreasesHandSize() {
        hand.addCard(card1);

        assertEquals(1, hand.getSize(), "Drawing a card should increase hand size by 1.");
    }

    @Test
    public void testPlayCardRemovesItFromHand() {
        // Add 3 cards.
        hand.addCard(card1);
        hand.addCard(card2);
        hand.addCard(card3);

        // Play 1 card (card2).
        Card played = hand.play(1);

        // Played card should be card2.
        assertEquals(card2, played);

        // Should be left with 2 cards in hand.
        assertEquals(2, hand.getSize(), "Playing a card should decrease hand size by 1.");
    }

    @Test
    public void testPlayCardWithInvalidIndex() {

        assertThrows(IndexOutOfBoundsException.class, () -> hand.play(2), "Card with given index doesn't exist yet");
    }
}
