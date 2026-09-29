package com.github.solidstatedan;

import com.github.solidstatedan.briscola.cli.ConsoleController;
import com.github.solidstatedan.briscola.cli.ConsoleView;
import com.github.solidstatedan.briscola.controller.GameEngine;
import com.github.solidstatedan.briscola.model.*;

/**
 * Application entry point for the Briscola CLI game.
 */
public class Main {

    public static void main(String[] args) {
        // Create Players (1 Human, 3 Easy AIs)
        Player humanPlayer = new Player("You", PlayerType.HUMAN);
        Player aiPlayer1 = new Player("Partner (North) (AI)", PlayerType.EASY_AI);
        Player aiPlayer2 = new Player("Opponent 1 (East), (AI)", PlayerType.EASY_AI);
        Player aiPlayer3 = new Player("Opponent 2 (West), (AI)", PlayerType.EASY_AI);

        // Group into Teams
        // Team A: You + AI Partner
        // Team B: Opponent 1 + Opponent 2
        Team teamA = new Team("Team Human & Partner", humanPlayer, aiPlayer1);
        Team teamB = new Team("Team AI Opponents", aiPlayer2, aiPlayer3);

        // Instantiate Model, View, and Controller
        GameEngine engine = new GameEngine(teamA, teamB);
        ConsoleView view = new ConsoleView();
        ConsoleController controller = new ConsoleController(engine, view);

        // Start the game
        controller.start();
    }
}
