/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.com.rfrench.jvm.ui;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;


public class UIMemoryPane {
    
    
    private Pane p;
    private TabPane mempane;
    private final String CSS_MEMORY_ID = "MEMORYPANE";
    
    public UIMemoryPane()
    {
        p = new Pane();
        
        mempane = new TabPane();
        mempane.setMinHeight(MainScene.HEIGHT_TENTH * 10);
        mempane.setMinWidth(MainScene.WIDTH_TENTH * 4);
        mempane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        Tab mem = new Tab();
        mem.setText("Java Stack Frame");
        mem.setContent(p);
                
        
        mempane.getTabs().addAll(mem);                
        p.setId(CSS_MEMORY_ID);

        
    }
    
    public Pane getPane()
    {
        return p;
    }
    
    public TabPane getTabPane()
    {
        return mempane;
    }
    
}
