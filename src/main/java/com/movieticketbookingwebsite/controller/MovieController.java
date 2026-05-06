package com.movieticketbookingwebsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieticketbookingwebsite.entity.Movies;
import com.movieticketbookingwebsite.service.MovieService;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

	@Autowired
	private MovieService movieService;
	
	@GetMapping
	public List<Movies> getAllMovies() {
        List<Movies> list = movieService.getAllMovie();
        System.out.println("Size of list: " + list.size()); // Thêm dòng này để check log Console
        return list;
    }
}
