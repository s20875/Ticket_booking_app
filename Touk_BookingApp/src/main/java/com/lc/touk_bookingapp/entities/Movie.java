package com.lc.touk_bookingapp.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;



@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id
    @SequenceGenerator(
            name = "movie_sequence",
            sequenceName = "movie_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "movie_sequence")
    private Long movieId;

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    @Min(1)
    private int length;

    @OneToMany(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "movie_id",
            referencedColumnName = "movieId"
    )
    private Set<Screening> screenings;
}
