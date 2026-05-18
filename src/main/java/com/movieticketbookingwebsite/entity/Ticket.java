package com.movieticketbookingwebsite.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Tickets", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"showtime_id", "seat_id"})
})
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(name = "total_price", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "booking_time")
    private LocalDateTime bookingTime = LocalDateTime.now();

    @Column(length = 20)
    private String status = "PENDING";
    
    @Column(name ="vnp_TxnRef", length = 50)
    private String vnpTxnRef;
    
    @Column(name ="vnp_TransactionNo", length = 50)
    private String vnpTransactionNo;
    
    @Column(name ="vnp_ResponseCode", length = 10)
    private String vnpResponseCode;
    
    @Column(name ="vnp_PayDate", length = 20)
    private String vnpPayDate;

	public String getVnpTxnRef() {
		return vnpTxnRef;
	}

	public void setVnpTxnRef(String vnpTxnRef) {
		this.vnpTxnRef = vnpTxnRef;
	}

	public String getVnpTransactionNo() {
		return vnpTransactionNo;
	}

	public void setVnpTransactionNo(String vnpTransactionNo) {
		this.vnpTransactionNo = vnpTransactionNo;
	}

	public String getVnpResponseCode() {
		return vnpResponseCode;
	}

	public void setVnpResponseCode(String vnpResponseCode) {
		this.vnpResponseCode = vnpResponseCode;
	}

	public String getVnpPayDate() {
		return vnpPayDate;
	}

	public void setVnpPayDate(String vnpPayDate) {
		this.vnpPayDate = vnpPayDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Showtime getShowtime() {
		return showtime;
	}

	public void setShowtime(Showtime showtime) {
		this.showtime = showtime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public LocalDateTime getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(LocalDateTime bookingTime) {
		this.bookingTime = bookingTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
}