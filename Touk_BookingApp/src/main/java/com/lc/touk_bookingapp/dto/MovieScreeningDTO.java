package com.lc.touk_bookingapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MovieScreeningDTO {

    public MovieScreeningDTO(String title,  LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
