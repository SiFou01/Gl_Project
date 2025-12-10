package com.itms.ui.screens.agent;

import com.itms.config.AppConfig;
import com.itms.dao.ConsumableDAO;
import com.itms.dao.ConsumableRequestDAO;
import com.itms.model.Consumable;
import com.itms.model.ConsumableRequest;
import com.itms.ui.MainFrame;
import com.itms.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class RequestConsumableView extends JPanel {
    private MainFrame mainFrame;
    private JComboBox<String> consumableCombo;
    private JTextField quantityField;
    private JTextArea messageArea;
    private List<Consumable> consumables;

    public RequestConsumableView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(AppConfig.BG_COLOR);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(40, 40, 40, 40));
        card.setPreferredSize(new Dimension(500, 500));

        JLabel title = new JLabel("Request Consumable");
        title.setFont(AppConfig.HEADER_FONT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        consumableCombo = new JComboBox<>();
        loadConsumables();

        quantityField = new JTextField();
        messageArea = new JTextArea(5, 20);
        messageArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Submit Button
        JButton submitBtn = new JButton("Submit Request");
        submitBtn.setBackground(new Color(13, 110, 253));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitBtn.setFocusPainted(false);
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.addActionListener(e -> submitRequest());

        card.add(title);
        card.add(Box.createVerticalStrut(30));

        consumableCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(createLabeledField("Select Consumable", consumableCombo));
        card.add(Box.createVerticalStrut(15));

        quantityField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(createLabeledField("Quantity", quantityField));
        card.add(Box.createVerticalStrut(15));

        JPanel msgPanel = new JPanel();
        msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.Y_AXIS));
        msgPanel.setBackground(Color.WHITE);
        msgPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel msgLabel = new JLabel("Message");
        msgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(messageArea);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        msgPanel.add(msgLabel);
        msgPanel.add(Box.createVerticalStrut(5));
        msgPanel.add(scroll);

        card.add(msgPanel);
        card.add(Box.createVerticalStrut(30));
        card.add(submitBtn);

        add(card);
    }

    private void loadConsumables() {
        ConsumableDAO dao = new ConsumableDAO();
        consumables = dao.getAllConsumables();
        for (Consumable c : consumables) {
            consumableCombo.addItem(c.getName());
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
        if (field.getMaximumSize().width < 1000) {
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        }

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        return panel;
    }

    private void submitRequest() {
        int index = consumableCombo.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Please select a consumable.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Consumable selected = consumables.get(index);
        int qty;
        try {
            qty = Integer.parseInt(quantityField.getText());
            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be a valid non-negative number.", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity must be a valid non-negative number.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (messageArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Message is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ConsumableRequest req = new ConsumableRequest();
        req.setConsumableId(selected.getId());
        req.setAgentId(SessionManager.getCurrentUser().getId());
        req.setQuantity(qty);
        req.setStatus("PENDING");
        req.setRequestDate(LocalDate.now().toString());
        req.setMessage(messageArea.getText());

        new ConsumableRequestDAO().createRequest(req);
        JOptionPane.showMessageDialog(this, "Request submitted successfully!");
        quantityField.setText("");
        messageArea.setText("");
    }
}
