package com.example.studywithme;

public class Payment {
    private int seatNum;
    private int selectedTime;
    private int fee;
    private String paymentDate;
    private String cardName;
    private int cafeId;

    public Payment() {

    }

    public Payment(int seatNum, int selectedTime, int fee, String paymentDate, String cardName, int cafeId) {
        this.seatNum = seatNum;
        this.selectedTime = selectedTime;
        this.fee = fee;
        this.paymentDate = paymentDate;
        this.cardName = cardName;
        this.cafeId = cafeId;
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

    public int getCafeId() {
        return cafeId;
    }
}
