package models;

import play.db.Database;
import java.sql.*;
import javax.inject.Inject;
import java.util.List;
import java.util.ArrayList;


public class Image {

    private final Database db;

    @Inject
    public Image(Database db) {
        this.db = db;
    }

    // CREATE
    public boolean uploadImage(Long user_id, String filePath) {
        String query = "INSERT INTO images (user_id, image_path) VALUES (?, ?)";
        
        try (Connection conn = db.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setLong(1, user_id);
            stmt.setString(2, filePath);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ
    public ImageData getImage(Long id) {
        String query = "SELECT * FROM images WHERE id = ?";
        ImageData image = null;

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                image = new ImageData(rs.getLong("id"), rs.getLong("user_id"), rs.getString("filepath"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return image;
    }

    // READ All
    public List<ImageData> getAllImages() {
        String query = "SELECT * FROM images";
        List<ImageData> images = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             
             ResultSet rs = stmt.executeQuery()) {


            while (rs.next()) {
                images.add(new ImageData(rs.getLong("id"), rs.getLong("user_id"), rs.getString("image_path")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    

        return images;
    }

    public List<ImageData> getImageById(Long user_id)
    {
        String query = "SELECT * FROM images WHERE user_id = ?";
        List<ImageData> images = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ) {
            stmt.setLong(1, user_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                images.add(new ImageData(rs.getLong("id"), rs.getLong("user_id"), rs.getString("image_path")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return images;
    }

    // DELETE
    public boolean deleteImage(Long id) {
        String query = "DELETE FROM images WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class ImageData {
        private String filePath;
        private Long id;
        private Long user_id;

        public ImageData(Long id, Long user_id, String filePath) {
            this.id = id;
            this.user_id = user_id;
            this.filePath = filePath;
        }

        public Long getId() {
            return id;
        }

        public Long getUserId() {
            return user_id;
        }

        public String getFilePath() {
            return filePath;
        }
    }
    public ImageDetail getImageDetailById(Long imageId) {
        String query = "SELECT i.id AS image_id, i.image_path, COUNT(l.id) AS like_count, " +
                       "GROUP_CONCAT(c.content SEPARATOR ' | ') AS comments, u.username AS uploader " +
                       "FROM images i " +
                       "LEFT JOIN likes l ON i.id = l.image_id " +
                       "LEFT JOIN comments c ON i.id = c.image_id " +
                       "JOIN users u ON i.user_id = u.id " +
                       "WHERE i.id = ? " +
                       "GROUP BY i.id, u.username";
    
        ImageDetail imageDetail = null;
    
        try (Connection conn = db.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setLong(1, imageId);
    
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String imagePath = rs.getString("image_path");
                    int likeCount = rs.getInt("like_count");
                    String comments = rs.getString("comments");
                    String uploader = rs.getString("uploader");
    
                    imageDetail = new ImageDetail(imageId, imagePath, likeCount, comments, uploader);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return imageDetail;
    }

    public static class ImageDetail {
        private Long imageId;
        private String imagePath;
        private int likeCount;
        private String comments;
        private String uploader;
    
        public ImageDetail(Long imageId, String imagePath, int likeCount, String comments, String uploader) {
            this.imageId = imageId;
            this.imagePath = imagePath;
            this.likeCount = likeCount;
            this.comments = comments;
            this.uploader = uploader;
        }
    
        public Long getImageId() {
            return imageId;
        }
    
        public String getImagePath() {
            return imagePath;
        }
    
        public int getLikeCount() {
            return likeCount;
        }
    
        public String getComments() {
            return comments;
        }
    
        public String getUploader() {
            return uploader;
        }
    }

    // ADD LIKE
public boolean addLike(Long userId, Long imageId) {
    String query = "INSERT INTO likes (user_id, image_id) VALUES (?, ?)";

    try (Connection conn = db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setLong(1, userId);
        stmt.setLong(2, imageId);
        stmt.executeUpdate();
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public boolean unLike(Long userId, Long imageId) {
    String query = "DELETE FROM likes WHERE user_id = ? AND image_id = ?";

    try (Connection conn = db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setLong(1, userId);
        stmt.setLong(2, imageId);
        stmt.executeUpdate();
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

// ADD COMMENT
public boolean addComment(Long userId, Long imageId, String content) {
    String query = "INSERT INTO comments (user_id, image_id, content) VALUES (?, ?, ?)";

    try (Connection conn = db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setLong(1, userId);
        stmt.setLong(2, imageId);
        stmt.setString(3, content);
        stmt.executeUpdate();
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    
}

