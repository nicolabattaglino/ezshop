package it.polito.ezshop.classes;

public class LoyaltyCardObj {
    private String cardCode;
    private Integer points;

    public LoyaltyCardObj(String uuidAsString) {
        this.cardCode = uuidAsString;
        this.points = 0;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public Integer getPoints(){
        return points;
    }
    public void setPoints(Integer points){
        this.points = points;
    }
}
