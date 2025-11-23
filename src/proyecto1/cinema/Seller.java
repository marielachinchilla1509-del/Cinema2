package proyecto1.cinema;

import java.util.Date;

/*
 * Represents a Seller employee within the cinema system.
 * <p>
 * A Seller is a specialized type of {@link Employee} who is responsible for 
 * managing sales operations such as selling movie tickets or products.
 * This class extends the Employee class by adding Seller-specific attributes
 * including the seller's state (active/inactive) and unique seller identifier.
 * </p>
 * 
 * <h3>Features:</h3>
 * <ul>
 *   <li>Inheritance from Employee class</li>
 *   <li>State control (availability)</li>
 *   <li>Unique seller identification code</li>
 * </ul>
 */
public class Seller extends Employee {

    /*
     * Indicates whether the seller is currently active.
     */
    private boolean state;

    /**
     * Unique identifier code for the seller.
     */
    private String idSeller;

    /*
     * Full constructor used for creating a Seller object with complete information.
     *
     */
    public Seller(String idSeller, boolean state, double salary, String address,
            String employeeId, String position, Date startDay,
            boolean disability, Date birthDay, String email, String id,
            String name, String phoneNumber) {

        super(salary, address, employeeId, position, startDay, disability,
                birthDay, email, id, name, phoneNumber);

        this.idSeller = idSeller;
        this.state = state;
    }

    /*
     * Default constructor initializing a seller with predefined values.
     * <p>
     * Used when creating an empty Seller record that will be filled later.
     * </p>
     */
    public Seller() {
        super(0.0, "Not registered", "Not registered", "Not registered", null,
                false, null, "Not registered", "Not registered", 
                "Not registered", "Not registered");
        this.state = false;
        this.idSeller = "Not registered";
    }

    /*
     * Returns whether the seller is active.
     *
     */
    public boolean isState() {
        return state;
    }

    /*
     * Updates the seller's state.
     *
     */
    public void setState(boolean state) {
        this.state = state;
    }

    /*
     * Returns the seller unique identifier.
     *
     * return seller ID
     */
    public String getIdSeller() {
        return idSeller;
    }

    /*
     * Sets a new unique seller identifier.
     *
     * 
     */
    public void setIdSeller(String idSeller) {
        this.idSeller = idSeller;
    }

    /*
     * Provides a string representation of the Seller including its state and ID.
     *
     * return formatted text describing the seller
     */
    @Override
    public String toString() {
        return "Seller{" + "state=" + state + ", idSeller=" + idSeller + '}';
    }

}

