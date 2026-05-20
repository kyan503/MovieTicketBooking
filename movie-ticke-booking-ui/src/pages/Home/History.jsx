import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import styles from './History.module.css';

const History = () => {
    const [historyList, setHistoryList] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchHistory = async () => {
            try {
                const token = localStorage.getItem("token");
                if (!token) {
                    alert("Vui lòng đăng nhập để xem lịch sử!");
                    return navigate("/login");
                }

                const res = await axios.get("http://localhost:8081/api/tickets/history", {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setHistoryList(res.data);
            } catch (err) {
                System.err.println("Lỗi tải lịch sử đặt vé:", err);
                if (err.response?.status === 401 || err.response?.status === 403) {
                    alert("Phiên đăng nhập hết hạn!");
                    navigate("/login");
                }
            } finally {
                setLoading(false);
            }
        };

        fetchHistory();
    }, [navigate]);

    if (loading) return <div style={{ color: '#fff', textAlign: 'center', marginTop: '50px' }}>Đang tải lịch sử...</div>;

    return (
        <div className={styles.container}>
            <h2 className={styles.title}>🍿 LỊCH SỬ ĐẶT VÉ CỦA BẠN</h2>
            
            {historyList.length === 0 ? (
                <p className={styles.noTicket}>Bạn chưa có lịch sử giao dịch đặt vé nào thành công.</p>
            ) : (
                historyList.map(ticket => (
                    <div key={ticket.id} className={styles.ticketCard}>
                        <div className={styles.leftInfo}>
                            {/* Hiển thị tên phim từ liên kết bảng nếu có, hoặc hiển thị mã suất chiếu */}
                            <h3>🎬 Phim: {ticket.showtime?.movie?.title || "Vé Xem Phim Hệ Thống"}</h3>
                            <p><strong>Mã tra cứu vé:</strong> #{ticket.id}</p>
                            <p><strong>Thời gian chiếu:</strong> {ticket.showtime?.startTime || "Chưa cập nhật"}</p>
                            <p><strong>Vị trí ghế:</strong> <span className={styles.seatHighlight}>{ticket.seat?.seatNumber || "N/A"}</span></p>
                            <p style={{ fontSize: '12px', color: '#777' }}>Mã GD VNPay: {ticket.vnpTransactionNo}</p>
                        </div>
                        
                        <div className={styles.rightPrice}>
                            <div className={styles.price}>
                                {ticket.totalPrice ? ticket.totalPrice.toLocaleString() : 0} VND
                            </div>
                            <span className={styles.statusPaid}>ĐÃ THANH TOÁN</span>
                        </div>
                    </div>
                ))
            )}
        </div>
    );
};

export default History;