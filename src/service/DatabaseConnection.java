package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Post;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/funny_post_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connectToBDD() {
        try {
            
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion à la base de données établie.");
                return connection;
                
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la connexion à la base de données.", e);
        }
    }

    
    public static void close(Connection connection, Statement st) {
        try {
	   st.close();
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion à la base de données fermée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	public void resetTable() throws SQLException {
		String query = "TRUNCATE TABLE post";
		try (
				Connection conn = DatabaseConnection.connectToBDD();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(query);
			DatabaseConnection.close(conn, stmt);
		}
	}
	public void deletePostById(int id) throws SQLException {
		String query = "DELETE FROM post WHERE id = ?";
		try (
				Connection conn = DatabaseConnection.connectToBDD();
				PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		}

		String resetAutoIncrementQuery = "ALTER TABLE post AUTO_INCREMENT = 1";
		try (Connection conn = DatabaseConnection.connectToBDD();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(resetAutoIncrementQuery);
		}
	}

	public void updatePost(Post post) throws SQLException {
		String query = "UPDATE post SET title = ?, body = ? WHERE id = ?";
		try (
				Connection conn = DatabaseConnection.connectToBDD();
				PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, post.getTitle());
			pstmt.setString(2, post.getBody());
			pstmt.setInt(3, post.getId());
			pstmt.executeUpdate();
			DatabaseConnection.close(conn, pstmt);
		}
	}
	public void insertPost(Post post) throws SQLException {
		    String query = "INSERT INTO post (id, title, body) VALUES (?, ?, ?)";
		    try (
		            Connection conn = connectToBDD();
		            PreparedStatement pstmt = conn.prepareStatement(query)) {
		        pstmt.setString(1, String.valueOf(post.getId()));
		        pstmt.setString(2, post.getTitle());
		        pstmt.setString(3, post.getBody());
		        pstmt.executeUpdate();
		        close(conn, pstmt);
		    }
		}

	public void deleteAllPosts() throws SQLException {
		String query = "DELETE FROM post";
		try (
				Connection conn = DatabaseConnection.connectToBDD();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(query);
			DatabaseConnection.close(conn, stmt);
		}
	}

	public boolean postExists(int postId) throws SQLException {
		String query = "SELECT 1 FROM post WHERE id = ?";
		try (
				Connection conn = DatabaseConnection.connectToBDD();
				PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, postId);  
			ResultSet rs = pstmt.executeQuery();
			return rs.next();  
		}
	}
	public List<Post> getAllPosts() throws SQLException {
		List<Post> posts = new ArrayList<>();
		String query = "SELECT * FROM post";

		try (
				Connection conn = DatabaseConnection.connectToBDD();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				posts.add(new Post(rs.getInt("id"), rs.getString("title"), rs.getString("body")));
			}
			DatabaseConnection.close(conn, stmt);
		}

		return posts;
	}
}
