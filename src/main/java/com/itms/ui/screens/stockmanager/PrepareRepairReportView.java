package com.itms.ui.screens.stockmanager;

import com.itms.config.AppConfig;
import com.itms.ui.MainFrame;
import com.itms.dao.ProductDAO;
import com.itms.model.Product;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class PrepareRepairReportView extends JPanel {
    private MainFrame mainFrame;

    // Form Fields
    private JComboBox<String> productCombo;
    private JTextField reportIdField;
    private JTextField customerNameField;
    private JTextField dateReceivedField;
    private JTextArea issueDescArea;
    private JTextArea diagnosisArea;
    private JTextArea actionsArea;
    private JTextField partsUsedField;
    private JTextField laborHoursField;
    private JTextField totalCostField;
    private JTextArea notesArea;

    private JCheckBox funcTestCheck;
    private JCheckBox visualCheck;
    private JCheckBox customerAppCheck;
    private JCheckBox warrantyCheck;

    public PrepareRepairReportView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250)); // Main BG

        // Scrollable Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(248, 249, 250));
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        content.add(createFormSection());
        content.add(Box.createVerticalStrut(20));
        content.add(createButtonsPanel());

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createFormSection() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Two Column Grid for top fields
        JPanel grid1 = new JPanel(new GridLayout(2, 2, 20, 20));
        grid1.setBackground(Color.WHITE);

        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAllProducts();
        String[] productNames = products.stream().map(Product::getName).toArray(String[]::new);

        productCombo = new JComboBox<>(productNames);
        reportIdField = new JTextField("RPT-" + System.currentTimeMillis() / 1000); // Auto-gen unique-ish
        customerNameField = new JTextField();
        dateReceivedField = new JTextField(java.time.LocalDate.now().toString());

        grid1.add(createFieldGroup("Product/Device", productCombo));
        grid1.add(createFieldGroup("Report ID", reportIdField));
        grid1.add(createFieldGroup("Customer Name", customerNameField));
        grid1.add(createFieldGroup("Date Received", dateReceivedField));

        form.add(grid1);
        form.add(Box.createVerticalStrut(20));

        issueDescArea = new JTextArea(3, 20);
        form.add(createFieldGroup("Issue Description", issueDescArea));
        form.add(Box.createVerticalStrut(30));

        JLabel detailsTitle = new JLabel("Repair Details", SwingConstants.CENTER);
        detailsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        detailsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(detailsTitle);
        form.add(Box.createVerticalStrut(20));

        JPanel grid2 = new JPanel(new GridLayout(1, 2, 20, 20));
        grid2.setBackground(Color.WHITE);

        diagnosisArea = new JTextArea(3, 20);
        actionsArea = new JTextArea(3, 20);

        grid2.add(createFieldGroup("Diagnosis", diagnosisArea));
        grid2.add(createFieldGroup("Repair Actions", actionsArea));
        form.add(grid2);
        form.add(Box.createVerticalStrut(20));

        JPanel grid3 = new JPanel(new GridLayout(1, 3, 20, 20));
        grid3.setBackground(Color.WHITE);

        partsUsedField = new JTextField();
        laborHoursField = new JTextField("0.0");
        totalCostField = new JTextField("0.00");

        grid3.add(createFieldGroup("Parts Used", partsUsedField));
        grid3.add(createFieldGroup("Labor Hours", laborHoursField));
        grid3.add(createFieldGroup("Total Cost", totalCostField));
        form.add(grid3);
        form.add(Box.createVerticalStrut(20));

        notesArea = new JTextArea(3, 20);
        form.add(createFieldGroup("Technician Notes", notesArea));
        form.add(Box.createVerticalStrut(30));

        JLabel qcTitle = new JLabel("Quality Check", SwingConstants.CENTER);
        qcTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        qcTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(qcTitle);
        form.add(Box.createVerticalStrut(15));

        JPanel qcGrid = new JPanel(new GridLayout(2, 2, 20, 10));
        qcGrid.setBackground(Color.WHITE);

        funcTestCheck = createCheckbox("Functionality Test Passed");
        visualCheck = createCheckbox("Visual Inspection Completed");
        customerAppCheck = createCheckbox("Customer Approval Received");
        warrantyCheck = createCheckbox("Warranty Applied");

        qcGrid.add(funcTestCheck);
        qcGrid.add(visualCheck);
        qcGrid.add(customerAppCheck);
        qcGrid.add(warrantyCheck);
        form.add(qcGrid);

        return form;
    }

    private JPanel createFieldGroup(String labelText, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setBackground(Color.WHITE);
        JLabel l = new JLabel(labelText);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        p.add(l, BorderLayout.NORTH);

        if (field instanceof JTextArea) {
            ((JTextArea) field).setLineWrap(true);
            JScrollPane sp = new JScrollPane(field);
            sp.setPreferredSize(new Dimension(100, 80));
            p.add(sp, BorderLayout.CENTER);
        } else {
            field.setPreferredSize(new Dimension(100, 40));
            p.add(field, BorderLayout.CENTER);
        }
        return p;
    }

    private JCheckBox createCheckbox(String text) {
        JCheckBox cb = new JCheckBox(text);
        cb.setBackground(Color.WHITE);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return cb;
    }

    private JPanel createButtonsPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.setBackground(new Color(248, 249, 250));

        JButton generateBtn = new JButton("Generate PDF");
        generateBtn.setBackground(new Color(0, 82, 204));
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setFocusPainted(false);
        generateBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        generateBtn.setPreferredSize(new Dimension(150, 40));

        generateBtn.addActionListener(e -> generatePDF());

        p.add(generateBtn);

        return p;
    }

    private void generatePDF() {
        String reportId = reportIdField.getText();
        if (reportId.isEmpty())
            reportId = "Report";
        String folderName = "Repair Report PDF";

        // Create folder
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdir();
        }

        String filename = folderName + File.separator + reportId + ".pdf";

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();

            document.add(new Paragraph("Repair Report"));
            document.add(new Paragraph("Generated: " + java.time.LocalDateTime.now()));
            document.add(new Paragraph("--------------------------------------------------"));

            document.add(new Paragraph("Report ID: " + reportIdField.getText()));
            document.add(new Paragraph("Product: " + productCombo.getSelectedItem()));
            document.add(new Paragraph("Customer: " + customerNameField.getText()));
            document.add(new Paragraph("Date Received: " + dateReceivedField.getText()));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Issue Description:"));
            document.add(new Paragraph(issueDescArea.getText()));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Diagnosis:"));
            document.add(new Paragraph(diagnosisArea.getText()));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Actions Performed:"));
            document.add(new Paragraph(actionsArea.getText()));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Parts Used: " + partsUsedField.getText()));
            document.add(new Paragraph("Labor Hours: " + laborHoursField.getText()));
            document.add(new Paragraph("Total Cost: " + totalCostField.getText()));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Technician Notes:"));
            document.add(new Paragraph(notesArea.getText()));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("QC Checks:"));
            document.add(new Paragraph("Functionality: " + (funcTestCheck.isSelected() ? "PASS" : "N/A")));
            document.add(new Paragraph("Visual: " + (visualCheck.isSelected() ? "PASS" : "N/A")));
            document.add(new Paragraph("Customer Approval: " + (customerAppCheck.isSelected() ? "YES" : "NO")));
            document.add(new Paragraph("Warranty: " + (warrantyCheck.isSelected() ? "YES" : "NO")));

            document.close();

            JOptionPane.showMessageDialog(this,
                    "PDF Report generated successfully:\n" + new File(filename).getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating PDF: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
