package com.lc.touk_bookingapp.dto;

import com.lc.touk_bookingapp.entities.Seat;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomAndFreeSeatsDTO {

    private String roomName;
    private List<Seat> freeSeats;

    public RoomAndFreeSeatsDTO(String roomName, List<Seat> seats) {
        this.roomName = roomName;
        this.freeSeats = seats;
    }
}
