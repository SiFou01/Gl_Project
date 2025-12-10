package com.itms.ui.screens.consultant;

import com.itms.config.AppConfig;
import com.itms.dao.ProductDAO;
import com.itms.dao.ConsumableDAO;
import com.itms.dao.RepairRequestDAO;
import com.itms.model.Product;
import com.itms.model.Consumable;
import com.itms.ui.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ConsultantView extends JPanel {
    private MainFrame mainFrame;
    private JTabbedPane tabbedPane;

    public ConsultantView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppConfig.BG_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Products", createProductsPanel());
        tabbedPane.addTab("Consumables", createConsumablesPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = { "ID", "Name", "Category", "Status", "Location" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setFont(AppConfig.BOLD_FONT);

        ProductDAO dao = new ProductDAO();
        RepairRequestDAO repairDAO = new RepairRequestDAO();
        List<Product> list = dao.getAllProducts();
        for (Product p : list) {
            // Check if product has active repair request
            String status = repairDAO.isProductInRepair(p.getId()) ? "IN_REPAIR" : "ACTIVE";
            model.addRow(new Object[] { p.getId(), p.getName(), p.getCategory(), status, p.getLocation() });
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createConsumablesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = { "ID", "Name", "Category", "Qty" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setFont(AppConfig.BOLD_FONT);

        ConsumableDAO dao = new ConsumableDAO();
        List<Consumable> list = dao.getAllConsumables();
        for (Consumable c : list) {
            model.addRow(new Object[] { c.getId(), c.getName(), c.getCategory(), c.getQuantity() });
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
}
