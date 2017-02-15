/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.helpers;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 *
 * @author Jordan
 */
public class DoubleTextDialog extends Dialog {

    private Label firstLabel;
    private Label secondLabel;
    private TextField firstTxt;
    private TextField secondTxt;
    private Node okButton;
    private boolean fillForced;

    public DoubleTextDialog() {
        super();
        firstLabel = new Label();
        secondLabel = new Label();
        firstTxt = new TextField();
        secondTxt = new TextField();
        fillForced = false;
        super.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(firstLabel, 0, 0);
        grid.add(firstTxt, 1, 0);
        grid.add(secondLabel, 0, 1);
        grid.add(secondTxt, 1, 1);

        okButton = super.getDialogPane().lookupButton(ButtonType.OK);
        super.getDialogPane().setContent(grid);


        firstTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable((newValue.trim().isEmpty() || secondTxt.getText().isEmpty()) && fillForced);
        });
        
        secondTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable((newValue.trim().isEmpty() || firstTxt.getText().isEmpty()) && fillForced);
        });
        
        super.setResultConverter(dialogButton -> {
    if (dialogButton == ButtonType.OK) {
        return new Pair<>(firstTxt.getText(), secondTxt.getText());
    }
    return null;
});
    }

    public Label getFirstLabel() {
        return firstLabel;
    }

    public Label getSecondLabel() {
        return secondLabel;
    }

    public TextField getFirstTxt() {
        return firstTxt;
    }

    public TextField getSecondTxt() {
        return secondTxt;
    }

    public boolean isFillForced() {
        return fillForced;
    }

    public void setFillForced(boolean fillForced) {
        this.fillForced = fillForced;
        okButton.setDisable(fillForced);
    }
}
