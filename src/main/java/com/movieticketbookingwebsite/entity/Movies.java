package com.movieticketbookingwebsite.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movies {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Khớp với IDENTITY(1,1)
    private Integer id;

    @Column(nullable = false, length = 200) // Khớp với NVARCHAR(200) NOT NULL
    private String title;

    @Column(columnDefinition = "NVARCHAR(MAX)") // Khớp với NVARCHAR(MAX)
    private String description;

    @Column(nullable = false) // Khớp với INT NOT NULL
    private Integer duration;

    @Column(name = "release_date") // Khớp với release_date DATE
    private LocalDate releaseDate;

    @Column(length = 100) // Khớp với NVARCHAR(100)
    private String genre;

    @Column(name = "poster_url", length = 500) // Khớp với VARCHAR(500)
    private String posterUrl;

    @Column(name = "trailer_url", length = 500) // Khớp với VARCHAR(500)
    private String trailerUrl;

    @Column(length = 50) // Khớp với NVARCHAR(50)
    private String language;

    
    
    
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	public String getTrailerUrl() {
		return trailerUrl;
	}

	public void setTrailerUrl(String trailerUrl) {
		this.trailerUrl = trailerUrl;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
