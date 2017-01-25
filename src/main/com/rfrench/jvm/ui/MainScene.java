
package main.com.rfrench.jvm.ui;

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
    
    private final String CSS_FILE_PATH = "/main/com/rfrench/jvm/resources/css/style.css";
          
    private BorderPane main_pane;
    private GridPane grid_pane; 
    
  
    private UIRegisterPane register_pane;
    private UIStackPane stack_pane;
    private UIAssemblyPane assembly_code_pane;
    private UIFramePane local_frame_pane;
    private UIConstantPoolPane constant_pool_pane;           
    private UIMenu m;
    private UIButtonPane button_pane;
          
    public MainScene(MethodArea method_area, Stage stage)
    {        
        main_pane = new BorderPane();       
        
        grid_pane = new GridPane();
        
        UIMemoryPane mp = new UIMemoryPane();
        grid_pane.add(mp.getTabPane(), 1, 0, 1, 4);
        
        local_frame_pane = new UIFramePane(mp.getPane());
                      
        int MAX_LOCAL_VAR = method_area.getMethod(0).getLocalSize();
        
        local_frame_pane.addFrameUI(new String[0], 0, MAX_LOCAL_VAR);
          
        stack_pane = new UIStackPane(mp.getPane());        
                
        assembly_code_pane = new UIAssemblyPane(method_area);
        
        grid_pane.add(assembly_code_pane.getTabPane(), 2, 1, 2, 2);
        
        constant_pool_pane = new UIConstantPoolPane(method_area);      
        
        grid_pane.add(constant_pool_pane.getTabPane(), 0, 1, 1, 2);        
                          
        register_pane = new UIRegisterPane();       
        grid_pane.add(register_pane.getTabPane(), 0, 3);
        button_pane = new UIButtonPane();
        grid_pane.add(button_pane.getButtonVBox(), 2, 3);
                               
        
//        UIMenu menu_bar = new UIMenu(stage);
//        main_pane.setTop(menu_bar.getMenuBar());
        main_pane.setCenter(grid_pane);

        main_pane.getStylesheets().add(getClass().getResource(CSS_FILE_PATH).toString());
    }
    
    public BorderPane getMainPane()
    {
        return main_pane;
    }
    
    public UIRegisterPane getRegister()
    {
        return register_pane;
    }
    
    public UIButtonPane getButton()
    {
        return button_pane;
    }
    
    public UIAssemblyPane getAssembly()
    {
        return assembly_code_pane;
    }
    
    public UIConstantPoolPane getConstantPool()
    {
        return constant_pool_pane;
    }
    
    public UIStackPane getStack()
    {
        return stack_pane;
    }
    
    public UIFramePane getFrame()
    {
        return local_frame_pane;
    }
}
