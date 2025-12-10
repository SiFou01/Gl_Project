package com.itms.ui.components;

import com.itms.config.AppConfig;
import com.itms.ui.MainFrame;
import com.itms.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class SidePanel extends JPanel {
    private MainFrame mainFrame;

    public SidePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(250, 800));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));

        add(createBrandHeader());
        add(Box.createVerticalStrut(20));

        String role = SessionManager.getCurrentUser().getRole();

        // Add Overview button for all roles except CONSULTANT (they have their own
        // below)
        if (!"CONSULTANT".equals(role)) {
            addMenuButton("Overview", e -> mainFrame.initMainLayout()); // Reloads dashboard logic
        }

        if ("STOCK_MANAGER".equals(role)) {
            addMenuButton("Manage Products", e -> mainFrame
                    .setView(new com.itms.ui.screens.stockmanager.ManageProductsView(mainFrame), "Manage Products"));
            addMenuButton("Manage Consumables",
                    e -> mainFrame.setView(new com.itms.ui.screens.stockmanager.ManageConsumablesView(mainFrame),
                            "Manage Consumables"));
            addMenuButton("Repair Requests", e -> mainFrame
                    .setView(new com.itms.ui.screens.stockmanager.RepairRequestsView(mainFrame), "Repair Requests"));
            addMenuButton("Request Spare Part", e -> mainFrame
                    .setView(new com.itms.ui.screens.stockmanager.RequestSparePartView(mainFrame),
                            "Request Spare Part"));
            addMenuButton("Prepare Repair Report", e -> mainFrame
                    .setView(new com.itms.ui.screens.stockmanager.PrepareRepairReportView(mainFrame),
                            "Prepare Repair Report"));
        }

        if ("ADMIN".equals(role)) {
            addMenuButton("Users",
                    e -> mainFrame.setView(new com.itms.ui.screens.admin.ManageUsersView(mainFrame), "Manage Users"));
            addMenuButton("Contracts", e -> mainFrame
                    .setView(new com.itms.ui.screens.admin.ManageContractsView(mainFrame), "Manage Contracts"));
            addMenuButton("Consumable Requests", e -> mainFrame
                    .setView(new com.itms.ui.screens.admin.ManageConsumableRequestsView(mainFrame),
                            "Consumable Requests"));
        }

        if ("AGENT".equals(role)) {
            addMenuButton("Request Consumable", e -> mainFrame
                    .setView(new com.itms.ui.screens.agent.RequestConsumableView(mainFrame), "Request Consumable"));
            addMenuButton("Request Repair", e -> mainFrame
                    .setView(new com.itms.ui.screens.agent.RequestRepairView(mainFrame), "Request Repair"));
        }

        if ("CONSULTANT".equals(role)) {
            addMenuButton("Overview", e -> mainFrame
                    .setView(new com.itms.ui.screens.consultant.ConsultantView(mainFrame), "Consultant Overview"));
        }
        add(Box.createVerticalGlue());
        addMenuButton("Logout", e -> {
            SessionManager.logout();
            mainFrame.showLoginScreen();
        });

        add(Box.createVerticalStrut(20));
    }

    private JPanel createBrandHeader() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel l = new JLabel("ITMS");
        l.setFont(new Font("Segoe UI", Font.BOLD, 24));
        l.setForeground(AppConfig.PRIMARY_COLOR);
        p.add(l);
        p.setMaximumSize(new Dimension(250, 80));
        return p;
    }

    private void addMenuButton(String text, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setBackground(Color.WHITE);
        btn.setForeground(AppConfig.TEXT_COLOR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(240, 245, 255));
                btn.setForeground(AppConfig.PRIMARY_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(AppConfig.TEXT_COLOR);
            }
        });

        btn.addActionListener(action);
        add(btn);
        add(Box.createVerticalStrut(5));
    }
}
