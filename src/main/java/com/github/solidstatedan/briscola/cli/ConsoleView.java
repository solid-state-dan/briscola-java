package com.github.solidstatedan.briscola.cli;

import com.github.solidstatedan.briscola.controller.GameEngine;
import com.github.solidstatedan.briscola.model.*;

import java.util.List;

/**
 * Handles all terminal rendering and formatted text output for the Briscola CLI.
 * <p>
 * This class is responsible purely for presentation (View layer) and contains
 * no game logic or input-handling mechanisms.
 **/
public class ConsoleView {

    private static final String DIVIDER = "==================================================";
    private static final String SUB_DIVIDER = "--------------------------------------------------";

    /**
     * Prints the initial welcome banner and basic point scoring rules.
     */
    public void printWelcome() {
        System.out.println(DIVIDER);
        System.out.println("             WELCOME TO BRISCOLA                  ");
        System.out.println(DIVIDER);
        System.out.println("Cards: Aces (11pt), 3s (10pt), Kings[10] (4pt), ");
        System.out.println("       Queens[9] (3pt), Jacks[8] (2pt). Others (0pt).\n");
    }

    /**
     * Renders the current state of the game table, including team scores,
     * remaining deck count, active trump card/suit, and cards played in the current trick.
     *
     * @param engine the active {@link GameEngine} providing the current state
     */
    public void printTableState(GameEngine engine) {
        System.out.println("\n" + DIVIDER);

        // Scores
        System.out.printf("SCORES -> %s: %d pts | %s: %d pts%n",
                engine.getTeamA().getName(), engine.getTeamA().getPoints(),
                engine.getTeamB().getName(), engine.getTeamB().getPoints());

        // Deck & Trump Info
        int cardsLeft = engine.getDeck().getSize();
        Card trumpCard = engine.getTrumpCard();
        Suit trumpSuit = engine.getTrumpSuit();

        if (trumpCard != null) {
            System.out.printf("DECK: %d cards left | TRUMP CARD: %s (Suit: %s)%n",
                    cardsLeft, trumpCard.getName(), trumpSuit);
        } else {
            System.out.printf("DECK: Empty | TRUMP SUIT: %s%n", trumpSuit);
        }

        System.out.println(SUB_DIVIDER);

        // Current Trick on Table
        Trick currentTrick = engine.getCurrentTrick();
        if (currentTrick.getTrickSize() == 0) {
            System.out.println("TRICK ON TABLE: [ Empty ]");
        } else {
            System.out.println("TRICK ON TABLE:");
            for (Card pc : currentTrick.getPlayedCards()) {
                System.out.printf("  - %s played %s%n", currentTrick.getPlayerForCard(pc).getName(), pc.getName());
            }
        }
        System.out.println(DIVIDER);
    }

    /**
     * Displays the specified human player's turn prompt along with their current hand indexed for selection.
     *
     * @param player the human {@link Player} whose turn it is
     */
    public void printPlayerHand(Player player) {
        System.out.printf("%nIt's %s's turn!%n", player.getName());
        System.out.println("Your hand:");
        List<Card> cards = player.getHand().getCards();
        for (int i = 0; i < cards.size(); i++) {
            System.out.printf("  [%d] %s%n", i, cards.get(i).getName());
        }
    }

    /**
     * Prints an action message announcing which card a player played.
     *
     * @param p the {@link Player} who played
     * @param cardPlayed the {@link Card} played
     */
    public void printPlayerAction(Player p, Card cardPlayed) {
        System.out.printf(">> %s played: %s%n", p.getName(), cardPlayed.getName());
    }

    /**
     * Announces the winner of a completed trick and the points won.
     *
     * @param winner the {@link Player} who won the trick
     * @param points total point value of the cards in the trick
     */
    public void printTrickWinner(Player winner, int points) {
        System.out.printf("%n*** %s WON THE TRICK (+%d pts) ***%n%n", winner.getName(), points);
    }

    /**
     * Displays the end-of-game summary screen with final team scores and the overall winner.
     *
     * @param teamA the first {@link Team}
     * @param teamB the second {@link Team}
     */
    public void printGameOver(Team teamA, Team teamB) {
        System.out.println("\n" + DIVIDER);
        System.out.println("                    GAME OVER                     ");
        System.out.println(DIVIDER);
        System.out.printf("%s: %d points%n", teamA.getName(), teamA.getPoints());
        System.out.printf("%s: %d points%n", teamB.getName(), teamB.getPoints());
        System.out.println(SUB_DIVIDER);

        if (teamA.getPoints() > teamB.getPoints()) {
            System.out.printf("WINNER: %s! Congratulations!%n", teamA.getName());
        } else if (teamB.getPoints() > teamA.getPoints()) {
            System.out.printf("WINNER: %s! Congratulations!%n", teamB.getName());
        } else {
            System.out.println("IT'S A DRAW!");
        }
        System.out.println(DIVIDER + "\n");
    }

    /**
     * Displays an error message when an out-of-bounds or non-numeric hand choice is entered.
     *
     * @param maxIndex the maximum valid 0-based card index in the hand
     */
    public void printInvalidInput(int maxIndex) {
        System.out.printf("Invalid choice! Please enter a number between 0 and %d.%n", maxIndex);
    }
}
