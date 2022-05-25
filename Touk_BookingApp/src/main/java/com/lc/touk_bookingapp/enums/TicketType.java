package com.lc.touk_bookingapp.enums;

public enum TicketType {
    ADULT(25),
    STUDENT(18),
    CHILD(12.5);

    private final double price;

    private TicketType(double price){
        this.price = price;
    }

    public double getPrice(){
        return this.price;
    }
}
