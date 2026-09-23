package com.github.solidstatedan.briscola.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TrickTest {

    private Player p1;
    private Player p2;
    private Player p3;
    private Player p4;

    private Card card1;
    private Card card2;
    private Card card3;
    private Card card4;

    private Trick trick;

    @BeforeEach
    public void setUp() {
        p1 = new Player("P1", PlayerType.HUMAN);
        p2 = new Player("P2", PlayerType.EASY_AI);
        p3 = new Player("P3", PlayerType.EASY_AI);
        p4 = new Player("P4", PlayerType.EASY_AI);

        card1 = new Card(Suit.COINS, 1);   // Ace
        card2 = new Card(Suit.COINS, 3);   // Three
        card3 = new Card(Suit.CLUBS, 10);  // King
        card4 = new Card(Suit.SWORDS, 7);  // Seven

        trick = new Trick();
    }

    @Test
    public void testTrickStartsEmpty() {

        assertTrue(trick.getPlayedCards().isEmpty());
        assertTrue(trick.getCardToPlayerMap().isEmpty());
    }

    @Test
    public void testPlayCardSuccessfully() {
        trick.playCard(p1, card1);

        // Assert trick size is now 1.
        assertEquals(1, trick.getTrickSize());

        // Assert that the played card is associated to
        // the player who actually played that card.
        assertEquals(p1, trick.getPlayerForCard(card1));
    }

    @Test
    public void testPlayedCardsMaintainChronologicalOrder() {
        // Run a whole trick.
        trick.playCard(p1, card1);
        trick.playCard(p2, card2);
        trick.playCard(p3, card3);
        trick.playCard(p4, card4);

        // Assert the chronological order.
        assertEquals(4, trick.getTrickSize());
        assertEquals(card1, trick.getPlayedCards().get(0));
        assertEquals(card2, trick.getPlayedCards().get(1));
        assertEquals(card3, trick.getPlayedCards().get(2));
        assertEquals(card4, trick.getPlayedCards().get(3));
    }

    @Test
    public void testCannotHaveNullPlayerOrNullCard() {
        assertThrows(IllegalArgumentException.class, () -> trick.playCard(p1, null));
        assertThrows(IllegalArgumentException.class, () -> trick.playCard(null, card1));
    }

    @Test
    public void testCannotPlaySameCardAgain() {
        trick.playCard(p1, card1);
        assertThrows(IllegalArgumentException.class, () -> trick.playCard(p1, card1));
    }

    @Test
    public void testCannotPlayAgainInSameTrick() {
        trick.playCard(p1, card1);
        assertThrows(IllegalArgumentException.class, () -> trick.playCard(p1, card2));
    }

    @Test
    public void testGetPlayerForCardThrowsIfCardNotFound() {
        assertThrows(IllegalArgumentException.class, () -> trick.getPlayerForCard(new Card(Suit.CUPS,3)));
    }
}
