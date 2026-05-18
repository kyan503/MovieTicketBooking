import React, { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

const PaymentSuccess = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const ticketId = searchParams.get('id');

    useEffect(() => {
        // Sau 5 giây tự động đưa về trang chủ
        const timer = setTimeout(() => {
            navigate('/home'); 
        }, 5000);

        return () => clearTimeout(timer);
    }, [navigate]);

    return (
        <div style={{ textAlign: 'center', marginTop: '100px' }}>
            <h1 style={{ color: 'green' }}>Thanh toán thành công!</h1>
            <p>Mã hóa đơn của bạn là: <strong>#{ticketId}</strong></p>
            <p>Hệ thống sẽ đưa bạn về trang chủ sau 5 giây...</p>
            <button onClick={() => navigate('/')} className="btn btn-primary">
                Về trang chủ ngay
            </button>
        </div>
    );
};

export default PaymentSuccess;