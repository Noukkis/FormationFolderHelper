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
import javafx.beans.Observable;
import javafx.beans.property.SimpleListProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
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
        lstEleves.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lstEleves.setCellFactory((ListView<Eleve> lv) -> new ListCell<Eleve>() {
            @Override
            protected void updateItem(Eleve item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.toString());
                    setStyle("-fx-text-fill: " + item.getColor() + " ;");
                } else {
                    setText("");
                }
            }
        });
    }

    public void init(Worker wrk) {
        this.wrk = wrk;
        lstEleves.getSelectionModel().selectedItemProperty().addListener((Observable listener) -> reloadWebView());
        lstEleves.getSelectionModel().select(0);
        lstEleves.getItems().addAll(wrk.getEleves());
        for (Eleve eleve : wrk.getEleves()) {
            Task task = new Task() {
                @Override
                protected Void call() throws Exception {
                    wrk.verifyEleve(eleve);
                    return null;
                }
            };
            new Thread(task).start();
            task.setOnSucceeded((Event event) -> {
                lstEleves.refresh();
                reloadWebView();
            });
        }
    }

    public ListView<Eleve> getLstEleves() {
        return lstEleves;
    }

    @FXML
    private void onMail(ActionEvent event) {
        DoubleTextDialog dialog = new DoubleTextDialog();
        dialog.setFillForced(true);
        dialog.setTitle("Envoyer Mails");
        dialog.getFirstLabel().setText("Votre adresse mail :");
        dialog.getSecondLabel().setText("Serveur mail :");
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

    @FXML
    private void reloadItems(ActionEvent event) {
        for (Eleve eleve : lstEleves.getSelectionModel().getSelectedItems()) {
            Task task = new Task() {
                @Override
                protected Void call() throws Exception {
                    eleve.setColor("blue");
                    eleve.clearFiles();
                    wrk.verifyEleve(eleve);
                    return null;
                }
            };
            new Thread(task).start();
            task.setOnSucceeded((Event e) -> {
                lstEleves.refresh();
                reloadWebView();
            });
            lstEleves.refresh();
        }
    }
    
    private void reloadWebView(){
        if (!lstEleves.getSelectionModel().getSelectedItems().isEmpty()) {
                String htmlString = lstEleves.getSelectionModel().getSelectedItems().get(0).getHtml();
                if (htmlString != null) {
                    webView.getEngine().loadContent(htmlString);
                }
            }
    }
}
