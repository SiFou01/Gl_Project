package com.itms.ui.screens.admin;

import com.itms.config.AppConfig;
import com.itms.dao.UserDAO;
import com.itms.model.User;
import com.itms.ui.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageUsersView extends JPanel {
    private MainFrame mainFrame;
    private JTable table;
    private DefaultTableModel tableModel;
    private UserDAO userDAO;

    public ManageUsersView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.userDAO = new UserDAO();

        setLayout(new BorderLayout());
        setBackground(AppConfig.BG_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(AppConfig.BG_COLOR);

        JButton addBtn = new JButton("Add User");
        addBtn.setBackground(AppConfig.PRIMARY_COLOR);
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> showAddUserDialog());

        toolbar.add(addBtn);
        add(toolbar, BorderLayout.NORTH);

        // Table
        String[] columns = { "ID", "Username", "Full Name", "Role" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(AppConfig.BOLD_FONT);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<User> users = userDAO.getAllUsers();
        for (User u : users) {
            tableModel.addRow(new Object[] {
                    u.getId(),
                    u.getUsername(),
                    u.getFullName(),
                    u.getRole()
            });
        }
    }

    private void showAddUserDialog() {
        JDialog dialog = new JDialog(mainFrame, "Add User", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JTextField fullNameField = new JTextField(20);
        String[] roles = { "ADMIN", "STOCK_MANAGER", "AGENT", "TECHNICIAN", "CONSULTANT" };
        JComboBox<String> roleCombo = new JComboBox<>(roles);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            User u = new User();
            u.setUsername(usernameField.getText());
            u.setPassword(new String(passwordField.getPassword()));
            u.setFullName(fullNameField.getText());
            u.setRole((String) roleCombo.getSelectedItem());
            u.setActive(true);

            userDAO.addUser(u);
            dialog.dispose();
            loadData();
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Username"));
        panel.add(usernameField);
        panel.add(new JLabel("Password"));
        panel.add(passwordField);
        panel.add(new JLabel("Full Name"));
        panel.add(fullNameField);
        panel.add(new JLabel("Role"));
        panel.add(roleCombo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(saveBtn);

        dialog.add(panel);
        dialog.setVisible(true);
    }
}
