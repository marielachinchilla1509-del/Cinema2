package cinema;

import java.util.Date;

public class Membership {

    private Double discount;

    private Date validityTime;

    private String benefits;

    private TypeMembership type;

    public Membership(Double discount, Date validityTime, String benefits, 
            TypeMembership type) {
        this.discount = discount;
        this.validityTime = validityTime;
        this.benefits = benefits;
        this.type = type;
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

    @Override
    public String toString() {
        return "Membership{" + "discount=" + discount + ", validityTime=" +
                validityTime + ", benefits=" + benefits + ", type=" + type + '}';
    }
    
    
}
    


