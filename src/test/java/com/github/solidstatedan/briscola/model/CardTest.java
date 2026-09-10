package com.github.solidstatedan.briscola.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    // Table-driven test for valid card properties (Suit, Rank, Expected Points, Expected Strength)
    @ParameterizedTest
    @CsvSource({
            "COINS,  1,  11, 10", // Ace of Coins
            "CUPS,   3,  10, 9",  // Three of Cups
            "SWORDS, 10, 4,  8",  // King of Swords
            "CLUBS,  9,  3,  7",  // Knight of Clubs
            "COINS,  8,  2,  6",  // Jack of Coins
            "CUPS,   7,  0,  5",  // Seven of Cups
            // ...
            "SWORDS, 2,  0,  1"   // Two of Swords
    })
    public void testValidCardInitialization(Suit suit, int rank, int expectedPoints, int expectedStrength) {
        Card card = new Card(suit, rank);

        assertEquals(suit, card.getSuit());
        assertEquals(rank, card.getRank());
        assertEquals(expectedPoints, card.getPoints());
        assertEquals(expectedStrength, card.getStrength());
    }

    // Table-driven test verifying error execution for boundary violations
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 11, 42})
    public void testInvalidCardRankThrowsException(int invalidRank) {
        // assertThrows expects the Exception class type and an executable lambda function
        assertThrows(IllegalArgumentException.class, () -> new Card(Suit.COINS, invalidRank));
    }
}
