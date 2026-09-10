package com.github.solidstatedan.briscola.model;

/**
 * Represents a playing card in a Briscola game.
 */
public class Card {
    private final Suit suit;
    private final int rank;
    private final int points;
    private final int strength;

    /**
     * Initializes a card with its suit and numeric rank.
     * Calculates points and strength based on Briscola rules.
     */
    public Card(Suit suit, int rank) {
        this.suit = suit;
        this.rank = rank;
        this.points = calculatePoints(rank);
        this.strength = calculateStrength(rank);
    }

    private int calculatePoints(int rank) {
        return switch (rank) {
            case 1 -> 11; // Ace
            case 3 -> 10; // Three
            case 10 -> 4; // King
            case 9 -> 3;  // Knight
            case 8 -> 2;  // Jack
            default -> 0; // All other cards (2, 4, 5, 6, 7)
        };
    }

    private int calculateStrength(int rank) {
        return switch (rank) {
            case 1 -> 10; // Ace is highest strength
            case 3 -> 9;  // Three
            case 10 -> 8; // King
            case 9 -> 7;  // Knight
            case 8 -> 6;  // Jack
            case 7 -> 5;
            case 6 -> 4;
            case 5 -> 3;
            case 4 -> 2;
            case 2 -> 1;  // Two is lowest strength
            default -> throw new IllegalArgumentException("Invalid card rank: " + rank);
        };
    }

    public Suit getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }

    public int getPoints() {
        return points;
    }

    public int getStrength() {
        return strength;
    }
}
