package com.movieticketbookingwebsite.service;

import java.util.List;

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
	
}
