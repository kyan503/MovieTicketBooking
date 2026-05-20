import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import styles from './Booking.module.css';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

const Booking = () => {
    const { showtimeId } = useParams();
    const [seats, setSeats] = useState([]);
    const [selectedSeats, setSelectedSeats] = useState([]);
    const navigate = useNavigate();

    // =========================================================
    // LẮNG NGHE WEBSOCKET REAL-TIME (ĐÃ SỬA THUỘC TÍNH CHO PHÙ HỢP CẤU TRÚC ĐB)
    // =========================================================
    useEffect(() => {
        // 1. Khởi tạo kết nối tới endpoint /ws của Spring Boot
        const socket = new SockJS('http://localhost:8081/ws');
        const stompClient = Stomp.over(socket);

        // Tắt bớt log debug của stomp trên console để tránh rối mắt
        stompClient.debug = null; 

        stompClient.connect({}, () => {
            console.log("Đã kết nối thành công WebSocket luồng giữ ghế!");

            // 2. Đăng ký (Subscribe) kênh lắng nghe chung của phòng vé
            stompClient.subscribe('/topic/seats', (response) => {
                const data = JSON.parse(response.body);
                
                // Đồng bộ kiểu dữ liệu bằng cách ép về String trước khi so sánh
                if (String(data.showtimeId) === String(showtimeId)) {
                    console.log("Nhận tín hiệu cập nhật ghế real-time hợp lệ:", data);

                    // Cập nhật lại danh sách ghế đang hiển thị trên màn hình
                    setSeats(prevSeats => prevSeats.map(seat => {
                        // Chuyển mảng seatIds nhận từ WebSocket thành chuỗi để so sánh chính xác với seat.seatId
                        const isMatch = data.seatIds.map(String).includes(String(seat.seatId));
                        
                        if (isMatch) {
                            return { 
                                ...seat, 
                                // Đổi chính xác thuộc tính 'booked' để CSS ăn theo và khóa nút bấm click
                                booked: data.status === "PENDING" 
                            };
                        }
                        return seat;
                    }));

                    // Xóa ghế khỏi danh sách đang chọn của bản thân nếu bị người khác nhanh tay giữ mất ở Tab 1
                    setSelectedSeats(prevSelected => {
                        // Tìm xem có ghế nào mình đang chọn nằm trong danh sách vừa bị khóa không
                        const hasLostSeat = prevSelected.some(s => 
                            data.seatIds.map(String).includes(String(s.seatId))
                        );

                        if (hasLostSeat && data.status === "PENDING") {
                            //alert("Có ghế bạn đang lựa chọn vừa được người khác tiến hành thanh toán!");
                            // Lọc bỏ ghế bị mất ra
                            return prevSelected.filter(s => 
                                !data.seatIds.map(String).includes(String(s.seatId))
                            );
                        }
                        return prevSelected;
                    });
                }
            });
        }, (error) => {
            console.error("Lỗi kết nối WebSocket:", error);
        });

        // Hủy kết nối khi rời khỏi trang đặt ghế
        return () => {
            if (stompClient && stompClient.connected) {
                stompClient.disconnect(() => {
                    console.log("Đã ngắt kết nối WebSocket");
                });
            }
        };
    }, [showtimeId]);

    // =========================================================
    // TẢI DANH SÁCH GHẾ BAN ĐẦU TỪ API BACKEND
    // =========================================================
    useEffect(() => {
        const fetchSeats = async () => {
            try {
                const token = localStorage.getItem("token");
                const res = await axios.get(`http://localhost:8081/api/booking/${showtimeId}/seats`, {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setSeats(res.data);
            } catch (err) {
                console.error("Lỗi tải ghế:", err);
                if (err.response?.status === 401 || err.response?.status === 403) {
                    alert("Phiên đăng nhập hết hạn, vui lòng đăng nhập lại!");
                    navigate("/login");
                }
            }
        };
        fetchSeats();
    }, [showtimeId, navigate]);

    // Logic chọn / hủy chọn ghế
    const toggleSeat = (seat) => {
        if (seat.booked) return; // Nếu ghế đã bị khóa/bán thì không làm gì hết
        
        if (selectedSeats.find(s => s.seatId === seat.seatId)) {
            setSelectedSeats(selectedSeats.filter(s => s.seatId !== seat.seatId));
        } else {
            setSelectedSeats([...selectedSeats, seat]);
        }
    };

    // Tính tổng tiền động dựa vào danh sách ghế chọn
    const totalPrice = selectedSeats.reduce((sum, s) => sum + s.price, 0);

    // Xử lý xác nhận tạo hóa đơn và nhảy link sang VNPay
    const handleConfirm = async () => {
        if (selectedSeats.length === 0) return alert("Vui lòng chọn ít nhất 1 ghế!");
        
        const token = localStorage.getItem("token");
        if (!token) {
            alert("Vui lòng đăng nhập để đặt vé!");
            return navigate("/login");
        }

        try {
            // BƯỚC 1: Tạo Ticket ở trạng thái PENDING trong Database
            const bookingRes = await axios.post(
                `http://localhost:8081/api/booking/create`, 
                {
                    showtimeId: parseInt(showtimeId),
                    seatIds: selectedSeats.map(s => s.seatId),
                    totalPrice: totalPrice
                },
                { headers: { Authorization: `Bearer ${token}` } }
            );

            const ticketId = bookingRes.data.id;

            // BƯỚC 2: Gọi API lấy URL thanh toán VNPay
            const paymentRes = await axios.post(
                `http://localhost:8081/api/payment/create-payment?ticketId=${ticketId}`,
                {}, 
                { headers: { Authorization: `Bearer ${token}` } }
            );

            // BƯỚC 3: Điều hướng người dùng đến trang điền OTP của VNPay
            if (paymentRes.data && paymentRes.data.url) {
                window.location.href = paymentRes.data.url;
            } else {
                alert("Không thể khởi tạo thanh toán, vui lòng thử lại.");
            }

        } catch (err) {
            console.error("Lỗi quy trình thanh toán:", err);
            alert(err.response?.data || "Đã xảy ra lỗi trong quá trình đặt vé.");
        }
    };

    return (
        <div className={styles.container}>
            <h2>MÀN HÌNH</h2>
            <div className={styles.screen}></div>

            <div className={styles.legend}>
                <div className={styles.legendItem}><div className={`${styles.seat} ${styles.available}`}></div> Trống</div>
                <div className={styles.legendItem}><div className={`${styles.seat} ${styles.selected}`}></div> Đang chọn</div>
                <div className={styles.legendItem}><div className={`${styles.seat} ${styles.booked}`}></div> Đã bán / Đang giữ</div>
            </div>

            <div className={styles.seatMap}>
                {seats.map(seat => (
                    <div 
                        key={seat.seatId}
                        className={`
                            ${styles.seat} 
                            ${seat.booked ? styles.booked : styles.available}
                            ${selectedSeats.find(s => s.seatId === seat.seatId) ? styles.selected : ''}
                            ${seat.seatType === 'VIP' ? styles.vip : ''}
                            ${seat.seatType === 'COUPLE' ? styles.couple : ''}
                        `}
                        // Nếu ghế có thuộc tính booked = true thì không cho click hoạt động nữa
                        onClick={() => !seat.booked && toggleSeat(seat)}
                        style={{ cursor: seat.booked ? 'not-allowed' : 'pointer' }}
                    >
                        {seat.seatNumber}
                    </div>
                ))}
            </div>

            <div className={styles.infoPanel}>
                <h3>Thông tin đặt vé</h3>
                <p>Ghế đã chọn: {selectedSeats.map(s => s.seatNumber).join(', ')}</p>
                <p>Tổng tiền: <strong>{totalPrice.toLocaleString()} VND</strong></p>
                <button className={styles.confirmBtn} onClick={handleConfirm}>
                    XÁC NHẬN ĐẶT VÉ
                </button>
            </div>
        </div>
    );
};

export default Booking;