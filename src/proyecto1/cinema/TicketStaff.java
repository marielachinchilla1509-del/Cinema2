package proyecto1.cinema;

import java.util.Date;

public class TicketStaff extends Employee {

    private String staffId;

    public TicketStaff(String staffId, double salary, String address,
            String employeeId, String position, Date startDay, 
            boolean disability, Date birthDay, String email, String id, 
            String name, String phoneNumber) {
        super(salary, address, employeeId, position, startDay, disability,
                birthDay, email, id, name, phoneNumber);
        this.staffId = staffId;
    }

    public TicketStaff() {
        String staffId = "Not registered";
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    @Override
    public String toString() {
        return "TicketStaff{" + "staffId=" + staffId + '}';
    }
    
    
   
}

