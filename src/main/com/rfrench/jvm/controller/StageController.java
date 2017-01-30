/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.com.rfrench.jvm.controller;

import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.com.rfrench.jvm.ui.MainScene;

/**
 *
 * @author Ryan
 */
public class StageController
{
    public StageController(Stage primaryStage, MainScene main_scene)
    {
        Scene scene = new Scene(main_scene.getMainPane(), MainScene.WIDTH_TENTH*100, MainScene.HEIGHT_TENTH*100);
                        
        primaryStage.setMaximized(true);
        primaryStage.setTitle("JVM Simulator");
        
        StackPane splash_pane = new StackPane();
                        
        final String IMAGE_URL = "/main/com/rfrench/jvm/resources/images/coollogo_com-320641003.png";
        
        splash_pane.getChildren().add(new ImageView(IMAGE_URL));
        
        Scene splash_scene = new Scene(splash_pane, MainScene.WIDTH_TENTH*100, MainScene.HEIGHT_TENTH * 100, Color.BLACK);
        
        primaryStage.setScene(splash_scene);
        primaryStage.show();                
        
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(event -> primaryStage.setScene(scene));
        delay.play();
    }
}
