package com.github.solidstatedan.briscola.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TrickEvaluatorTest {

    private Player p1, p2, p3, p4;
    private Trick trick;
    private TrickEvaluator evaluator;

    @BeforeEach
    public void setUp() {
        p1 = new Player("P1", PlayerType.HUMAN);
        p2 = new Player("P2", PlayerType.EASY_AI);
        p3 = new Player("P3", PlayerType.EASY_AI);
        p4 = new Player("P4", PlayerType.EASY_AI);

        trick = new Trick();

        evaluator = new TrickEvaluator();
    }

    @Test
    public void testHighestStrengthInLeadSuitWinsWhenNoTrumpPlayed() {

        // SCENARIO: Trump is CUPS (NO TRUMP PLAYED)
        Card card1 = new Card(Suit.COINS, 2);   // Two      (STRENGTH  1)
        Card card2 = new Card(Suit.COINS, 10);  // King     (STRENGTH  8)
        Card card3 = new Card(Suit.COINS, 1);   // Ace      (STRENGTH 10)
        Card card4 = new Card(Suit.COINS, 3);   // Three    (STRENGTH  9)

        trick.playCard(p1, card1);
        trick.playCard(p2, card2);
        trick.playCard(p3, card3);
        trick.playCard(p4, card4);

        // 1. NO trump card was played.
        // 2. Lead suit is COINS.
        // 3. Ace of COINS (card3, played by p3) has the highest strength in the lead suit,
        //    so the winner should be p3.
        Player winner = evaluator.evaluateWinner(trick, Suit.CUPS);
        assertEquals(p3, winner);
    }

    @Test
    public void testNotLeadSuitCardCannotWinEvenIfStronger() {
        // SCENARIO: Trump is CUPS (NO TRUMP PLAYED)
        Card card1 = new Card(Suit.COINS, 2);   // Two      (STRENGTH  1)
        Card card2 = new Card(Suit.CLUBS, 10);  // King     (STRENGTH  8)
        Card card3 = new Card(Suit.SWORDS, 1);  // Ace      (STRENGTH 10)
        Card card4 = new Card(Suit.SWORDS, 3);  // Three    (STRENGTH  9)

        trick.playCard(p1, card1);
        trick.playCard(p2, card2);
        trick.playCard(p3, card3);
        trick.playCard(p4, card4);

        // 1. NO trump card was played.
        // 2. Lead suit is COINS.
        // 3. Two of COINS (card1, played by p1) is the only card in the lead suit, it automatically wins.
        Player winner = evaluator.evaluateWinner(trick, Suit.CUPS);
        assertEquals(p1, winner);

        // 4. card2, card3, card4: although they are stronger than card1, they are not lead suit nor
        //    trump suit cards.
        assertNotEquals(p2, winner);
        assertNotEquals(p3, winner);
        assertNotEquals(p4, winner);
    }

    @Test
    public void testTrumpCardBeatsHigherLeadSuitCard() {
        // SCENARIO: Trump is CUPS (TRUMP CARD PLAYED)
        Card card1 = new Card(Suit.COINS, 10);  // King     (STRENGTH  8)
        Card card2 = new Card(Suit.COINS, 1);   // Ace      (STRENGTH 10)
        Card card3 = new Card(Suit.COINS, 3);   // Three    (STRENGTH  9)
        Card card4 = new Card(Suit.CUPS, 2);    // Two      (STRENGTH  1)

        trick.playCard(p1, card1);
        trick.playCard(p2, card2);
        trick.playCard(p3, card3);
        trick.playCard(p4, card4);

        // 1. Lead suit is CUPS
        // 2. Only 1 trump suit card was played: Two of CUPS (card4, played by p4).
        //    It automatically wins, even if it has lower strength.
        Player winner = evaluator.evaluateWinner(trick, Suit.CUPS);
        assertEquals(p4, winner);
    }

    @Test
    public void testStrongestTrumpWinsWhenMultipleTrumpsPlayed() {
        // SCENARIO: Trump is CUPS (MULTIPLE TRUMPS PLAYED)
        Card card1 = new Card(Suit.CUPS, 2);   // Two      (STRENGTH  1)
        Card card2 = new Card(Suit.COINS, 10); // King     (STRENGTH  8)
        Card card3 = new Card(Suit.CUPS, 1);   // Ace      (STRENGTH 10)
        Card card4 = new Card(Suit.CUPS, 3);   // Three    (STRENGTH  9)

        trick.playCard(p1, card1);
        trick.playCard(p2, card2);
        trick.playCard(p3, card3);
        trick.playCard(p4, card4);

        // 1. Multiple trumps played.
        // 2. Ace of CUPS (card3, played by p3) is the strongest in the trump suit, so they win.
        Player winner = evaluator.evaluateWinner(trick, Suit.CUPS);
        assertEquals(p3, winner);
    }

    @Test
    public void testCalculateTrickPoints() {
        // SCENARIO: Trump is CUPS (MULTIPLE TRUMPS PLAYED)
        Card card1 = new Card(Suit.CUPS, 2);   // Two      (POINTS  0)
        Card card2 = new Card(Suit.COINS, 10); // King     (POINTS  4)
        Card card3 = new Card(Suit.CUPS, 1);   // Ace      (POINTS 11)
        Card card4 = new Card(Suit.CUPS, 3);   // Three    (POINTS 10)

        trick.playCard(p1, card1);
        trick.playCard(p2, card2);
        trick.playCard(p3, card3);
        trick.playCard(p4, card4);

        // Assert total points that go to the winner of this trick.
        assertEquals(25, evaluator.calculateTrickPoints(trick));
    }

    @Test
    public void testCannotEvaluateOnNullTrickOrNullTrump() {

        // Cannot evaluate with null inputs.
        assertThrows(IllegalArgumentException.class, () -> evaluator.evaluateWinner(null, Suit.COINS));
        assertThrows(IllegalArgumentException.class, () -> evaluator.evaluateWinner(trick, null));
    }

    @Test
    public void testCannotEvaluateIfTrickNotComplete() {

        Card card1 = new Card(Suit.COINS, 1);
        Card card2 = new Card(Suit.COINS, 2);
        Card card3 = new Card(Suit.COINS, 3);
        Card card4 = new Card(Suit.COINS, 4);

        // Only 3/4 people have laid down a card in this trick.
        trick.playCard(p1, card1);
        trick.playCard(p2, card2);
        trick.playCard(p3, card3);

        // Cannot evaluate if trick is still ongoing.
        assertThrows(IllegalArgumentException.class, () -> evaluator.evaluateWinner(trick, Suit.COINS));
    }

    @Test
    public void testCannotCalculatePointsOfNullTrick() {
        assertThrows(IllegalArgumentException.class, () -> evaluator.calculateTrickPoints(null));
    }
}
