package com.lc.touk_bookingapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationResultDTO {
    private double totalAmountToPay;
    private LocalDateTime expirationTime;

    public ReservationResultDTO(double totalAmountToPay, LocalDateTime expirationTime) {
        this.totalAmountToPay = totalAmountToPay;
        this.expirationTime = expirationTime;
    }
}
