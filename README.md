

🛒 ERP Grocery Management System

A Desktop-based ERP Grocery Management System developed using Java (Core + Swing) and MySQL.
The system is designed to manage grocery store operations including user authentication, inventory management, billing, and transaction handling with proper relational database structure.

📌 Project Overview

This application provides a complete ERP-style solution for small to medium grocery stores with:

Role-based access control

Real-time stock updates

GST-enabled billing system

Transaction-safe database operations

Automated invoice generation

🚀 Features
🔐 Authentication & Authorization

User Registration

Secure Login System

Role-based Access (Admin / Manager / Cashier)

📦 Inventory Management

Add New Products

View Product List

Update Stock Automatically After Sale

🧾 Billing System

Create Bills with GST Calculation

Multiple Products in Single Bill

Auto Invoice Generation (.txt file)

💾 Transaction Handling

JDBC Transaction Management

Commit & Rollback Support

Prevents Partial or Failed Data Entries

📊 Database Design

Proper Relational Schema

Sales & Sale Items Separation

Foreign Key Relationships

🛠 Tech Stack
Technology	Usage
Java (Core + Swing)	Frontend GUI & Business Logic
MySQL	Database
JDBC	Database Connectivity
Git & GitHub	Version Control
🗄 Database Structure
Main Tables

users

roles

products

suppliers

sales

sale_items

Relationships

One sale → Many sale_items

One role → Many users

One product → Many sale_items

📂 Project Structure
ERP-Grocery/
│
├── src/
│   ├── db/               # Database connection & config
│   ├── model/            # Entity classes (User, Product, Sale)
│   ├── dao/              # Database operations (CRUD)
│   ├── service/          # Business logic
│   ├── ui/               # Swing GUI screens
│   └── util/             # Utilities (Invoice Generator, Helpers)
│
├── invoices/             # Generated invoice .txt files
├── database/             # SQL Schema file
└── README.md

⚙️ How to Run

Clone the repository

Import project into IntelliJ IDEA / Eclipse

Configure MySQL Database

Update DB credentials in DBConnection.java

Run Main.java

🎯 Learning Outcomes

Practical understanding of ERP architecture

Hands-on JDBC transaction handling

Relational database design concepts

Role-based authentication implementation

File handling in Java

👨‍💻 Author

Aman Agrahari
MCA | Java Developer
Skilled in Java, JDBC, MySQL, DBMS, OOPS
