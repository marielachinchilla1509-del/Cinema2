package proyecto1.cinema;

import java.util.Date;

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

    public Ticket() {
        double price = 0;
        Date date = null;
        String sitNumber = "Not registered";
        String movieTitle= "Not registered";
        String paymentMethod= "Not registered";
        String roomNumber= "Not registered";
        String status= " Not registered";
        String ticketId= "Not registered";
        String type= "Not registered";
        String showTime= "Not registered";
    }

    Ticket(String customer, String string, int qty, double price, double total) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

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

    @Override
    public String toString() {
        return "Ticket{" + "price=" + price + ", date=" + date + ", sitNumber="
                + sitNumber + ", movieTitle=" + movieTitle + ", paymentMethod="
                + paymentMethod + ", roomNumber=" + roomNumber + ", status=" + 
                status + ", ticketId=" + ticketId + ", type=" + type + 
                ", showTime=" + showTime + '}';
    }

    
    
    
}
