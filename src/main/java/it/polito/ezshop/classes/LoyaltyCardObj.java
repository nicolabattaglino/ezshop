package it.polito.ezshop.classes;

public class LoyaltyCardObj {
    private String cardCode;
    private Integer points;
    private boolean isAttached;
    
    public LoyaltyCardObj(String cardCode) {
        this.cardCode = cardCode;
        this.points = 0;
        this.isAttached = false;
    }

    public boolean getIsAttached(){
        return isAttached;
    }

    public void setIsAttached(boolean card){
         isAttached = card;
    }
    public String getCardCode() {
        return cardCode;
    }
    
    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }
    
    public Integer getPoints() {
        return points;
    }
    
    public void setPoints(Integer points) {
        this.points = points;
    }
}
