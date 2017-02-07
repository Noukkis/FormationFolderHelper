/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ihms;

import app.beans.Eleve;
import app.workers.ConfigWorker;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 *
 * @author Jordan
 */
public class ViewController implements Initializable {

    private ConfigWorker wrk;
    private HashMap<Tab, TabController> tabs;

    File elevesPath;
    String folderPath;
    @FXML
    private Tab tabMain;
    @FXML
    private Tab tabModules;
    @FXML
    private Tab tabKeywords;

    @FXML
    private ListView<Eleve> lstEleves;
    @FXML
    private TextField txtPathEleve;
    @FXML
    private TextField txtPathFormation;
    @FXML
    private Tab tabEleves;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wrk = new ConfigWorker();
        tabs = new HashMap<>();
        wrk.init();
        try {
            wrk.getCtrls().put("Keywords", initTab(tabKeywords, "KeywordsView"));
            wrk.getCtrls().put("Modules", initTab(tabModules, "ModulesView"));
            wrk.getCtrls().put("Eleves", initTab(tabEleves, "ElevesView"));
        } catch (IOException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private TabController initTab(Tab tab, String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml + ".fxml"));
        Parent root = (Parent) loader.load();
        TabController ctrl = loader.getController();
        ctrl.init(wrk);
        tab.setContent(root);
        tabs.put(tab, ctrl);
        return ctrl;
    }

    public void update() {
        if(txtPathFormation.getText() == null){
            txtPathFormation.setText("");
        }
        elevesPath = new File(txtPathEleve.getText());
        folderPath = txtPathFormation.getText().replace("[eleve]", "");
        lstEleves.getItems().clear();
        lstEleves.getItems().addAll(wrk.updateEleves(elevesPath, folderPath));
    }

    public void quitter() {
        Platform.exit();
    }

    @FXML
    private void searchPathEleve(ActionEvent event) throws IOException {
        DirectoryChooser chooser = new DirectoryChooser();
        File file = chooser.showDialog(txtPathEleve.getScene().getWindow());
        if (file != null) {
            txtPathEleve.setText(file.getAbsolutePath());
            update();
        }
    }

    @FXML
    private void searchPathFormation(ActionEvent event) {
        File defaut = null;
        if (elevesPath != null && elevesPath.isDirectory()) {
            for (File file : elevesPath.listFiles()) {
                if (file.isDirectory()) {
                    defaut = file;
                    break;
                }
            }
        }
        if (defaut != null) {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setInitialDirectory(defaut);
            File choosen = chooser.showDialog(txtPathFormation.getScene().getWindow());
            if (choosen != null && choosen.getAbsolutePath().contains(elevesPath.getAbsolutePath())) {
                txtPathFormation.setText(choosen.getAbsolutePath().replace(chooser.getInitialDirectory().getAbsolutePath(), "[eleve]"));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Le répertoire choisi n'est pas un sous-repertoire du répertoire d'emplacement des élèves");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Le répertoire d'emplacement des élèves est incorecte");
            alert.showAndWait();
        }
        update();
    }

    @FXML
    private void onLaunch(ActionEvent event) {
    }

    @FXML
    private void txtPathOnAction(ActionEvent event) {
        update();
    }

    @FXML
    private void loadConfig(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName("config");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier de config ffh", "*.ffh"));
        wrk.loadConfig(fc.showOpenDialog(lstEleves.getScene().getWindow()), this);
    }

    @FXML
    private void saveConfig(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName("config");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier de config ffh", "*.ffh"));
        wrk.saveConfig(fc.showSaveDialog(lstEleves.getScene().getWindow()), elevesPath, folderPath);
    }

    public void setElevesPath(File path) {
        if (path != null) {
            txtPathEleve.setText(path.getAbsolutePath());
        } else {
            txtPathEleve.setText("");
        }
    }

    public void setFolderPath(String path) {
        if (path != null) {
            txtPathFormation.setText(folderPath);
        } else {
            txtPathFormation.setText("");
        }
    }
}
