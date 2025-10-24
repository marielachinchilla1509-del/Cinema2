package cinema;

import java.util.Date;

public class Employee extends Person {

    private double salary;

    private String address;

    private String employeeId;

    private String position;

    private Date startDay;

    public Employee(double salary, String address, String employeeId, 
            String position, Date startDay, boolean disability, Date birthDay, 
            String email, String id, String name, String phoneNumber) {
        super(disability, birthDay, email, id, name, phoneNumber);
        this.salary = salary;
        this.address = address;
        this.employeeId = employeeId;
        this.position = position;
        this.startDay = startDay;
    }

    public Employee() {
        double salary = 0;
        Date startDay = null;
        String address= "Not registered";
        String emplyeeId="Not registered";
        String position = "Not registered";
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    @Override
    public String toString() {
        return "Employee{" + "salary=" + salary + ", address=" + address + 
                ", employeeId=" + employeeId + ", position=" + position + 
                ", startDay=" + startDay + '}';
    }
    
    
    
    

    
}
