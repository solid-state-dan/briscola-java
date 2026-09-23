package com.github.solidstatedan.briscola.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {

    private Player p1;
    private Player p2;
    private Team team;

    @BeforeEach
    public void setUp() {
        p1 = new Player("P1", PlayerType.HUMAN);
        p2 = new Player("P2", PlayerType.EASY_AI);
        team = new Team("TEAM X", p1, p2);
    }

    @Test
    public void testTeamInitialization() {

        // Assert team name.
        assertEquals("TEAM X", team.getName());

        // Assert Player 1.
        assertEquals(p1, team.getPlayer1());

        // Assert Player 2.
        assertEquals(p2, team.getPlayer2());

        // A team should start with 0 points.
        assertEquals(0, team.getPoints());
    }

    @Test
    public void teamAddPointsCorrectly() {

        team.addPoints(4);
        assertEquals(4, team.getPoints());

        team.addPoints(11);
        assertEquals(15, team.getPoints());
    }

    @Test
    public void testCannotCreateTeamWithSamePlayer() {
        assertThrows(IllegalArgumentException.class, () -> new Team("INVALID TEAM", p1, p1));
    }

    @Test
    public void testCannotCreateTeamWithNoPlayers() {
        assertThrows(IllegalArgumentException.class, () -> new Team("INVALID TEAM", null, null));
    }

    @Test
    public void testCannotAddNegativePoints() {
        assertThrows(IllegalArgumentException.class, () -> team.addPoints(-100));
    }
}
