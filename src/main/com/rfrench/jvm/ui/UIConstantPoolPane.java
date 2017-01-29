/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.com.rfrench.jvm.ui;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import main.com.rfrench.jvm.java.MethodArea;

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
    private MethodArea method_area;    

    private final String CSS_ASSEMBLY_ID = "CONSTANT_POOL";
    
    
    public UIConstantPoolPane(MethodArea method_area)
    {
        this.method_area = method_area;        
        
        memory_tabpane = new TabPane();
        Tab cpp_memory = new Tab();
        cpp_memory.setText("Constant Pool");
        
        memory_tabpane.setId(CSS_ASSEMBLY_ID);
        
        addConstantPoolInfo();
                        
        cpp_memory.setContent(constantpool_listview);
        memory_tabpane.setMinWidth(MainScene.WIDTH_TENTH * 30);
        memory_tabpane.setMinHeight(MainScene.HEIGHT_TENTH * 20);
        memory_tabpane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                        
        Tooltip tool_tip = new Tooltip();
        tool_tip.setText("Constant Pool Memory Area");
        
        constantpool_listview.setTooltip(tool_tip);
        
        memory_tabpane.getTabs().add(cpp_memory);
    }
    
    private void addConstantPoolInfo()
    {
        constantpool_listview = new ListView();
        
        final int CONSTANT_POOL_SIZE = method_area.getConstantPool().size();
        
        for(int i = 0; i < CONSTANT_POOL_SIZE; i++)
        {
            String method_ref = method_area.getConstantPool().get(i);
            Label constant_pool_label = new Label(method_ref);
            constant_pool_label.setTextFill(Color.AZURE);
            constantpool_listview.getItems().add(constant_pool_label);
        }
        
        
        
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
