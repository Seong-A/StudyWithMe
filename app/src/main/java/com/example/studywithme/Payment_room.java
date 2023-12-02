package com.example.studywithme;

public class Payment_room {
    private int id ;
    private int selectedTime;
    private int fee;
    private String paymentDate;
    private String cardName;
    private String cafeId;
    private String userEmail;

    public Payment_room() {

    }

    public Payment_room(int id, int selectedTime, int fee, String paymentDate, String cardName, String cafeId, String userEmail) {
        this.id = id;
        this.selectedTime = selectedTime;
        this.fee = fee;
        this.paymentDate = paymentDate;
        this.cardName = cardName;
        this.cafeId = cafeId;
        this.userEmail = userEmail;
    }

    public int getId() {
        return id;
    }

    public int getSelectedTime() {
        return selectedTime;
    }

    public int getFee() {
        return fee;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCafeId() {
        return cafeId;
    }

    public String getUserEmail() { return userEmail; }
}
