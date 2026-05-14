package com.movieticketbookingwebsite.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ShowtimeDTO {

	private Integer id;
    private LocalDateTime startTime;
    private BigDecimal ticketPrice;
    private String movieTitle;
    private String posterUrl;
    private String trailerUrl;
    private String roomName;
    
	public ShowtimeDTO(Integer id, LocalDateTime startTime, BigDecimal ticketPrice, String movieTitle, String posterUrl,
			String trailerUrl,String roomName) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.ticketPrice = ticketPrice;
		this.movieTitle = movieTitle;
		this.posterUrl = posterUrl;
		this.trailerUrl = trailerUrl;
		this.roomName = roomName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public BigDecimal getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(BigDecimal ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public String getMovieTitle() {
		return movieTitle;
	}

	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
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
	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	
}
