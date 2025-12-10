package com.itms.dao;

import com.itms.config.DatabaseConnection;
import com.itms.model.Consumable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsumableDAO {

    public List<Consumable> getAllConsumables() {
        List<Consumable> list = new ArrayList<>();
        String sql = "SELECT * FROM consumables";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Consumable(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getString("status"),
                        rs.getString("supplier")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addConsumable(Consumable c) {
        String sql = "INSERT INTO consumables (name, category, quantity, status, supplier) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getName());
            pstmt.setString(2, c.getCategory());
            pstmt.setInt(3, c.getQuantity());
            pstmt.setString(4, c.getStatus());
            pstmt.setString(5, c.getSupplier());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteConsumable(int id) {
        String sql = "DELETE FROM consumables WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getConsumableIdByName(String name) {
        String sql = "SELECT id FROM consumables WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
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
