/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.com.rfrench.jvm.java;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.stage.Screen;

/*
    Program Title: UIConstantPoolPane.java
    Author: Ryan French
    Created: 07-Nov-2016
    Version: 1.0
*/

public class UIConstantPoolPane 
{
    
    private TabPane memory_tabpane;
    private ListView constantpool_listview;
    
    
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    
    public UIConstantPoolPane()
    {
        
        memory_tabpane = new TabPane();
        Tab cpp_memory = new Tab();
        cpp_memory.setText("CPP");
        
        
        constantpool_listview = new ListView();
        
        cpp_memory.setContent(constantpool_listview);
        memory_tabpane.setMinWidth(MainScene.WIDTH_TENTH * 2);
        memory_tabpane.setMinHeight(MainScene.HEIGHT_TENTH * 2);
        memory_tabpane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        constantpool_listview.getItems().add(new Label("CPP"));
        
        Tooltip tool_tip = new Tooltip();
        tool_tip.setText("Constant Pool Memory Area");
        
        constantpool_listview.setTooltip(tool_tip);
        
        memory_tabpane.getTabs().add(cpp_memory);
    }
    
    public TabPane getTabPane()
    {
        return memory_tabpane;
    }
    
    public ListView getConstantpoolListView()
    {
        return constantpool_listview;
    }
}
