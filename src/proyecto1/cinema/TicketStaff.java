package proyecto1.cinema;

import java.util.Date;

/*
 * <h2>Class representing the ticket staff employees in the cinema.</h2>
 * 
 * <p>
 * This class is part of the Cinema Management System and allows registering
 * and handling staff responsible for ticket-related operations.
 * </p>
 * 
 * <p>
 * It inherits from the Employee class, extending basic employee information
 * with a specific identifier code for ticket staff members.
 * </p>
 * 
 * <p>
 * Authors: Mariela, Astrid, Buso, Kendall
 * </p>
 */
public class TicketStaff extends Employee {

    /* Unique identifier code assigned to ticket staff. */
    private String staffId;

    /*
     * Constructor that initializes a ticket staff employee with full data.
     */
    public TicketStaff(String staffId, double salary, String address,
            String employeeId, String position, Date startDay, 
            boolean disability, Date birthDay, String email, String id, 
            String name, String phoneNumber) {
        super(salary, address, employeeId, position, startDay, disability,
                birthDay, email, id, name, phoneNumber);
        this.staffId = staffId;
    }

    /*
     * Default constructor that sets default values if no data is provided.
     */
    public TicketStaff() {
        this.staffId = "Not registered";
    }

    /* Returns the ticket staff unique identifier. */
    public String getStaffId() {
        return staffId;
    }

    /* Updates the unique identifier for the ticket staff. */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    /* Returns a formatted string showing ticket staff data. */
    @Override
    public String toString() {
        return "TicketStaff{" + "staffId=" + staffId + '}';
    }
}
