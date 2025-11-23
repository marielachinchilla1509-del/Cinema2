package proyecto1.cinema;

import java.util.Date;

/*
 * Represents a Technical employee within the cinema system.
 * <p>
 * A Technical employee is responsible for operational and maintenance tasks
 * such as equipment management, projector handling, and technical support.
 * This class extends {@link Employee} adding a unique technical identifier.
 * </p>
 */
public class Technical extends Employee {

    /*
     * Unique identifier assigned to the technical staff member.
     */
    private String idTechnical;

    /*
     * Full constructor used for creating a Technical object with complete information.
     */
    public Technical(String idTechnical, double salary, String address,
            String employeeId, String position, Date startDay,
            boolean disability, Date birthDay, String email, String id,
            String name, String phoneNumber) {

        super(salary, address, employeeId, position, startDay, disability,
                birthDay, email, id, name, phoneNumber);

        this.idTechnical = idTechnical;
    }

    /*
     * Default constructor initializing the technical employee with basic values.
     * <p>
     * Used when creating a technical record that will be completed later.
     * </p>
     */
    public Technical() {
        super(0.0, "Not registered", "Not registered", "Not registered", null,
                false, null, "Not registered", "Not registered",
                "Not registered", "Not registered");
        this.idTechnical = "Not registered";
    }

    /*
     * Returns the technical employee unique identifier.
     *
     * return idTechnical technical ID code
     */
    public String getIdTechnical() {
        return idTechnical;
    }

    /*
     * Assigns a new technical identifier code.
     *
     */
    public void setIdTechnical(String idTechnical) {
        this.idTechnical = idTechnical;
    }

    /*
     * Returns a textual representation of the Technical object.
     *
     * @return string describing the technical employee
     */
    @Override
    public String toString() {
        return "Technical{" + "idTechnical=" + idTechnical + '}';
    }

}
