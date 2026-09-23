package com.github.solidstatedan.briscola.model;

import java.util.*;

/**
 * Represents a single trick in a Briscola round where players take turns laying down a card.
 * Chronologically tracks played cards and maps each card to the player who laid it down.
 */
public class Trick {

    // FIELDS
    /** Takes track of the played cards and the order they were played. */
    private final List<Card> playedCards = new ArrayList<>();

    /** Maps each played card to the player who laid it down. */
    private final Map<Card,Player> cardToPlayerMap = new HashMap<>();

    // METHODS

    /**
     * Plays a card into this trick on behalf of a specific player.
     *
     * @param p The player laying down the card.
     * @param c The card being played.
     * @throws IllegalArgumentException If either the player or card is null, if the
     *                                  card has already been played, or if the player
     *                                  has already played its turn in this trick.
     */
    public void playCard(Player p, Card c) {
        if (p == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        if (c == null) {
            throw new IllegalArgumentException("Card cannot be null.");
        }
        if (cardToPlayerMap.get(c) != null) {
            throw new IllegalArgumentException("This card was already played.");
        }
        if (cardToPlayerMap.containsValue(p)) {
            throw new IllegalArgumentException("This player has already played their turn.");
        }

        playedCards.add(c);
        cardToPlayerMap.put(c, p);
    }

    // GETTERS
    /**
     * Gets a read-only view of the cards played in this trick, preserving chronological order.
     *
     * @return An unmodifiable list of played cards.
     */
    public List<Card> getPlayedCards() {
        return Collections.unmodifiableList(playedCards);
    }

    /**
     * Gets the number of cards played up to this point in this trick.
     */
    public int getTrickSize() {
        return playedCards.size();
    }

    /**
     * Gets a read-only view of the card-to-player mappings for this trick.
     *
     * @return An unmodifiable map of cards to their respective players.
     */
    public Map<Card, Player> getCardToPlayerMap() {
        return Collections.unmodifiableMap(cardToPlayerMap);
    }

    /**
     * Given a card, gets the player who laid it down in this trick.
     *
     * @param c The card to look up.
     * @return The player who laid it down.
     * @throws IllegalArgumentException If the provided card has not been played yet in this trick.
     */
    public Player getPlayerForCard(Card c) {
        if (!cardToPlayerMap.containsKey(c)) {
            throw new IllegalArgumentException("Provided card has not been played yet.");
        }

        return cardToPlayerMap.get(c);
    }
}
