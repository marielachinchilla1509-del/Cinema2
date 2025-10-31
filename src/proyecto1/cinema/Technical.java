package proyecto1.cinema;

import java.util.Date;

public class Technical extends Employee {

    private String idTechnical;

    public Technical(String idTechnical, double salary, String address,
            String employeeId, String position, Date startDay,
            boolean disability, Date birthDay, String email, String id,
            String name, String phoneNumber) {
        super(salary, address, employeeId, position, startDay, disability,
                birthDay, email, id, name, phoneNumber);
        this.idTechnical = idTechnical;
    }

    public Technical() {
        String idTechnical = "Not registered";
        
    }

    public String getIdTechnical() {
        return idTechnical;
    }

    public void setIdTechnical(String idTechnical) {
        this.idTechnical = idTechnical;
    }

    @Override
    public String toString() {
        return "Technical{" + "idTechnical=" + idTechnical + '}';
    }
    
    
    
}