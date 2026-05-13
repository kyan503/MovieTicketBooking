package com.movieticketbookingwebsite.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.movieticketbookingwebsite.entity.Movies;
import com.movieticketbookingwebsite.repository.MovieRepository;

@Service
public class MovieService {

	@Autowired 
	private MovieRepository movieRepository;
	
	@Autowired
	private CloudinaryService cloudinaryService;
	
	public List<Movies> getAllMovie(){
		return movieRepository.findAll();
	}
	
	public Optional<Movies> getMovieById(Integer id){
		return movieRepository.findById(id);
	
	}
	
	public Movies createMovie(Movies movie, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            // Upload lên Cloudinary và lấy URL trả về
            String url = cloudinaryService.uploadFile(file);
            movie.setPosterUrl(url); // Gán URL vào entity
        }
        return movieRepository.save(movie);
    }
	
	public Movies updateMovie(Integer id, Movies movieDetails, MultipartFile file) throws IOException {
        return movieRepository.findById(id).map(movie -> {
            movie.setTitle(movieDetails.getTitle());
            movie.setDescription(movieDetails.getDescription());
            movie.setDuration(movieDetails.getDuration());
            movie.setGenre(movieDetails.getGenre());
            movie.setReleaseDate(movieDetails.getReleaseDate());
            movie.setLanguage(movieDetails.getLanguage());
            movie.setTrailerUrl(movieDetails.getTrailerUrl());
            
            // Xử lý ảnh khi cập nhật
            if (file != null && !file.isEmpty()) {
                try {
                    String url = cloudinaryService.uploadFile(file);
                    movie.setPosterUrl(url);
                } catch (IOException e) {
                    throw new RuntimeException("Upload failed");
                }
            } else {
                // Nếu không có file mới, giữ nguyên URL cũ
                movie.setPosterUrl(movieDetails.getPosterUrl());
            }
            
            return movieRepository.save(movie);
        }).orElseThrow(() -> new RuntimeException("Movie not found"));
    }
				
		
	public void deleteMovie(Integer id) {
		movieRepository.deleteById(id);
		
	}
}
