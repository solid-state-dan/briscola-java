package com.github.solidstatedan.briscola.model;

/**
 * Represents a player in a game match.
 * Each player carries a name, an intelligence profile type,
 * and an individual card hand.
 */
public class Player {

    // FIELDS
    private final String name;
    private final PlayerType playerType;
    private final Hand hand;

    // CONSTRUCTOR
    /**
     * Initializes a player with an empty hand.
     * @param name The player's display name.
     * @param playerType The {@link PlayerType} profile directing this player's choices.
     */
    public Player(String name, PlayerType playerType) {
        this.name = name;
        this.playerType = playerType;
        this.hand = new Hand();
    }

    // METHODS
    // GETTERS
    /**
     * Gets the player's display name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's current hand.
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Gets the player's {@link PlayerType} profile.
     */
    public PlayerType getType() {
        return playerType;
    }
}
