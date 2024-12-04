package service;

import model.Post;
import java.sql.SQLException;
import java.util.List;

public class Webservice {

    private DatabaseConnection dbConnection = new DatabaseConnection();

    public List<Post> getAllPosts() throws SQLException {
        return dbConnection.getAllPosts();
    }
}
