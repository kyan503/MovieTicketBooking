package com.movieticketbookingwebsite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.movieticketbookingwebsite.entity.Seat;

public interface SeatBookingRepository extends JpaRepository<Seat, Integer> {
	@Query("SELECT s.id, s.seatNumber, s.seatType, " +
	           "(CASE WHEN t.id IS NOT NULL THEN true ELSE false END) as isBooked " +
	           "FROM Seat s " +
	           "JOIN Showtime st ON s.room.id = st.room.id " +
	           "LEFT JOIN Ticket t ON s.id = t.seat.id AND t.showtime.id = st.id " +
	           "WHERE st.id = :showtimeId")
	    List<Object[]> findSeatStatusByShowtime(@Param("showtimeId") Integer showtimeId);

}
