package com.itms.ui.screens;

import com.itms.config.AppConfig;
import com.itms.dao.UserDAO;
import com.itms.model.User;
import com.itms.ui.MainFrame;
import com.itms.utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginScreen extends JPanel {
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    public LoginScreen(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(AppConfig.BG_COLOR);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(40, 40, 40, 40));
        // Add shadow/border logic if possible, for now simple border
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(40, 40, 40, 40)));

        JLabel title = new JLabel("IT Management System");
        title.setFont(AppConfig.HEADER_FONT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Welcome back! Please sign in to continue.");
        subtitle.setFont(AppConfig.REGULAR_FONT);
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Sign In");
        loginButton.setBackground(AppConfig.PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(AppConfig.BOLD_FONT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(300, 40));

        loginButton.addActionListener(e -> performLogin());

        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(10));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(30));

        card.add(createLabeledField("Username", usernameField));
        card.add(Box.createVerticalStrut(15));
        card.add(createLabeledField("Password", passwordField));
        card.add(Box.createVerticalStrut(25));

        card.add(loginButton);
        card.add(Box.createVerticalStrut(10));
        card.add(errorLabel);

        add(card);
    }

    private JPanel createLabeledField(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));

        field.setMaximumSize(new Dimension(300, 35));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        return panel;
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        UserDAO userDAO = new UserDAO();
        User user = userDAO.authenticate(username, password);

        if (user != null) {
            SessionManager.login(user);
            mainFrame.initMainLayout();
        } else {
            errorLabel.setText("Invalid username or password");
        }
    }
}
