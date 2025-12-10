package com.itms.constants;

public class ProductCategory {
    public static final String CENTRAL_UNIT = "Central Unit";
    public static final String MONITOR = "Monitor";
    public static final String LAPTOP = "Laptop";
    public static final String PRINTER = "Printer";
    public static final String UPS = "UPS";
    public static final String SERVER = "Server";
    public static final String INDUSTRIAL_PRINTER = "Industrial Printer";

    public static final String[] ALL_CATEGORIES = {
            CENTRAL_UNIT,
            MONITOR,
            LAPTOP,
            PRINTER,
            UPS,
            SERVER,
            INDUSTRIAL_PRINTER
    };

    private ProductCategory() {
        // Utility class, prevent instantiation
    }
}
