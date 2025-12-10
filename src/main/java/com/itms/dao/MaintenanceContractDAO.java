package com.itms.dao;

import com.itms.config.DatabaseConnection;
import com.itms.model.MaintenanceContract;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceContractDAO {

    public List<MaintenanceContract> getAllContracts() {
        List<MaintenanceContract> list = new ArrayList<>();
        String sql = "SELECT * FROM maintenance_contracts";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new MaintenanceContract(
                        rs.getInt("id"),
                        rs.getString("supplier_name"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("status"),
                        rs.getString("terms"),
                        rs.getString("product")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addContract(MaintenanceContract c) {
        String sql = "INSERT INTO maintenance_contracts (supplier_name, start_date, end_date, status, terms, product) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getSupplierName());
            pstmt.setString(2, c.getStartDate());
            pstmt.setString(3, c.getEndDate());
            pstmt.setString(4, c.getStatus());
            pstmt.setString(5, c.getTerms());
            pstmt.setString(6, c.getProduct());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateContract(MaintenanceContract c) {
        String sql = "UPDATE maintenance_contracts SET supplier_name = ?, start_date = ?, end_date = ?, status = ?, terms = ?, product = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getSupplierName());
            pstmt.setString(2, c.getStartDate());
            pstmt.setString(3, c.getEndDate());
            pstmt.setString(4, c.getStatus());
            pstmt.setString(5, c.getTerms());
            pstmt.setString(6, c.getProduct());
            pstmt.setInt(7, c.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteContract(int id) {
        String sql = "DELETE FROM maintenance_contracts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
