/*
    MIT License

    Copyright (c) 2017 Ryan French

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
*/

package main.com.rfrench.jvm.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.com.rfrench.jvm.ui.MainScene;

/*
    Program Title: StageController.java
    Author: Ryan French
    Created: 03-Feb-2016
    Version: 1.0
*/

public class StageController implements Initializable
{
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
                      
    }   
    
    public StageController(Stage primaryStage, MainScene main_scene)
    {
        AnchorPane main_scene_root_pane = main_scene.getRootPane();
        Scene test_scene = new Scene(main_scene_root_pane);
        
        primaryStage.setMaximized(true);
        primaryStage.setTitle("JVM Simulator");

        StackPane splash_pane = new StackPane();
                
        Label title_label = new Label("JVM Simulator");
        title_label.setId("title_label");

        splash_pane.getChildren().add(title_label);

        Scene splash_scene = new Scene(splash_pane, MainScene.WIDTH_TENTH*100, MainScene.HEIGHT_TENTH * 100, Color.BLACK);

        primaryStage.setScene(splash_scene);
        primaryStage.show();                

        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> primaryStage.setScene(test_scene));
        delay.play();
              
    }
    
}
