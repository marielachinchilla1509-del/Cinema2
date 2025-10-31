package proyecto1.cinema;

public class Product {

    private double price;

    private int stock;

    private String category;

    private String code;

    private String description;

    private String productName;

    private String Status;

    public Product(double price, int stock, String category, String code,
            String description, String productName, String Status) {
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.code = code;
        this.description = description;
        this.productName = productName;
        this.Status = Status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    @Override
    public String toString() {
        return "Product{" + "price=" + price + ", stock=" + stock
                + ", category=" + category + ", code=" + code + ", description="
                + description + ", productName=" + productName + ", Status="
                + Status + '}';
    }

}
