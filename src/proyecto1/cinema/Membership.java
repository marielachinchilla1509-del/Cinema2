package proyecto1.cinema;

import java.util.Date;

/*
 * The Membership class represents the membership program of the cinema.
 * A membership provides extra benefits to customers such as discounts,
 * special promotions, and access to exclusive services.
 * 
 * It includes information about the discount applied, validity period,
 * benefits description, and the specific membership type.
 * 
 * This class is typically used when calculating invoice discounts
 * or displaying membership details to customers.
 *
 */
public class Membership {

    /* Percentage discount the membership provides (e.g., 10%, 15%). */
    private Double discount;

    /* Date until the membership remains valid. */
    private Date validityTime;

    /* Description of additional benefits for the customer. */
    private String benefits;

    /* Type of membership (e.g., GOLD, PLATINUM, VIP, REGULAR). */
    private TypeMembership type;

    /*
     * Full constructor used to create a membership with specific values.
     *
     */
    public Membership(Double discount, Date validityTime, String benefits,
                      TypeMembership type) {
        this.discount = discount;
        this.validityTime = validityTime;
        this.benefits = benefits;
        this.type = type;
    }

    /*
     * Default constructor used when no membership data is available.
     */
    public Membership() {
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Date getValidityTime() {
        return validityTime;
    }

    public void setValidityTime(Date validityTime) {
        this.validityTime = validityTime;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public TypeMembership getType() {
        return type;
    }

    public void setType(TypeMembership type) {
        this.type = type;
    }

    /*
     * Returns a readable representation of the membership.
     *
     * return formatted string representation
     */
    @Override
    public String toString() {
        return "Membership{" +
                "discount=" + discount +
                ", validityTime=" + validityTime +
                ", benefits='" + benefits + '\'' +
                ", type=" + type +
                '}';
    }
}


