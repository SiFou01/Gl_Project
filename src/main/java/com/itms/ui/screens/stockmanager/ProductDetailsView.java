package com.itms.ui.screens.stockmanager;

import com.itms.config.AppConfig;
import com.itms.model.Product;
import com.itms.ui.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProductDetailsView extends JPanel {
    private MainFrame mainFrame;
    private Product product;

    public ProductDetailsView(MainFrame mainFrame, Product product) {
        this.mainFrame = mainFrame;
        this.product = product;

        setLayout(new BorderLayout());
        setBackground(AppConfig.BG_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header with Back Button
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(AppConfig.BG_COLOR);
        JButton backBtn = new JButton("← Back");
        backBtn.addActionListener(e -> mainFrame.setView(new ManageProductsView(mainFrame), "Manage Products"));
        header.add(backBtn);

        add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(AppConfig.BG_COLOR);

        // Product Info Card
        JPanel infoCard = new JPanel(new GridLayout(0, 2, 10, 10));
        infoCard.setBackground(Color.WHITE);
        infoCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        infoCard.setMaximumSize(new Dimension(1000, 200));

        infoCard.add(createDetailLabel("Name", product.getName()));
        infoCard.add(createDetailLabel("Category", product.getCategory()));
        infoCard.add(createDetailLabel("Quantity", String.valueOf(product.getQuantity())));
        infoCard.add(createDetailLabel("Status", product.getStatus()));
        infoCard.add(createDetailLabel("Location", product.getLocation()));
        infoCard.add(createDetailLabel("Supplier", product.getSupplier()));

        content.add(infoCard);
        content.add(Box.createVerticalStrut(20));

        // Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.setBackground(AppConfig.BG_COLOR);
        JButton editBtn = new JButton("Edit Details");
        editBtn.setBackground(AppConfig.PRIMARY_COLOR);
        editBtn.setForeground(Color.WHITE);

        actions.add(editBtn);
        content.add(actions);

        add(content, BorderLayout.CENTER);
    }

    private JLabel createDetailLabel(String title, String value) {
        return new JLabel("<html><b>" + title + ":</b> " + value + "</html>");
    }
}
