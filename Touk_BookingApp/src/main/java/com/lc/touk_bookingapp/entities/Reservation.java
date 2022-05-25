package com.lc.touk_bookingapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @SequenceGenerator(name = "reservation_sequence", sequenceName = "reservation_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_sequence")
    private Long reservationId;

    @Length(min = 3, message = "Field should be at least 3 characters long")
    private String name;

    @Length(min = 3, message = "Field should be at least 3 characters long")
    @Pattern(regexp = "^[A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]*'[A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]*$", message = "Wrong characters pattern")
    private String surname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screening_Id", referencedColumnName = "screeningId")
    @JsonIgnore
    private Screening screening;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", referencedColumnName = "reservationId", foreignKey = @ForeignKey(name = "FK_reservation_ticket"))
    @NotEmpty(message = "Reservation should apply to at least one seat")
    private Set<Ticket> tickets;

    public Reservation(String name, String surname,Screening screening) {
        this.name = name;
        this.surname = surname;
        this.screening = screening;
        tickets = new HashSet<>();
    }
}
