package com.itms.dao;

import com.itms.config.DatabaseConnection;
import com.itms.model.ConsumableRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsumableRequestDAO {

    public List<ConsumableRequest> getAllRequests() {
        List<ConsumableRequest> list = new ArrayList<>();
        String sql = "SELECT cr.*, c.name as consumable_name, u.full_name as agent_name " +
                "FROM consumable_requests cr " +
                "JOIN consumables c ON cr.consumable_id = c.id " +
                "JOIN users u ON cr.agent_id = u.id";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ConsumableRequest req = new ConsumableRequest(
                        rs.getInt("id"),
                        rs.getInt("consumable_id"),
                        rs.getInt("agent_id"),
                        rs.getInt("quantity"),
                        rs.getString("status"),
                        rs.getString("request_date"),
                        rs.getString("message"));
                req.setConsumableName(rs.getString("consumable_name"));
                req.setAgentName(rs.getString("agent_name"));
                list.add(req);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ConsumableRequest> getRequestsByAgentId(int agentId) {
        List<ConsumableRequest> list = new ArrayList<>();
        String sql = "SELECT cr.*, c.name as consumable_name, u.full_name as agent_name " +
                "FROM consumable_requests cr " +
                "JOIN consumables c ON cr.consumable_id = c.id " +
                "JOIN users u ON cr.agent_id = u.id " +
                "WHERE cr.agent_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, agentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ConsumableRequest req = new ConsumableRequest(
                            rs.getInt("id"),
                            rs.getInt("consumable_id"),
                            rs.getInt("agent_id"),
                            rs.getInt("quantity"),
                            rs.getString("status"),
                            rs.getString("request_date"),
                            rs.getString("message"));
                    req.setConsumableName(rs.getString("consumable_name"));
                    req.setAgentName(rs.getString("agent_name"));
                    list.add(req);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void createRequest(ConsumableRequest req) {
        String sql = "INSERT INTO consumable_requests (consumable_id, agent_id, quantity, status, request_date, message) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, req.getConsumableId());
            pstmt.setInt(2, req.getAgentId());
            pstmt.setInt(3, req.getQuantity());
            pstmt.setString(4, req.getStatus());
            pstmt.setString(5, req.getRequestDate());
            pstmt.setString(6, req.getMessage());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(int id, String status) {
        String sql = "UPDATE consumable_requests SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
