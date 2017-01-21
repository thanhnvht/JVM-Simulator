
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
    //public static final String FILE_PATH = "/main/resources/javap/while_test2.txt";   
    public static final String FILE_PATH = "/main/resources/javap/main_method.txt";  
    
    public static final String JSON_FILE_PATH = "/main/resources/json/bytecodes.json";
     
    
    private Memory memory;
    
    private Register SP;
    private Register PC;
    private Register CPP;
    private Register LV;
        
    private MainScene main_scene;
    private MainSceneController main_scene_controller;
    private ExecutionEngine execution_engine;        
    private ClassLoader assembly_data;
                                
    @Override
    public void start(Stage primaryStage)
    {      
        memory = new Memory();      
        
        assembly_data = new ClassLoader(memory);
        
        assembly_data.readFile(FILE_PATH);
        
        createRegisters();        
                
        main_scene = new MainScene(memory, assembly_data, primaryStage);                 
        
        main_scene_controller = new MainSceneController(main_scene);
                
        execution_engine = new ExecutionEngine(main_scene, main_scene_controller, memory, assembly_data);                                       
                        
        Scene scene = new Scene(main_scene.getMainPane(), MainScene.WIDTH_TENTH*10, MainScene.HEIGHT_TENTH*10);
        
        primaryStage.setMaximized(true);
        primaryStage.setTitle("JVM Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();                
    }
    
    private void createRegisters()
    {                
        int LV_size = assembly_data.getMaxLocalVariable();
        
        PC = new Register(0);                     
        
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

    public Register getPC() {
        return PC;
    }

    public void setPC(Register PC) {
        this.PC = PC;
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
