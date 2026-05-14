import React from 'react';
import styles from './Home.module.css';

const Trailer = ({ isOpen, onClose, trailerUrl }) => {
    if (!isOpen) return null;

    return (
        <div className={styles.modalOverlay} onClick={onClose}>
            <div className={styles.modalContent} onClick={e => e.stopPropagation()}>
                <button className={styles.closeBtn} onClick={onClose}>&times;</button>
                <div className={styles.videoWrapper}>
                    <iframe
                        width="100%"
                        height="100%"
                        src={`${trailerUrl}?autoplay=1`} // Thêm tự động phát nếu muốn
                        title="Movie Trailer"
                        frameBorder="0"
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                        allowFullScreen
                    ></iframe>
                </div>
            </div>
        </div>
    );
};

export default Trailer;