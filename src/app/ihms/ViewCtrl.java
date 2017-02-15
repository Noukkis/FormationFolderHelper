/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ihms;

import app.beans.Eleve;
import app.helpers.DoubleTextDialog;
import app.workers.Worker;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.Pair;

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
    private void onMail(ActionEvent event) {
        DoubleTextDialog dialog = new DoubleTextDialog();
        dialog.setFillForced(true);
        dialog.setTitle("Envoye Mails");
        dialog.getFirstLabel().setText("Votre adresse mail :");
        dialog.getSecondLabel().setText("Serveur smtp :");
        Optional<Pair<String, String>> option = dialog.showAndWait();
        option.ifPresent(pair -> wrk.sendMails(pair.getKey(), pair.getValue()));
            
    }

    @FXML
    private void onSave(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier XML", "*.xml"));
        fc.setSelectedExtensionFilter(fc.getExtensionFilters().get(0));
        wrk.saveXml(fc.showSaveDialog(pane.getScene().getWindow()));
    }
}
