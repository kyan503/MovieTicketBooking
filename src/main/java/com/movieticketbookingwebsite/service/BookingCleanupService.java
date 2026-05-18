package com.movieticketbookingwebsite.service;

import com.movieticketbookingwebsite.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class BookingCleanupService {

    @Autowired
    private TicketRepository ticketRepository;

    // Chạy mỗi 1 phút một lần để kiểm tra
    @Scheduled(fixedRate = 60000) 
    @Transactional
    public void cleanupExpiredBookings() {
        // Định nghĩa thời gian hết hạn (ví dụ: 15 phút trước)
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(5);

        // Xóa các vé có trạng thái PENDING và thời gian đặt cũ hơn thời gian hết hạn
        // Bạn cần viết thêm hàm deleteByStatusAndBookingTimeBefore trong TicketRepository
        ticketRepository.deleteByStatusAndBookingTimeBefore("PENDING", expirationTime);
        
        System.out.println("Đã dọn dẹp các vé PENDING hết hạn lúc: " + LocalDateTime.now());
    }
}