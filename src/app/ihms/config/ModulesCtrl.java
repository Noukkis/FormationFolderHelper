/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ihms.config;

import app.workers.ConfigWorker;
import javafx.collections.ObservableList;

/**
 *
 * @author Jordan
 */
public class ModulesCtrl extends DoubleListCtrl{

    @Override
    protected ObservableList loadListLeft(ConfigWorker wrk) {
        return wrk.getModules();
    }

    @Override
    protected ObservableList loadListRight(ConfigWorker wrk) {
       return wrk.getIgnoredModules();
    }
    
}
