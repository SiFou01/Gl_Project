package com.itms.ui.screens.stockmanager;

import com.itms.config.AppConfig;
import com.itms.dao.ConsumableDAO;
import com.itms.dao.ConsumableRequestDAO;
import com.itms.dao.ProductDAO;
import com.itms.dao.RepairRequestDAO;
import com.itms.model.ConsumableRequest;
import com.itms.model.RepairRequest;
import com.itms.ui.MainFrame;
import com.itms.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class SMDashboard extends JPanel {
        private MainFrame mainFrame;

        public SMDashboard(MainFrame mainFrame) {
                this.mainFrame = mainFrame;
                setLayout(new BorderLayout());
                setBackground(new Color(248, 249, 250)); // Light background for dashboard

                // Removed Header as requested

                // Main Scrollable Content
                JPanel content = new JPanel();
                content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
                content.setBackground(new Color(248, 249, 250));
                content.setBorder(new EmptyBorder(30, 30, 30, 30));

                // Stats Row
                content.add(createStatsRow());
                content.add(Box.createVerticalStrut(30));

                // Quick Actions Title (Centered)
                JLabel qaTitle = createSectionTitle("Quick Actions");
                qaTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
                content.add(qaTitle);
                content.add(Box.createVerticalStrut(15));

                // Quick Actions Row
                content.add(createQuickActionsRow());
                content.add(Box.createVerticalStrut(30));

                // Recent Activity Title (Centered)
                JLabel raTitle = createSectionTitle("Recent Activity");
                raTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
                content.add(raTitle);
                content.add(Box.createVerticalStrut(15));

                // Recent Activity Panel
                content.add(createRecentActivityPanel());

                JScrollPane scrollPane = new JScrollPane(content);
                scrollPane.setBorder(null);
                scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                add(scrollPane, BorderLayout.CENTER);
        }

        private JPanel createStatsRow() {
                JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
                panel.setBackground(new Color(248, 249, 250));
                panel.setPreferredSize(new Dimension(1000, 180));
                panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

                int productCount = new ProductDAO().getAllProducts().size();
                int consumableCount = new ConsumableDAO().getAllConsumables().size();
                int requestCount = new RepairRequestDAO().getAllRequests().size();

                panel.add(createStatCard("Total Products", String.format("%,d", productCount), "Manage Products",
                                Color.BLUE,
                                e -> mainFrame.setView(new ManageProductsView(mainFrame), "Manage Products")));
                panel.add(createStatCard("Consumables", String.format("%,d", consumableCount), "Manage Consumables",
                                new Color(0, 150, 0),
                                e -> mainFrame.setView(new ManageConsumablesView(mainFrame), "Manage Consumables")));
                panel.add(createStatCard("Repair Requests", String.valueOf(requestCount), "View Requests", Color.ORANGE,
                                e -> mainFrame.setView(new RepairRequestsView(mainFrame), "Repair Requests")));
                int reportsCount = new RepairRequestDAO().getAllRequests().stream()
                                .filter(r -> "COMPLETED".equalsIgnoreCase(r.getStatus()))
                                .collect(Collectors.toList()).size();
                panel.add(createStatCard("Reports Generated", String.valueOf(reportsCount), "Create Report",
                                new Color(150, 0, 200),
                                e -> mainFrame.setView(new PrepareRepairReportView(mainFrame),
                                                "Prepare Repair Report")));

                return panel;
        }

        private JPanel createStatCard(String title, String value, String btnText, Color colorBar,
                        java.awt.event.ActionListener action) {
                JPanel card = new JPanel();
                card.setLayout(new BorderLayout());
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                JPanel center = new JPanel(new GridLayout(2, 1));
                center.setBackground(Color.WHITE);
                center.setBorder(new EmptyBorder(15, 0, 15, 0));

                JLabel val = new JLabel(value);
                val.setFont(new Font("Segoe UI", Font.BOLD, 28));
                center.add(val);

                JLabel tit = new JLabel(title);
                tit.setForeground(Color.GRAY);
                center.add(tit);

                card.add(center, BorderLayout.CENTER);

                JButton btn = new JButton(btnText);
                btn.setBackground(new Color(0, 82, 204));
                btn.setForeground(Color.WHITE);
                btn.setFocusPainted(false);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btn.addActionListener(action);

                card.add(btn, BorderLayout.SOUTH);

                return card;
        }

        private JPanel createQuickActionsRow() {
                JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
                panel.setBackground(new Color(248, 249, 250));
                panel.setPreferredSize(new Dimension(1000, 100));
                panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

                panel.add(createActionCard("Add New Product", "Register new inventory item", new Color(225, 240, 255),
                                e -> mainFrame.setView(new ManageProductsView(mainFrame), "Manage Products")));
                panel.add(createActionCard("Request Spare Part", "Submit part request", new Color(225, 255, 225),
                                e -> mainFrame.setView(new RequestSparePartView(mainFrame), "Request Spare Part")));
                panel.add(createActionCard("Generate Report", "Create inventory report", new Color(245, 230, 255),
                                e -> mainFrame.setView(new PrepareRepairReportView(mainFrame),
                                                "Prepare Repair Report")));

                return panel;
        }

        private JButton createActionCard(String title, String sub, Color iconBg, java.awt.event.ActionListener action) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));

                JLabel icon = new JLabel(" + ");
                icon.setOpaque(true);
                icon.setBackground(iconBg);
                icon.setFont(new Font("Segoe UI", Font.BOLD, 20));
                icon.setHorizontalAlignment(SwingConstants.CENTER);
                icon.setPreferredSize(new Dimension(50, 50));

                card.add(icon, BorderLayout.WEST);

                JPanel text = new JPanel(new GridLayout(2, 1));
                text.setBackground(Color.WHITE);
                text.setBorder(new EmptyBorder(0, 15, 0, 0));

                JLabel t = new JLabel(title);
                t.setFont(new Font("Segoe UI", Font.BOLD, 14));
                text.add(t);

                JLabel s = new JLabel(sub);
                s.setForeground(Color.GRAY);
                s.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                text.add(s);

                card.add(text, BorderLayout.CENTER);

                JButton btn = new JButton();
                btn.setLayout(new BorderLayout());
                btn.add(card);
                btn.setBorderPainted(false);
                btn.setFocusPainted(false);
                btn.setBackground(Color.WHITE);
                btn.addActionListener(action);

                return btn;
        }

        private JPanel createRecentActivityPanel() {
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBackground(Color.WHITE);
                panel.setBorder(new EmptyBorder(20, 20, 20, 20));

                // Fetch Real Data
                RepairRequestDAO repairDAO = new RepairRequestDAO();
                List<RepairRequest> completedRepairs = repairDAO.getAllRequests().stream()
                                .filter(r -> "COMPLETED".equalsIgnoreCase(r.getStatus()))
                                .limit(3)
                                .collect(Collectors.toList());

                ConsumableRequestDAO consumableDAO = new ConsumableRequestDAO();
                List<ConsumableRequest> recentRequests = consumableDAO.getAllRequests().stream()
                                .limit(3)
                                .collect(Collectors.toList());

                boolean hasActivity = false;

                // Recent Repairs (All statuses, limited to 3)
                List<RepairRequest> recentRepairs = repairDAO.getAllRequests().stream()
                                .sorted((r1, r2) -> Integer.compare(r2.getId(), r1.getId())) // Sort by ID desc (proxy
                                                                                             // for recent)
                                .limit(3)
                                .collect(Collectors.toList());

                for (RepairRequest r : recentRepairs) {
                        String status = r.getStatus();
                        Color bg = "COMPLETED".equalsIgnoreCase(status) ? new Color(225, 255, 225)
                                        : new Color(255, 240, 220);

                        panel.add(createActivityItem(
                                        "Repair Request",
                                        "Product: " + r.getProductName() + " - " + r.getIssueDescription(),
                                        r.getRequestDate(),
                                        status,
                                        bg,
                                        "COMPLETED".equalsIgnoreCase(status)));
                        panel.add(Box.createVerticalStrut(10));
                        panel.add(new JSeparator());
                        panel.add(Box.createVerticalStrut(10));
                        hasActivity = true;
                }

                for (ConsumableRequest cr : recentRequests) {
                        String statusMsg = "Request " + cr.getStatus();
                        Color bg = "APPROVED".equalsIgnoreCase(cr.getStatus()) ? new Color(225, 255, 225)
                                        : ("REJECTED".equalsIgnoreCase(cr.getStatus()) ? new Color(255, 225, 225)
                                                        : new Color(255, 240, 220));

                        panel.add(createActivityItem(
                                        "Spare Part Application",
                                        "Item: " + cr.getConsumableName() + " (Qty: " + cr.getQuantity() + ")",
                                        cr.getRequestDate(),
                                        cr.getStatus(),
                                        bg,
                                        false));
                        panel.add(Box.createVerticalStrut(10));
                        panel.add(new JSeparator());
                        panel.add(Box.createVerticalStrut(10));
                        hasActivity = true;
                }

                if (!hasActivity) {
                        JLabel noAct = new JLabel("No recent activity.");
                        noAct.setAlignmentX(Component.CENTER_ALIGNMENT);
                        panel.add(noAct);
                }

                return panel;
        }

        private JPanel createActivityItem(String title, String desc, String time, String status, Color iconBg,
                        boolean showReportBtn) {
                JPanel item = new JPanel(new BorderLayout());
                item.setBackground(Color.WHITE);

                JLabel icon = new JLabel("   ");
                icon.setOpaque(true);
                icon.setBackground(iconBg);
                icon.setPreferredSize(new Dimension(40, 40));
                item.add(icon, BorderLayout.WEST);

                JPanel text = new JPanel(new GridLayout(2, 1));
                text.setBackground(Color.WHITE);
                text.setBorder(new EmptyBorder(0, 15, 0, 0));

                JLabel t = new JLabel(title);
                t.setFont(new Font("Segoe UI", Font.BOLD, 14));
                text.add(t);

                JLabel d = new JLabel(desc);
                d.setForeground(Color.GRAY);
                text.add(d);

                item.add(text, BorderLayout.CENTER);

                // Right side: Button (if any) + Status/Date Stack
                JPanel rightContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
                rightContainer.setBackground(Color.WHITE);

                if (showReportBtn) {
                        JButton reportBtn = new JButton("Detailed Report");
                        reportBtn.setForeground(Color.BLUE);
                        reportBtn.setBorderPainted(false);
                        reportBtn.setContentAreaFilled(false);
                        reportBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        reportBtn.addActionListener(e -> mainFrame.setView(new PrepareRepairReportView(mainFrame),
                                        "Prepare Repair Report"));
                        rightContainer.add(reportBtn);
                }

                JPanel statusDatePanel = new JPanel(new GridLayout(2, 1));
                statusDatePanel.setBackground(Color.WHITE);

                JLabel statusLabel = new JLabel(status, SwingConstants.RIGHT);
                statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
                // Basic color coding for status text
                if ("APPROVED".equalsIgnoreCase(status) || "COMPLETED".equalsIgnoreCase(status))
                        statusLabel.setForeground(new Color(40, 167, 69));
                else if ("REJECTED".equalsIgnoreCase(status))
                        statusLabel.setForeground(Color.RED);
                else
                        statusLabel.setForeground(Color.ORANGE);

                statusDatePanel.add(statusLabel);

                JLabel timeLabel = new JLabel(time == null ? "Just now" : time, SwingConstants.RIGHT);
                timeLabel.setForeground(Color.GRAY);
                timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                statusDatePanel.add(timeLabel);

                rightContainer.add(statusDatePanel);

                item.add(rightContainer, BorderLayout.EAST);

                return item;
        }

        private JLabel createSectionTitle(String text) {
                JLabel label = new JLabel(text);
                label.setFont(new Font("Segoe UI", Font.BOLD, 18));
                label.setAlignmentX(Component.LEFT_ALIGNMENT);
                return label;
        }
}
