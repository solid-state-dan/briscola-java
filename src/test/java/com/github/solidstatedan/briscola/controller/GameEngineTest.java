package com.github.solidstatedan.briscola.controller;

import com.github.solidstatedan.briscola.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {

    private GameEngine engine;
    private Player p1, p2, p3, p4;

    @BeforeEach
    public void setUp() {

        p1 = new Player("P1", PlayerType.HUMAN);
        p2 = new Player("P2", PlayerType.EASY_AI);
        p3 = new Player("P3", PlayerType.EASY_AI);
        p4 = new Player("P4", PlayerType.EASY_AI);

        Team teamA = new Team("TEAM A", p1, p2);
        Team teamB = new Team("TEAM B", p3, p4);

        engine = new GameEngine(teamA, teamB);
    }

    @Test
    public void testGameInitialization() {

        engine.startNewGame();

        // Each player should have 3 cards.
        assertEquals(3, p1.getHand().getSize());
        assertEquals(3, p2.getHand().getSize());
        assertEquals(3, p3.getHand().getSize());
        assertEquals(3, p4.getHand().getSize());

        // Deck should have 27 cards left.
        // Started with 40 cards: 12 dealt + 1 trump card exposed = 27 left in deck.
        assertEquals(27, engine.getDeck().getSize());
        assertNotNull(engine.getTrumpSuit());
    }

    @Test
    public void testTrickWinnerStartsFirst() {
        engine.startNewGame();

        playTrick(1);

        // The player who won the trick should start first next.
        assertEquals(engine.getLastTrickWinner(), engine.getCurrentPlayer());
    }

    @Test
    public void testStandardReplenish() {
        engine.startNewGame();

        playTrick(1);

        // After the last turn that completed the trick,
        // the auto replenish should've kicked in. So, now
        // there should be 23 card left in deck.
        assertEquals(23, engine.getDeck().getSize());
    }

    @Test
    public void testLastReplenishGivesTrumpToLastPlayer() {
        engine.startNewGame();

        // Play 6 complete tricks -> leaves 3 card in deck.
        playTrick(6);
        assertEquals(3, engine.getDeck().getSize());

        // Save the trump card before it becomes null, so can compare it later.
        Card expectedTrumpCard = engine.getTrumpCard();

        // Play 7th trick.
        playTrick(1);

        // Auto replenish should've kicked in, and at this point:
        // 1. There should be 0 cards left in deck
        assertEquals(0, engine.getDeck().getSize());

        // 2. The trump card was taken...
        assertNull(engine.getTrumpCard());
        //    ... by the last player to draw
        //    (the 1st player to draw is at index 0 in draw order,
        //    so the 4th player is 3 positions after).
        int lastPlayerIndex = (engine.getCurrentPlayerIndex() + 3) % 4;
        boolean lastPlayerTookTrumpCard = engine.getPlayers().get(lastPlayerIndex).getHand().getCards().contains(expectedTrumpCard);
        assertTrue(lastPlayerTookTrumpCard);
    }

    @Test
    public void testReplenishOnEmptyDeckDecreasesHandSize() {
        engine.startNewGame();

        // Complete 7 tricks -> Deck becomes 0, trump card is taken, everyone has 3 cards
        playTrick(7);
        assertEquals(0, engine.getDeck().getSize());
        assertNull(engine.getTrumpCard());
        for (Player p : engine.getPlayers()) {
            assertEquals(3, p.getHand().getSize());
        }

        // Play 8th trick (no cards left to draw)
        playTrick(1);

        // Everyone should have 2 cards now.
        for (Player p : engine.getPlayers()) {
            assertEquals(2, p.getHand().getSize());
        }
    }

    @Test
    public void testGameOver() {
        engine.startNewGame();

        // Complete 10 tricks -> 0 cards left to play, game finishes
        playTrick(10);

        // Everyone should have 0 cards now.
        for (Player p : engine.getPlayers()) {
            assertEquals(0, p.getHand().getSize());
        }

        assertTrue(engine.isGameOver());

        // Cannot play anymore.
        assertThrows(IllegalStateException.class, () -> playTrick(1));
    }

    @Test
    public void testAiAutoPlays() {
        engine.startNewGame();

        playTrick(1);
        assertEquals(23, engine.getDeck().getSize());

        playTrick(1);
        assertEquals(19, engine.getDeck().getSize());
    }

    @Test
    public void testGetCardsReturnsUnmodifiableList() {
        engine.startNewGame();

        assertThrows(UnsupportedOperationException.class, () -> engine.getPlayers().getFirst().getHand().getCards().clear());
    }

    /**
     * Plays 4 turns to complete 1 trick.
     */
    private void playTrick(int count) {
        for (int i = 0; i < count; i++) {
            engine.playTurn(0);
            engine.playTurn(0);
            engine.playTurn(0);
            engine.playTurn(0);
        }
    }
}
