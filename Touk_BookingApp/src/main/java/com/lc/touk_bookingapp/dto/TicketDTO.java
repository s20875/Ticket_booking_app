package com.lc.touk_bookingapp.dto;

import com.lc.touk_bookingapp.enums.TicketType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketDTO {

    private Long seatId;
    private TicketType ticketType;

    public TicketDTO(Long seatId, TicketType ticketType) {
        this.seatId = seatId;
        this.ticketType = ticketType;
    }
}
