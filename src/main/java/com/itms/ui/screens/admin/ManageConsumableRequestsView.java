package com.itms.ui.screens.admin;

import com.itms.config.AppConfig;
import com.itms.dao.ConsumableRequestDAO;
import com.itms.model.ConsumableRequest;
import com.itms.ui.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageConsumableRequestsView extends JPanel {
    private MainFrame mainFrame;
    private JTable table;
    private DefaultTableModel tableModel;
    private ConsumableRequestDAO dao;

    public ManageConsumableRequestsView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.dao = new ConsumableRequestDAO();

        setLayout(new BorderLayout());
        setBackground(AppConfig.BG_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(AppConfig.BG_COLOR);

        JButton approveBtn = new JButton("Approve");
        approveBtn.setBackground(new Color(40, 167, 69)); // Green
        approveBtn.setForeground(Color.WHITE);
        approveBtn.addActionListener(e -> updateStatus("APPROVED"));

        JButton rejectBtn = new JButton("Reject");
        rejectBtn.setBackground(new Color(220, 53, 69)); // Red
        rejectBtn.setForeground(Color.WHITE);
        rejectBtn.addActionListener(e -> updateStatus("REJECTED"));

        toolbar.add(approveBtn);
        toolbar.add(rejectBtn);

        add(toolbar, BorderLayout.NORTH);

        // Table
        String[] columns = { "ID", "Consumable", "Agent", "Quantity", "Date", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(AppConfig.BOLD_FONT);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<ConsumableRequest> list = dao.getAllRequests();
        for (ConsumableRequest r : list) {
            tableModel.addRow(new Object[] {
                    r.getId(),
                    r.getConsumableName(),
                    r.getAgentName(),
                    r.getQuantity(),
                    r.getRequestDate(),
                    r.getStatus()
            });
        }
    }

    private void updateStatus(String status) {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            dao.updateStatus(id, status);
            loadData();
            JOptionPane.showMessageDialog(this, "Request " + status.toLowerCase() + ".");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a request.");
        }
    }
}
