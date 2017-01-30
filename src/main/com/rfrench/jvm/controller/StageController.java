/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.com.rfrench.jvm.controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.com.rfrench.jvm.java.ExecutionEngine;
import main.com.rfrench.jvm.ui.MainScene;

/**
 *
 * @author Ryan
 */
public class StageController
{
    private MainScene main_scene;
    private ExecutionEngine execution_engine;
    
    public StageController(Stage primaryStage, MainScene main_scene, ExecutionEngine execution_engine)
    {
        this.main_scene = main_scene;
        this.execution_engine = execution_engine;
        
        Scene scene = new Scene(main_scene.getMainPane(), MainScene.WIDTH_TENTH*100, MainScene.HEIGHT_TENTH*100);
        setupMainSceneMenuButtons();
        
        primaryStage.setMaximized(true);
        primaryStage.setTitle("JVM Simulator");
        
        StackPane splash_pane = new StackPane();
                        
        final String IMAGE_URL = "/main/com/rfrench/jvm/resources/images/coollogo_com-320641003.png";
        
        splash_pane.getChildren().add(new ImageView(IMAGE_URL));
        
        Scene splash_scene = new Scene(splash_pane, MainScene.WIDTH_TENTH*100, MainScene.HEIGHT_TENTH * 100, Color.BLACK);
        
        primaryStage.setScene(splash_scene);
        primaryStage.show();                
        
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> primaryStage.setScene(scene));
        delay.play();
        
        
    }
    
    private void setupMainSceneMenuButtons()
    {
        Menu next_instruct_button = main_scene.getMenu().getNextInstructionButton();
        
        Node menu_image = next_instruct_button.getGraphic();
        
        menu_image.setOnMouseClicked((MouseEvent event) -> 
        {
            execution_engine.executeInstruction();
        });

    }
}
