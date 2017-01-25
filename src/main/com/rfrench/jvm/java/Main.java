
package main.com.rfrench.jvm.java;

import main.com.rfrench.jvm.controller.MainSceneController;
import main.com.rfrench.jvm.ui.MainScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
    Program Title: Main.java
    Author: Ryan French
    Created: 19-Oct-2016
    Version: 1.0
*/

// Runtime.getRuntime().exec("cmd /c start build.bat");
// ^^ Use this later to run javap -p -v -c 'file'

public class Main extends Application
{    
    public static final String FILE_PATH = "/main/com/rfrench/jvm/resources/javap/while_test_verbose.txt";   
    //public static final String FILE_PATH = "/main/com/rfrench/jvm/resources/javap/multiple_methods.txt";  
    
    public static final String JSON_FILE_PATH = "/main/com/rfrench/jvm/resources/json/bytecodes.json";
         
    private MethodArea method_area;
    private ClassLoader class_loader; 
    private ExecutionEngine execution_engine;        
         
    private MainScene main_scene;
    private MainSceneController main_scene_controller;
                                
    @Override
    public void start(Stage primaryStage)
    {      
                     
        class_loader = new ClassLoader(FILE_PATH);
        
        class_loader.readFile();
        
        method_area = new MethodArea(class_loader); 

        main_scene = new MainScene(method_area, primaryStage);                 
        
        main_scene_controller = new MainSceneController(main_scene);
                
        new ExecutionEngine(main_scene, main_scene_controller, method_area);                                       
                        
        Scene scene = new Scene(main_scene.getMainPane(), MainScene.WIDTH_TENTH*10, MainScene.HEIGHT_TENTH*10);
        
        primaryStage.setMaximized(true);
        primaryStage.setTitle("JVM Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();                
    }
        
    public static void main(String[] args)
    {
        launch(args);
    }       

}
