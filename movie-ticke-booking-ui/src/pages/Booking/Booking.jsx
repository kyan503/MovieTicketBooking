import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import styles from './Booking.module.css';

const Booking = () => {
    const { showtimeId } = useParams();
    const [seats, setSeats] = useState([]);
    const [selectedSeats, setSelectedSeats] = useState([]);
    const navigate = useNavigate();
useEffect(() => {
    const fetchSeats = async () => {
        try {
            const token = localStorage.getItem("token"); // Lấy token từ storage
            const res = await axios.get(`http://localhost:8081/api/booking/${showtimeId}/seats`, {
                headers: { Authorization: `Bearer ${token}` } // Thêm header này vào
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
}, [showtimeId]);

    const toggleSeat = (seat) => {
        if (seat.booked) return;
        
        if (selectedSeats.find(s => s.seatId === seat.seatId)) {
            setSelectedSeats(selectedSeats.filter(s => s.seatId !== seat.seatId));
        } else {
            setSelectedSeats([...selectedSeats, seat]);
        }
    };

    const totalPrice = selectedSeats.reduce((sum, s) => sum + s.price, 0);

    const handleConfirm = () => {
        if (selectedSeats.length === 0) return alert("Vui lòng chọn ít nhất 1 ghế!");
        // Ở đây sẽ gọi tiếp API thanh toán/đặt vé
        console.log("Dữ liệu đặt vé:", { showtimeId, selectedSeats });
        alert("Chuyển đến trang thanh toán...");
    };

    return (
        <div className={styles.container}>
            <h2>MÀN HÌNH</h2>
            <div className={styles.screen}></div>

            <div className={styles.legend}>
                <div className={styles.legendItem}><div className={`${styles.seat} ${styles.available}`}></div> Trống</div>
                <div className={styles.legendItem}><div className={`${styles.seat} ${styles.selected}`}></div> Đang chọn</div>
                <div className={styles.legendItem}><div className={`${styles.seat} ${styles.booked}`}></div> Đã bán</div>
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
                        onClick={() => toggleSeat(seat)}
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