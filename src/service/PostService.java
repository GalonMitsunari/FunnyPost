package service;

import model.Post;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.sql.SQLException;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostService {

    private static final String WEB_SERVICE_URL = "https://jsonplaceholder.typicode.com/posts";
    private DatabaseConnection dbConnection = new DatabaseConnection();

    public List<Post> fetchPostsFromWebservice() throws IOException {
        List<Post> posts = new ArrayList<>();
        HttpURLConnection connection = (HttpURLConnection) new URL(WEB_SERVICE_URL).openConnection();

        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            Gson gson = new Gson();
            Post[] postsArray = gson.fromJson(response.toString(), Post[].class);

            for (Post post : postsArray) {
                posts.add(post);
            }
        } else {
            throw new IOException("Échec de la récupération des données du webservice.");
        }

        return posts;
    }

    public void savePostIfNotExist(Post post) throws SQLException {
        if (!dbConnection.postExists(post.getId())) {
            dbConnection.insertPost(post);
        }
    }


    public void resetTable() throws SQLException {
        dbConnection.resetTable();
    }

    public void deletePostById(int id) throws SQLException {
        dbConnection.deletePostById(id);
    }

    public void updatePost(Post post) throws SQLException {
        dbConnection.updatePost(post);
    }

    public List<Post> initPosts() throws SQLException, IOException {
        List<Post> postsFromWebservice = fetchPostsFromWebservice();
        List<Post> updatedPosts = new ArrayList<>();

        for (Post post : postsFromWebservice) {
            if (!dbConnection.postExists(post.getId())) {
                dbConnection.insertPost(post);
                updatedPosts.add(post);
            }
        }

        return updatedPosts;
    }

    public List<Post> getAllPosts() throws SQLException {
        return dbConnection.getAllPosts();
    }
}
