package com.movieticketbookingwebsite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.movieticketbookingwebsite.entity.Ticket;

import jakarta.mail.internet.MimeMessage;



@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTicketConfirmationEmail(String toEmail, Ticket ticket) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("🍿 XÁC NHẬN ĐẶT VÉ XEM PHIM THÀNH CÔNG - Mã hóa đơn #" + ticket.getId());

            // Thiết kế giao diện HTML gửi về email khách hàng
            String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>"
                    + "<h2 style='color: #e50914; text-align: center;'>CẢM ƠN BẠN ĐÃ ĐẶT VÉ!</h2>"
                    + "<p>Chào <strong>" + ticket.getUser().getUsername() + "</strong>,</p>"
                    + "<p>Giao dịch của bạn đã được xác nhận thành công thông qua cổng VNPay. Dưới đây là thông tin chi tiết vé xem phim của bạn:</p>"
                    + "<hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'>"
                    + "<table style='width: 100%; border-collapse: collapse;'>"
                    + "  <tr><td style='padding: 8px 0; color: #555;'><strong>Mã vé (Mã tra cứu):</strong></td><td style='padding: 8px 0;'><strong>" + ticket.getId() + "</strong></td></tr>"
                    + "  <tr><td style='padding: 8px 0; color: #555;'><strong>Suất chiếu ID:</strong></td><td style='padding: 8px 0;'>" + ticket.getShowtime().getId() + "</td></tr>"
                    + "  <tr><td style='padding: 8px 0; color: #555;'><strong>Ghế đã đặt:</strong></td><td style='padding: 8px 0; color: #e50914; font-weight: bold;'>" + ticket.getSeat().getSeatNumber() + "</td></tr>"
                    + "  <tr><td style='padding: 8px 0; color: #555;'><strong>Tổng tiền thanh toán:</strong></td><td style='padding: 8px 0; font-weight: bold;'>" + String.format("%,.0f", ticket.getTotalPrice()) + " VND</td></tr>"
                    + "  <tr><td style='padding: 8px 0; color: #555;'><strong>Mã giao dịch VNPay:</strong></td><td style='padding: 8px 0; color: #777;'>" + ticket.getVnpTransactionNo() + "</td></tr>"
                    + "</table>"
                    + "<hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'>"
                    + "<p style='font-size: 13px; color: #777; text-align: center;'>Vui lòng đưa mã vé này cho nhân viên tại quầy để nhận vé cứng vào rạp. Chúc bạn xem phim vui vẻ!</p>"
                    + "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
            System.out.println("Email xác nhận vé đã được gửi tới: " + toEmail);
            
        } catch (Exception e) {
            System.err.println("Lỗi xảy ra khi gửi email: " + e.getMessage());
        }
    }
}