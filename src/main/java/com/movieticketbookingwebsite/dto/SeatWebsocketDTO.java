package com.movieticketbookingwebsite.dto;

import java.util.List;

public class SeatWebsocketDTO {
	private Integer showtimeId;
    private List<Integer> seatIds;
    private String status; // "PENDING" hoặc "AVAILABLE"
    
 // Constructor trống
    public SeatWebsocketDTO() {}

    // Constructor có tham số
    public SeatWebsocketDTO(Integer showtimeId, List<Integer> seatIds, String status) {
        this.showtimeId = showtimeId;
        this.seatIds = seatIds;
        this.status = status;
    }
    public Integer getShowtimeId() { return showtimeId; }
    public void setShowtimeId(Integer showtimeId) { this.showtimeId = showtimeId; }

    public List<Integer> getSeatIds() { return seatIds; }
    public void setSeatIds(List<Integer> seatIds) { this.seatIds = seatIds; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}
