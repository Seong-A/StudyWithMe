package com.example.studywithme;

public class Payment_locker {
    private int LockerNumber;
    private int selectedTime;
    private int fee;
    private String paymentDate;
    private String cardName;
    private String cafeId;
    private String userEmail;

    public Payment_locker() {

    }

    public Payment_locker(int LockerNumber, int selectedTime, int fee, String paymentDate, String cardName, String cafeId, String userEmail) {
        this.LockerNumber = LockerNumber;
        this.selectedTime = selectedTime;
        this.fee = fee;
        this.paymentDate = paymentDate;
        this.cardName = cardName;
        this.cafeId = cafeId;
        this.userEmail = userEmail;
    }

    public int getLockerNumber() {
        return LockerNumber;
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
