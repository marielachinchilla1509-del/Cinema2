package cinema;

import java.util.Date;

public class Person {

    private boolean disability;

    private Date birthDay;

    private String email;

    private String id;

    private String name;

    private String phoneNumber;

    public Person(boolean disability, Date birthDay, String email, String id, 
            String name, String phoneNumber) {
        this.disability = disability;
        this.birthDay = birthDay;
        this.email = email;
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Person() {
      boolean disability = false;
      Date birthday = null;
      String email= "Not registered";
      String id = "Not registered";
      String name = "Not registered ";
      String phoneNumber = "Not registered";
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

    @Override
    public String toString() {
        return "Person{" + "disability=" + disability + ", birthDay=" + 
                birthDay + ", email=" + email + ", id=" + id + ", name=" +
                name + ", phoneNumber=" + phoneNumber + '}';
    }
    
    
    
    
    
    

    
    
    
    
}
