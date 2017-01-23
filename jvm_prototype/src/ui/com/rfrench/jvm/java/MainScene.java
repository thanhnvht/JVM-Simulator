
package ui.com.rfrench.jvm.java;

import main.com.rfrench.jvm.java.ClassLoader;
import main.com.rfrench.jvm.java.MethodArea;

import javafx.geometry.Rectangle2D;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

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
    
    private ClassLoader class_loader;
    
    private UIRegisterPane rp;
    private UIStackPane sp;
    private UIAssemblyPane ap;
    private UIFramePane fp;
    private UIConstantPoolPane cp;           
    private UIMenu m;
    private UIButtonPane bp;
          
    public MainScene(MethodArea method_area, ClassLoader class_loader, Stage stage)
    {
        
        main_pane = new BorderPane();       
        this.class_loader = class_loader;
        
        grid_pane = new GridPane();
        
        UIMemoryPane mp = new UIMemoryPane();
        grid_pane.add(mp.getTabPane(), 1, 0, 1, 4);
        
        fp = new UIFramePane(mp.getPane());
        
        int MAX_LOCAL_VAR = class_loader.getMethods().get(0).getLocalSize();
        
        for(int i = 0; i < MAX_LOCAL_VAR; i++)
        {
            fp.addFrameUI(i, "", 0);
        }
          
        sp = new UIStackPane(mp.getPane());        
                
        ap = new UIAssemblyPane(class_loader, new String[]{"", "i", "j", "k"}, method_area);
        
        grid_pane.add(ap.getTabPane(), 2, 1, 2, 2);
        
        cp = new UIConstantPoolPane(method_area);      
        
        grid_pane.add(cp.getTabPane(), 0, 1, 1, 2);        
                          
        rp = new UIRegisterPane();       
        grid_pane.add(rp.getTabPane(), 0, 3);
        bp = new UIButtonPane();
        grid_pane.add(bp.getButtonVBox(), 2, 3);
                               
        
//        UIMenu menu_bar = new UIMenu(stage);
//        main_pane.setTop(menu_bar.getMenuBar());
        main_pane.setCenter(grid_pane);

        main_pane.getStylesheets().add(getClass().getResource("/main/resources/css/style.css").toString());
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
