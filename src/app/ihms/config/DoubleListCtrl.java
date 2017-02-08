/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ihms.config;

import app.workers.ConfigWorker;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

/**
 *
 * @author Jordan
 */
public abstract class DoubleListCtrl implements Initializable, TabCtrl {

    private ConfigWorker wrk;

    @FXML
    protected ListView lstLeft;
    @FXML
    protected ListView lstRight;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void init(ConfigWorker wrk) {
        this.wrk = wrk;
        lstLeft.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lstRight.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lstLeft.setItems(loadListLeft(wrk));
        lstRight.setItems(loadListRight(wrk));

    }
    
    protected abstract ObservableList loadListLeft(ConfigWorker wrk);
    
    protected abstract ObservableList loadListRight(ConfigWorker wrk);

    @FXML
    private void everythingToRight(ActionEvent event) {
        toRight(lstLeft.getItems());
    }

    @FXML
    private void toRight(ActionEvent event) {
        toRight(lstLeft.getSelectionModel().getSelectedItems());
    }

    @FXML
    private void toLeft(ActionEvent event) {
        toLeft(lstRight.getSelectionModel().getSelectedItems());
    }

    @FXML
    private void everythingToLeft(ActionEvent event) {
        toLeft(lstRight.getItems());
    }

    public void toRight(List items) {
        lstRight.getItems().addAll(items);
        lstLeft.getItems().removeAll(items);
    }

    public void toLeft(List items) {
        lstLeft.getItems().addAll(items);
        lstRight.getItems().removeAll(items);
    }

    public ListView getLstLeft() {
        return lstLeft;
    }

    public ListView getLstRight() {
        return lstRight;
    }
}
