
package main.com.rfrench.jvm.java;

import javafx.animation.PauseTransition;
import main.com.rfrench.jvm.controller.MainSceneController;
import main.com.rfrench.jvm.ui.MainScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.com.rfrench.jvm.controller.StageController;

/*
    Program Title: Main.java
    Author: Ryan French
    Created: 19-Oct-2016
    Version: 1.0
*/

public class Main extends Application
{    
    public static final String FILE_PATH = "/main/com/rfrench/jvm/resources/javap/while_test_verbose.txt";   
    //public static final String FILE_PATH = "/main/com/rfrench/jvm/resources/javap/multiple_methods.txt";  
    
    public static final String JSON_FILE_PATH = "/main/com/rfrench/jvm/resources/json/bytecodes.json";             
                                
    @Override
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void start(Stage primaryStage)
    {      
                     
        ClassLoader class_loader = new ClassLoader(FILE_PATH);
                
        class_loader.readFile();
        
        MethodArea method_area = new MethodArea(class_loader); 

        MainScene main_scene = new MainScene(method_area);                 
        
        MainSceneController main_scene_controller = new MainSceneController(main_scene);
                
        new ExecutionEngine(main_scene, main_scene_controller, method_area, primaryStage);                                       
                

        StageController stage_controller = new StageController(primaryStage, main_scene);
        

    }
        
    public static void main(String[] args)
    {
        launch(args);
    }   

    
    
}
