package proyecto1.cinema;

import java.util.Date;

/*
 * Represents an employee in the cinema system.
 * Inherits common personal information from the Person class and includes
 * additional employee-related data such as salary, work position, and contract start date.
 */
public class Employee extends Person {

    /* Employee salary value. */
    private double salary;

    /* Physical address or residence of the employee. */
    private String address;

    /* Unique identifier assigned to the employee. */
    private String employeeId;

    /* Job title or assigned work position within the organization. */
    private String position;

    /*Date on which the employee started working in the company. */
    private Date startDay;

    /*
     * Creates an Employee with specified values and inherited Person data.
     * Initializes salary, address, internal ID, position, and start date.
     */
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

    /*
     * Default constructor initializing values with default or unregistered states.
     */
    public Employee() {
        this.salary = 0;
        this.startDay = null;
        this.address = "Not registered";
        this.employeeId = "Not registered";
        this.position = "Not registered";
    }

    /*
     * Retrieves employee salary.
     */
    public double getSalary() {
        return salary;
    }

    /*
     * Updates employee salary.
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }

    /*
     * Retrieves employee address.
     */
    public String getAddress() {
        return address;
    }

    /*
     * Updates employee address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /*
     * Retrieves employee unique ID.
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /*
     * Updates employee unique ID.
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /*
     * Retrieves employee job position.
     */
    public String getPosition() {
        return position;
    }

    /*
     * Updates employee job position.
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /*
     * Retrieves employee start date.
     */
    public Date getStartDay() {
        return startDay;
    }

    /*
     * Updates employee start date.
     */
    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    /*
     * Returns a readable representation of employee information.
     */
    @Override
    public String toString() {
        return "Employee{" + "salary=" + salary + ", address=" + address + 
                ", employeeId=" + employeeId + ", position=" + position + 
                ", startDay=" + startDay + '}';
    }
}
