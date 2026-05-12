package com.movieticketbookingwebsite.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.movieticketbookingwebsite.dto.SeatStatusDTO;
import com.movieticketbookingwebsite.entity.Showtime;
import com.movieticketbookingwebsite.repository.SeatBookingRepository;
import com.movieticketbookingwebsite.repository.ShowtimeRepository;


@Service
public class SeatBookingService {

	@Autowired
	private SeatBookingRepository seatBookingRepository;
	
	@Autowired
	private ShowtimeRepository showtimeReposioty;
	
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
}

