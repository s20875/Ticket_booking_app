package com.lc.touk_bookingapp.repositories;

import com.lc.touk_bookingapp.entities.Screening;
import com.lc.touk_bookingapp.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening,Long> {

    @Query(
            value = "select s from Screening s " +
                    "join fetch s.movie m " +
                    "where s.startDate >= ?1 and s.startDate <= ?2 " +
                    "order by m.title, s.startDate"
    )
    List<Screening> findAllByPeriod(LocalDateTime minDate, LocalDateTime maxDate);

    @Query(
            value = "select se from Seat se " +
                    "join fetch se.room r " +
                    "join fetch r.screenings s " +
                    "where s.screeningId = ?1 and se.seatId not in ( select s.seatId from Ticket t join t.seat s )"
    )
    List<Seat> findAllFreeSeats(Long screeningId);

}
