package com.movieticketbookingwebsite.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Movies> createMovie(
	        @ModelAttribute Movies movie, 
	        @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
	    return ResponseEntity.ok(movieService.createMovie(movie, file));
	}

	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Movies> updateMovie(
	        @PathVariable Integer id, 
	        @ModelAttribute Movies movie,
	        @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
	    return ResponseEntity.ok(movieService.updateMovie(id, movie, file));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Movies> deleteMovie(@PathVariable Integer id){
	 movieService.deleteMovie(id);
	 return ResponseEntity.ok().build();
		
	}
	
}
