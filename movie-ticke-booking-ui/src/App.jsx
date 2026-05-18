import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Auth/Login';
import Register from './pages/Auth/Register';
import Home from './pages/Home/Home';
import AdminMovie from './pages/Admin/AdminMovie';
import Booking from './pages/Booking/Booking';
import PaymentSuccess from './pages/Booking/PaymentSuccess'; 
//import PaymentFailed from './pages/PaymentFailed';


// Component kiểm tra quyền truy cập
const ProtectedRoute = ({ children, allowedRoles }) => {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    if (!token) return <Navigate to="/login" />;
    if (!allowedRoles.includes(role)) return <Navigate to="/home" />;
    return children;
};

function App() {
    return (
        <Router>
            <Routes>
                {/* Các route không cần login */}
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                
                {/* Các route bảo vệ */}
                <Route path="/home" element={
                    <ProtectedRoute allowedRoles={['ROLE_USER', 'ROLE_ADMIN']}>
                        <Home />
                    </ProtectedRoute>
                } />

                <Route path="/booking/:showtimeId/seats" element={
                    <ProtectedRoute allowedRoles={['ROLE_USER', 'ROLE_ADMIN']}>
                        <Booking />
                    </ProtectedRoute>
                    
                } />
                <Route path="/payment-success" element={
                    <ProtectedRoute allowedRoles={['ROLE_USER', 'ROLE_ADMIN']}>
                        <PaymentSuccess />
                    </ProtectedRoute>
                } />

      

                <Route path="/admin/movies" element={
                    <ProtectedRoute allowedRoles={['ROLE_ADMIN']}>
                        <AdminMovie />
                    </ProtectedRoute>
                } />

                <Route path="/" element={<Navigate to="/login" />} />
            </Routes>
        </Router>
    );
}

export default App;