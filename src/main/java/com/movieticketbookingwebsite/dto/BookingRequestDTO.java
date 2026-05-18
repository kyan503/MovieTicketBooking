package com.movieticketbookingwebsite.dto;

import java.math.BigDecimal;
import java.util.List;

public class BookingRequestDTO {
	private Integer showtimeId;      // ID của suất chiếu
    private List<Integer> seatIds;   // Danh sách ID các ghế đã chọn
    private BigDecimal totalPrice; 
    
    
    // Tổng số tiền cần thanh toán
	public Integer getShowtimeId() {
		return showtimeId;
	}
	public void setShowtimeId(Integer showtimeId) {
		this.showtimeId = showtimeId;
	}
	public List<Integer> getSeatIds() {
		return seatIds;
	}
	public void setSeatIds(List<Integer> seatIds) {
		this.seatIds = seatIds;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
}
