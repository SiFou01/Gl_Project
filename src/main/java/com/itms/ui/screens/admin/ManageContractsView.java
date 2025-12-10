package com.itms.ui.screens.admin;

import com.itms.config.AppConfig;
import com.itms.dao.MaintenanceContractDAO;
import com.itms.model.MaintenanceContract;
import com.itms.ui.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ManageContractsView extends JPanel {
    private MainFrame mainFrame;
    private JTable table;
    private DefaultTableModel tableModel;
    private MaintenanceContractDAO dao;

    public ManageContractsView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.dao = new MaintenanceContractDAO();

        setLayout(new BorderLayout());
        setBackground(AppConfig.BG_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(AppConfig.BG_COLOR);

        JButton addBtn = new JButton("Add Contract");
        addBtn.setBackground(new Color(13, 110, 253));
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> showContractDialog(null));

        JButton editBtn = new JButton("Edit Contract");
        editBtn.setBackground(new Color(255, 193, 7));
        editBtn.setForeground(Color.BLACK);
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) tableModel.getValueAt(row, 0); // ID is col 0
                // Find contract by ID
                MaintenanceContract c = dao.getAllContracts().stream().filter(mc -> mc.getId() == id).findFirst()
                        .orElse(null);
                if (c != null)
                    showContractDialog(c);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a contract to edit.");
            }
        });

        toolbar.add(addBtn);
        toolbar.add(editBtn);
        add(toolbar, BorderLayout.NORTH);

        // Table
        String[] columns = { "Contract ID", "Product", "Supplier", "Start Date", "End Date", "Status" };
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
        List<MaintenanceContract> list = dao.getAllContracts();
        LocalDate now = LocalDate.now();

        for (MaintenanceContract c : list) {
            String status = "ACTIVE";
            try {
                if (c.getEndDate() != null) {
                    LocalDate end = LocalDate.parse(c.getEndDate());
                    if (end.isBefore(now)) {
                        status = "EXPIRED";
                    }
                }
            } catch (DateTimeParseException e) {
                // Keep default or handle error
            }
            // Update status in object if changed? Just display based on date for now.
            // Or strictly use what's in DB? User said "make it dynamic with the dates".
            // So display value should be calculated.

            tableModel.addRow(new Object[] {
                    c.getId(),
                    c.getProduct(),
                    c.getSupplierName(),
                    c.getStartDate(),
                    c.getEndDate(),
                    status
            });
        }
    }

    private void showContractDialog(MaintenanceContract contract) {
        JDialog dialog = new JDialog(mainFrame, contract == null ? "Add Contract" : "Edit Contract", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(700, 400);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(mainFrame);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField productField = new JTextField(contract != null ? contract.getProduct() : "");
        JTextField supplierField = new JTextField(contract != null ? contract.getSupplierName() : "");
        JTextField startField = new JTextField(contract != null ? contract.getStartDate() : LocalDate.now().toString());
        JTextField endField = new JTextField(
                contract != null ? contract.getEndDate() : LocalDate.now().plusYears(1).toString());
        JTextArea termsArea = new JTextArea(contract != null ? contract.getTerms() : "Standard Terms");

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Product:"), gbc);
        gbc.gridx = 1;
        dialog.add(productField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Supplier:"), gbc);
        gbc.gridx = 1;
        dialog.add(supplierField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        dialog.add(startField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        dialog.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        dialog.add(endField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        dialog.add(new JLabel("Terms:"), gbc);
        gbc.gridx = 1;
        dialog.add(new JScrollPane(termsArea), gbc);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            MaintenanceContract c = contract == null ? new MaintenanceContract() : contract;
            c.setProduct(productField.getText());
            c.setSupplierName(supplierField.getText());
            c.setStartDate(startField.getText());
            c.setEndDate(endField.getText());
            c.setTerms(termsArea.getText());
            c.setStatus("ACTIVE"); // Default, will be calculated on view

            if (contract == null) {
                dao.addContract(c);
            } else {
                dao.updateContract(c);
            }
            loadData();
            dialog.dispose();
        });

        gbc.gridx = 1;
        gbc.gridy = 5;
        dialog.add(saveBtn, gbc);

        dialog.setVisible(true);
    }
}
