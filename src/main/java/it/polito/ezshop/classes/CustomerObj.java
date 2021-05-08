package it.polito.ezshop.classes;

public class CustomerObj {

    private Integer id;
    private String name;
    private String surname;
    private LoyaltyCardObj loyaltyCard;

    public CustomerObj(Integer id, String customerName) {
        this.id = id;
        this.name = customerName;
    }

    public String getCustomerName() {
        return name;
    }

    public void setCustomerName(String customerName) {
        this.name = customerName;
    }

    public String getCustomerCard() {
        return loyaltyCard.getCardCode();
    }

    public void setCustomerCard(String customerCard) {

    }

    public Integer getId() {
        return null;
    }

    public void setId(Integer id) {

    }

    public Integer getPoints() {
        return loyaltyCard.getPoints();
    }

    public void setPoints(Integer points) {
        loyaltyCard.setPoints(points);
    }

}
