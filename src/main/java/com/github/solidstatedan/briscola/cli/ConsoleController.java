package com.github.solidstatedan.briscola.cli;

import com.github.solidstatedan.briscola.controller.GameEngine;
import com.github.solidstatedan.briscola.model.*;

import java.util.Scanner;

/**
 * Orchestrates the CLI game loop, handling user input and turns execution.
 */
public class ConsoleController {

    private final GameEngine engine;
    private final ConsoleView view;
    private final Scanner scanner;

    public ConsoleController(GameEngine engine, ConsoleView view) {
        this.engine = engine;
        this.view = view;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts and runs the main game loop until the game is over.
     */
    public void start() {
        view.printWelcome();
        engine.startNewGame();

        while (!engine.isGameOver()) {
            Player currentPlayer = engine.getCurrentPlayer();

            if (currentPlayer.getType() == PlayerType.HUMAN) {
                handleHumanTurn(currentPlayer);
            } else {
                handleAiTurn(currentPlayer);
            }
        }

        view.printGameOver(engine.getTeamA(), engine.getTeamB());
    }

    private void handleHumanTurn(Player human) {
        view.printTableState(engine);
        view.printPlayerHand(human);

        int handSize = human.getHand().getSize();
        int chosenIndex = -1;

        while (true) {
            System.out.printf("Select a card to play (0 - %d): ", handSize - 1);
            String input = scanner.nextLine().trim();

            try {
                chosenIndex = Integer.parseInt(input);
                if (chosenIndex >= 0 && chosenIndex < handSize) {
                    break; // Valid input, exit while loop.
                } else {
                    view.printInvalidInput(handSize - 1);
                }
            } catch (NumberFormatException e) {
                view.printInvalidInput(handSize - 1);
            }
        }

        executeTurn(chosenIndex);
    }

    private void handleAiTurn(Player aiPlayer) {
        System.out.printf("%nIt's %s's turn...%n", aiPlayer.getName());

        // Add delay so AI turns don't instantly flood the terminal.
        pause(800);

        // Easy AI always plays card at index 0.
        executeTurn(0);
    }

    private void executeTurn(int cardIndex) {
        Player currentPlayer = engine.getCurrentPlayer();
        int trickSizeBefore = engine.getCurrentTrick().getTrickSize();

        view.printPlayerAction(currentPlayer, currentPlayer.getHand().getCards().get(cardIndex));
        engine.playTurn(cardIndex);

        // If a trick just completed, announce the trick's winner and the relative points.
        if (trickSizeBefore == 3 && engine.getLastTrickWinner() != null) {
            Player winner = engine.getLastTrickWinner();
            view.printTrickWinner(winner, engine.getLastTrickPoints());
            pause(1200);
        }
    }

    private void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
