package projectsrc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductService {

    // Add a new product
    public String addProduct(String name, String description, double price, int quantity) {
        String query = "INSERT INTO products (name, description, price, stock) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, price);
            preparedStatement.setInt(4, quantity);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                return "Product added successfully!";
            }
        } catch (SQLException e) {
            return "Error adding product: " + e.getMessage();
        }
        return "Failed to add product.";
    }

    // View all products
    public void viewProducts() {
        String query = "SELECT * FROM products";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.printf("%-5s %-20s %-30s %-10s %-10s\n", "ID", "Name", "Description", "Price", "Stock");
            System.out.println("--------------------------------------------------------------------------");

            while (resultSet.next()) {
                System.out.printf("%-5d %-20s %-30s %-10.2f %-10d\n",
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("stock"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
    }

    // Update a product
    public String updateProduct(int productId, String name, String description, double price, int quantity) {
        String query = "UPDATE products SET name = ?, description = ?, price = ?, stock = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, price);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setInt(5, productId);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                return "Product updated successfully!";
            }
        } catch (SQLException e) {
            return "Error updating product: " + e.getMessage();
        }
        return "Failed to update product.";
    }

    // Delete a product
    public String deleteProduct(int productId) {
        String query = "DELETE FROM products WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, productId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                return "Product deleted successfully!";
            }
        } catch (SQLException e) {
            return "Error deleting product: " + e.getMessage();
        }
        return "Failed to delete product.";
    }
}
