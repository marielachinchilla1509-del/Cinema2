package cinema;

public enum TypeMembership {


    BASIC(0.05, "Acceso básico a funciones y descuentos mínimos"),
    PRO(0.10, "Descuentos moderados y acceso anticipado a funciones"),
    PREMIUM(0.20, "Todos los beneficios, máximo descuento y funciones exclusivas");

    
    private double discount;
    private String benefits;

    private TypeMembership(double discount, String benefits) {
        this.discount = discount;
        this.benefits = benefits;
    }

    public double getDiscount() {
        return discount;
    }

    public String getBenefits() {
        return benefits;
    }
    
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    
    @Override
    public String toString() {
        return name() + " {" +
               "discount=" + (discount * 100) + "%, " +
               "benefits='" + benefits + '\'' +
               '}';
    }
}

   


    
    
    
