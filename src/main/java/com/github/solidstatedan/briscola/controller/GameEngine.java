package com.github.solidstatedan.briscola.controller;

import com.github.solidstatedan.briscola.model.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Coordinates the core game loop and rules for a 4-player Briscola game.
 * <p>
 * Manages player turns, trick evaluations, hand replenishment, score tracking,
 * and game state progression across teams.
 */
public class GameEngine {

    // FIELDS
    private final Team teamA, teamB;
    private final List<Player> players = new ArrayList<>(4);
    private final TrickEvaluator evaluator = new TrickEvaluator();

    private Deck deck;
    private Card trumpCard;
    private Suit trumpSuit;
    private int currentPlayerIndex = 0;
    private Trick currentTrick;
    private Player lastTrickWinner;
    private boolean isGameOver;


    // CONSTRUCTOR
    /**
     * Constructs a new GameEngine with two opposing teams.
     * Seats players in alternating team order: Team A1, Team B1, Team A2, Team B2.
     *
     * @param teamA the first team
     * @param teamB the second team
     * @throws IllegalArgumentException if either team parameter is null
     */
    public GameEngine(Team teamA, Team teamB) {

        if (teamA == null || teamB == null) {
            throw new IllegalArgumentException("A team cannot be null.");
        }
        this.teamA = teamA;
        this.teamB = teamB;

        // Standard alternating seating order.
        players.add(teamA.getPlayer1());
        players.add(teamB.getPlayer1());
        players.add(teamA.getPlayer2());
        players.add(teamB.getPlayer2());
    }

    // METHODS
    /**
     * Initializes and starts a fresh game of Briscola.
     * Shuffles the deck, deals initial hands of 3 cards to all players,
     * reveals the trump card, and resets the game state.
     */
    public void startNewGame() {

        // Reset
        this.isGameOver = false;
        this.deck = new Deck();
        this.deck.shuffle();
        this.currentTrick = new Trick();
        this.currentPlayerIndex = 0;
        this.lastTrickWinner = null;

        teamA.resetPoints();
        teamB.resetPoints();

        // Reset player hands in case of restart.
        for (Player p : players) {
            p.getHand().clear();
        }

        // In order, deal 1 card to each player until they all have 3.
        for (int i = 0; i < 3; i++) {
            for (Player p : players) {
                p.getHand().addCard(deck.draw());
            }
        }

        // Expose top card from deck as trump card.
        trumpCard = deck.draw();
        trumpSuit = trumpCard.getSuit();
    }

    /**
     * Executes a single turn for the active player.
     * If the current player is a Human, plays the card at the specified index.
     * If the current player is an AI, delegates card selection to the AI strategy.
     *
     * @param cardIndex the 0-based index of the card to play from hand (if human)
     * @throws IllegalStateException if the game is already over
     */
    public void playTurn(int cardIndex) {

        if (isGameOver) {
            throw new IllegalStateException("Cannot play a turn, game already ended.");
        }

        // Current player plays a card.
        Player currentPlayer = getCurrentPlayer();
        Card playedCard;

        if (currentPlayer.getType() == PlayerType.HUMAN) {
            // Human plays the exact card they selected in CLI / GUI
            playedCard = currentPlayer.getHand().playCard(cardIndex);
        } else {
            // AI delegates to its selection strategy (currently picks index 0)
            playedCard = selectAiCard(currentPlayer);
        }

        currentTrick.playTurn(currentPlayer, playedCard);

        // If trick is complete, resolve it.
        if (currentTrick.getTrickSize() == 4) {
            resolveTrick();
        } // Otherwise, advance to next player.
        else {
            currentPlayerIndex = (currentPlayerIndex + 1) % 4;
        }

        // Check if the game is over:
        if (checkAllHandsEmpty()) {
            this.isGameOver = true;
        }
    }

    private Card selectAiCard(Player aiPlayer) {
        return switch (aiPlayer.getType()) {
            case EASY_AI -> aiPlayer.getHand().playCard(0);
            // will implement other difficulties later on.
            default -> aiPlayer.getHand().playCard(0);
        };
    }

    private boolean checkAllHandsEmpty() {

        if (deck.getSize() != 0 || trumpCard != null) {
            return false;
        }

        for (Player p : players) {
            if (p.getHand().getSize() != 0) {
                return false;
            }
        }

        return true;
    }

    private void replenishHands() {
        for (int i = 0; i < 4; i++) {

            int targetPlayerIndex = (currentPlayerIndex + i) % 4;
            Player playerToDraw = players.get(targetPlayerIndex);

            // Can still draw.
            if (deck.getSize() > 0) {
                playerToDraw.getHand().addCard(deck.draw());
            }
            // There's only the trump card left to draw.
            else if (deck.getSize() == 0 && trumpCard != null) {
                playerToDraw.getHand().addCard(trumpCard);
                trumpCard = null;
            }

            // else if deck is 0 and trump is null, don't have to do nothing.
        }
    }

    private void resolveTrick() {

        // Assign points to the winner team.
        Player winner = evaluator.evaluateWinner(currentTrick, trumpSuit);
        Team winnerTeam = getTeamForPlayer(winner);
        int pointsWon = evaluator.calculateTrickPoints(currentTrick);

        winnerTeam.addPoints(pointsWon);

        // Save whom won this trick.
        lastTrickWinner = winner;

        // Reset trick, and the winner will start first next trick.
        currentTrick = new Trick();
        currentPlayerIndex = players.indexOf(winner);

        replenishHands();
    }

    // GETTERS
    /**
     * Gets the current deck.
     *
     * @return the active deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Gets the face-up trump card at the bottom of the draw pile.
     *
     * @return the trump card, or {@code null} if the trump card has been drawn
     */
    public Card getTrumpCard() {
        return trumpCard;
    }

    /**
     * Gets the active trump suit for the game.
     *
     * @return the trump suit
     */
    public Suit getTrumpSuit() {
        return trumpSuit;
    }

    /**
     * Gets the zero-based index of the player whose turn it currently is.
     *
     * @return active player index (0 to 3)
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Gets the player whose turn it currently is.
     *
     * @return active {@link Player}
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Finds which team a given player belongs to.
     *
     * @param p the player to search for
     * @return the {@link Team} containing the player
     */
    public Team getTeamForPlayer(Player p) {
        if (teamA.getPlayer1().equals(p) || teamA.getPlayer2().equals(p)) {
            return teamA;
        }
        return teamB;
    }

    /**
     * Gets the player who won the most recently completed trick.
     *
     * @return winner of the last trick, or {@code null} if no trick has completed yet
     */
    public Player getLastTrickWinner() {
        return lastTrickWinner;
    }

    /**
     * Gets an unmodifiable view of the players in their seating order.
     *
     * @return unmodifiable list of players
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /**
     * Checks if all cards have been played and the game has concluded.
     *
     * @return {@code true} if game is over; {@code false} otherwise
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Gets the current trick on the table.
     *
     * @return active {@link Trick}
     */
    public Trick getCurrentTrick() {
        return currentTrick;
    }

    /**
     * Gets Team A.
     *
     * @return Team A
     */
    public Team getTeamA() {
        return teamA;
    }

    /**
     * Gets Team B.
     *
     * @return Team B
     */
    public Team getTeamB() {
        return teamB;
    }
}
