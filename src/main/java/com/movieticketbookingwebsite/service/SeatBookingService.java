package com.movieticketbookingwebsite.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movieticketbookingwebsite.dto.BookingRequestDTO;
import com.movieticketbookingwebsite.dto.SeatStatusDTO;
import com.movieticketbookingwebsite.entity.Seat;
import com.movieticketbookingwebsite.entity.Showtime;
import com.movieticketbookingwebsite.entity.Ticket;
import com.movieticketbookingwebsite.entity.User;
import com.movieticketbookingwebsite.repository.SeatBookingRepository;
import com.movieticketbookingwebsite.repository.ShowtimeRepository;
import com.movieticketbookingwebsite.repository.TicketRepository;
import com.movieticketbookingwebsite.repository.UserRepository;


@Service
public class SeatBookingService {

	@Autowired
	private SeatBookingRepository seatBookingRepository;
	
	@Autowired
	private ShowtimeRepository showtimeReposioty;
	
	@Autowired
    private TicketRepository ticketRepository;
	@Autowired
    private UserRepository userRepository;
	
	public List<SeatStatusDTO> getSeatsForShowTime(Integer showtimeId){
		// 1. Lấy thông tin suất chiếu để có giá gốc
		Showtime showtime = showtimeReposioty.findById(showtimeId).orElseThrow(() -> new RuntimeException("Không tìm thấy xuất chiếu"));
		
		BigDecimal basePrice = showtime.getTicketPrice();
		
		// 2. Lấy dữ liệu ghế và trạng thái đặt
        List<Object[]> results = seatBookingRepository.findSeatStatusByShowtime(showtimeId);
        return results.stream().map(row -> {
            SeatStatusDTO dto = new SeatStatusDTO();
            dto.setSeatId((Integer) row[0]);
            dto.setSeatNumber((String) row[1]);
            String type = (String) row[2];
            dto.setSeatType(type);
            dto.setBooked((Boolean) row[3]);

            // 3. Logic tính giá động dựa trên loại ghế
            BigDecimal finalPrice = basePrice;
            if ("VIP".equals(type)) {
                finalPrice = finalPrice.add(BigDecimal.valueOf(20000));
            } else if ("COUPLE".equals(type)) {
                finalPrice = finalPrice.add(BigDecimal.valueOf(50000));
            }
            
            dto.setPrice(finalPrice);
            return dto;
	}).collect(Collectors.toList());
	}
	
	@Transactional
    public Ticket createPendingTicket(BookingRequestDTO request, String username) {
		// 1. Lấy đối tượng User trực tiếp
	    User user = userRepository.findByUsername(username);

	    // 2. Kiểm tra null thay vì dùng orElseThrow
	    if (user == null) {
	        throw new RuntimeException("User not found: " + username);
	    }

        // 2. Tìm Suất chiếu
        Showtime showtime = showtimeReposioty.findById(request.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        // 3. Với VNPay, thường ta tạo 1 giao dịch tổng. 
        // Tuy nhiên, nếu DB của bạn thiết kế 1 Ticket là 1 ghế, hãy lấy ghế đầu tiên đại diện hoặc tạo vòng lặp.
        // Ở đây tôi xử lý tạo 1 bản ghi Ticket đại diện cho giao dịch này:
        
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setShowtime(showtime);
        
        // Giả sử lấy ghế đầu tiên trong danh sách chọn để lưu vào bảng Tickets (do ràng buộc DB của bạn)
        if (!request.getSeatIds().isEmpty()) {
            Seat seat = seatBookingRepository.findById(request.getSeatIds().get(0))
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            ticket.setSeat(seat);
        }

        ticket.setTotalPrice(request.getTotalPrice());
        ticket.setStatus("PENDING");
        ticket.setBookingTime(LocalDateTime.now());
        
        // Lưu vào DB để lấy Ticket ID
        return ticketRepository.save(ticket);
    }

}

