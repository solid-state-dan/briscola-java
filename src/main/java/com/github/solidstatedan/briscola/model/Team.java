package com.github.solidstatedan.briscola.model;

/**
 * Represents a 2-player team in a 4-player Briscola game.
 * Manages the team name, player associations, and the running total points.
 */
public class Team {

    // FIELDS
    private final String name;
    private final Player p1;
    private final Player p2;
    private int points = 0;

    // CONSTRUCTOR
    /**
     * Constructs a new Team with a name and two distinct players.
     *
     * @param name The identification name for this team.
     * @param p1   The first player on the team.
     * @param p2   The second player on the team.
     * @throws IllegalArgumentException If either player is null, or if p1 and p2
     *                                  refer to the exact same player instance.
     */
    public Team(String name, Player p1, Player p2) {

        if (p1 == null || p2 == null) {
            throw new IllegalArgumentException("Players cannot be null.");
        }
        if (p1.equals(p2)) {
            throw new IllegalArgumentException("Team must consist of two different players.");
        }

        this.name = name;
        this.p1 = p1;
        this.p2 = p2;
    }

    // METHODS
    /**
     * Add points to the team's total score.
     *
     * @param pointsToAdd Points scored (must be non-negative).
     * @throws IllegalArgumentException If the provided point value is negative.
     */
    public void addPoints(int pointsToAdd) {
        if (pointsToAdd < 0) {
            throw new IllegalArgumentException("Cannot add negative points: " + pointsToAdd);
        }
        this.points += pointsToAdd;
    }

    // GETTERS
    /**
     * Gets the name identification of the team.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the 1st player instance of the team.
     */
    public Player getPlayer1() {
        return p1;
    }

    /**
     * Gets the 2nd player instance of the team.
     */
    public Player getPlayer2() {
        return p2;
    }

    /**
     * Gets the total score accumulated by this team.
     */
    public int getPoints() {
        return points;
    }
}
