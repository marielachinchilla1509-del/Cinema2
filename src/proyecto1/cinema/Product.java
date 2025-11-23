package proyecto1.cinema;

/*
 * The Product class represents an item available for purchase inside
 * the cinema, typically including snacks, beverages, or merchandise.
 *
 * It contains essential commercial information such as price, stock
 * quantity, category classification, product code, description, and
 * availability status.
 *
 * This class is used by the sales module to register, modify and
 * display product information within the system.
 */
public class Product {

    /* Price assigned to the product. */
    private double price;

    /* Number of units available in stock. */
    private int stock;

    /* Product category (e.g., food, drink, candy, souvenir). */
    private String category;

    /* Unique identification code for the product. */
    private String code;

    /* Brief description of the product. */
    private String description;

    /* Official commercial name of the product. */
    private String productName;

    /** Availability status (e.g., active, inactive, out of stock). */
    private String status;

    /*
     * Default constructor that initializes product information
     * with default placeholder values.
     */
    public Product() {
        this.price = 0.0;
        this.stock = 0;
        this.category = "";
        this.code = "";
        this.description = "";
        this.productName = "";
        this.status = "";
    }

    /*
     * Constructor used to define a product with specific parameters.
     */
    public Product(double price, int stock, String category, String code,
                   String description, String productName, String status) {
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.code = code;
        this.description = description;
        this.productName = productName;
        this.status = status;
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
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /*
     * Returns a formatted string representation of product details.
     */
    @Override
    public String toString() {
        return "Product{" +
                "price=" + price +
                ", stock=" + stock +
                ", category='" + category + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", productName='" + productName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
