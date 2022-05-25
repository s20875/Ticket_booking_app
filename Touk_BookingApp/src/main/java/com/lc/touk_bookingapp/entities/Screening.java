package com.lc.touk_bookingapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Screening {

    @Id
    @SequenceGenerator(name = "screening_sequence", sequenceName = "screening_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "screening_sequence")
    private Long screeningId;

    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", referencedColumnName = "movieId", foreignKey = @ForeignKey(name = "FK_movie_screening"))
    @JsonIgnore
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", referencedColumnName = "roomId", foreignKey = @ForeignKey(name = "FK_room_screening"))
    @JsonIgnore
    private Room room;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "screening_Id", referencedColumnName = "screeningId", foreignKey = @ForeignKey(name = "FK_screening_reservation") )
    private Set<Reservation> reservations;
}
