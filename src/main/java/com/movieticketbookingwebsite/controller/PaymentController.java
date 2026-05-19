package com.movieticketbookingwebsite.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movieticketbookingwebsite.service.EmailService;
import com.movieticketbookingwebsite.service.TicketService;
import com.movieticketbookingwebsite.service.VNPayService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin("http://localhost:5173")
public class PaymentController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private EmailService emailService;

    // 1. Endpoint gọi từ React khi nhấn thanh toán
    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(
            @RequestParam("ticketId") Integer ticketId,
            HttpServletRequest request) {
        
        // Lấy thông tin vé từ DB để lấy số tiền thực tế
        return ticketService.getTicketById(ticketId).<ResponseEntity<?>>map(ticket -> {
            long amount = ticket.getTotalPrice().longValue();
            String vnp_TxnRef = String.valueOf(ticket.getId()); // Dùng ID vé làm mã tham chiếu
            
            // Cập nhật vnp_TxnRef vào Ticket trước khi gửi đi
            ticket.setVnpTxnRef(vnp_TxnRef);
            ticketService.save(ticket);

            String paymentUrl = vnPayService.createPaymentUrl(amount, "Thanh toan ve xem phim", vnp_TxnRef, request);
            
            Map<String, String> response = new HashMap<>();
            response.put("url", paymentUrl);
            return ResponseEntity.ok(response);
        }).orElse(ResponseEntity.badRequest().body("Không tìm thấy vé"));
    }

    // 2. Endpoint xử lý kết quả trả về từ VNPay (Redirect về Frontend)
    @GetMapping("/vnpay-callback")
    public ResponseEntity<?> paymentCallback(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);

        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String vnp_PayDate = request.getParameter("vnp_PayDate");

        if (paymentStatus == 1) {
            // 1. Cập nhật Ticket sang trạng thái PAID trong cơ sở dữ liệu
            ticketService.updatePaymentStatus(vnp_TxnRef, vnp_TransactionNo, vnp_ResponseCode, vnp_PayDate, "PAID");
            
            // 2. KÍCH HOẠT GỬI EMAIL: Lấy thông tin vé vừa thanh toán xong để lấy email khách hàng
            try {
                Integer ticketId = Integer.parseInt(vnp_TxnRef);
                ticketService.getTicketById(ticketId).ifPresent(ticket -> {
                    // Lấy email từ đối tượng User liên kết với Ticket
                    String customerEmail = ticket.getUser().getEmail(); 
                    if (customerEmail != null && !customerEmail.isEmpty()) {
                        // Tiến hành gửi Email bất đồng bộ hoặc đồng bộ trực tiếp
                        emailService.sendTicketConfirmationEmail(customerEmail, ticket);
                   }
                });
            } catch (Exception e) {
                System.err.println("Lỗi tìm kiếm vé hoặc gửi email xác nhận: " + e.getMessage());
            }

            return ResponseEntity.status(302).location(URI.create("http://localhost:5173/payment-success?id=" + vnp_TxnRef)).build();
        } else {
            ticketService.updatePaymentStatus(vnp_TxnRef, vnp_TransactionNo, vnp_ResponseCode, vnp_PayDate, "CANCELLED");
            return ResponseEntity.status(302).location(URI.create("http://localhost:5173/payment-failed")).build();
        }
    }
}
