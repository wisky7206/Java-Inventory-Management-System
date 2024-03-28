package org.InventoryManagementSystem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

public class DatabaseConnection {
    private Connection connection;
    
    // Constructor - Establishes the database connection
    public DatabaseConnection(String databaseName) {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            
            // Connect to the specified database
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
            System.out.println("Connected to the database.");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database: " + e.getMessage());
        }
    }
    
    // Close the database connection
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to close the database connection: " + e.getMessage());
        }
    }
    
//    Create the database schema for the application
    public void createSchema() {
        try {
            // Create the products table
            String createProductsTableQuery = "CREATE TABLE IF NOT EXISTS products (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "quantity INTEGER NOT NULL," +
                    "price REAL NOT NULL," +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP)";
            Statement createProductsTableStatement = connection.createStatement();
            createProductsTableStatement.execute(createProductsTableQuery);
            createProductsTableStatement.close();
            
            // Create the categories table
            String createCategoriesTableQuery = "CREATE TABLE IF NOT EXISTS categories (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL)";
            Statement createCategoriesTableStatement = connection.createStatement();
            createCategoriesTableStatement.execute(createCategoriesTableQuery);
            createCategoriesTableStatement.close();
            
            // Create the product_categories table
            String createProductCategoriesTableQuery = "CREATE TABLE IF NOT EXISTS product_categories (" +
                    "product_id INTEGER," +
                    "category_id INTEGER," +
                    "FOREIGN KEY (product_id) REFERENCES products(id)," +
                    "FOREIGN KEY (category_id) REFERENCES categories(id)," +
                    "PRIMARY KEY (product_id, category_id))";
            Statement createProductCategoriesTableStatement = connection.createStatement();
            createProductCategoriesTableStatement.execute(createProductCategoriesTableQuery);
            createProductCategoriesTableStatement.close();
            
            System.out.println("Schema created successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to create schema: " + e.getMessage());
        }
    }

    // Insert a product into the products table
    public void insertProduct(String name, int quantity, double price) {
        String query = "INSERT INTO products (name, quantity, price) VALUES (?, ?, ?)";
        
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setInt(2, quantity);
            statement.setDouble(3, price);
            
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Product inserted successfully.");
            } else {
                System.out.println("Failed to insert product.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to insert product: " + e.getMessage());
        }
    }
    
    // Insert a category into the categories table
    public void insertCategory(String name) {
        String query = "INSERT INTO categories (name) VALUES (?)";
        
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Category inserted successfully.");
            } else {
                System.out.println("Failed to insert category.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to insert category: " + e.getMessage());
        }
    }
    
    // Assign a category to a product in the product_categories table
    public void assignCategoryToProduct(int productId, int categoryId) {
        String query = "INSERT INTO product_categories (product_id, category_id) VALUES (?, ?)";
        
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productId);
            statement.setInt(2, categoryId);
            
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Category assigned to product successfully.");
            } else {
                System.out.println("Failed to assign category to product.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to assign category to product: " + e.getMessage());
        }
    }
    
 // Update the quantity of a product for a purchase
    public void purchaseProduct(int productId, int quantityToPurchase) {
        String query = "UPDATE products SET quantity = quantity + ? WHERE id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, quantityToPurchase);
            statement.setInt(2, productId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Product purchased successfully.");
            } else {
                System.out.println("Failed to purchase product.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to purchase product: " + e.getMessage());
        }
    }

    // Retrieve all products
    public void getAllProducts(DefaultTableModel tableModel) {
        String query = "SELECT * FROM products";
        tableModel.setRowCount(0);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");
                String createdAt = resultSet.getString("created_at");
                String updatedAt = resultSet.getString("updated_at");
                
                tableModel.addRow(new Object[]{id, name, quantity, price, createdAt, updatedAt});


            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Failed to retrieve products: " + e.getMessage());
        }
    }

    // Retrieve products by category
    public void getProductsByCategory(int categoryId,DefaultTableModel tableModel) {
        String query = "SELECT p.* FROM products p " +
                "JOIN product_categories pc ON p.id = pc.product_id " +
                "WHERE pc.category_id = ?";
        tableModel.setRowCount(0);
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");
                String createdAt = resultSet.getString("created_at");
                String updatedAt = resultSet.getString("updated_at");
                
                
                tableModel.addRow(new Object[] {id,name,quantity,price,createdAt,updatedAt});
            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Failed to retrieve products by category: " + e.getMessage());
        }
    }

 // Delete a product by ID
    public void deleteProduct(int productId) {
        String query = "DELETE FROM products WHERE id = ?";
        
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productId);
            
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Failed to delete product. Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to delete product: " + e.getMessage());
        }
    }

    // Delete a product by ID
    public void deleteCategory(int categoryId) {
        String query = "DELETE FROM categories WHERE id = ?";
        
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, categoryId);
            
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Failed to delete product. Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to delete product: " + e.getMessage());
        }
    }

    // Retrieve all categories
    public void getAllCategories(DefaultTableModel tableModel) {
        String query = "SELECT * FROM categories";
        tableModel.setRowCount(0);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                
                tableModel.addRow(new Object[]{id,name});
            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Failed to retrieve categories: " + e.getMessage());
        }
    }

 // Retrieve all ID and names 
    public void updateComboBox(JComboBox<String> cbx) {
        String query = "SELECT id,name FROM categories";
        
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                 
                cbx.addItem(id+"|"+name);
            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Failed to retrieve categories: " + e.getMessage());
        }
    }


    public String getProductNameById(int productId) {
        try {
            String query = "SELECT name FROM products WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Product Name"; // Default value if product not found or an error occurs
    }

    // Method to get the product price by ID
    public double getProductPriceById(int productId) {
        try {
            String query = "SELECT price FROM products WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; // Default value if product not found or an error occurs
    }

    public int getProductQuantityById(int productId) {
        try {
            String query = "SELECT quantity FROM products WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt("quantity");
            } else {
                return -1; // Return a negative value to indicate that the product doesn't exist
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return a negative value in case of an error
        }
    }


    // Method to calculate the total bill amount
    public double calculateTotalBillAmount(ArrayList<String> productIds, ArrayList<String> quantities) {
        double totalAmount = 0.0;

        for (int i = 0; i < productIds.size(); i++) {
            int productId = Integer.parseInt(productIds.get(i));
            int quantity = Integer.parseInt(quantities.get(i));
            double productPrice = getProductPriceById(productId);

            totalAmount += quantity * productPrice;
        }

        return totalAmount;
    }

//    Metthod to sell multiple products 
    public String sellMultipleProducts(List<String> productIds, List<String> quantities) {
        if (productIds.size() != quantities.size()) {
            return "Number of product IDs must match the number of quantities.";
            
        }

        String updateQuery = "UPDATE products SET quantity = quantity - ? WHERE id = ?";

        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            try {
				// Iterate over the product IDs and quantities
				for (int i = 0; i < productIds.size(); i++) {
				    int productId = Integer.valueOf(productIds.get(i));
				    int quantityToSell = Integer.valueOf(quantities.get(i));

				    // Update the quantity of the product
				    updateStatement.setInt(1, quantityToSell);
				    updateStatement.setInt(2, productId);
				    updateStatement.executeUpdate();
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return "Only integer values are allowed";
				
			}

            return "Products sold successfully.";

        } catch (SQLException e) {
            return "Failed to sell products: " + e.getMessage();
        }
    }

}
