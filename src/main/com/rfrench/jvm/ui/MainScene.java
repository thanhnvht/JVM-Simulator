
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
    
    public static final double WIDTH_TENTH = SCREEN_SIZE.getWidth() / 100;
    public static final double HEIGHT_TENTH = SCREEN_SIZE.getHeight() / 100;
    
    private final String CSS_FILE_PATH = "/main/com/rfrench/jvm/resources/css/style.css";
          
    private MethodArea method_area;
    
    private BorderPane main_pane;
    private GridPane grid_pane; 
    
    private UIMemoryPane memory_pane;
    private UIRegisterPane register_pane;
    private UIStackPane stack_pane;
    private UIAssemblyPane assembly_code_pane;
    private UIFramePane local_frame_pane;
    private UIConstantPoolPane constant_pool_pane;           
    private UIMenu m;
    private UIButtonPane button_pane;
    private UIByteCodeInfoPane bytecode_info_pane;
          
    public MainScene(MethodArea method_area, Stage stage)
    {        
        this.method_area = method_area;
        
        main_pane = new BorderPane();       
        
        grid_pane = new GridPane();
        
        addMemoryPane();
        addFramePane();
        addStackPane(); 
        addAssemblyPane(); 
        addConstantPoolPane();     
        addRegisterPane();
        addByteCodeInfoPane();
        addButtonPane();

        main_pane.setCenter(grid_pane);

        main_pane.getStylesheets().add(getClass().getResource(CSS_FILE_PATH).toString());
    }
    
    private void addMemoryPane()
    {
        memory_pane = new UIMemoryPane();
        
        grid_pane.add(memory_pane.getTabPane(), 1, 0, 1, 3);
    }
    
    private void addFramePane()
    {
        local_frame_pane = new UIFramePane(memory_pane.getPane());
                      
        int MAX_LOCAL_VAR = method_area.getMethod(0).getLocalSize();
        
        String test = method_area.getMethod(0).getMethodName();
        
        String[] test_array = new String[1];
        test_array[0] = test;
        
        //System.out.println(test);
        
        local_frame_pane.addFrameUI(test_array, 0, MAX_LOCAL_VAR);
    }
    
    private void addStackPane()
    {
        stack_pane = new UIStackPane(memory_pane.getPane());  
    }
    
    private void addAssemblyPane()
    {
        assembly_code_pane = new UIAssemblyPane(method_area);
        
        grid_pane.add(assembly_code_pane.getTabPane(), 2, 1, 2, 1);
    }
    
    private void addConstantPoolPane()
    {
        constant_pool_pane = new UIConstantPoolPane(method_area);  
        
        grid_pane.add(constant_pool_pane.getTabPane(), 0, 1, 1, 1);   
    }
    
    private void addRegisterPane()
    {
        register_pane = new UIRegisterPane();       
        
        grid_pane.add(register_pane.getTabPane(), 0, 2);
    }
    
    private void addByteCodeInfoPane()
    {
        bytecode_info_pane = new UIByteCodeInfoPane();
                
        grid_pane.add(bytecode_info_pane.getPane(), 1, 2);
    }
    
    private void addButtonPane()
    {
        button_pane = new UIButtonPane();
        
        grid_pane.add(button_pane.getButtonVBox(), 2, 2);
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
    
    public UIByteCodeInfoPane getByteCodeInfoPane()
    {
        return bytecode_info_pane;
    }
}
