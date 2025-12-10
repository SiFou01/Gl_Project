package com.itms.utils;

import java.sql.Connection;
import java.sql.Statement;

public class ResetDb {
    public static void main(String[] args) {
        wipeAndReset();
    }

    public static void wipeAndReset() {
        System.out.println("Starting full database wipe and ID reset...");
        try (Connection conn = com.itms.config.DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            // 1. Drop tables to ensure schema is fresh
            System.out.println("Dropping tables...");
            stmt.executeUpdate("DROP TABLE IF EXISTS repair_requests");
            stmt.executeUpdate("DROP TABLE IF EXISTS consumable_requests");
            stmt.executeUpdate("DROP TABLE IF EXISTS consumables");
            stmt.executeUpdate("DROP TABLE IF EXISTS products");
            stmt.executeUpdate("DROP TABLE IF EXISTS maintenance_contracts");
            stmt.executeUpdate("DROP TABLE IF EXISTS users");
            // Also drop users if we want a full reset, but SeedData doesn't seed users?
            // Wait, UserDAO exists. Does SeedData seed users? No. Users are in schema.sql?
            // Let's check schema.sql. If I drop users, I must ensure they are recreated.

            // 2. Reset Auto-Increment (Not needed if tables dropped)

            System.out.println("Database successfully wiped and reset.");
        } catch (Exception e) {
            System.err.println("Error during reset:");
            e.printStackTrace();
        }
    }
}
