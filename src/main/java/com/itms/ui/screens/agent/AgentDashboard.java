package com.itms.ui.screens.agent;

import com.itms.config.AppConfig;
import com.itms.dao.ConsumableRequestDAO;
import com.itms.dao.RepairRequestDAO;
import com.itms.model.ConsumableRequest;
import com.itms.model.RepairRequest;
import com.itms.ui.MainFrame;
import com.itms.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AgentDashboard extends JPanel {

    private MainFrame mainFrame;

    public AgentDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppConfig.BG_COLOR);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Main Scrollable Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(AppConfig.BG_COLOR);

        // Header
        // content.add(createHeader()); // Removed as per user request
        // content.add(Box.createVerticalStrut(30)); // Removed styling space

        // Quick Actions
        content.add(createQuickActions());
        content.add(Box.createVerticalStrut(30));

        // Fetch Data
        int userId = SessionManager.getCurrentUser().getId();
        List<RepairRequest> repairs = new RepairRequestDAO().getRequestsByAgentId(userId);
        List<ConsumableRequest> consumables = new ConsumableRequestDAO().getRequestsByAgentId(userId);

        // Stats Cards
        content.add(createStatsCards(repairs, consumables));
        content.add(Box.createVerticalStrut(30));

        // Recent Activity
        content.add(createRecentRequests(repairs, consumables));

        JScrollPane scrollPane = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppConfig.BG_COLOR);
        panel.setMaximumSize(new Dimension(2000, 60));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(AppConfig.BG_COLOR);

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel subtitle = new JLabel("Welcome back, " + SessionManager.getCurrentUser().getFullName().split(" ")[0]
                + "! Here's what's happening today.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);

        left.add(title);
        left.add(subtitle);

        panel.add(left, BorderLayout.WEST);

        // User Profile (simplified)
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(AppConfig.BG_COLOR);
        JLabel userLabel = new JLabel(SessionManager.getCurrentUser().getFullName());
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        right.add(userLabel);

        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    private JPanel createQuickActions() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(AppConfig.BG_COLOR);
        panel.setMaximumSize(new Dimension(2000, 50));

        JButton reqConsumable = createActionButton("Request Consumable", new Color(13, 110, 253));
        reqConsumable
                .addActionListener(e -> mainFrame.setView(new RequestConsumableView(mainFrame), "Request Consumable"));

        JButton reqRepair = createActionButton("Request Repair", Color.WHITE);
        reqRepair.setForeground(Color.BLACK);
        reqRepair.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        reqRepair.addActionListener(e -> mainFrame.setView(new RequestRepairView(mainFrame), "Request Repair"));

        panel.add(reqConsumable);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(reqRepair);

        return panel;
    }

    private JButton createActionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(180, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createStatsCards(List<RepairRequest> repairs, List<ConsumableRequest> consumables) {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(AppConfig.BG_COLOR);
        panel.setMaximumSize(new Dimension(2000, 150));

        int total = repairs.size() + consumables.size();

        long pending = repairs.stream().filter(r -> "PENDING".equalsIgnoreCase(r.getStatus())).count() +
                consumables.stream().filter(c -> "PENDING".equalsIgnoreCase(c.getStatus())).count();

        long completed = repairs.stream().filter(
                r -> "COMPLETED".equalsIgnoreCase(r.getStatus()) || "APPROVED".equalsIgnoreCase(r.getStatus())).count()
                +
                consumables.stream().filter(
                        c -> "COMPLETED".equalsIgnoreCase(c.getStatus()) || "APPROVED".equalsIgnoreCase(c.getStatus()))
                        .count();

        panel.add(createCard("My Requests", String.valueOf(total)));
        panel.add(createCard("Pending Approvals", String.valueOf(pending)));
        panel.add(createCard("Completed Requests", String.valueOf(completed)));

        return panel;
    }

    private JPanel createCard(String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setForeground(Color.GRAY);

        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 36));
        v.setForeground(Color.BLACK);

        card.add(t);
        card.add(Box.createVerticalStrut(10));
        card.add(v);

        return card;
    }

    private JPanel createRecentRequests(List<RepairRequest> repairs, List<ConsumableRequest> consumables) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        JLabel title = new JLabel("Recent Requests");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        // JButton viewAll = new JButton("View All");
        // viewAll.setBorderPainted(false);
        // viewAll.setContentAreaFilled(false);
        // viewAll.setForeground(new Color(13, 110, 253));
        // viewAll.setFont(new Font("Segoe UI", Font.BOLD, 14));
        // viewAll.setCursor(new Cursor(Cursor.HAND_CURSOR));

        header.add(title, BorderLayout.WEST);
        // header.add(viewAll, BorderLayout.EAST); // View All not implemented yet

        panel.add(header);
        panel.add(Box.createVerticalStrut(20));

        // Combine and Sort
        List<Object> allRequests = new ArrayList<>();
        allRequests.addAll(repairs);
        allRequests.addAll(consumables);

        // Sort by date descending (simple string comparison for YYYY-MM-DD works)
        allRequests.sort((o1, o2) -> {
            String d1 = (o1 instanceof RepairRequest) ? ((RepairRequest) o1).getRequestDate()
                    : ((ConsumableRequest) o1).getRequestDate();
            String d2 = (o2 instanceof RepairRequest) ? ((RepairRequest) o2).getRequestDate()
                    : ((ConsumableRequest) o2).getRequestDate();
            return d2.compareTo(d1);
        });

        List<Object> recent = allRequests.stream().limit(5).collect(Collectors.toList());

        for (Object req : recent) {
            if (req instanceof RepairRequest) {
                RepairRequest r = (RepairRequest) req;
                panel.add(createRequestRow("Repair: " + r.getProductName(), r.getRequestDate(), r.getStatus()));
            } else {
                ConsumableRequest c = (ConsumableRequest) req;
                panel.add(createRequestRow("Consumable: " + c.getConsumableName(), c.getRequestDate(), c.getStatus()));
            }
            panel.add(Box.createVerticalStrut(10));
            panel.add(new JSeparator());
            panel.add(Box.createVerticalStrut(10));
        }

        if (recent.isEmpty()) {
            JPanel empty = new JPanel(new FlowLayout(FlowLayout.CENTER));
            empty.setBackground(Color.WHITE);
            empty.add(new JLabel("No recent requests found."));
            panel.add(empty);
        }

        return panel;
    }

    private JPanel createRequestRow(String titleVal, String dateVal, String statusVal) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(2000, 50));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(Color.WHITE);

        JLabel t = new JLabel(titleVal);
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel d = new JLabel("Requested on " + dateVal);
        d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        d.setForeground(Color.GRAY);

        left.add(t);
        left.add(d);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(Color.WHITE);

        JLabel status = new JLabel(statusVal);
        status.setOpaque(true);
        status.setFont(new Font("Segoe UI", Font.BOLD, 12));
        status.setBorder(new EmptyBorder(5, 10, 5, 10));

        if ("APPROVED".equalsIgnoreCase(statusVal) || "COMPLETED".equalsIgnoreCase(statusVal)) {
            status.setBackground(new Color(220, 255, 220));
            status.setForeground(new Color(0, 100, 0));
        } else if ("PENDING".equalsIgnoreCase(statusVal)) {
            status.setBackground(new Color(255, 248, 220));
            status.setForeground(new Color(150, 100, 0));
        } else {
            status.setBackground(new Color(255, 220, 220));
            status.setForeground(new Color(100, 0, 0));
        }

        right.add(status);

        row.add(left, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);

        return row;
    }
}
