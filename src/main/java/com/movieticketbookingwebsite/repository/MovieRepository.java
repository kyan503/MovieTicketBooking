package com.movieticketbookingwebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movieticketbookingwebsite.entity.Movies;


public interface MovieRepository extends JpaRepository<Movies,Integer> {

}
