package com.itms.ui.screens.stockmanager;

import com.itms.config.AppConfig;
import com.itms.dao.ConsumableDAO;
import com.itms.model.Consumable;
import com.itms.ui.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class ManageConsumablesView extends JPanel {
    private MainFrame mainFrame;
    private JTable table;
    private DefaultTableModel tableModel;
    private ConsumableDAO dao;
    private TableRowSorter<DefaultTableModel> sorter;

    public ManageConsumablesView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.dao = new ConsumableDAO();

        setLayout(new BorderLayout());
        setBackground(AppConfig.BG_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(AppConfig.BG_COLOR);

        JTextField searchField = new JTextField(20);
        JButton addButton = new JButton("Add Consumable");
        addButton.setBackground(AppConfig.PRIMARY_COLOR);
        addButton.setForeground(Color.WHITE);

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);

        toolbar.add(new JLabel("Search: "));
        toolbar.add(searchField);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(addButton);
        toolbar.add(deleteButton);

        addButton.addActionListener(e -> showAddConsumableDialog());
        deleteButton.addActionListener(e -> deleteSelectedConsumable());

        add(toolbar, BorderLayout.NORTH);

        // Table
        // Removed Status Column
        String[] columns = { "Name", "Category", "Qty", "Supplier" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(AppConfig.BOLD_FONT);
        table.getTableHeader().setBackground(Color.WHITE);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Sorter
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Filter Logic
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            public void changedUpdate(DocumentEvent e) {
                filter();
            }

            private void filter() {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();
    }

    private void deleteSelectedConsumable() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a consumable to delete.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        // ID column is hidden, so we need to get ID from model.
        // Wait, if ID column is removed from view, we can't get it from table easily
        // unless we keep it in model but hide in view.
        // For simplicity, let's assume valid selection logic or store ID in a hidden
        // column.
        // Better approach: Keep ID in model but don't show it?
        // Or just remove visual column but keep logic.
        // Actually, if we remove it from "columns" array, it's gone from model too if
        // we initialized it that way.
        // Let's check how loadData works.
        // The user wants to "remove ID", usually means from view.
        // But logic depends on it.
        // I will keep ID in model but find a way to hide it OR I will have to fetch it
        // differently.
        // EASIEST WAY: Keep ID in model, just remove it from `columns` array displayed?
        // No, JTable shows all model columns by default.
        // I will remove it from the columns array and thus the model. But then delete
        // won't work easily.
        // Actually, I should probably keep it in the loop but not add it to the row?
        // Wait, if I remove ID from columns, the column index 0 becomes Name.
        // But `deleteSelectedConsumable` retrieves ID from column 0.
        // I need to change `deleteSelectedConsumable` to find the Consumable by Name
        // (assuming unique names) OR
        // I should keep ID in a hidden column.
        // Given constraints, I'll switch to finding by Name for deletion since we made
        // names unique previously.
        String name = (String) tableModel.getValueAt(modelRow, 0); // Name is at column 0
        int id = dao.getConsumableIdByName(name);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete '" + name + "'?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dao.deleteConsumable(id);
            loadData();
            JOptionPane.showMessageDialog(this, "Consumable deleted successfully.");
        }
    }

    private void showAddConsumableDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Consumable", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField nameField = new JTextField(20);
        JComboBox<String> categoryCombo = new JComboBox<>(com.itms.constants.ConsumableCategory.ALL_CATEGORIES);
        JTextField quantityField = new JTextField(20);
        String[] statuses = { "AVAILABLE", "LOW_STOCK", "OUT_OF_STOCK" };
        JComboBox<String> statusCombo = new JComboBox<>(statuses);
        JTextField supplierField = new JTextField(20);

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryCombo);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusCombo);
        formPanel.add(new JLabel("Supplier:"));
        formPanel.add(supplierField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(AppConfig.PRIMARY_COLOR);
        saveBtn.setForeground(Color.WHITE);
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> {
            try {
                Consumable c = new Consumable();
                c.setName(nameField.getText());
                c.setCategory((String) categoryCombo.getSelectedItem());
                c.setQuantity(Integer.parseInt(quantityField.getText()));
                c.setStatus((String) statusCombo.getSelectedItem());
                c.setSupplier(supplierField.getText());

                dao.addConsumable(c);
                JOptionPane.showMessageDialog(dialog, "Consumable added successfully!");
                dialog.dispose();
                loadData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number for quantity.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Consumable> list = dao.getAllConsumables();
        for (Consumable c : list) {
            tableModel.addRow(new Object[] {
                    c.getName(),
                    c.getCategory(),
                    c.getQuantity(),
                    // c.getStatus(), // Removed
                    c.getSupplier()
            });
        }
    }
}
