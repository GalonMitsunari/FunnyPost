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
            showError("Erreur de base de données", "Impossible de récupérer les posts depuis la base de données.");
            e.printStackTrace();
        }
    }

    @FXML
    private void deletePost() {
        try {
            int id = Integer.parseInt(postIdField.getText());
            postService.deletePostById(id);
            loadPosts();
            showSuccess("Succès", "Le post avec l'ID " + id + " a été supprimé.");
        } catch (NumberFormatException e) {
            showError("Erreur", "Veuillez entrer un ID valide.");
        } catch (SQLException e) {
            showError("Erreur de base de données", "Impossible de supprimer le post.");
            e.printStackTrace();
        }
    }

    @FXML
    private void initPosts() {
        try {
            List<Post> updatedPosts = postService.initPosts();

            if (!updatedPosts.isEmpty()) {
                loadPosts();
                showSuccess("Succès", "Les posts ont été récupérés et ajoutés avec succès.");
            } else {
                showSuccess("Info", "Aucun nouveau post à ajouter.");
            }
        } catch (IOException | SQLException e) {
            showError("Erreur", "Impossible de récupérer ou enregistrer les posts.");
            e.printStackTrace();
        }
    }

    @FXML
    private void resetDatabase() {
        try {
            postService.resetTable();
            postListView.getItems().clear();
            showSuccess("Succès", "La base a été réinitialisée.");
        } catch (SQLException e) {
            showError("Erreur", "Impossible de réinitialiser la base.");
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
                showError("Erreur", "Le titre et le corps du post ne peuvent pas être vides.");
                return;
            }

            Post updatedPost = new Post(id, title, body);
            postService.updatePost(updatedPost);

            loadPosts();
            showSuccess("Succès", "Le post a été mis à jour avec succès.");
        } catch (NumberFormatException e) {
            showError("Erreur", "Veuillez entrer un ID valide.");
        } catch (SQLException e) {
            showError("Erreur", "Impossible de mettre à jour le post.");
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
