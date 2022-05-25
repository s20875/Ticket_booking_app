package com.lc.touk_bookingapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserReservationDataDTO {

    private String name;
    private String surname;
    private List<TicketDTO> tickets;

    public UserReservationDataDTO(String name, String surname, List<TicketDTO> tickets) {
        this.name = name;
        this.surname = surname;
        this.tickets = tickets;
    }
}
