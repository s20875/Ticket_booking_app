package com.lc.touk_bookingapp.controllers;

import com.lc.touk_bookingapp.dto.MovieScreeningDTO;
import com.lc.touk_bookingapp.dto.RoomAndFreeSeatsDTO;
import com.lc.touk_bookingapp.dto.UserReservationDataDTO;
import com.lc.touk_bookingapp.services.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "screening")
public class ScreeningController {

    private final ScreeningService screeningService;

    @Autowired
    public ScreeningController(ScreeningService screeningService){
        this.screeningService = screeningService;
    }

    @GetMapping(path = "/get")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<MovieScreeningDTO> getScreeningsInGivenTimePeriod(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime minDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime maxDate){
        List<MovieScreeningDTO> resp = screeningService.findScreeningsInGivenTimePeriod(minDate,maxDate);
        return resp;
    }

    @GetMapping(path = "get/{id}")
    public ResponseEntity<Object> getRoomAndFreeSeats(@PathVariable("id") Long screeningId){
        return screeningService.getRoomAndFreeSeats(screeningId);
    }

    @PostMapping (path = "reservation/{id}")
    public ResponseEntity<Object> makeReservation(@PathVariable("id") Long screeningId, @RequestBody UserReservationDataDTO reservationData){

        return screeningService.makeReservation(screeningId,reservationData);
    }

}
