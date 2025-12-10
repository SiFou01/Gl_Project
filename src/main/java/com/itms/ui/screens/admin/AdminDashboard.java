package com.itms.ui.screens.admin;

import com.itms.config.AppConfig;
import com.itms.dao.ConsumableRequestDAO;
import com.itms.dao.MaintenanceContractDAO;
import com.itms.dao.UserDAO;
import com.itms.model.ConsumableRequest;
import com.itms.ui.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class AdminDashboard extends JPanel {
    private MainFrame mainFrame;

    public AdminDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppConfig.BG_COLOR);

        // Main Content with ScrollPane
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(AppConfig.BG_COLOR);
        mainContent.setBorder(new EmptyBorder(30, 30, 30, 30));

        // 1. Header (Cleaned up)
        // Removed internal title as it duplicates the main view title
        mainContent.add(Box.createVerticalStrut(10));

        // 2. Quick Actions / Stat Cards
        JLabel qaLabel = new JLabel("Quick Actions");
        qaLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        qaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(qaLabel);
        mainContent.add(Box.createVerticalStrut(15));

        mainContent.add(createCardsPanel());
        mainContent.add(Box.createVerticalStrut(30));

        // 3. Bottom Section (Pending Requests)
        mainContent.add(createBottomSection());

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createCardsPanel() {
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setBackground(AppConfig.BG_COLOR);
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        cardsPanel.setPreferredSize(new Dimension(1000, 200));
        cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        int userCount = new UserDAO().getAllUsers().size();
        int contractCount = new MaintenanceContractDAO().getAllContracts().size();
        // Dynamic Pending Requests
        int pendingRequests = (int) new ConsumableRequestDAO().getAllRequests().stream()
                .filter(r -> "PENDING".equalsIgnoreCase(r.getStatus()))
                .count();

        cardsPanel.add(createCard("Active Contracts", String.valueOf(contractCount), "Manage Maintenance Contracts",
                e -> mainFrame.setView(new ManageContractsView(mainFrame), "Manage Contracts")));

        cardsPanel.add(createCard("Consumable Requests", String.valueOf(pendingRequests), "Manage Consumable Requests",
                e -> mainFrame.setView(new ManageConsumableRequestsView(mainFrame), "Consumable Requests")));

        cardsPanel.add(createCard("Total Users", String.valueOf(userCount), "Manage Users",
                e -> mainFrame.setView(new ManageUsersView(mainFrame), "Manage Users")));

        return cardsPanel;
    }

    private JPanel createCard(String topTitle, String number, String mainTitle,
            java.awt.event.ActionListener action) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(25, 25, 25, 25)));

        // Content Panel (Horizontal for Number + Text)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Number
        JLabel lblNum = new JLabel(number);
        lblNum.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblNum.setForeground(Color.BLACK);
        contentPanel.add(lblNum);

        // Text (Title/Description) next to number
        // "Active Contracts", "Consumable Requests", "Total Users"
        JLabel lblText = new JLabel(topTitle);
        lblText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblText.setForeground(Color.GRAY);
        contentPanel.add(lblText);

        card.add(contentPanel);

        card.add(Box.createVerticalGlue());

        // Removed the "Big Text above Manage Button" (lblMain)
        // card.add(lblMain);
        // card.add(Box.createVerticalStrut(10));

        // Bottom: Button
        JButton btn = new JButton("Manage");
        btn.setBackground(new Color(13, 110, 253));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);

        card.add(btn);

        return card;
    }

    private JPanel createBottomSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(20, 20, 20, 20)));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        // Dynamic Pending Requests
        ConsumableRequestDAO requestDAO = new ConsumableRequestDAO();
        long pendingCount = requestDAO.getAllRequests().stream()
                .filter(r -> "PENDING".equalsIgnoreCase(r.getStatus()))
                .count();

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel lblTitle = new JLabel("Pending Requests");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.add(lblTitle, BorderLayout.WEST);

        JLabel badge = new JLabel("  " + pendingCount + "  ");
        badge.setOpaque(true);
        badge.setBackground(new Color(255, 243, 224));
        badge.setForeground(new Color(230, 81, 0));
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.add(badge, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);
        panel.add(createRequestsList(), BorderLayout.CENTER);

        return panel;
    }

    private JComponent createRequestsList() {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(Color.WHITE);

        ConsumableRequestDAO requestDAO = new ConsumableRequestDAO();
        List<ConsumableRequest> pending = requestDAO.getAllRequests().stream()
                .filter(r -> "PENDING".equalsIgnoreCase(r.getStatus()))
                .limit(5) // Show top 5
                .collect(Collectors.toList());

        if (pending.isEmpty()) {
            list.add(new JLabel("No pending requests."));
        } else {
            for (ConsumableRequest r : pending) {
                list.add(createRequestItem(r));
                list.add(Box.createVerticalStrut(10));
            }
        }
        return list;
    }

    private JPanel createRequestItem(ConsumableRequest r) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(new Color(255, 250, 240));
        item.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(255, 250, 240));

        JLabel t = new JLabel(r.getConsumableName());
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel timeLbl = new JLabel(r.getRequestDate());
        timeLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        timeLbl.setForeground(new Color(230, 81, 0));

        top.add(t, BorderLayout.WEST);
        top.add(timeLbl, BorderLayout.EAST);

        // Details using Quantity and Agent Name
        JLabel d = new JLabel("<html>Qty: " + r.getQuantity() + "<br>Requested by: " + r.getAgentName() + "</html>");
        d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        d.setForeground(Color.GRAY);
        d.setBorder(new EmptyBorder(5, 0, 10, 0));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actions.setBackground(new Color(255, 250, 240));

        JButton approve = new JButton("Approve");
        approve.setBackground(new Color(13, 110, 253));
        approve.setForeground(Color.WHITE);
        approve.setFont(new Font("Segoe UI", Font.BOLD, 12));
        approve.setBorderPainted(false);
        approve.setFocusPainted(false);
        approve.setPreferredSize(new Dimension(100, 30));
        approve.addActionListener(e -> {
            new ConsumableRequestDAO().updateStatus(r.getId(), "APPROVED");
            // Refresh view? Ideally we refresh the dashboard, but for now simple message
            JOptionPane.showMessageDialog(this, "Request Approved. Refresh dashboard to see changes.");
        });

        JButton reject = new JButton("Reject");
        reject.setBackground(new Color(220, 220, 220));
        reject.setForeground(Color.BLACK);
        reject.setFont(new Font("Segoe UI", Font.BOLD, 12));
        reject.setBorderPainted(false);
        reject.setFocusPainted(false);
        reject.setPreferredSize(new Dimension(80, 30));
        reject.addActionListener(e -> {
            new ConsumableRequestDAO().updateStatus(r.getId(), "REJECTED");
            JOptionPane.showMessageDialog(this, "Request Rejected. Refresh dashboard to see changes.");
        });

        actions.add(approve);
        actions.add(Box.createHorizontalStrut(10));
        actions.add(reject);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(255, 250, 240));
        content.add(top);
        content.add(d);
        content.add(actions);

        item.add(content, BorderLayout.CENTER);
        return item;
    }
}
