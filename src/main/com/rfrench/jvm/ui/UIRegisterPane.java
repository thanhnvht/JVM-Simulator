
package main.com.rfrench.jvm.ui;


import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

/*
    Program Title: UIRegisterPane.java
    Author: Ryan French
    Created: 07-Nov-2016
    Version: 1.0
*/

public class UIRegisterPane 
{
    private TabPane register_tabpane;
    
    private Label PC_label;
    
    private final String CSS_ASSEMBLY_ID = "REGISTER";
        
    public UIRegisterPane()
    {       
        setupPCLabel();                             
        
        setupRegisterTabPane();               
    }
    
    private void setupRegisterTabPane()
    {
        final String TAB_TITLE = "Registers";
        
        register_tabpane = new TabPane();
        register_tabpane.setMinHeight(MainScene.HEIGHT_TENTH * 50);
        register_tabpane.setMinWidth(MainScene.WIDTH_TENTH * 20);
        
        Tab register_tab = new Tab();
        register_tab.setText(TAB_TITLE);
        
        
        register_tabpane.setId(CSS_ASSEMBLY_ID); 

        register_tabpane.getTabs().add(register_tab);
        register_tabpane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        
        register_tab.setContent(PC_label); 
    }
    
    private void setupPCLabel()
    {
        PC_label =  new Label("PC  : ");
        PC_label.setTextFill(Color.AZURE);
        
        Tooltip tool_tip = new Tooltip();
        tool_tip.setText("PC - Program Counter, the location of what bytecode to run next");
        PC_label.setTooltip(tool_tip);
    }

    
    public void updateLabel(String register_name, int choice)
    {
        PC_label.setText(register_name);        
    }
    
    public TabPane getTabPane()
    {
        return register_tabpane;
    }        
}
