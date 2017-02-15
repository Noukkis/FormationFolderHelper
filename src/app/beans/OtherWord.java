/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author Jordan
 */
public class OtherWord {
    private String name;
    private BooleanProperty active;

    public OtherWord(String name) {
        this.name = name;
        this.active = new SimpleBooleanProperty(true);
    }

    public OtherWord(String name, BooleanProperty active) {
        this.name = name;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public BooleanProperty getActive() {
        return active;
    }

    @Override
    public String toString() {
        return name.replaceFirst(">", "");
    }
    
}
