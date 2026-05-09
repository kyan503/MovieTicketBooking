import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';

const Login = () => {
    const [credentials, setCredentials] = useState({ username: '', password: '' });
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const res = await axios.post("http://localhost:8081/api/auth/login", credentials);
            localStorage.setItem("token", res.data.accessToken);
            localStorage.setItem("role", res.data.role);

            if (res.data.role === 'ROLE_ADMIN') navigate("/admin/movies");
            else navigate("/home");
        } catch (err) {
            alert("Tên đăng nhập hoặc mật khẩu không đúng!");
        }
    };

    return (
        <div className="auth-page">
            <div className="auth-card">
                <h2>Đăng Nhập</h2>
                <form onSubmit={handleLogin}>
                    <div className="auth-group">
                        <input 
                            className="auth-input"
                            type="text" 
                            placeholder="Username" 
                            required
                            onChange={e => setCredentials({...credentials, username: e.target.value})} 
                        />
                    </div>
                    <div className="auth-group">
                        <input 
                            className="auth-input"
                            type="password" 
                            placeholder="Mật khẩu" 
                            required
                            onChange={e => setCredentials({...credentials, password: e.target.value})} 
                        />
                    </div>
                    <button type="submit" className="auth-btn">Đăng Nhập</button>
                </form>
                <div className="auth-footer">
                    Mới sử dụng dịch vụ? <Link to="/register">Đăng ký ngay.</Link>
                </div>
            </div>
        </div>
    );
};

export default Login;