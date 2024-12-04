package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import service.PostService;
import model.Post;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Controller {

    @FXML
    private ListView<String> postListView;

    @FXML
    private TextField postIdField;

    @FXML
    private TextField updatePostIdField;
    @FXML
    private TextField updatePostTitleField;
    @FXML
    private TextField updatePostBodyField;

    private PostService postService = new PostService();

    @FXML
    public void initialize() {
        loadPosts();
    }

    @FXML
    private void loadPosts() {
        try {
            postListView.getItems().clear();
            List<Post> posts = postService.getAllPosts();

            for (Post post : posts) {
                postListView.getItems().add(post.getId() + ": " + post.getTitle());
            }
        } catch (SQLException e) {
            showError("Erreur de base de donn�es", "Impossible de r�cup�rer les posts depuis la base de donn�es.");
            e.printStackTrace();
        }
    }

    @FXML
    private void deletePost() {
        try {
            int id = Integer.parseInt(postIdField.getText());
            postService.deletePostById(id);
            loadPosts();
            showSuccess("Succ�s", "Le post avec l'ID " + id + " a �t� supprim�.");
        } catch (NumberFormatException e) {
            showError("Erreur", "Veuillez entrer un ID valide.");
        } catch (SQLException e) {
            showError("Erreur de base de donn�es", "Impossible de supprimer le post.");
            e.printStackTrace();
        }
    }

    @FXML
    private void initPosts() {
        try {
            List<Post> updatedPosts = postService.initPosts();

            if (!updatedPosts.isEmpty()) {
                loadPosts();
                showSuccess("Succ�s", "Les posts ont �t� r�cup�r�s et ajout�s avec succ�s.");
            } else {
                showSuccess("Info", "Aucun nouveau post � ajouter.");
            }
        } catch (IOException | SQLException e) {
            showError("Erreur", "Impossible de r�cup�rer ou enregistrer les posts.");
            e.printStackTrace();
        }
    }

    @FXML
    private void resetDatabase() {
        try {
            postService.resetTable();
            postListView.getItems().clear();
            showSuccess("Succ�s", "La base a �t� r�initialis�e.");
        } catch (SQLException e) {
            showError("Erreur", "Impossible de r�initialiser la base.");
            e.printStackTrace();
        }
    }

    @FXML
    private void updatePost() {
        try {
            int id = Integer.parseInt(updatePostIdField.getText());
            String title = updatePostTitleField.getText();
            String body = updatePostBodyField.getText();

            if (title.isEmpty() || body.isEmpty()) {
                showError("Erreur", "Le titre et le corps du post ne peuvent pas �tre vides.");
                return;
            }

            Post updatedPost = new Post(id, title, body);
            postService.updatePost(updatedPost);

            loadPosts();
            showSuccess("Succ�s", "Le post a �t� mis � jour avec succ�s.");
        } catch (NumberFormatException e) {
            showError("Erreur", "Veuillez entrer un ID valide.");
        } catch (SQLException e) {
            showError("Erreur", "Impossible de mettre � jour le post.");
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
