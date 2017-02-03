
package main.com.rfrench.jvm.ui;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import main.com.rfrench.jvm.java.MethodArea;

import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.com.rfrench.jvm.controller.TestFXMLController;

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
   
    private TestFXMLController controller;
    
    private AnchorPane root_pane;
          
    public MainScene(MethodArea method_area, Stage primary_stage)
    {        
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/main/com/rfrench/jvm/controller/TestFXML.fxml"));            
            Parent content = loader.load();
            
            controller = loader.<TestFXMLController>getController();            
            controller.setMainScene(this);
            controller.setupBytecodeTab(method_area);
            controller.setupLocalVariableFrame(method_area);
            controller.setupBytecodeInfoPane();
            controller.setupOperandStackPane();
            
            root_pane = (AnchorPane) content;            
        }

        catch(IOException e)
        {
            e.printStackTrace();
        }
    
    }
        
    public AnchorPane getRootPane()
    {
        return root_pane;
    }
    
    public TestFXMLController getFXMLController()
    {
        return controller;
    }
}
