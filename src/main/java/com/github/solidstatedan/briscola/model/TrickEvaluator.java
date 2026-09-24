package com.github.solidstatedan.briscola.model;

import java.util.Comparator;

/**
 * Service class responsible for evaluating the winner and total point value
 * of a completed Briscola trick.
 */
public class TrickEvaluator {

    /**
     * Determines the winning player of a completed 4-card trick based on Briscola rules.
     *
     * @param trick     The trick to evaluate (must contain exactly 4 cards).
     * @param trump     The active trump suit (Briscola) for the current round.
     * @return The {@link Player} who won the trick.
     * @throws IllegalArgumentException If the trick or trump is null, or if the trick does not contain 4 cards.
     */
    public Player evaluateWinner(Trick trick, Suit trump) {

        if (trick == null || trump == null) {
            throw new IllegalArgumentException("Trick or trump suit cannot be null.");
        }

        if (trick.getTrickSize() != 4) {
            throw new IllegalArgumentException("Trick must be complete (4 cards) to evaluate a winner.");
        }

        // Get the suit of the first card played.
        Suit leadSuit = trick.getPlayedCards().getFirst().getSuit();

        // Comparator prioritizes:
        // 1. Is the card a Trump?
        // 2. Is the card part of the lead suit?
        // 3. Intrinsic strength of the card.
        Comparator<Card> briscolaComparator = Comparator
                .comparing((Card c) -> c.getSuit() == trump)
                .thenComparing(c -> c.getSuit() == leadSuit)
                .thenComparingInt(Card::getStrength);

        Card winningCard = trick.getPlayedCards().stream()
                .max(briscolaComparator)
                .orElseThrow();

        return trick.getPlayerForCard(winningCard);
    }

    /**
     * Calculates the total point value of all cards played in the trick.
     *
     * @param trick The trick to calculate points for.
     * @return Sum of points for all cards in the trick.
     * @throws IllegalArgumentException If trick is null.
     */
    public int calculateTrickPoints(Trick trick) {
        if (trick == null) {
            throw new IllegalArgumentException("Trick cannot be null.");
        }

        return trick.getPlayedCards().stream()
                .mapToInt(Card::getPoints)
                .sum();
    }
}
