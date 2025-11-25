package proyecto1.cinema;

import java.util.Date;

/*
 * The Person class represents a generic individual within the cinema system.
 * It provides basic personal information shared by customers and employees.
 * 
 * This class functions as a parent class for specialized roles such as
 * Employee and Customer, allowing common attributes to be reused and reducing
 * code duplication throughout the project.
 * 
 * It stores identification details, personal contact information, and
 * accessibility indicators.
 */
public class Person {

    /* Indicates whether the person has any disability condition. */
    boolean disability;

    /* Birth date of the person. */
    Date birthDay;

    /* Email contact address of the person. */
    private String email;

    /* Identification number of the person. */
    private String id;

    /* Full name of the person. */
    private String name;

    /* Phone contact number of the person. */
    String phoneNumber;

    /*
     * Constructor used to initialize all information of the person.
     */
    public Person(boolean disability, Date birthDay, String email, String id,
                  String name, String phoneNumber) {
        this.disability = disability;
        this.birthDay = birthDay;
        this.email = email;
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    /*
     * Default constructor used when no personal information is available.
     */
    public Person() {
        this.disability = false;
        this.birthDay = null;
        this.email = "Not registered";
        this.id = "Not registered";
        this.name = "Not registered";
        this.phoneNumber = "Not registered";
    }
    public boolean isDisability() {
        return disability;
    }

    public void setDisability(boolean disability) {
        this.disability = disability;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /*
     * Generates a readable string representation of the person's information.
     */
    @Override
    public String toString() {
        return "Person{" + "disability=" + disability + ", birthDay=" +
                birthDay + ", email=" + email + ", id=" + id + ", name=" +
                name + ", phoneNumber=" + phoneNumber + '}';
    }
}
