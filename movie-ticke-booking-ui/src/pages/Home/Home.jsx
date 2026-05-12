import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import styles from './Home.module.css'; 

const Home = () => {
    // 1. Đồng nhất tên state: dùng 'showtimes' vì API trả về danh sách suất chiếu
    const [showtimes, setShowtimes] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchShowtimes = async () => {
            try {
                const token = localStorage.getItem("token");
                const res = await axios.get("http://localhost:8081/api/showtimes", {
                    headers: { Authorization: `Bearer ${token}` }
                });
                // 2. Cập nhật đúng tên hàm setter
                setShowtimes(res.data);
            } catch (err) {
                console.error("Lỗi lấy danh sách suất chiếu:", err);
                if (err.response?.status === 401 || err.response?.status === 403) {
                    navigate("/login");
                }
            }
        };
        // 3. Gọi đúng tên hàm đã khai báo bên trên
        fetchShowtimes();
    }, [navigate]);

    const handleLogout = () => {
        localStorage.clear();
        navigate("/login");
    };

    return (
        <div className={styles.container}>
            {/* Header / Navbar */}
            <nav className={styles.navbar}>
                <h1 className={styles.logo}>MOVIE TICKET</h1>
                <button onClick={handleLogout} className={styles.logoutBtn}>Đăng xuất</button>
            </nav>

            {/* Banner */}
            <div className={styles.banner}>
                <h2>Phim Đang Hot</h2>
                <p>Khám phá những bộ phim bom tấn mới nhất ngay hôm nay!</p>
            </div>

            {/* Danh sách suất chiếu */}
            <div className={styles.movieGrid}>
                {showtimes.length > 0 ? (
                    showtimes.map(st => (
                        <div key={st.id} className={styles.card}>
                            <img 
                                // 4. Sử dụng posterUrl từ DTO Backend trả về
                                src={st.posterUrl || 'https://via.placeholder.com/200x300'} 
                                alt={st.movieTitle} 
                                className={styles.image} 
                            />
                            <div className={styles.cardInfo}>
                                <h3 className={styles.title}>{st.movieTitle}</h3>
                                <p className={styles.genre}>Phòng: {st.roomName}</p>
                                <p className={styles.time}>
                                    {/* Định dạng lại thời gian cho dễ đọc */}
                                    {new Date(st.startTime).toLocaleString('vi-VN', {
                                        hour: '2-digit',
                                        minute: '2-digit',
                                        day: '2-digit',
                                        month: '2-digit'
                                    })}
                                </p>
                                {/* 5. st.id chính là showtimeId cần thiết cho trang booking */}
                                <button 
                                    className={styles.bookBtn} 
                                    onClick={() => navigate(`/booking/${st.id}/seats`)}
                                >
                                    Đặt Vé
                                </button>
                            </div>
                        </div>
                    ))
                ) : (
                    <p style={{textAlign: 'center', width: '100%'}}>Hiện không có suất chiếu nào khả dụng.</p>
                )}
            </div>
        </div>
    );
};

export default Home;