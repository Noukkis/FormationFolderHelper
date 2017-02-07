/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans;

import java.io.Serializable;
import java.util.HashMap;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Jordan
 */
public class Module{

    private StringProperty name;
    private HashMap<String, BooleanProperty> keywords;

    public Module(String name) {
        this.name = new SimpleStringProperty(name);
        this.keywords = new HashMap<>();
    }
    
    public Module(String name, HashMap<String, Boolean> bools){
        this.name = new SimpleStringProperty(name);
        this.keywords = new HashMap<>();
        for (String keyword : bools.keySet()) {
            keywords.put(keyword, new SimpleBooleanProperty(bools.get(keyword)));
        }
    }

    public HashMap<String, BooleanProperty> getKeywords() {
        return keywords;
    }
        
    @Override
    public String toString(){
        return name.getValue();
    }

    public StringProperty nameProperty() {
        return name;
    }
}
