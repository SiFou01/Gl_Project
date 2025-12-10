package com.itms.utils;

import java.sql.Connection;
import java.sql.Statement;

public class DataSeeder {

    public static void wipeData() {
        System.out.println("Deleting all consumables and products...");
        try (Connection conn = com.itms.config.DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            // Delete requests first due to foreign keys
            stmt.executeUpdate("DELETE FROM repair_requests");
            stmt.executeUpdate("DELETE FROM consumable_requests");

            // Delete main tables
            stmt.executeUpdate("DELETE FROM consumables");
            stmt.executeUpdate("DELETE FROM products");

            System.out.println("All data deleted successfully.");
        } catch (Exception e) {
            System.err.println("Error deleting data:");
            e.printStackTrace();
        }
    }
}
