package com.itms.dao;

import com.itms.config.DatabaseConnection;
import com.itms.model.RepairRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepairRequestDAO {

    public List<RepairRequest> getAllRequests() {
        List<RepairRequest> list = new ArrayList<>();
        String sql = "SELECT r.*, p.name as product_name, u.full_name as agent_name " +
                "FROM repair_requests r " +
                "JOIN products p ON r.product_id = p.id " +
                "JOIN users u ON r.agent_id = u.id";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                RepairRequest req = new RepairRequest(
                        rs.getInt("id"),
                        rs.getInt("product_id"),
                        rs.getInt("agent_id"),
                        rs.getString("issue_description"),
                        rs.getString("status"),
                        rs.getString("request_date"),
                        rs.getString("priority"));
                req.setProductName(rs.getString("product_name"));
                req.setAgentName(rs.getString("agent_name"));
                list.add(req);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<RepairRequest> getRequestsByAgentId(int agentId) {
        List<RepairRequest> list = new ArrayList<>();
        String sql = "SELECT r.*, p.name as product_name, u.full_name as agent_name " +
                "FROM repair_requests r " +
                "JOIN products p ON r.product_id = p.id " +
                "JOIN users u ON r.agent_id = u.id " +
                "WHERE r.agent_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, agentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    RepairRequest req = new RepairRequest(
                            rs.getInt("id"),
                            rs.getInt("product_id"),
                            rs.getInt("agent_id"),
                            rs.getString("issue_description"),
                            rs.getString("status"),
                            rs.getString("request_date"),
                            rs.getString("priority"));
                    req.setProductName(rs.getString("product_name"));
                    req.setAgentName(rs.getString("agent_name"));
                    list.add(req);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void createRequest(RepairRequest req) {
        String sql = "INSERT INTO repair_requests (product_id, agent_id, issue_description, status, request_date, priority) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, req.getProductId());
            pstmt.setInt(2, req.getAgentId());
            pstmt.setString(3, req.getIssueDescription());
            pstmt.setString(4, req.getStatus());
            pstmt.setString(5, req.getRequestDate());
            pstmt.setString(6, req.getPriority());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(int id, String status) {
        String sql = "UPDATE repair_requests SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isProductInRepair(int productId) {
        String sql = "SELECT COUNT(*) as count FROM repair_requests WHERE product_id = ? AND status IN ('PENDING', 'APPROVED', 'IN_PROGRESS')";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
