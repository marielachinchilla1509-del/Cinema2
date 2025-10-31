package proyecto1.cinema;

import java.util.Date;

public class Seller extends Employee {
    
    private boolean state;

    private String idSeller;
    

    public Seller(String idSeller, boolean state, double salary, String address, 
            String employeeId, String position, Date startDay, 
            boolean disability, Date birthDay, String email, String id,
            String name, String phoneNumber) {
        super(salary, address, employeeId, position, startDay, disability, 
                birthDay, email, id, name, phoneNumber);
        this.idSeller = idSeller;
        this.state = state;
    }

    public Seller() {
        boolean state= false;
        String idSeller = "Not registered";
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getIdSeller() {
        return idSeller;
    }

    public void setIdSeller(String idSeller) {
        this.idSeller = idSeller;
    }

    @Override
    public String toString() {
        return "Seller{" + "state=" + state + ", idSeller=" + idSeller + '}';
    }

   

   


    
    }

    


