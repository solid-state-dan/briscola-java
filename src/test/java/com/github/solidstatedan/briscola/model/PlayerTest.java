package com.github.solidstatedan.briscola.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testPlayerInitialization() {

        Player player = new Player("BOT", PlayerType.EASY_AI);

        // Assert name.
        assertEquals("BOT", player.getName());

        // Assert player type.
        assertEquals(PlayerType.EASY_AI, player.getType());

        // Assert that it's a Player.
        assertInstanceOf(Player.class, player);

        // Initial hand should be empty.
        assertEquals(0, player.getHand().getSize());
    }
}
