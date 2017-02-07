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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import javafx.beans.property.ListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
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
            ArrayList<String> list = new ArrayList<>();
            Scanner sc = new Scanner(choosen);
            while (sc.hasNext()) {
                list.add(sc.nextLine());
            }
            
            ArrayList toChange = new ArrayList();
            for (Object eleve : lstLeft.getItems()) {
                if (!list.contains(eleve.toString())) {
                    toChange.add(eleve);
                }
            }
            
            lstLeft.getItems().removeAll(toChange);
            lstRight.getItems().addAll(toChange);
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
    protected ObservableList loadListRight(ConfigWorker wrk) {
        return wrk.getIgnoredEleves();
    }

    @Override
    protected ObservableList loadListLeft(ConfigWorker wrk) {
       return wrk.getEleves();
    }

}
