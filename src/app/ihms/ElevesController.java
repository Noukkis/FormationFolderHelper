/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ihms;

import app.beans.Eleve;
import app.workers.ConfigWorker;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Jordan
 */
public class ElevesController extends DoubleListCtrl{
        
    @FXML
    private void loadList(ActionEvent event) throws FileNotFoundException {
        FileChooser chooser = new FileChooser();
        File choosen = chooser.showOpenDialog(lstLeft.getScene().getWindow());
        if (choosen != null) {
            super.update();
            ArrayList<String> list = new ArrayList<>();
            Scanner sc = new Scanner(choosen);
            while (sc.hasNext()) {
                list.add(sc.nextLine());
            }
            for (Eleve eleve : wrk.getEleves()) {
                if (!list.contains(eleve.toString())) {
                    lstLeft.getItems().remove(eleve);
                    lstRight.getItems().add(eleve);
                }
            }
            if (list.size() > lstLeft.getItems().size()) {
                ListView<String> lstProblemes = new ListView<>();
                ArrayList<String> elevesString = new ArrayList<>();
                for (Object eleve : lstLeft.getItems()) {
                    elevesString.add(eleve.toString());
                }
                
                for (String string : list) {
                    if(!elevesString.contains(string)){
                        lstProblemes.getItems().add(string);
                    }
                }
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(lstProblemes.getItems().size() + " élèves de la liste n'ont pas été trouvés");
                alert.getDialogPane().setExpandableContent(lstProblemes);
                alert.showAndWait();
            }
        }
    }

    @Override
    protected void fillList(ConfigWorker wrk) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
