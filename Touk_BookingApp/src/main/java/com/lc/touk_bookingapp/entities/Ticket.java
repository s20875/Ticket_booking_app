package com.lc.touk_bookingapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lc.touk_bookingapp.enums.TicketType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    @Id
    @SequenceGenerator(name = "ticket_sequence", sequenceName = "ticket_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_sequence")
    private Long ticketId;

    @NotNull
    private TicketType ticketType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", referencedColumnName = "reservationId")
    @JsonIgnore
    private Reservation reservation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", referencedColumnName = "seatId", foreignKey = @ForeignKey(name = "FK_ticket_seat"))
    private Seat seat;

    public Ticket( TicketType ticketType, Reservation reservation, Seat seat) {
        this.ticketType = ticketType;
        this.reservation = reservation;
        this.seat = seat;
    }
}
