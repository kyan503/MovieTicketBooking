package com.movieticketbookingwebsite.repository;



import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieticketbookingwebsite.entity.Ticket;
import com.movieticketbookingwebsite.entity.User;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
	Optional<Ticket> findByVnpTxnRef(String vnpTxnRef);
	void deleteByStatusAndBookingTimeBefore(String status, LocalDateTime time);
	
	List<Ticket> findByUserAndStatusOrderByIdDesc(User user, String status);

}
