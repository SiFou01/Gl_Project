package com.itms.dao;

import com.itms.config.DatabaseConnection;
import com.itms.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getInt("quantity"),
                rs.getString("status"),
                rs.getString("barcode"),
                rs.getString("location"),
                rs.getString("supplier"),
                rs.getString("purchase_date"),
                rs.getString("warranty_end"),
                rs.getString("description"),
                rs.getDouble("price"));
    }

    public void addProduct(Product p) {
        String sql = "INSERT INTO products (name, category, quantity, status, barcode, location, supplier, purchase_date, warranty_end, description, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getName());
            pstmt.setString(2, p.getCategory());
            pstmt.setInt(3, p.getQuantity());
            pstmt.setString(4, p.getStatus());
            pstmt.setString(5, p.getBarcode());
            pstmt.setString(6, p.getLocation());
            pstmt.setString(7, p.getSupplier());
            pstmt.setString(8, p.getPurchaseDate());
            pstmt.setString(9, p.getWarrantyEnd());
            pstmt.setString(10, p.getDescription());
            pstmt.setDouble(11, p.getPrice());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getProductIdByBarcode(String barcode) {
        String sql = "SELECT id FROM products WHERE barcode = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, barcode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
