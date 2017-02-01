/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.com.rfrench.jvm.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
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
public class StageController implements Initializable
{
    private MainScene main_scene;
    private ExecutionEngine execution_engine;
    
    @FXML
    private FlowPane local_variable_pane;
    @FXML
    private TabPane bytecode_pane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        Label test_label = new Label("Test");
                
        local_variable_pane.getChildren().add(test_label);
        
        Tab test_tab = new Tab();
        test_tab.setText("A method");
        test_tab.setContent(test_label);
        
        bytecode_pane.getTabs().add(test_tab);                        
    }   
    
    public StageController(Stage primaryStage, MainScene main_scene, ExecutionEngine execution_engine)
    {

        this.main_scene = main_scene;
        this.execution_engine = execution_engine;

        //Scene scene = new Scene(main_scene.getMainPane(), MainScene.WIDTH_TENTH*100, MainScene.HEIGHT_TENTH*100);
        
        AnchorPane main_scene_root_pane = main_scene.getRootPane();
        Scene test_scene = new Scene(main_scene_root_pane);

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
        //delay.setOnFinished(event -> primaryStage.setScene(scene));
        delay.setOnFinished(event -> primaryStage.setScene(test_scene));
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
