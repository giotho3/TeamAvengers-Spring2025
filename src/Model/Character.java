package Model;

import java.sql.*;

public abstract class Character {
    protected int id;
    protected String name;
    protected int health;
    protected int attackPower;

    private static final String DB_URL = "jdbc:sqlite:src/identifier.sqlite";

    public Character(int id, String name, int health, int attackPower) {
        this.id = id;
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }

    /** Handle damage taken, update health in database **/
    public void takeDamage(int damage) {
        health -= damage;
        System.out.println(name + " takes " + damage + " damage! Health left: " + health);
        updateHealthInDatabase();

        if (health <= 0) {
            die();
        }
    }

    /** Update health status in SQLite **/
    private void updateHealthInDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement("UPDATE PlayerState SET health_points = ? WHERE id = ?")) {
            stmt.setInt(1, health);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Handle character defeat **/
    protected void die() {
        System.out.println(name + " has been defeated!");
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement("UPDATE PlayerState SET health_points = 0 WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Combat interaction **/
    public void attack(Character target) {
        System.out.println(name + " attacks " + target.getName() + " for " + attackPower + " damage!");
        target.takeDamage(attackPower);
    }
}