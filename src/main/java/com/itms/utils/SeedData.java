package com.itms.utils;

import com.itms.dao.ConsumableDAO;
import com.itms.dao.ProductDAO;
import com.itms.model.Consumable;
import com.itms.model.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SeedData {

    static class ItemTemplate {
        String name;
        String category;
        String supplier;

        public ItemTemplate(String name, String category, String supplier) {
            this.name = name;
            this.category = category;
            this.supplier = supplier;
        }
    }

    public static void main(String[] args) {
        seed();
    }

    public static void seed() {
        System.out.println("Seeding 112 Products and 89 Consumables...");
        Random rand = new Random();

        // --------------------------------------------------------------------------------
        // 112 Products (Duplicates allowed)
        // Valid Categories: Central Unit, Monitor, Laptop, Printer, UPS, Server,
        // Industrial Printer
        // --------------------------------------------------------------------------------
        List<ItemTemplate> prodTemplates = new ArrayList<>();

        // --- Laptops (Category: Laptop) ---
        prodTemplates.add(new ItemTemplate("Dell Latitude 5520", "Laptop", "Dell"));
        prodTemplates.add(new ItemTemplate("HP EliteBook 840 G8", "Laptop", "HP"));
        prodTemplates.add(new ItemTemplate("Lenovo ThinkPad X1 Carbon", "Laptop", "Lenovo"));
        prodTemplates.add(new ItemTemplate("MacBook Pro 14", "Laptop", "Apple"));
        prodTemplates.add(new ItemTemplate("Dell XPS 15", "Laptop", "Dell"));
        prodTemplates.add(new ItemTemplate("Microsoft Surface Laptop 4", "Laptop", "Microsoft"));

        // --- Central Units (Category: Central Unit) ---
        prodTemplates.add(new ItemTemplate("Dell OptiPlex 7090", "Central Unit", "Dell"));
        prodTemplates.add(new ItemTemplate("HP ProDesk 400 G7", "Central Unit", "HP"));
        prodTemplates.add(new ItemTemplate("Lenovo ThinkCentre M70q", "Central Unit", "Lenovo"));
        prodTemplates.add(new ItemTemplate("Dell Precision 3650", "Central Unit", "Dell"));
        prodTemplates.add(new ItemTemplate("Intel NUC 11 Pro", "Central Unit", "Intel"));

        // --- Monitors (Category: Monitor) ---
        prodTemplates.add(new ItemTemplate("Samsung F24T350", "Monitor", "Samsung"));
        prodTemplates.add(new ItemTemplate("Dell P2419H", "Monitor", "Dell"));
        prodTemplates.add(new ItemTemplate("LG 27QN600-B", "Monitor", "LG"));
        prodTemplates.add(new ItemTemplate("HP Z24f G3", "Monitor", "HP"));
        prodTemplates.add(new ItemTemplate("ASUS ProArt PA278QV", "Monitor", "ASUS"));

        // --- Printers (Category: Printer) ---
        prodTemplates.add(new ItemTemplate("HP LaserJet Pro M404n", "Printer", "HP"));
        prodTemplates.add(new ItemTemplate("Canon imageCLASS MF445dw", "Printer", "Canon"));
        prodTemplates.add(new ItemTemplate("Brother HL-L2350DW", "Printer", "Brother"));
        prodTemplates.add(new ItemTemplate("Epson EcoTank ET-2760", "Printer", "Epson"));

        // --- Industrial Printers (Category: Industrial Printer) ---
        prodTemplates.add(new ItemTemplate("Zebra ZT411", "Industrial Printer", "Zebra"));
        prodTemplates.add(new ItemTemplate("Honeywell PM45", "Industrial Printer", "Honeywell"));

        // --- Servers (Category: Server) ---
        prodTemplates.add(new ItemTemplate("Dell PowerEdge R740", "Server", "Dell"));
        prodTemplates.add(new ItemTemplate("HPE ProLiant DL380 Gen10", "Server", "HPE"));
        prodTemplates.add(new ItemTemplate("Lenovo ThinkSystem SR650", "Server", "Lenovo"));

        // --- UPS (Category: UPS) ---
        prodTemplates.add(new ItemTemplate("APC Smart-UPS 1500", "UPS", "APC"));
        prodTemplates.add(new ItemTemplate("CyberPower CP1500PFCLCD", "UPS", "CyberPower"));
        prodTemplates.add(new ItemTemplate("Tripp Lite 1500VA", "UPS", "Tripp Lite"));

        String[] locations = { "Server Room", "Office 101", "Office 102", "Office 103", "Warehouse A", "Warehouse B",
                "Reception", "IT Lab" };

        ProductDAO pDao = new ProductDAO();
        for (int i = 0; i < 112; i++) {
            Product p = new Product();
            ItemTemplate t = prodTemplates.get(rand.nextInt(prodTemplates.size()));

            p.setName(t.name); // No " - Unit X". Duplicates allowed.
            p.setCategory(t.category);
            p.setSupplier(t.supplier);

            p.setQuantity(1); // Usually assets are tracked individually, so qty 1 but multiple rows
            p.setStatus(rand.nextInt(10) > 1 ? "ACTIVE" : "OUT_OF_STOCK");
            p.setBarcode("ASSET-" + (10000 + i)); // Unique barcode
            p.setLocation(locations[rand.nextInt(locations.length)]);
            p.setPrice(100.0 + rand.nextInt(2000));
            p.setDescription("Standard IT Asset: " + t.name);
            p.setPurchaseDate("2024-" + (1 + rand.nextInt(12)) + "-15");
            p.setWarrantyEnd("2027-01-01");
            pDao.addProduct(p);
        }

        // --------------------------------------------------------------------------------
        // 89 Unique Consumables (KEPT AS IS)
        // --------------------------------------------------------------------------------
        List<ItemTemplate> consumTemplates = new ArrayList<>();
        // Copied from previous correct implementation
        consumTemplates.add(new ItemTemplate("Samsung 970 EVO Plus 500GB", "Hard Drive", "Samsung"));
        consumTemplates.add(new ItemTemplate("Samsung 970 EVO Plus 1TB", "Hard Drive", "Samsung"));
        consumTemplates.add(new ItemTemplate("Samsung 980 PRO 1TB", "Hard Drive", "Samsung"));
        consumTemplates.add(new ItemTemplate("Samsung 980 PRO 2TB", "Hard Drive", "Samsung"));
        consumTemplates.add(new ItemTemplate("Samsung 870 EVO 500GB", "Hard Drive", "Samsung"));
        consumTemplates.add(new ItemTemplate("Samsung 870 QVO 1TB", "Hard Drive", "Samsung"));
        consumTemplates.add(new ItemTemplate("WD Blue SN570 500GB", "Hard Drive", "Western Digital"));
        consumTemplates.add(new ItemTemplate("WD Blue SN570 1TB", "Hard Drive", "Western Digital"));
        consumTemplates.add(new ItemTemplate("WD Black SN850X 1TB", "Hard Drive", "Western Digital"));
        consumTemplates.add(new ItemTemplate("WD Red Plus 4TB HDD", "Hard Drive", "Western Digital"));
        consumTemplates.add(new ItemTemplate("WD Gold 8TB HDD", "Hard Drive", "Western Digital"));
        consumTemplates.add(new ItemTemplate("Seagate Barracuda 2TB HDD", "Hard Drive", "Seagate"));
        consumTemplates.add(new ItemTemplate("Seagate IronWolf 4TB HDD", "Hard Drive", "Seagate"));
        consumTemplates.add(new ItemTemplate("Crucial P5 Plus 1TB", "Hard Drive", "Crucial"));
        consumTemplates.add(new ItemTemplate("Kingston A2000 1TB", "Hard Drive", "Kingston"));
        consumTemplates.add(new ItemTemplate("Corsair Vengeance LPX 8GB", "RAM", "Corsair"));
        consumTemplates.add(new ItemTemplate("Corsair Vengeance LPX 16GB", "RAM", "Corsair"));
        consumTemplates.add(new ItemTemplate("Corsair Vengeance RGB 32GB", "RAM", "Corsair"));
        consumTemplates.add(new ItemTemplate("Kingston Fury Beast 8GB", "RAM", "Kingston"));
        consumTemplates.add(new ItemTemplate("Kingston Fury Beast 16GB", "RAM", "Kingston"));
        consumTemplates.add(new ItemTemplate("Crucial Ballistix 8GB", "RAM", "Crucial"));
        consumTemplates.add(new ItemTemplate("Crucial Ballistix 16GB", "RAM", "Crucial"));
        consumTemplates.add(new ItemTemplate("G.Skill Ripjaws V 16GB", "RAM", "G.Skill"));
        consumTemplates.add(new ItemTemplate("G.Skill Trident Z 32GB", "RAM", "G.Skill"));
        consumTemplates.add(new ItemTemplate("TeamGroup T-Force 16GB", "RAM", "TeamGroup"));
        consumTemplates.add(new ItemTemplate("ADATA XPG 8GB", "RAM", "ADATA"));
        consumTemplates.add(new ItemTemplate("Samsung Laptop RAM 8GB", "RAM", "Samsung"));
        consumTemplates.add(new ItemTemplate("Intel Core i3-12100", "Processor", "Intel"));
        consumTemplates.add(new ItemTemplate("Intel Core i5-12400", "Processor", "Intel"));
        consumTemplates.add(new ItemTemplate("Intel Core i5-12600K", "Processor", "Intel"));
        consumTemplates.add(new ItemTemplate("Intel Core i7-12700K", "Processor", "Intel"));
        consumTemplates.add(new ItemTemplate("Intel Core i9-12900K", "Processor", "Intel"));
        consumTemplates.add(new ItemTemplate("AMD Ryzen 5 5600X", "Processor", "AMD"));
        consumTemplates.add(new ItemTemplate("AMD Ryzen 5 5600G", "Processor", "AMD"));
        consumTemplates.add(new ItemTemplate("AMD Ryzen 7 5700G", "Processor", "AMD"));
        consumTemplates.add(new ItemTemplate("AMD Ryzen 7 5800X", "Processor", "AMD"));
        consumTemplates.add(new ItemTemplate("AMD Ryzen 9 5900X", "Processor", "AMD"));
        consumTemplates.add(new ItemTemplate("NVIDIA RTX 3050", "Graphics Card", "NVIDIA"));
        consumTemplates.add(new ItemTemplate("NVIDIA RTX 3060", "Graphics Card", "NVIDIA"));
        consumTemplates.add(new ItemTemplate("NVIDIA RTX 3060 Ti", "Graphics Card", "NVIDIA"));
        consumTemplates.add(new ItemTemplate("NVIDIA RTX 3070", "Graphics Card", "NVIDIA"));
        consumTemplates.add(new ItemTemplate("NVIDIA RTX 3080", "Graphics Card", "NVIDIA"));
        consumTemplates.add(new ItemTemplate("AMD Radeon RX 6600", "Graphics Card", "AMD"));
        consumTemplates.add(new ItemTemplate("AMD Radeon RX 6700 XT", "Graphics Card", "AMD"));
        consumTemplates.add(new ItemTemplate("PNY Quadro T1000", "Graphics Card", "PNY"));
        consumTemplates.add(new ItemTemplate("ASUS Prime B560M-A", "Motherboard", "ASUS"));
        consumTemplates.add(new ItemTemplate("ASUS TUF Gaming Z590-Plus", "Motherboard", "ASUS"));
        consumTemplates.add(new ItemTemplate("MSI MAG B550 Tomahawk", "Motherboard", "MSI"));
        consumTemplates.add(new ItemTemplate("MSI MPG Z490 Gaming Plus", "Motherboard", "MSI"));
        consumTemplates.add(new ItemTemplate("Gigabyte B450M DS3H", "Motherboard", "Gigabyte"));
        consumTemplates.add(new ItemTemplate("Gigabyte Z690 Gaming X", "Motherboard", "Gigabyte"));
        consumTemplates.add(new ItemTemplate("ASRock B550M Pro4", "Motherboard", "ASRock"));
        consumTemplates.add(new ItemTemplate("ASRock H510M-HDV", "Motherboard", "ASRock"));
        consumTemplates.add(new ItemTemplate("Corsair CX550", "Power Supply", "Corsair"));
        consumTemplates.add(new ItemTemplate("Corsair RM750x", "Power Supply", "Corsair"));
        consumTemplates.add(new ItemTemplate("Corsair RM850x", "Power Supply", "Corsair"));
        consumTemplates.add(new ItemTemplate("EVGA 500 W1", "Power Supply", "EVGA"));
        consumTemplates.add(new ItemTemplate("EVGA 600 GD", "Power Supply", "EVGA"));
        consumTemplates.add(new ItemTemplate("Seasonic Focus GX-650", "Power Supply", "Seasonic"));
        consumTemplates.add(new ItemTemplate("Seasonic Prime TX-850", "Power Supply", "Seasonic"));
        consumTemplates.add(new ItemTemplate("Cooler Master MWE 550", "Power Supply", "Cooler Master"));
        consumTemplates.add(new ItemTemplate("HP 58A Black Toner", "Toner", "HP"));
        consumTemplates.add(new ItemTemplate("HP 58X High Yield Black", "Toner", "HP"));
        consumTemplates.add(new ItemTemplate("HP 206A Black Toner", "Toner", "HP"));
        consumTemplates.add(new ItemTemplate("HP 206A Cyan Toner", "Toner", "HP"));
        consumTemplates.add(new ItemTemplate("HP 206A Magenta Toner", "Toner", "HP"));
        consumTemplates.add(new ItemTemplate("HP 206A Yellow Toner", "Toner", "HP"));
        consumTemplates.add(new ItemTemplate("Canon 057 Black Toner", "Toner", "Canon"));
        consumTemplates.add(new ItemTemplate("Brother TN-760 Black", "Toner", "Brother"));
        consumTemplates.add(new ItemTemplate("Brother TN-227C Cyan", "Toner", "Brother"));
        consumTemplates.add(new ItemTemplate("Epson 502 Black Ink", "Ink Cartridge", "Epson"));
        consumTemplates.add(new ItemTemplate("Epson 502 Cyan Ink", "Ink Cartridge", "Epson"));
        consumTemplates.add(new ItemTemplate("Epson 502 Magenta Ink", "Ink Cartridge", "Epson"));
        consumTemplates.add(new ItemTemplate("Epson 502 Yellow Ink", "Ink Cartridge", "Epson"));
        consumTemplates.add(new ItemTemplate("Canon PG-245 Black Ink", "Ink Cartridge", "Canon"));
        consumTemplates.add(new ItemTemplate("Canon CL-246 Color Ink", "Ink Cartridge", "Canon"));
        consumTemplates.add(new ItemTemplate("Xerox Vitality Multipurpose Paper", "Paper", "Xerox"));
        consumTemplates.add(new ItemTemplate("HP Office20 Copy Paper", "Paper", "HP"));
        consumTemplates.add(new ItemTemplate("Hammermill Fore MP Paper", "Paper", "Hammermill"));
        // Need 13 more unique items to verify count.
        // Assuming previous list was correct.
        consumTemplates.add(new ItemTemplate("AmazonBasics 9V Batteries", "Others", "Amazon"));
        consumTemplates.add(new ItemTemplate("Duracell AA Batteries 24-Pack", "Others", "Duracell"));
        consumTemplates.add(new ItemTemplate("Logitech C270 Webcam", "Others", "Logitech"));
        consumTemplates.add(new ItemTemplate("Microsoft LifeCam HD-3000", "Others", "Microsoft"));
        consumTemplates.add(new ItemTemplate("Creative Sound Blaster Z SE", "Sound Card", "Creative"));
        consumTemplates.add(new ItemTemplate("ASUS Xonar AE", "Sound Card", "ASUS"));
        consumTemplates.add(new ItemTemplate("StarTech USB-C Adapter", "Others", "StarTech"));
        consumTemplates.add(new ItemTemplate("Anker 4-Port USB Hub", "Others", "Anker"));
        consumTemplates.add(new ItemTemplate("Belkin Surge Protector", "Others", "Belkin"));
        consumTemplates.add(new ItemTemplate("Tripp Lite Surge Protector", "Others", "Tripp Lite"));

        // Some fillers if count < 89 due to copy-paste gap
        consumTemplates.add(new ItemTemplate("Kensington Lock", "Others", "Kensington"));
        consumTemplates.add(new ItemTemplate("3M Privacy Filter", "Others", "3M"));
        consumTemplates.add(new ItemTemplate("Fellowes Shredder", "Others", "Fellowes"));

        ConsumableDAO cDao = new ConsumableDAO();
        int count = 0;
        for (ItemTemplate t : consumTemplates) {
            if (count >= 89)
                break;
            Consumable c = new Consumable();
            c.setName(t.name);
            c.setCategory(t.category);
            c.setSupplier(t.supplier);
            c.setQuantity(10 + rand.nextInt(90));
            c.setStatus(c.getQuantity() > 20 ? "AVAILABLE" : "LOW_STOCK");
            cDao.addConsumable(c);
            count++;
        }

        System.out.println("Seeding complete. Seeded " + count + " unique consumables.");
        System.out.println("Seeded 112 product assets (duplicates allowed).");

        // --------------------------------------------------------------------------------
        // 4 Sample Maintenance Contracts
        // --------------------------------------------------------------------------------
        com.itms.dao.MaintenanceContractDAO contractDao = new com.itms.dao.MaintenanceContractDAO();
        com.itms.model.MaintenanceContract mc1 = new com.itms.model.MaintenanceContract();
        mc1.setSupplierName("Dell Enterprise Services");
        mc1.setProduct("Dell Latitude & OptiPlex Fleet");
        mc1.setStartDate("2024-01-01");
        mc1.setEndDate("2026-01-01");
        mc1.setStatus("ACTIVE");
        mc1.setTerms("Full hardware coverage, next-day onsite support.");
        contractDao.addContract(mc1);

        com.itms.model.MaintenanceContract mc2 = new com.itms.model.MaintenanceContract();
        mc2.setSupplierName("HP Care Pack");
        mc2.setProduct("HP LaserJet Printers");
        mc2.setStartDate("2023-06-15");
        mc2.setEndDate("2025-06-15");
        mc2.setStatus("ACTIVE");
        mc2.setTerms("Toner replacement and maintenance 24/7.");
        contractDao.addContract(mc2);

        com.itms.model.MaintenanceContract mc3 = new com.itms.model.MaintenanceContract();
        mc3.setSupplierName("Cisco SmartNet");
        mc3.setProduct("Network Infrastructure (Switches/AP)");
        mc3.setStartDate("2022-01-01");
        mc3.setEndDate("2023-01-01"); // Expired
        mc3.setStatus("EXPIRED");
        mc3.setTerms("Software updates and hardware replacement.");
        contractDao.addContract(mc3);

        com.itms.model.MaintenanceContract mc4 = new com.itms.model.MaintenanceContract();
        mc4.setSupplierName("Samsung Business Support");
        mc4.setProduct("Samsung Monitors & SSDs");
        mc4.setStartDate("2024-03-01");
        mc4.setEndDate("2027-03-01");
        mc4.setStatus("ACTIVE");
        mc4.setTerms("On-site repair and extended warranty.");
        contractDao.addContract(mc4);

        System.out.println("Seeded 4 Maintenance Contracts.");

        // --------------------------------------------------------------------------------
        // Default Users
        // --------------------------------------------------------------------------------
        com.itms.dao.UserDAO userDao = new com.itms.dao.UserDAO();

        // Admin
        com.itms.model.User admin = new com.itms.model.User();
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setRole("ADMIN");
        admin.setFullName("System Administrator");
        admin.setEmail("admin@itms.com");
        admin.setActive(true);
        userDao.addUser(admin);

        // Stock Manager
        com.itms.model.User stock = new com.itms.model.User();
        stock.setUsername("stock");
        stock.setPassword("stock123");
        stock.setRole("STOCK_MANAGER");
        stock.setFullName("Stock Manager");
        stock.setEmail("stock@itms.com");
        stock.setActive(true);
        userDao.addUser(stock);

        // Agent
        com.itms.model.User agent = new com.itms.model.User();
        agent.setUsername("agent");
        agent.setPassword("agent123");
        agent.setRole("AGENT");
        agent.setFullName("Helpdesk Agent");
        agent.setEmail("agent@itms.com");
        agent.setActive(true);
        userDao.addUser(agent);

        // Technician
        com.itms.model.User tech = new com.itms.model.User();
        tech.setUsername("tech");
        tech.setPassword("tech123");
        tech.setRole("TECHNICIAN");
        tech.setFullName("Field Technician");
        tech.setEmail("tech@itms.com");
        tech.setActive(true);
        userDao.addUser(tech);

        // Consultant
        com.itms.model.User consult = new com.itms.model.User();
        consult.setUsername("consultant");
        consult.setPassword("consult123");
        consult.setRole("CONSULTANT");
        consult.setFullName("IT Consultant");
        consult.setEmail("consultant@itms.com");
        consult.setActive(true);
        userDao.addUser(consult);

        System.out.println("Seeded default users (admin, stock, agent, tech, consultant).");
    }
}
