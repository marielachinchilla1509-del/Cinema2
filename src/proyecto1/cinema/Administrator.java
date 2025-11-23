package proyecto1.cinema;

import java.util.Date;

/*
 * Represents an administrator employee in the cinema system. Contains
 * additional administrative responsibilities such as calculating total sales,
 * updating prices, and reviewing cash registers. Inherits common employee
 * information from the Employee class.
 */
public class Administrator extends Employee {

    private double bonus;

    private double calculateTotalSales;

    private String adminId;

    private String performanceGrade;

    private String updatePrices;

    private String reviewCashRegistrer;

    public Administrator(double bonus, double calculateTotalSales,
            String adminId, String performanceGrade, String updatePrices,
            String reviewCashRegistrer, double salary, String address,
            String employeeId, String position, Date startDay,
            boolean disability, Date birthDay, String email, String id,
            String name, String phoneNumber) {
        super(salary, address, employeeId, position, startDay, disability,
                birthDay, email, id, name, phoneNumber);
        this.bonus = bonus;
        this.calculateTotalSales = calculateTotalSales;
        this.adminId = adminId;
        this.performanceGrade = performanceGrade;
        this.updatePrices = updatePrices;
        this.reviewCashRegistrer = reviewCashRegistrer;
    }

    /*
     * Creates an Administrator with all corresponding values and inherited
     * employee data. Initializes administrative and performance-related fields.
     */
    public Administrator() {
        this.bonus = 0;
        this.calculateTotalSales = 0;
        this.adminId = "Not registered";
        this.performanceGrade = "Not registered";
        this.updatePrices = "Not registered";
        this.reviewCashRegistrer = "Not registered";
    }
// we used the setter and getters

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getCalculateTotalSales() {
        return calculateTotalSales;
    }

    public void setCalculateTotalSales(double calculateTotalSales) {
        this.calculateTotalSales = calculateTotalSales;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getPerformanceGrade() {
        return performanceGrade;
    }

    public void setPerformanceGrade(String performanceGrade) {
        this.performanceGrade = performanceGrade;
    }

    public String getUpdatePrices() {
        return updatePrices;
    }

    public void setUpdatePrices(String updatePrices) {
        this.updatePrices = updatePrices;
    }

    public String getReviewCashRegistrer() {
        return reviewCashRegistrer;
    }

    public void setReviewCashRegistrer(String reviewCashRegistrer) {
        this.reviewCashRegistrer = reviewCashRegistrer;
    }
// and we used the to string for return the information

    @Override
    public String toString() {
        return "Administrator{" + "bonus=" + bonus + ", calculateTotalSales="
                + calculateTotalSales + ", adminId=" + adminId
                + ", performanceGrade=" + performanceGrade + ", updatePrices="
                + updatePrices + ", reviewCashRegistrer="
                + reviewCashRegistrer + '}';
    }

}


    

