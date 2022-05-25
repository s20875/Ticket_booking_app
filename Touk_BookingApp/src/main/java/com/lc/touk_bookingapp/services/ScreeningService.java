package com.lc.touk_bookingapp.services;

import com.lc.touk_bookingapp.dto.*;
import com.lc.touk_bookingapp.entities.*;
import com.lc.touk_bookingapp.repositories.ReservationReposiotry;
import com.lc.touk_bookingapp.repositories.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Collator;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final ReservationReposiotry reservationReposiotry;

    @Autowired
    public ScreeningService(ScreeningRepository screeningRepository, ReservationReposiotry reservationReposiotry){
        this.screeningRepository = screeningRepository;
        this.reservationReposiotry = reservationReposiotry;
    }

    public List<MovieScreeningDTO> findScreeningsInGivenTimePeriod(LocalDateTime minDate, LocalDateTime maxDate){
        /*return screeningRepository.findAllByPeriod(minDate, maxDate).stream().map(s -> new MovieScreeningDTO(
                                    s.getMovie().getTitle(), s.getStartDate(), s.getEndDate())).collect(Collectors.toList());*/
        Collator coll = Collator.getInstance(new Locale("pl","PL"));
        return screeningRepository.findAllByPeriod(minDate, maxDate).stream().map(s -> new MovieScreeningDTO(
                s.getMovie().getTitle(), s.getStartDate(), s.getEndDate())).sorted(Comparator.comparing(MovieScreeningDTO::getTitle,coll).thenComparing(MovieScreeningDTO::getStartDate,Comparator.naturalOrder())).collect(Collectors.toList());
    }

    public ResponseEntity<Object> getRoomAndFreeSeats(Long screeningId){
        Optional<Screening> screeningOpt = screeningRepository.findById(screeningId);
        if(screeningOpt.isPresent()){
            Room room = screeningOpt.get().getRoom();
            List<Seat> freeSeats = screeningRepository.findAllFreeSeats(screeningId);
            RoomAndFreeSeatsDTO res = new RoomAndFreeSeatsDTO(room.getName(),freeSeats);
            return new ResponseEntity<>(res,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Screening with given id doesnt exist", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public ResponseEntity<Object> makeReservation(Long screeningId, UserReservationDataDTO reservationData){
        Pattern p = Pattern.compile("^[A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]*'[A-ZŻŹĆĄŚĘŁÓŃ][a-zżźćńółęąś]*$");
        Matcher m = p.matcher(reservationData.getSurname());
        if(reservationData.getName().length()<3 || !m.matches()){
            return new ResponseEntity<>("Name or surname in wrong format", HttpStatus.BAD_REQUEST);
        }

        Optional<Screening> screeningOpt = screeningRepository.findById(screeningId);
        if(screeningOpt.isEmpty()){
            return new ResponseEntity<>("Screening with given id doesnt exist", HttpStatus.NOT_FOUND);
        }
        if(!screeningOpt.get().getStartDate().isAfter(LocalDateTime.now().plusMinutes(16))){
            return new ResponseEntity<>("Reservation should be done minimum 15 minutes before screening", HttpStatus.BAD_REQUEST);
        }

        Map<Long, Seat> freeSeats = screeningRepository.findAllFreeSeats(screeningId).stream().collect(Collectors.toMap(Seat::getSeatId, s -> s));
        for (TicketDTO t: reservationData.getTickets()) {
            if(!freeSeats.containsKey(t.getSeatId())){
                return new ResponseEntity<>("Chosen seat with id: "+ t.getSeatId()+" is already taken", HttpStatus.BAD_REQUEST);
            }

            Seat seat =freeSeats.get(t.getSeatId());

            if(reservationData.getTickets().stream().noneMatch(s -> {
                Seat tmp = freeSeats.get(s.getSeatId());
                return tmp.getSeatColumn() == seat.getSeatColumn()+1 && tmp.getSeatRow() == seat.getSeatRow();
            })){
                Optional<Map.Entry<Long, Seat>> first = freeSeats.entrySet().stream().filter(s -> s.getValue().getSeatRow() == seat.getSeatRow() && s.getValue().getSeatColumn() == seat.getSeatColumn() + 1).findFirst();
                if(first.isPresent()){
                    Optional<Map.Entry<Long, Seat>> second = freeSeats.entrySet().stream().filter(s -> s.getValue().getSeatRow() == seat.getSeatRow() && s.getValue().getSeatColumn() == seat.getSeatColumn() + 2).findFirst();
                    if(second.isPresent()){
                        if(reservationData.getTickets().stream().anyMatch(s -> {
                            Seat tmp = freeSeats.get(s.getSeatId());
                            return tmp.getSeatColumn() == seat.getSeatColumn()+2 && tmp.getSeatRow() == seat.getSeatRow();
                        })){
                            return new ResponseEntity<>("One free seat can't be left", HttpStatus.BAD_REQUEST);
                        }
                    } else{
                        return new ResponseEntity<>("One free seat can't be left", HttpStatus.BAD_REQUEST);
                    }


                }
            }
            if(reservationData.getTickets().stream().noneMatch(s -> {
                Seat tmp = freeSeats.get(s.getSeatId());
                return tmp.getSeatColumn() == seat.getSeatColumn()-1 && tmp.getSeatRow() == seat.getSeatRow();
            })){
                Optional<Map.Entry<Long, Seat>> first = freeSeats.entrySet().stream().filter(s -> s.getValue().getSeatRow() == seat.getSeatRow() && s.getValue().getSeatColumn() == seat.getSeatColumn() - 1).findFirst();
                if(first.isPresent()){
                    Optional<Map.Entry<Long, Seat>> second = freeSeats.entrySet().stream().filter(s -> s.getValue().getSeatRow() == seat.getSeatRow() && s.getValue().getSeatColumn() == seat.getSeatColumn() - 2).findFirst();
                    if(second.isPresent()){
                        if(reservationData.getTickets().stream().anyMatch(s -> {
                            Seat tmp = freeSeats.get(s.getSeatId());
                            return tmp.getSeatColumn() == seat.getSeatColumn()-2 && tmp.getSeatRow() == seat.getSeatRow();
                        })){
                            return new ResponseEntity<>("One free seat can't be left", HttpStatus.BAD_REQUEST);
                        }
                    } else{
                        return new ResponseEntity<>("One free seat can't be left", HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }


        Reservation reservation =new Reservation(reservationData.getName(),reservationData.getSurname(),screeningOpt.get());

        double totalAmount =0.0;
        for (TicketDTO t: reservationData.getTickets()) {
            reservation.getTickets().add(new Ticket(t.getTicketType(),reservation,freeSeats.get(t.getSeatId())));
            totalAmount += t.getTicketType().getPrice();
        }

        reservationReposiotry.save(reservation);
        return new ResponseEntity<>(new ReservationResultDTO(totalAmount,LocalDateTime.now().plusMinutes(20)),HttpStatus.OK);

    }
}
