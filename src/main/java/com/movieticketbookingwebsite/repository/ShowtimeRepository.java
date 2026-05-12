package com.movieticketbookingwebsite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.movieticketbookingwebsite.entity.Showtime;

public interface ShowtimeRepository extends JpaRepository<Showtime, Integer>{
	@Query("SELECT s FROM Showtime s JOIN FETCH s.movie JOIN FETCH s.room")
   List<Showtime> findAllWithMovieAndRoom();
}
