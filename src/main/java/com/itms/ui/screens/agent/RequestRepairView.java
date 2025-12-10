package com.itms.ui.screens.agent;

import com.itms.config.AppConfig;
import com.itms.dao.ProductDAO;
import com.itms.dao.RepairRequestDAO;
import com.itms.model.Product;
import com.itms.model.RepairRequest;

import com.itms.utils.SessionManager;

import com.itms.ui.MainFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class RequestRepairView extends JPanel {

    private JComboBox<String> productCombo;
    private JTextArea issueArea;
    private List<Product> products;

    private MainFrame mainFrame;

    public RequestRepairView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(AppConfig.BG_COLOR);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(40, 40, 40, 40));
        card.setPreferredSize(new Dimension(500, 500));

        JLabel title = new JLabel("Request Repair");
        title.setFont(AppConfig.HEADER_FONT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        productCombo = new JComboBox<>();
        loadProducts();

        issueArea = new JTextArea(5, 20);
        issueArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Submit Button
        JButton submitBtn = new JButton("Submit Request");
        submitBtn.setBackground(new Color(13, 110, 253)); // Bootstrap Primary Blue
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitBtn.setFocusPainted(false);
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.addActionListener(e -> submitRequest());

        card.add(title);
        card.add(Box.createVerticalStrut(30));

        // Product Field
        productCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(createLabeledField("Select Product", productCombo));
        card.add(Box.createVerticalStrut(15));

        // Issue Field
        JPanel msgPanel = new JPanel();
        msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.Y_AXIS));
        msgPanel.setBackground(Color.WHITE);
        msgPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel msgLabel = new JLabel("Describe the Issue");
        msgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        issueArea.setLineWrap(true);
        issueArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(issueArea);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        // scroll.setPreferredSize(new Dimension(Integer.MAX_VALUE, 100)); // Let it
        // stretch

        msgPanel.add(msgLabel);
        msgPanel.add(Box.createVerticalStrut(5));
        msgPanel.add(scroll);

        card.add(msgPanel);
        card.add(Box.createVerticalStrut(30));
        card.add(submitBtn);

        add(card);
    }

    private void loadProducts() {
        ProductDAO dao = new ProductDAO();
        products = dao.getAllProducts();
        for (Product p : products) {
            productCombo.addItem(p.getName() + " (" + p.getBarcode() + ")");
        }
    }

    private JPanel createLabeledField(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT); // Fix alignment mismatch

        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Maximum size provided by caller or default to max width
        if (field.getMaximumSize().width < 1000) {
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        }

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        return panel;
    }

    private void submitRequest() {
        int index = productCombo.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Product selected = products.get(index);

        if (issueArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Issue description is required.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        RepairRequest req = new RepairRequest();
        req.setProductId(selected.getId());
        req.setAgentId(SessionManager.getCurrentUser().getId());
        req.setIssueDescription(issueArea.getText());
        req.setStatus("PENDING");
        req.setRequestDate(LocalDate.now().toString());
        req.setPriority("NORMAL");

        new RepairRequestDAO().createRequest(req);
        JOptionPane.showMessageDialog(this, "Repair request submitted successfully!");
        issueArea.setText("");
    }
}
