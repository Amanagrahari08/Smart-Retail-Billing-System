package model;

public class Product {

    private String name;
    private double price;
    private int quantity;
    private double gstPercentage;
    private int supplierId;

    public Product(String name, double price, int quantity, double gstPercentage, int supplierId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.gstPercentage = gstPercentage;
        this.supplierId = supplierId;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public double getGstPercentage() { return gstPercentage; }
    public int getSupplierId() { return supplierId; }
}
