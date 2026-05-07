package com.movieticketbookingwebsite.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movieticketbookingwebsite.entity.Movies;
import com.movieticketbookingwebsite.repository.MovieRepository;

@Service
public class MovieService {

	@Autowired 
	private MovieRepository movieRepository;
	
	public List<Movies> getAllMovie(){
		return movieRepository.findAll();
	}
	
	public Optional<Movies> getMovieById(Integer id){
		return movieRepository.findById(id);
	
	}
	
	public Movies createMovie(Movies movie) {
		return movieRepository.save(movie);
		
	}
	
	public Movies updateMovie(Integer id, Movies movieDetails) {
        return movieRepository.findById(id).map(movie -> {
            movie.setTitle(movieDetails.getTitle());
            movie.setDescription(movieDetails.getDescription());
            movie.setDuration(movieDetails.getDuration());
            movie.setGenre(movieDetails.getGenre());
            movie.setReleaseDate(movieDetails.getReleaseDate());
            movie.setLanguage(movieDetails.getLanguage());
            movie.setPosterUrl(movieDetails.getPosterUrl());
            movie.setTrailerUrl(movieDetails.getTrailerUrl());
            return movieRepository.save(movie);
        }).orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
    }
				
		
	public void deleteMovie(Integer id) {
		movieRepository.deleteById(id);
		
	}
}
