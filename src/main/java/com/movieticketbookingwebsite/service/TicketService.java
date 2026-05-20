package com.movieticketbookingwebsite.service;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;

import com.movieticketbookingwebsite.entity.Ticket;
import com.movieticketbookingwebsite.entity.User;
import com.movieticketbookingwebsite.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {

	@Autowired
    private  TicketRepository ticketRepository;
	
	@Transactional
	public void updatePaymentStatus(String txnRef, String transNo, String responseCode, String payDate, String status) {
	   ticketRepository.findByVnpTxnRef(txnRef).ifPresent(ticket -> {
		   ticket.setVnpTxnRef(txnRef);
		   ticket.setVnpTransactionNo(transNo);
		   ticket.setVnpResponseCode(responseCode);
		   ticket.setVnpPayDate(payDate);    
		   ticket.setStatus(status); 
	       ticketRepository.save(ticket);
	   });
		
		
	}
	public Optional<Ticket> getTicketById(Integer id) {
        return ticketRepository.findById(id);
    }

    public void save(Ticket ticket) {
        ticketRepository.save(ticket);
    }
    
    public List<Ticket> getBookingHistory(User user){
    	return ticketRepository.findByUserAndStatusOrderByIdDesc(user,"PAID");
    	
    }

}
