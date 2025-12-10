package com.itms.ui.screens.stockmanager;

import com.itms.config.AppConfig;
import com.itms.dao.ProductDAO;
import com.itms.model.Product;
import com.itms.ui.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class ManageProductsView extends JPanel {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private ProductDAO productDAO;
    private TableRowSorter<DefaultTableModel> sorter;

    public ManageProductsView(MainFrame mainFrame) {
        this.productDAO = new ProductDAO();

        setLayout(new BorderLayout());
        setBackground(AppConfig.BG_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(AppConfig.BG_COLOR);

        JTextField searchField = new JTextField(20);
        JButton addButton = new JButton("+ Add Product");
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

        addButton.addActionListener(e -> showAddProductDialog());
        deleteButton.addActionListener(e -> deleteSelectedProduct());

        add(toolbar, BorderLayout.NORTH);

        // Table
        // Removed Status Column
        String[] columns = { "Name", "Category", "Barcode", "Location" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(tableModel);
        productTable.setRowHeight(30);
        productTable.getTableHeader().setFont(AppConfig.BOLD_FONT);
        productTable.getTableHeader().setBackground(Color.WHITE);
        productTable.setShowGrid(false);
        productTable.setIntercellSpacing(new Dimension(0, 0));
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Single selection for simplicity

        // Sorter
        sorter = new TableRowSorter<>(tableModel);
        productTable.setRowSorter(sorter);

        // Search Filter
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

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane, BorderLayout.CENTER);

        loadData();
    }

    private void deleteSelectedProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = productTable.convertRowIndexToModel(selectedRow);
        // ID not in table; use Barcode to find ID
        String barcode = (String) tableModel.getValueAt(modelRow, 2); // Barcode is column 2
        int id = productDAO.getProductIdByBarcode(barcode);
        String name = (String) tableModel.getValueAt(modelRow, 0); // Name is column 0

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete '" + name + "'?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            productDAO.deleteProduct(id);
            loadData(); // Reload
            JOptionPane.showMessageDialog(this, "Product deleted successfully.");
        }
    }

    private void showAddProductDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Product", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField nameField = new JTextField(20);
        // Using existing categories (assumed loaded or constant)
        // Ideally should match current category system. The previous file used
        // com.itms.constants.ProductCategory.ALL_CATEGORIES
        JComboBox<String> categoryCombo = new JComboBox<>(com.itms.constants.ProductCategory.ALL_CATEGORIES);
        JTextField quantityField = new JTextField(20);
        String[] statuses = { "ACTIVE", "OUT_OF_STOCK", "DISCONTINUED" };
        JComboBox<String> statusCombo = new JComboBox<>(statuses);
        JTextField barcodeField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        JTextField supplierField = new JTextField(20);
        JTextField purchaseDateField = new JTextField(20);
        JTextField warrantyEndField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);
        JTextField priceField = new JTextField(20);

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryCombo);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusCombo);
        formPanel.add(new JLabel("Barcode:"));
        formPanel.add(barcodeField);
        formPanel.add(new JLabel("Location:"));
        formPanel.add(locationField);
        formPanel.add(new JLabel("Supplier:"));
        formPanel.add(supplierField);
        formPanel.add(new JLabel("Purchase Date (YYYY-MM-DD):"));
        formPanel.add(purchaseDateField);
        formPanel.add(new JLabel("Warranty End (YYYY-MM-DD):"));
        formPanel.add(warrantyEndField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);
        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(AppConfig.PRIMARY_COLOR);
        saveBtn.setForeground(Color.WHITE);
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> {
            try {
                Product p = new Product();
                p.setName(nameField.getText());
                p.setCategory((String) categoryCombo.getSelectedItem());
                p.setQuantity(Integer.parseInt(quantityField.getText()));
                p.setStatus((String) statusCombo.getSelectedItem());
                p.setBarcode(barcodeField.getText());
                p.setLocation(locationField.getText());
                p.setSupplier(supplierField.getText());
                p.setPurchaseDate(purchaseDateField.getText());
                p.setWarrantyEnd(warrantyEndField.getText());
                p.setDescription(descriptionField.getText());
                p.setPrice(Double.parseDouble(priceField.getText()));

                productDAO.addProduct(p);
                JOptionPane.showMessageDialog(dialog, "Product added successfully!");
                dialog.dispose();
                loadData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for quantity and price.", "Error",
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
        List<Product> products = productDAO.getAllProducts();
        for (Product p : products) {
            tableModel.addRow(new Object[] {
                    p.getName(),
                    p.getCategory(),
                    // p.getQuantity(), // Removed
                    // p.getStatus(), // Removed from view
                    p.getBarcode(),
                    p.getLocation()
            });
        }
    }
}
