
package ui.com.rfrench.jvm.java;

import main.com.rfrench.jvm.java.ClassLoader;
import main.com.rfrench.jvm.java.Memory;

import javafx.geometry.Rectangle2D;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.com.rfrench.jvm.java.Main;

/*
    Program Title: MainScene.java
    Author: Ryan French
    Created: 06-Nov-2016
    Version: 1.0
*/

public class MainScene 
{
    private static final Rectangle2D SCREEN_SIZE = Screen.getPrimary().getVisualBounds();
    
    public static final double WIDTH_TENTH = SCREEN_SIZE.getWidth() / 10;
    public static final double HEIGHT_TENTH = SCREEN_SIZE.getHeight() / 10;
          
    private BorderPane main_pane;
    private GridPane grid_pane; 
    private BorderPane memory_pane;
    
    private ClassLoader input_file;
    
    private UIRegisterPane rp;
    private UIStackPane sp;
    private UIAssemblyPane ap;
    private UIFramePane fp;
    private UIConstantPoolPane cp;           
    private UIMenu m;
    private UIButtonPane bp;
          
    public MainScene(Memory M, ClassLoader input_file, Stage stage)
    {
        main_pane = new BorderPane();       
        this.input_file = input_file;
        
        grid_pane = new GridPane();
        
        UIMemoryPane mp = new UIMemoryPane();
        grid_pane.add(mp.getTabPane(), 1, 0, 1, 4);
        
        fp = new UIFramePane(mp.getPane());
                
        for(int i = 0; i < input_file.getMaxLocalVariable(); i++)
            fp.addFrameUI(i+1, M);
          
        sp = new UIStackPane(mp.getPane());        
                
        ap = new UIAssemblyPane(input_file, new String[]{"", "i", "j", "k"}, M);
        grid_pane.add(ap.getTabPane(), 2, 1, 2, 2);
        cp = new UIConstantPoolPane();      
        grid_pane.add(cp.getTabPane(), 0, 1, 1, 2);        
                          
        rp = new UIRegisterPane();       
        grid_pane.add(rp.getTabPane(), 0, 3);
        bp = new UIButtonPane();
        grid_pane.add(bp.getButtonVBox(), 2, 3);
                               
        
//        UIMenu menu_bar = new UIMenu(stage);
//        main_pane.setTop(menu_bar.getMenuBar());
        main_pane.setCenter(grid_pane);

        main_pane.getStylesheets().add(getClass().getResource("/main/resources/style.css").toString());
    }
    
    public BorderPane getMainPane()
    {
        return main_pane;
    }
    
    public UIRegisterPane getRegister()
    {
        return rp;
    }
    
    public UIButtonPane getButton()
    {
        return bp;
    }
    
    public UIAssemblyPane getAssembly()
    {
        return ap;
    }
    
    public UIConstantPoolPane getConstantPool()
    {
        return cp;
    }
    
    public UIStackPane getStack()
    {
        return sp;
    }
    
    public UIFramePane getFrame()
    {
        return fp;
    }
}