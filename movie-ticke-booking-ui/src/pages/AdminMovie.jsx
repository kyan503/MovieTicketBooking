import axios from 'axios';
import { useEffect, useState } from 'react';
import '../App.css';

function AdminMovie() {
  const [movies, setMovies] = useState([]);
  const [newMovie, setNewMovie] = useState({ title: '', genre: '', duration: '' });
  const [isEditing, setIsEditing] = useState(false); // Trạng thái kiểm tra xem có đang sửa không
  const [currentId, setCurrentId] = useState(null); // Lưu ID phim đang được chọn để sửa
  


  const fetchMovies = async () => {
    try {
      const getHeader = () => {
        const token = localStorage.getItem("token");
        return { headers: { Authorization: `Bearer ${token}` } };
      };
      const response = await axios.get('http://localhost:8081/api/movies', getHeader());
      setMovies(response.data);
    } catch (error) {
      console.error("Lỗi khi lấy dữ liệu:", error);
    }
  };
  

  const deleteMovie = async (id) => {
    if (window.confirm("Bạn có chắc chắn muốn xóa phim này?")) {
      try {
        await axios.delete(`http://localhost:8081/api/movies/${id}`);
        fetchMovies();
      } catch (error) {
        console.error("Lỗi khi xóa:", error);
      }
    }
  };

  // Hàm để đưa dữ liệu phim cũ lên form để sửa
  const editMovie = (movie) => {
    setIsEditing(true);
    setCurrentId(movie.id);
    setNewMovie({ title: movie.title, genre: movie.genre, duration: movie.duration });
  };

  const handleSaveMovie = async (e) => {
    e.preventDefault();
    try {
      if (isEditing) {
        // Nếu đang sửa -> Gọi API PUT
        await axios.put(`http://localhost:8081/api/movies/${currentId}`, newMovie);
        setIsEditing(false);
        setCurrentId(null);
      } else {
        // Nếu thêm mới -> Gọi API POST
        await axios.post('http://localhost:8081/api/movies', newMovie);
      }
      setNewMovie({ title: '', genre: '', duration: '' });
      fetchMovies();
    } catch (error) {
      console.error("Lỗi khi lưu phim:", error);
    }
  };

  const cancelEdit = () => {
    setIsEditing(false);
    setNewMovie({ title: '', genre: '', duration: '' });
    setCurrentId(null);
  };

  useEffect(() => {
    fetchMovies();
  }, []);

  return (
    <div className="container">
      <h1>Quản Lý Danh Sách Phim</h1>

      {/* Form dùng chung cho cả Thêm và Sửa */}
      <div className="form-container">
        <h3>{isEditing ? "Cập nhật phim" : "Thêm phim mới"}</h3>
        <form onSubmit={handleSaveMovie}>
          <input
            placeholder="Tên phim"
            value={newMovie.title}
            onChange={(e) => setNewMovie({ ...newMovie, title: e.target.value })}
            required
          />
          <input
            placeholder="Thể loại"
            value={newMovie.genre}
            onChange={(e) => setNewMovie({ ...newMovie, genre: e.target.value })}
            required
          />
          <input
            placeholder="Thời lượng (phút)"
            type="number"
            value={newMovie.duration}
            onChange={(e) => setNewMovie({ ...newMovie, duration: e.target.value })}
            required
          />
          <button type="submit" className="btn-save">
            {isEditing ? "Cập nhật" : "Thêm phim"}
          </button>
          {isEditing && (
            <button type="button" onClick={cancelEdit} className="btn-cancel">Hủy</button>
          )}
        </form>
      </div>

      <table className="movie-table">
        <thead>
          <tr>
            <th>Tên Phim</th>
            <th>Thể Loại</th>
            <th>Thời Lượng</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {movies.map((movie) => (
            <tr key={movie.id}>
              <td>{movie.title}</td>
              <td>{movie.genre}</td>
              <td>{movie.duration} phút</td>
              <td>
                <button onClick={() => editMovie(movie)} className="btn-edit">Sửa</button>
                <button onClick={() => deleteMovie(movie.id)} className="btn-delete">Xóa</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default AdminMovie;