
package main.com.rfrench.jvm.ui;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;


public class BytecodeInfoPane 
{
    private ScrollPane bytecode_info_pane;
    private ListView bytecode_info_listview;
        
    public BytecodeInfoPane(ScrollPane bytecode_info_pane)
    {        
        this.bytecode_info_pane = bytecode_info_pane;
        
        setupListView();        
        setupPane();                                              
    }
    
    private void setupPane()
    {   
        bytecode_info_pane.setContent(bytecode_info_listview);        
        //bytecode_info_pane.setVvalue(1.0);
    }
    
    private void setupListView()
    {
        bytecode_info_listview = new ListView();
        //bytecode_info_listview.setMinHeight(MainScene.HEIGHT_TENTH * 40);
        bytecode_info_listview.setMinWidth(MainScene.WIDTH_TENTH * 100);     
                
        Label l = new Label("**************");
        
        bytecode_info_listview.getItems().add(l);
    }
    
    public void addByteCodeInfo(String bytecode_info_text)
    {
        int size = bytecode_info_listview.getItems().size();
        bytecode_info_listview.getItems().add(new Label(bytecode_info_text));
        bytecode_info_listview.scrollTo(size - 1);
    }
    
    public ScrollPane getPane()
    {
        return bytecode_info_pane;
    }
}
