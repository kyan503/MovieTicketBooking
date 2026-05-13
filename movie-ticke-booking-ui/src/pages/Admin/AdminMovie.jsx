import axios from 'axios';
import { useEffect, useState } from 'react';
import styles from './AdminMovie.module.css';

function AdminMovie() {
  const [movies, setMovies] = useState([]);
  const [isEditing, setIsEditing] = useState(false);
  const [currentId, setCurrentId] = useState(null);
  const [file, setFile] = useState(null); // Để lưu file poster chọn từ máy tính

  // State quản lý toàn bộ thông tin phim
  const [newMovie, setNewMovie] = useState({
    title: '',
    genre: '',
    duration: '',
    description: '',
    language: '',
    releaseDate: '',
    trailerUrl: ''
  });

  const token = localStorage.getItem("token");

  // Hàm lấy Header (Quan trọng: Multipart cho upload file)
  const getHeader = (isMultipart = false) => {
    return {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": isMultipart ? "multipart/form-data" : "application/json",
      },
    };
  };

  const fetchMovies = async () => {
    try {
      const response = await axios.get('http://localhost:8081/api/movies', getHeader());
      setMovies(response.data);
    } catch (error) {
      console.error("Lỗi khi lấy dữ liệu:", error);
    }
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleSaveMovie = async (e) => {
    e.preventDefault();
    
    // Sử dụng FormData để gửi cả Text và File lên Backend
    const formData = new FormData();
    formData.append("title", newMovie.title);
    formData.append("genre", newMovie.genre);
    formData.append("duration", newMovie.duration);
    formData.append("description", newMovie.description);
    formData.append("language", newMovie.language);
    formData.append("releaseDate", newMovie.releaseDate);
    formData.append("trailerUrl", newMovie.trailerUrl);
    
    if (file) {
      formData.append("file", file); // Key này phải khớp với @RequestParam("file") ở Backend
    }

    try {
      if (isEditing) {
        await axios.put(`http://localhost:8081/api/movies/${currentId}`, formData, getHeader(true));
        alert("Cập nhật thành công!");
      } else {
        await axios.post('http://localhost:8081/api/movies', formData, getHeader(true));
        alert("Thêm phim thành công!");
      }
      cancelEdit();
      fetchMovies();
    } catch (error) {
      console.error("Lỗi khi lưu phim:", error.response?.data || error.message);
      alert("Lỗi: " + (error.response?.data?.message || "Không thể lưu phim"));
    }
  };

  const deleteMovie = async (id) => {
    if (window.confirm("Bạn có chắc chắn muốn xóa phim này?")) {
      try {
        await axios.delete(`http://localhost:8081/api/movies/${id}`, getHeader());
        fetchMovies();
      } catch (error) {
        console.error("Lỗi khi xóa:", error);
      }
    }
  };

  const editMovie = (movie) => {
    setIsEditing(true);
    setCurrentId(movie.id);
    setNewMovie({
      title: movie.title || '',
      genre: movie.genre || '',
      duration: movie.duration || '',
      description: movie.description || '',
      language: movie.language || '',
      releaseDate: movie.releaseDate || '',
      trailerUrl: movie.trailerUrl || ''
    });
  };

  const cancelEdit = () => {
    setIsEditing(false);
    setCurrentId(null);
    setNewMovie({ title: '', genre: '', duration: '', description: '', language: '', releaseDate: '', trailerUrl: '' });
    setFile(null);
    if (document.getElementById('fileInput')) document.getElementById('fileInput').value = '';
  };

  useEffect(() => {
    fetchMovies();
  }, []);

  return (
    <div className={styles.container}>
      <h1>Quản Lý Danh Sách Phim</h1>

      <div className={styles.formContainer}>
        <h3>{isEditing ? "Cập nhật phim" : "Thêm phim mới"}</h3>
        <form onSubmit={handleSaveMovie} className={styles.adminForm}>
          <div className={styles.inputGroup}>
            <input placeholder="Tên phim" value={newMovie.title} onChange={(e) => setNewMovie({ ...newMovie, title: e.target.value })} required />
            <input placeholder="Thể loại" value={newMovie.genre} onChange={(e) => setNewMovie({ ...newMovie, genre: e.target.value })} required />
            <input placeholder="Thời lượng (phút)" type="number" value={newMovie.duration} onChange={(e) => setNewMovie({ ...newMovie, duration: e.target.value })} required />
            <input placeholder="Ngôn ngữ" value={newMovie.language} onChange={(e) => setNewMovie({ ...newMovie, language: e.target.value })} />
            <input type="date" value={newMovie.releaseDate} onChange={(e) => setNewMovie({ ...newMovie, releaseDate: e.target.value })} title="Ngày phát hành" />
            <input placeholder="Link Trailer (URL)" value={newMovie.trailerUrl} onChange={(e) => setNewMovie({ ...newMovie, trailerUrl: e.target.value })} />
          </div>

          <textarea 
            placeholder="Mô tả phim chi tiết..." 
            value={newMovie.description} 
            onChange={(e) => setNewMovie({ ...newMovie, description: e.target.value })}
            className={styles.textArea}
          />

          <div className={styles.fileSection}>
            <label>Chọn Poster phim: </label>
            <input type="file" id="fileInput" accept="image/*" onChange={handleFileChange} />
          </div>

          <div className={styles.actionButtons}>
            <button type="submit" className={styles.btnSave}>
              {isEditing ? "Lưu thay đổi" : "Thêm phim mới"}
            </button>
            {isEditing && <button type="button" onClick={cancelEdit} className={styles.btnCancel}>Hủy</button>}
          </div>
        </form>
      </div>

      <table className={styles.movieTable}>
        <thead>
          <tr>
            <th>Poster</th>
            <th>Thông tin phim</th>
            <th>Mô tả</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {movies.map((movie) => (
            <tr key={movie.id}>
              <td>
                <img src={movie.posterUrl || 'https://via.placeholder.com/80x120'} alt="poster" className={styles.posterPreview} />
              </td>
              <td>
                <strong>{movie.title}</strong> <br/>
                <small>{movie.genre} | {movie.duration}p</small> <br/>
                <small>NN: {movie.language}</small>
              </td>
              <td className={styles.descriptionCell}>{movie.description}</td>
              <td>
                <div className={styles.tableActions}>
                  <button onClick={() => editMovie(movie)} className={styles.btnEdit}>Sửa</button>
                  <button onClick={() => deleteMovie(movie.id)} className={styles.btnDelete}>Xóa</button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default AdminMovie;