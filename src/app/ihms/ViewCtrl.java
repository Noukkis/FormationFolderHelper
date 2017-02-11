/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ihms;

import app.workers.Worker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Jordan
 */
public class ViewCtrl implements Initializable {

    Worker wrk;
    @FXML
    private BorderPane pane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void init(Worker wrk) {
        this.wrk = wrk;
    }

    public void quitter() {

    }
}
