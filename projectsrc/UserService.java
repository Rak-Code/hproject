package projectsrc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    public UserService() {
        // Ensure an admin user exists in the database
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT IGNORE INTO users (username, password, email) VALUES ('admin', 'admin123', 'admin@example.com')";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error initializing admin user: " + e.getMessage());
        }
    }

    // Register a new user
    public String registerUser(String username, String password, String email) {
        String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                return "User registered successfully!";
            }
        } catch (SQLException e) {
            return "Error registering user: " + e.getMessage();
        }
        return "Registration failed.";
    }

    // Login user
    public User loginUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getString("username"),
                                resultSet.getString("password"),
                                resultSet.getString("email"));
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
        }
        return null;
    }

    // Get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                users.add(new User(resultSet.getString("username"),
                                   resultSet.getString("password"),
                                   resultSet.getString("email")));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
        return users;
    }
}
