import React, { useEffect, useState } from 'react';
import axios from 'axios';
import styles from './Profile.module.css'; // Import file CSS Module vừa tạo

const Profile = ({ isOpen, onClose }) => {
    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (isOpen) {
            const fetchProfile = async () => {
                setLoading(true);
                try {
                    const token = localStorage.getItem("token");
                    const res = await axios.get("http://localhost:8081/api/users/profile", {
                        headers: { Authorization: `Bearer ${token}` }
                    });
                    setProfile(res.data);
                } catch (err) {
                    console.error("Lỗi lấy thông tin profile:", err);
                } finally {
                    setLoading(false);
                }
            };
            fetchProfile();
        }
    }, [isOpen]);

    if (!isOpen) return null;

    return (
        <div className={styles.overlay}>
            <div className={styles.content}>
                <h3 className={styles.title}>👤 THÔNG TIN TÀI KHOẢN</h3>
                <hr className={styles.divider} />
                
                {loading ? (
                    <p className={styles.loading}>Đang tải thông tin...</p>
                ) : profile ? (
                    <div className={styles.infoGroup}>
                        <p><strong>Tên tài khoản:</strong> {profile.username}</p>
                        <p><strong>Email đăng ký:</strong> {profile.email || "Chưa cập nhật"}</p>
                    </div>
                ) : (
                    <p className={styles.error}>Không thể tải thông tin.</p>
                )}

                <button onClick={onClose} className={styles.closeBtn}>Đóng</button>
            </div>
        </div>
    );
};

export default Profile;