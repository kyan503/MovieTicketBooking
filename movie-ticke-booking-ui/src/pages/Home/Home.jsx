import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Trailer from './Trailer'; // Component TrailerModal của bạn
import Profile from './Profile'; // ĐÃ IMPORT: Component Profile vừa tách riêng
import styles from './Home.module.css';

const Home = () => {
    const [showtimes, setShowtimes] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedTrailer, setSelectedTrailer] = useState("");
    const navigate = useNavigate();

    // ĐÃ BỔ SUNG: State kiểm soát trạng thái đóng/mở của cửa sổ Profile
    const [isProfileOpen, setIsProfileOpen] = useState(false);
    
    const handleWatchTrailer = (url) => {
        if (!url) {
            alert("Phim này hiện chưa có trailer!");
            return;
        }

        let embedUrl = url;
        if (url.includes("watch?v=")) {
            embedUrl = url.replace("watch?v=", "embed/");
        }

        setSelectedTrailer(embedUrl);
        setIsModalOpen(true);
    };

    useEffect(() => {
        const fetchShowtimes = async () => {
            try {
                const token = localStorage.getItem("token");
                const res = await axios.get("http://localhost:8081/api/showtimes", {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setShowtimes(res.data);
            } catch (err) {
                console.error("Lỗi lấy danh sách suất chiếu:", err);
                if (err.response?.status === 401 || err.response?.status === 403) {
                    navigate("/login");
                }
            }
        };
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
                <h1 className={styles.logo} onClick={() => navigate("/")} style={{ cursor: 'pointer' }}>
                    MOVIE TICKET
                </h1>
                
                {/* Khối bọc các nút chức năng trên Navbar */}
                <div className={styles.navActions} style={{ display: 'flex', gap: '15px', alignItems: 'center' }}>
                    
                    {/* ĐÃ BỔ SUNG: Nút bấm để mở Profile cá nhân */}
                    <button 
                        onClick={() => setIsProfileOpen(true)} 
                        style={{
                            backgroundColor: 'transparent',
                            color: '#ccc',
                            border: '1px solid #555',
                            padding: '8px 12px',
                            borderRadius: '4px',
                            cursor: 'pointer',
                            fontWeight: '500',
                            transition: 'all 0.2s'
                        }}
                    >
                        👤 Tài khoản
                    </button>

                    <button 
                        onClick={() => navigate("/history")} 
                        className={styles.historyBtn}
                        style={{
                            backgroundColor: '#e50914',
                            color: 'white',
                            border: 'none',
                            padding: '8px 16px',
                            borderRadius: '4px',
                            fontWeight: 'bold',
                            cursor: 'pointer',
                            transition: 'background 0.2s'
                        }}
                    >
                        🎟️ Lịch sử đặt vé
                    </button>
                    <button onClick={handleLogout} className={styles.logoutBtn}>Đăng xuất</button>
                </div>
            </nav>

            {/* Banner */}
            <header className={styles.banner}>
                <h2>Danh Sách Phim Đang Chiếu</h2>
                <p>Đặt vé ngay để trải nghiệm những siêu phẩm điện ảnh mới nhất</p>
            </header>

            {/* Danh sách suất chiếu */}
            <div className={styles.movieGrid}>
                {showtimes.length > 0 ? (
                    showtimes.map(st => (
                        <div key={st.id} className={styles.card}>
                            <img
                                src={st.posterUrl || 'https://via.placeholder.com/200x300'}
                                alt={st.movieTitle}
                                className={styles.image}
                            />
                            <div className={styles.cardInfo}>
                                <h3 className={styles.title}>{st.movieTitle}</h3>
                                <p className={styles.genre}>Phòng: {st.roomName}</p>
                                <p className={styles.time}>
                                    {new Date(st.startTime).toLocaleString('vi-VN', {
                                        hour: '2-digit',
                                        minute: '2-digit',
                                        day: '2-digit',
                                        month: '2-digit'
                                    })}
                                </p>

                                {/* Nút Xem Trailer bổ sung */}
                                <button
                                    className={styles.trailerBtn}
                                    onClick={() => handleWatchTrailer(st.trailerUrl)}
                                    style={{
                                        width: '100%',
                                        marginBottom: '10px',
                                        backgroundColor: '#333',
                                        color: 'white',
                                        border: 'none',
                                        padding: '8px',
                                        borderRadius: '4px',
                                        cursor: 'pointer'
                                    }}
                                >
                                    Xem Trailer
                                </button>

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
                    <p style={{ textAlign: 'center', width: '100%' }}>Hiện không có suất chiếu nào khả dụng.</p>
                )}
            </div>

            {/* Modal Trailer */}
            <Trailer
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                trailerUrl={selectedTrailer}
            />

            {/* ĐÃ BỔ SUNG: Nhúng Component Profile ẩn vào đây */}
            <Profile 
                isOpen={isProfileOpen} 
                onClose={() => setIsProfileOpen(false)} 
            />
        </div>
    );
};

export default Home;