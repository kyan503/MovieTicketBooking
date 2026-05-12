import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';
import styles from './Auth.module.css';

const Register = () => {
    const [formData, setFormData] = useState({ username: '', password: '', email: '' });
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            await axios.post("http://localhost:8081/api/auth/register", formData);
            alert("Đăng ký thành công!");
            navigate("/login");
        } catch (err) {
            alert(err.response?.data || "Đăng ký thất bại!");
        }
    };

   return (
        <div className={styles.authPage}>
            <div className={styles.authCard}>
                <h2>Đăng Ký</h2>
                <form onSubmit={handleRegister}>
                    <div className={styles.authGroup}>
                        <input 
                            className={styles.authInput}
                            type="text" 
                            placeholder="Tên đăng nhập" 
                            required
                            onChange={e => setFormData({...formData, username: e.target.value})} 
                        />
                    </div>
                    <div className={styles.authGroup}>
                        <input 
                            className={styles.authInput}
                            type="email" 
                            placeholder="Email" 
                            required
                            onChange={e => setFormData({...formData, email: e.target.value})} 
                        />
                    </div>
                    <div className={styles.authGroup}>
                        <input 
                            className={styles.authInput}
                            type="password" 
                            placeholder="Mật khẩu" 
                            required
                            onChange={e => setFormData({...formData, password: e.target.value})} 
                        />
                    </div>
                    <button type="submit" className={styles.authBtn}>Đăng Ký</button>
                </form>
                <div className={styles.authFooter}>
                    Đã có tài khoản? <Link to="/login">Đăng nhập ngay.</Link>
                </div>
            </div>
        </div>
    );
};

export default Register;