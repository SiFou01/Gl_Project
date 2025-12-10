package com.itms.ui.components;

import com.itms.config.AppConfig;
import com.itms.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HeaderBar extends JPanel {
    private JLabel titleLabel;
    private JLabel userLabel;

    public HeaderBar() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1000, 70));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(AppConfig.HEADER_FONT);
        titleLabel.setBorder(new EmptyBorder(0, 20, 0, 0));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(Color.WHITE);
        
        String userInfo = SessionManager.getCurrentUser().getFullName() + " (" + SessionManager.getCurrentUser().getRole() + ")";
        userLabel = new JLabel(userInfo);
        userLabel.setFont(AppConfig.BOLD_FONT);
        
        // Placeholder for avatar
        JLabel avatar = new JLabel("👤");
        avatar.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        
        userPanel.add(userLabel);
        userPanel.add(Box.createHorizontalStrut(10));
        userPanel.add(avatar);
        userPanel.setBorder(new EmptyBorder(0, 0, 0, 20));

        add(titleLabel, BorderLayout.WEST);
        add(userPanel, BorderLayout.EAST);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }
}
