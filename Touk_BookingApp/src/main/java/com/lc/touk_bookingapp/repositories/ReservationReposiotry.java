package com.lc.touk_bookingapp.repositories;

import com.lc.touk_bookingapp.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationReposiotry extends JpaRepository<Reservation, Long> {
}
