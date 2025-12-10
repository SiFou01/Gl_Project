package com.itms.utils;

import com.itms.model.Product;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.List;

public class PDFGenerator {

    public static void generateProductReport(List<Product> products, String filePath) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("IT Management System - Product Report"));
            document.add(new Paragraph("Generated on: " + java.time.LocalDate.now()));
            document.add(new Paragraph(" ")); // Spacer

            PdfPTable table = new PdfPTable(5);
            table.addCell("ID");
            table.addCell("Name");
            table.addCell("Category");
            table.addCell("Quantity");
            table.addCell("Status");

            for (Product p : products) {
                table.addCell(String.valueOf(p.getId()));
                table.addCell(p.getName());
                table.addCell(p.getCategory());
                table.addCell(String.valueOf(p.getQuantity()));
                table.addCell(p.getStatus());
            }

            document.add(table);
            document.close();
            System.out.println("PDF Report generated at: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
