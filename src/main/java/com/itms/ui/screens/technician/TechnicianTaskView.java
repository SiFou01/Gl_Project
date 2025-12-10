package com.itms.ui.screens.technician;

import com.itms.config.AppConfig;
import com.itms.dao.RepairRequestDAO;
import com.itms.model.RepairRequest;
import com.itms.ui.MainFrame;
import com.itms.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TechnicianTaskView extends JPanel {
    private MainFrame mainFrame;
    private JTable table;
    private DefaultTableModel tableModel;
    private RepairRequestDAO dao;

    public TechnicianTaskView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.dao = new RepairRequestDAO();

        setLayout(new BorderLayout());
        setBackground(AppConfig.BG_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(AppConfig.BG_COLOR);

        JButton startBtn = new JButton("Start Repair");
        startBtn.setBackground(AppConfig.PRIMARY_COLOR);
        startBtn.setForeground(Color.WHITE);
        startBtn.addActionListener(e -> updateStatus("IN_PROGRESS"));

        JButton completeBtn = new JButton("Complete Repair");
        completeBtn.setBackground(new Color(40, 167, 69));
        completeBtn.setForeground(Color.WHITE);
        completeBtn.addActionListener(e -> updateStatus("COMPLETED"));

        toolbar.add(startBtn);
        toolbar.add(completeBtn);
        add(toolbar, BorderLayout.NORTH);

        // Table
        String[] columns = { "ID", "Product", "Issue", "Status", "Date" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(AppConfig.BOLD_FONT);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<RepairRequest> list = dao.getAllRequests();
        // Show PENDING, APPROVED, and IN_PROGRESS repair requests
        for (RepairRequest r : list) {
            if ("PENDING".equals(r.getStatus()) || "APPROVED".equals(r.getStatus()) || "IN_PROGRESS".equals(r.getStatus())) {
                tableModel.addRow(new Object[] {
                        r.getId(),
                        r.getProductName(),
                        r.getIssueDescription(),
                        r.getStatus(),
                        r.getRequestDate()
                });
            }
        }
    }

    private void updateStatus(String status) {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            dao.updateStatus(id, status);
            loadData();
        }
    }
}
