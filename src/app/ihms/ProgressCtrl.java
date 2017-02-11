/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ihms;

import app.workers.Worker;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jordan
 */
public class ProgressCtrl implements Initializable {

    @FXML
    private Label label;
    @FXML
    private ProgressBar progressBar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void init(Worker wrk) {
        Task t = wrk.init();
        label.textProperty().bind(t.messageProperty());
        progressBar.progressProperty().bind(t.progressProperty());
        t.setOnSucceeded(listener -> {
            Stage stage = (Stage) label.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("View.fxml"));
        try {
            Parent root = (Parent) loader.load();
            ViewCtrl ctrl = loader.getController();
            ctrl.init(wrk);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("FFH");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ConfigViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        );
    }

}
