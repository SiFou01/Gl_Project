-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role TEXT NOT NULL, -- 'STOCK_MANAGER', 'ADMIN', 'AGENT', 'TECHNICIAN', 'CONSULTANT'
    full_name TEXT,
    email TEXT,
    active BOOLEAN DEFAULT 1
);

-- Products Table
CREATE TABLE IF NOT EXISTS products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    category TEXT,
    quantity INTEGER DEFAULT 0,
    status TEXT, -- 'ACTIVE', 'OUT_OF_STOCK', 'DISCONTINUED'
    barcode TEXT,
    location TEXT,
    supplier TEXT,
    purchase_date TEXT,
    warranty_end TEXT,
    description TEXT,
    price REAL
);

-- Consumables Table
CREATE TABLE IF NOT EXISTS consumables (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    category TEXT,
    quantity INTEGER DEFAULT 0,
    status TEXT,
    supplier TEXT
);

-- Repair Requests
CREATE TABLE IF NOT EXISTS repair_requests (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    product_id INTEGER,
    agent_id INTEGER,
    issue_description TEXT,
    status TEXT, -- 'PENDING', 'APPROVED', 'REJECTED', 'IN_PROGRESS', 'COMPLETED'
    request_date TEXT,
    priority TEXT,
    FOREIGN KEY(product_id) REFERENCES products(id),
    FOREIGN KEY(agent_id) REFERENCES users(id)
);

-- Consumable Requests
CREATE TABLE IF NOT EXISTS consumable_requests (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    consumable_id INTEGER,
    agent_id INTEGER,
    quantity INTEGER,
    status TEXT, -- 'PENDING', 'APPROVED', 'REJECTED'
    request_date TEXT,
    message TEXT,
    FOREIGN KEY(consumable_id) REFERENCES consumables(id),
    FOREIGN KEY(agent_id) REFERENCES users(id)
);

-- Maintenance Contracts
CREATE TABLE IF NOT EXISTS maintenance_contracts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    supplier_name TEXT,
    start_date TEXT,
    end_date TEXT,
    status TEXT, -- 'ACTIVE', 'EXPIRED'
    terms TEXT,
    product TEXT
);


