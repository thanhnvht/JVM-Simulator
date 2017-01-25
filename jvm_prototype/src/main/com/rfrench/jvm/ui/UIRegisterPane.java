
package main.com.rfrench.jvm.ui;


import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.Tooltip;

/*
    Program Title: UIRegisterPane.java
    Author: Ryan French
    Created: 07-Nov-2016
    Version: 1.0
*/

public class UIRegisterPane 
{
    private TabPane register_tabpane;
    private ListView register_listview;
    
    private Label PC;
    private Label SP;
    private Label LV;
    private Label CPP;
    
    private final String CSS_ASSEMBLY_ID = "REGISTER";
        
    public UIRegisterPane()
    {
        register_tabpane = new TabPane();
        register_tabpane.setMinHeight(MainScene.HEIGHT_TENTH * 5);
        register_tabpane.setMinWidth(MainScene.WIDTH_TENTH * 2);
        
        Tab register_tab = new Tab();
        register_tab.setText("Registers");
        
        register_listview = new ListView();
        
        register_listview.setId(CSS_ASSEMBLY_ID); 
        
        register_tab.setContent(register_listview);
        register_tabpane.getTabs().add(register_tab);
        register_tabpane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        
        setupPC();
        setupSP();
        setupLV();
        setupCPP();                               
        
        register_listview.getItems().addAll(PC, SP, LV, CPP);
        
    }
    
    private void setupPC()
    {
        PC =  new Label("PC  : ");
        Tooltip tool_tip = new Tooltip();
        tool_tip.setText("PC - Program Counter, the location of what bytecode to run next");
        PC.setTooltip(tool_tip);
    }
    
    private void setupSP()
    {
        SP =  new Label("SP  : ");
        Tooltip tool_tip = new Tooltip();
        tool_tip.setText("SP - Stack Pointer, the location of the top of stack in memory");
        SP.setTooltip(tool_tip);
    }
    
    private void setupLV()
    {
        LV =  new Label("LV  :");
        Tooltip tool_tip = new Tooltip();
        tool_tip.setText("LV - Local Variable, the location of the top of local variable in memory");
        SP.setTooltip(tool_tip);
    }
    
    private void setupCPP()
    {
        CPP = new Label("CPP : ");
        Tooltip tool_tip = new Tooltip();
        tool_tip.setText("CPP - Constant Pool Pointer, *** DESCRIPTION ***"); //FIND DESCRIPTION OF CPP
        SP.setTooltip(tool_tip);
    }
    
    public void updateLabel(String register_name, int choice)
    {
        switch(choice)
        {
            case(1) : PC.setText(register_name);   break;
            case(2) : SP.setText(register_name);   break;
            case(3) : LV.setText(register_name);   break;
            case(4) : CPP.setText(register_name);  break;
        }        
    }
    
    public TabPane getTabPane()
    {
        return register_tabpane;
    }
    
    public ListView getListView()
    {
        return register_listview;
    }
    
}
