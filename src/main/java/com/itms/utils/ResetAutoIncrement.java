package com.itms.utils;

import java.sql.Connection;
import java.sql.Statement;

public class ResetAutoIncrement {

    public static void resetIDs() {
        System.out.println("Resetting auto-increment IDs for products and consumables...");
        try (Connection conn = com.itms.config.DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            // Reset auto-increment for products table
            stmt.executeUpdate("ALTER TABLE products AUTO_INCREMENT = 1");

            // Reset auto-increment for consumables table
            stmt.executeUpdate("ALTER TABLE consumables AUTO_INCREMENT = 1");

            System.out.println("Auto-increment IDs reset successfully.");
        } catch (Exception e) {
            System.err.println("Error resetting auto-increment:");
            e.printStackTrace();
        }
    }
}
