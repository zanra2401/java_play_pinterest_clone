package models;

import play.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private final Database db;

    public User(Database db) {
        this.db = db;
    }

    public boolean register(String username, String password) {
        String query = "INSERT INTO users (username, password_user) VALUES (?, ?)";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // sebaiknya password di-hash dulu

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace(); // untuk debug
            return false;
        }
    }

    public Long login(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password_user = ?";
        
        try (Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // sebaiknya password di-hash dulu

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                {
                    return rs.getLong("id"); // return true kalau user ditemukan
                }
            }

            return (long) 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return (long) 0;
        }
    }

    public String getUsername(Long id)
    {
        String query = "SELECT username FROM users WHERE id = ?";
        try (Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);
      
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                {
                    return rs.getString("username"); // return true kalau user ditemukan
                }
            }

            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getPPById(Long id)
    {
        String query = "SELECT pp_path FROM users WHERE id = ?";

        try (Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);
      
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                {
                    return rs.getString("pp_path"); // return true kalau user ditemukan
                }
            }

            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean telahLike(Long imageId, Long userId)
    {
        String query = "SELECT * FROM likes WHERE user_id = ? AND image_id = ?";

        try (Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, imageId);
    
            try (ResultSet rs = stmt.executeQuery()) {
               return rs.next();
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sudahKomen(Long imageId, Long userId)
    {
        String query = "SELECT * FROM comments WHERE user_id = ? AND image_id = ?";

        try (Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, imageId);
    
            try (ResultSet rs = stmt.executeQuery()) {
               return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUsername(Long userId, String newUsername) {
        String query = "UPDATE users SET username = ? WHERE id = ?";
        
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, newUsername);
            stmt.setLong(2, userId);
    
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUsernameExist(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
    
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, username);
    
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true jika ditemukan
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProfilePicture(Long userId, String newPpPath) {
        String query = "UPDATE users SET pp_path = ? WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newPpPath);  // path file foto profil
            stmt.setLong(2, userId);        // ID pengguna yang foto profilnya akan diubah

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
