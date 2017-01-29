
package main.com.rfrench.jvm.ui;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

/*
    Program Title: UIByteCodeInfoPane.java
    Author: Ryan French
    Created: 29-Jan-2017
    Version: 1.0
*/

public class UIByteCodeInfoPane 
{
    private Pane bytecode_info_pane;
    private ListView bytecode_info_listview;
    
    private final String CSS_ASSEMBLY_ID = "BYTECODE_INFO";
    
    public UIByteCodeInfoPane()
    {        
        setupListView();        
        setupPane();                                              
    }
    
    private void setupPane()
    {
        bytecode_info_pane = new Pane();
        bytecode_info_pane.setId(CSS_ASSEMBLY_ID);
        bytecode_info_pane.getChildren().add(bytecode_info_listview); 
    }
    
    private void setupListView()
    {
        bytecode_info_listview = new ListView();
        bytecode_info_listview.setMinHeight(MainScene.HEIGHT_TENTH * 40);
        bytecode_info_listview.setMinWidth(MainScene.WIDTH_TENTH * 40);          
        
        Label l = new Label("**************");
        
        bytecode_info_listview.getItems().add(l);
    }
    
    public void addByteCodeInfo(String bytecode_info_text)
    {
        bytecode_info_listview.getItems().add(new Label(bytecode_info_text));
    }
    
    public Pane getPane()
    {
        return bytecode_info_pane;
    }
    
}
