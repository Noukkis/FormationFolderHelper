/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ihms;

import app.beans.Eleve;
import app.workers.Worker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

/**
 * FXML Controller class
 *
 * @author Jordan
 */
public class ViewCtrl implements Initializable {

    Worker wrk;
    @FXML
    private BorderPane pane;
    @FXML
    private ListView<Eleve> lstEleves;
    @FXML
    private WebView webView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void init(Worker wrk) {
        this.wrk = wrk;
        lstEleves.getItems().addAll(wrk.getEleves());
        lstEleves.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String htmlString = wrk.getElevesHtml().get(newValue);
            if (htmlString != null) {
                webView.getEngine().loadContent(htmlString);
            }
        });
        lstEleves.getSelectionModel().select(0);
    }

    @FXML
    private void OnSave(ActionEvent event) {
    }

    @FXML
    private void onMail(ActionEvent event) {
    }
}
