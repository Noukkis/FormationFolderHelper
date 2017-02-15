/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.helpers;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

/**
 *
 * @author Jordan
 */
public class ProgressDialog extends Dialog {

    ProgressBar bar;
    Label label;

    public ProgressDialog(Task<Void> t) {
        super();
        bar = new ProgressBar();
        label = new Label();
        bar.progressProperty().bind(t.progressProperty());
        label.textProperty().bind(t.messageProperty());
        label.setPrefWidth(500);
        bar.setPrefWidth(500);
        bar.setPrefHeight(20);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setAlignment(Pos.CENTER);
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(label, bar);
        super.getDialogPane().setContent(box);
        super.getDialogPane().setPrefHeight(75);
        super.getDialogPane().setPrefWidth(500);

        t.stateProperty().addListener((ObservableValue<? extends javafx.concurrent.Worker.State> observable, javafx.concurrent.Worker.State oldValue, javafx.concurrent.Worker.State newValue) -> {
            if (newValue.equals(Worker.State.SUCCEEDED)) {
                super.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                Button b = (Button) super.getDialogPane().lookupButton(ButtonType.CANCEL);
                b.fire();
            }
        });
    }
}
