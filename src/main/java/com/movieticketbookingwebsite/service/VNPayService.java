package com.movieticketbookingwebsite.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.movieticketbookingwebsite.config.VNPayConfig;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class VNPayService {
	@Value("${vnp.pay_url}")
    private String vnp_PayUrl;
    @Value("${vnp.return_url}")
    private String vnp_ReturnUrl;
    @Value("${vnp.tmn_code}")
    private String vnp_TmnCode;
    @Value("${vnp.hash_secret}")
    private String vnp_HashSecret;

    public String createPaymentUrl(long amount, String orderInfo,String vnp_TxnRef, HttpServletRequest request) {
    	String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
       
        String vnp_IpAddr = VNPayConfig.getIpAddress(request);
        String vnp_TmnCode_Value = vnp_TmnCode;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode_Value);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // VNPay tính theo đơn vị VND x 100
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return vnp_PayUrl + "?" + queryUrl;
    	
    }
    public int orderReturn(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        
        // Lấy tất cả tham số từ request gửi về
        for (java.util.Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            
            // Chỉ lấy các tham số có giá trị và không trống
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue); // SỬA TẠI ĐÂY: Dùng put thay vì add
            }
        }

        // Lấy chữ ký từ VNPay gửi về để đối soát
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        
        // Loại bỏ các trường không tham gia vào việc tạo mã băm kiểm tra
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");
        
        // Sắp xếp các tham số còn lại theo thứ tự bảng chữ cái A-Z
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        
        StringBuilder hashData = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                // Lưu ý: VNPay trả về đã là chuỗi đã được encode hoặc nguyên bản tùy version, 
                // thông thường với v2.1.0 ta nối trực tiếp giá trị.
                hashData.append(java.net.URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                
                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }
        }

        // Tạo mã băm từ dữ liệu nhận được với HashSecret của bạn
        String signValue = VNPayConfig.hmacSHA512(vnp_HashSecret, hashData.toString());
        
        // So sánh chữ ký của bạn tạo ra với chữ ký VNPay gửi về
        if (signValue.equalsIgnoreCase(vnp_SecureHash)) {
            // Nếu khớp, kiểm tra mã phản hồi vnp_ResponseCode
            if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                return 1; // Thanh toán thành công
            } else {
                return 0; // Thanh toán thất bại (Người dùng hủy, lỗi thẻ...)
            }
        } else {
            return -1; // Sai chữ ký (Dữ liệu có dấu hiệu bị can thiệp)
        }
    }
}
