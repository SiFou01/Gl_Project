package com.itms.ui.screens.stockmanager;

import com.itms.dao.ConsumableDAO;
import com.itms.dao.ConsumableRequestDAO;
import com.itms.model.Consumable;
import com.itms.model.ConsumableRequest;
import com.itms.ui.MainFrame;
import com.itms.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RequestSparePartView extends JPanel {
    private MainFrame mainFrame;
    private JComboBox<Consumable> partCombo; // Use Consumable object directly

    public RequestSparePartView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(40, 40, 40, 40));

        // Center card
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(30, 30, 30, 30)));
        card.setMaximumSize(new Dimension(600, 500));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Header
        JLabel title = new JLabel("Request Spare Part");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(30));

        // Form Fields
        card.add(createLabel("Select Part Type (Consumable)"));

        // Load Consumables from DB
        ConsumableDAO consumableDAO = new ConsumableDAO();
        List<Consumable> consumables = consumableDAO.getAllConsumables();
        partCombo = new JComboBox<>(consumables.toArray(new Consumable[0]));
        // Custom renderer to show name
        partCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Consumable) {
                    setText(((Consumable) value).getName());
                }
                return this;
            }
        });

        partCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        partCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(partCombo);
        card.add(Box.createVerticalStrut(20));

        card.add(createLabel("Quantity"));
        JTextField qtyField = new JTextField();
        qtyField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        qtyField.setAlignmentX(Component.LEFT_ALIGNMENT);
        qtyField.putClientProperty("JTextField.placeholderText", "Enter quantity");
        card.add(qtyField);
        card.add(Box.createVerticalStrut(20));

        card.add(createLabel("Message"));
        JTextArea messageArea = new JTextArea(4, 20);
        messageArea.setLineWrap(true);
        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        messageScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.add(messageScroll);
        card.add(Box.createVerticalStrut(30));

        // Button
        JButton submitBtn = new JButton("Submit Request");
        submitBtn.setBackground(new Color(0, 82, 204)); // Blue color
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        submitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        submitBtn.addActionListener(e -> {
            String qtyText = qtyField.getText().trim();
            String message = messageArea.getText().trim();

            if (message.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Message is required.", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int qty = Integer.parseInt(qtyText);
                if (qty < 0) {
                    throw new NumberFormatException();
                }

                Consumable selected = (Consumable) partCombo.getSelectedItem();
                if (selected == null) {
                    JOptionPane.showMessageDialog(this, "Please select a consumable.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save to DB
                ConsumableRequest req = new ConsumableRequest(
                        0,
                        selected.getId(),
                        SessionManager.getCurrentUser().getId(),
                        qty,
                        "PENDING",
                        new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                        message);

                new ConsumableRequestDAO().createRequest(req);

                JOptionPane.showMessageDialog(this, "Spare part request submitted to Admin successfully!");

                // Reset fields
                qtyField.setText("");
                messageArea.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantity must be a valid non-negative number.", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        card.add(submitBtn);

        // Wrapper for centering
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(248, 249, 250)); // Light grey background
        wrapper.add(card);

        add(wrapper, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(50, 50, 50));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 5, 0));
        return label;
    }
}
