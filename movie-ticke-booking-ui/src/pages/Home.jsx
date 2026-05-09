import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../App.css';

const Home = () => {
    const [movies, setMovies] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchMovies = async () => {
            try {
                const token = localStorage.getItem("token");
                const res = await axios.get("http://localhost:8081/api/movies", {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setMovies(res.data);
            } catch (err) {
                console.error("Lỗi lấy danh sách phim:", err);
                if (err.response?.status === 403) {
                    navigate("/login");
                }
            }
        };
        fetchMovies();
    }, [navigate]);

    const handleLogout = () => {
        localStorage.clear();
        navigate("/login");
    };

    return (
        <div style={styles.container}>
            {/* Header / Navbar */}
            <nav style={styles.navbar}>
                <h1 style={styles.logo}>MOVIE TICKET</h1>
                <button onClick={handleLogout} style={styles.logoutBtn}>Đăng xuất</button>
            </nav>

            {/* Banner Quảng Cáo */}
            <div style={styles.banner}>
                <h2>Phim Đang Hot</h2>
                <p>Khám phá những bộ phim bom tấn mới nhất ngay hôm nay!</p>
            </div>

            {/* Danh sách phim */}
            <div style={styles.movieGrid}>
                {movies.map(movie => (
                    <div key={movie.id} style={styles.card}>
                        <img 
                            src={movie.imageUrl || 'https://via.placeholder.com/200x300'} 
                            alt={movie.title} 
                            style={styles.image} 
                        />
                        <div style={styles.cardInfo}>
                            <h3 style={styles.title}>{movie.title}</h3>
                            <p style={styles.genre}>{movie.genre}</p>
                            <button style={styles.bookBtn}>Đặt Vé</button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

// CSS inline đơn giản để bạn xem kết quả ngay
const styles = {
    container: { backgroundColor: '#141414', minHeight: '100vh', color: 'white', fontFamily: 'Arial, sans-serif' },
    navbar: { display: 'flex', justifyContent: 'space-between', padding: '20px 50px', backgroundColor: '#000' },
    logo: { color: '#e50914', margin: 0 },
    logoutBtn: { backgroundColor: '#e50914', color: 'white', border: 'none', padding: '8px 15px', borderRadius: '4px', cursor: 'pointer' },
    banner: { padding: '60px 50px', background: 'linear-gradient(to bottom, #000, #141414)', textAlign: 'center' },
    movieGrid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: '30px', padding: '0 50px 50px' },
    card: { backgroundColor: '#2f2f2f', borderRadius: '8px', overflow: 'hidden', textAlign: 'center', transition: '0.3s' },
    image: { width: '100%', height: '280px', objectFit: 'cover' },
    cardInfo: { padding: '15px' },
    title: { fontSize: '18px', margin: '10px 0' },
    genre: { color: '#b3b3b3', fontSize: '14px', marginBottom: '15px' },
    bookBtn: { backgroundColor: 'white', color: 'black', border: 'none', padding: '10px 20px', borderRadius: '4px', cursor: 'pointer', fontWeight: 'bold' }
};

export default Home;