package Model;
import java.sql.*;



public class ItemFactory {
    public static Item createItem(int id, String name, String type, int features, String description, int location) {
        //Instantiating the concrete subclass bc we want to keep Item abstract
        return new ConcreteItem(id, name, type, features, description, location);
    }
    public static Item createItem(String name) {
        String query = "SELECT * FROM Items WHERE item_name = ?";
        String DB_URL = "jdbc:sqlite:identifier.sqlite";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("item_id");
                String type = rs.getString("item_type");
                int features = rs.getInt("item_features");
                String availability = rs.getString("when_avaialble");
                String description = rs.getString("item_desc");
                int roomNumber = rs.getInt("room_number");

                return new ConcreteItem(id, name.trim(), type, features, availability,roomNumber);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching item data: " + e.getMessage());
        }

        return null; // Return null if item isn't found
    }
}