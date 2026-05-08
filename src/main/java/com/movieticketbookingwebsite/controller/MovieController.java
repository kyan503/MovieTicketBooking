package com.movieticketbookingwebsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieticketbookingwebsite.entity.Movies;
import com.movieticketbookingwebsite.service.MovieService;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "http://localhost:5173")
public class MovieController {

	@Autowired
	private MovieService movieService;
	
	@GetMapping
	public List<Movies> getAllMovies() {
        return movieService.getAllMovie();
    }
	
	@GetMapping("/{id}")
	public ResponseEntity<Movies> getById(@PathVariable Integer id) {
        return movieService.getMovieById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
	
	@PostMapping
	public Movies createMovie(@RequestBody Movies movie ) {
		return movieService.createMovie(movie);
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Movies> updateMoive(@PathVariable Integer id, @RequestBody Movies movie){
	return ResponseEntity.ok(movieService.updateMovie(id, movie));
	
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Movies> deleteMovie(@PathVariable Integer id){
	 movieService.deleteMovie(id);
	 return ResponseEntity.ok().build();
		
	}
	
}
