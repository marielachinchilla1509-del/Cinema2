package proyecto1.cinema;

import java.util.Date;

/**
 * Represents a movie ticket in the cinema system.
 * Stores all relevant information for ticket sales.
 * 
 * @author Kendall
 * @author Carolina
 */
public class Ticket {

    private double price;
    private Date date;
    private String sitNumber;
    private String movieTitle;
    private String paymentMethod;
    private String roomNumber;
    private String status;
    private String ticketId;
    private String type;
    private String showTime;

    // ===== Constructor with all parameters =====
    public Ticket(double price, Date date, String sitNumber, String movieTitle,
                  String paymentMethod, String roomNumber, String status,
                  String ticketId, String type, String showTime) {

        this.price = price;
        this.date = date;
        this.sitNumber = sitNumber;
        this.movieTitle = movieTitle;
        this.paymentMethod = paymentMethod;
        this.roomNumber = roomNumber;
        this.status = status;
        this.ticketId = ticketId;
        this.type = type;
        this.showTime = showTime;
    }

    // ===== Default constructor =====
    public Ticket() {
        this.price = 0.0;
        this.date = new Date(); // sets current date
        this.sitNumber = "";
        this.movieTitle = "";
        this.paymentMethod = "";
        this.roomNumber = "";
        this.status = "";
        this.ticketId = "";
        this.type = "";
        this.showTime = "";
    }

    // ===== Getters and Setters =====
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSitNumber() {
        return sitNumber;
    }

    public void setSitNumber(String sitNumber) {
        this.sitNumber = sitNumber;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    // ===== Display Ticket Info (Optional for debugging) =====
    public void printTicketInfo() {
        System.out.println("Movie: " + movieTitle);
        System.out.println("Room: " + roomNumber);
        System.out.println("Seat: " + sitNumber);
        System.out.println("Type: " + type);
        System.out.println("Payment: " + paymentMethod);
        System.out.println("Price: $" + price);
        System.out.println("Date: " + date);
        System.out.println("Status: " + status);
    }

    public Object getCustomerName() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
