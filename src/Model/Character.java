package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public abstract class Character {
    protected int id;
    protected String name;
    protected int health;
    protected int attackPower;

    private static final Logger LOGGER = Logger.getLogger(Character.class.getName());
    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";

    public Character(int health, int attack) {
        this.health = health;
        this.attackPower = attack;


    }

    public Character(int id, String name, int health, int attackPower) {
        this.id = id;
        this.name = name;
        this.health = Math.max(health, 0); // Prevent negative health
        this.attackPower = attackPower;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getAttackPower() { return attackPower; }

    /** Handle damage and prevent negative health **/
    public void takeDamage(int damage) {
        health = Math.max(health - damage, 0);
        System.out.println(name + " takes " + damage + " damage! Health left: " + health);
        updateHealthInDatabase();

        if (health == 0) {
            die();
        }
    }

    /** Database update **/
    private void updateHealthInDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement("UPDATE PlayerState SET health_points = ? WHERE id = ?")) {
                stmt.setInt(1, health);
                stmt.setInt(2, id);
                stmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                LOGGER.severe("Failed to update health: " + e.getMessage());
            }
        } catch (SQLException e) {
            LOGGER.severe("Database connection error: " + e.getMessage());
        }
    }

    /** Handle defeat **/
    protected void die() {
        System.out.println(name + " has been defeated!");
    }

    /** Combat interaction with critical hit chance **/
    public void attack(Character target) {
        int damage = attackPower;
        if (Math.random() < 0.2) { // 20% chance of critical hit
            damage *= 1.5;
            System.out.println(name + " lands a CRITICAL HIT!");
        }

        System.out.println(name + " attacks " + target.getName() + " for " + damage + " damage!");
        target.takeDamage(damage);
    }
}