package com.itms.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.itms.config.AppConfig;
import com.itms.ui.components.HeaderBar;
import com.itms.ui.components.SidePanel;
import com.itms.ui.screens.LoginScreen;
import com.itms.utils.SessionManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel mainContentPanel;
    private SidePanel sidePanel;
    private HeaderBar headerBar;
    private CardLayout cardLayout;
    private JPanel contentArea;

    public MainFrame() {
        setTitle("IT Management System");
        // setSize(1280, 800); // Removed fixed size in favor of maximize
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize Layout
        setLayout(new BorderLayout());

        // Check if logged in
        if (!SessionManager.isLoggedIn()) {
            showLoginScreen();
        } else {
            initMainLayout();
        }
    }

    public void showLoginScreen() {
        getContentPane().removeAll();
        add(new LoginScreen(this), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void initMainLayout() {
        getContentPane().removeAll();

        sidePanel = new SidePanel(this);
        headerBar = new HeaderBar();

        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.add(headerBar, BorderLayout.NORTH);

        contentArea = new JPanel();
        cardLayout = new CardLayout();
        contentArea.setLayout(cardLayout);
        contentArea.setBackground(AppConfig.BG_COLOR);

        mainContentPanel.add(contentArea, BorderLayout.CENTER);

        add(sidePanel, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);

        // Load default view based on role
        loadDashboard();

        revalidate();
        repaint();
    }

    private void loadDashboard() {
        String role = SessionManager.getCurrentUser().getRole();
        JPanel dashboard = null;
        String title = "Dashboard";

        if ("STOCK_MANAGER".equals(role)) {
            dashboard = new com.itms.ui.screens.stockmanager.SMDashboard(this);
            title = "Stock Manager Dashboard";
        } else if ("ADMIN".equals(role)) {
            dashboard = new com.itms.ui.screens.admin.AdminDashboard(this);
            title = "Admin Dashboard";
        } else if ("AGENT".equals(role)) {
            dashboard = new com.itms.ui.screens.agent.AgentDashboard(this);
            title = "Agent Dashboard";
        } else if ("TECHNICIAN".equals(role)) {
            dashboard = new com.itms.ui.screens.technician.TechnicianTaskView(this);
            title = "Technician Dashboard";
        } else if ("CONSULTANT".equals(role)) {
            dashboard = new com.itms.ui.screens.consultant.ConsultantView(this);
            title = "Consultant Overview";
        }

        if (dashboard != null) {
            contentArea.add(dashboard, "DASHBOARD");
            cardLayout.show(contentArea, "DASHBOARD");
            headerBar.setTitle(title);
        }
    }

    public void setView(JPanel panel, String title) {
        contentArea.removeAll();
        contentArea.add(panel, title);
        cardLayout.show(contentArea, title);
        headerBar.setTitle(title);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
