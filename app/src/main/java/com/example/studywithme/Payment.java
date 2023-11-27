package com.example.studywithme;

public class Payment {
    private int seatNum;
    private int selectedTime;
    private int fee;
    private String paymentDate;
    private String cardName;
    private String cafeId;
    private String userEmail;

    public Payment() {

    }

    public Payment(int seatNum, int selectedTime, int fee, String paymentDate, String cardName, String cafeId, String userEmail) {
        this.seatNum = seatNum;
        this.selectedTime = selectedTime;
        this.fee = fee;
        this.paymentDate = paymentDate;
        this.cardName = cardName;
        this.cafeId = cafeId;
        this.userEmail = userEmail;
    }

    public int getSeatNum() {
        return seatNum;
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
