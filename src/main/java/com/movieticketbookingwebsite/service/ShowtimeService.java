package com.movieticketbookingwebsite.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movieticketbookingwebsite.dto.ShowtimeDTO;
import com.movieticketbookingwebsite.repository.ShowtimeRepository;

@Service
public class ShowtimeService {

	@Autowired
	private ShowtimeRepository showtimeRepositoy;
	
    public List<ShowtimeDTO> getAllShowtimes() {
        return showtimeRepositoy.findAllWithMovieAndRoom().stream()
            .map(s -> new ShowtimeDTO(
                s.getId(),
                s.getStartTime(),
                s.getTicketPrice(),
                s.getMovie().getTitle(),
                s.getMovie().getPosterUrl(),
                s.getMovie().getTrailerUrl(),
                s.getRoom().getName()
            )).collect(Collectors.toList());
    }
	

}
