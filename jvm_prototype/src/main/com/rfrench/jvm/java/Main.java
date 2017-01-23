
package main.com.rfrench.jvm.java;

import controller.com.rfrench.jvm.java.MainSceneController;
import ui.com.rfrench.jvm.java.MainScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
    Program Title: Main.java
    Author: Ryan French
    Created: 19-Oct-2016
    Version: 1.0
*/

public class Main extends Application
{    
    public static final String FILE_PATH = "/main/resources/javap/while_test_verbose.txt";   
    //public static final String FILE_PATH = "/main/resources/javap/multiple_methods.txt";  
    
    public static final String JSON_FILE_PATH = "/main/resources/json/bytecodes.json";
         
    private MethodArea method_area;
    
    private Register SP;
    private Register CPP;
    private Register LV;
        
    private MainScene main_scene;
    private MainSceneController main_scene_controller;
    private ExecutionEngine execution_engine;        
    private ClassLoader class_loader;
                                
    @Override
    public void start(Stage primaryStage)
    {      
                     
        class_loader = new ClassLoader(FILE_PATH);
        
        class_loader.readFile();
        
        method_area = new MethodArea(class_loader); 
                        
        createRegisters();        
                
        main_scene = new MainScene(method_area, class_loader, primaryStage);                 
        
        main_scene_controller = new MainSceneController(main_scene);
                
        execution_engine = new ExecutionEngine(main_scene, main_scene_controller, method_area, class_loader);                                       
                        
        Scene scene = new Scene(main_scene.getMainPane(), MainScene.WIDTH_TENTH*10, MainScene.HEIGHT_TENTH*10);
        
        primaryStage.setMaximized(true);
        primaryStage.setTitle("JVM Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();                
    }
    
    private void createRegisters()
    {                
        int LV_size = class_loader.getMethods().get(0).getLocalSize();                        
        
        LV = new Register(2000);        
        
        SP = new Register(LV.get() + LV_size); 
        
        CPP = new Register(0);
    }
    
    public static void main(String[] args)
    {
        launch(args);
    }       
    
        
    public Register getSP() {
        return SP;
    }

    public void setSP(Register SP) {
        this.SP = SP;
    }

    public Register getCPP() {
        return CPP;
    }

    public void setCPP(Register CPP) {
        this.CPP = CPP;
    }

    public Register getLV() {
        return LV;
    }

    public void setLV(Register LV) {
        this.LV = LV;
    }
}
