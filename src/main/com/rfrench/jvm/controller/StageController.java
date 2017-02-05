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
import main.com.rfrench.jvm.ui.SplashScene;

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
        SplashScene s_scene = new SplashScene();
        
        AnchorPane splash_scene_root_pane = s_scene.getRootPane();
        Scene splash_scene = new Scene(splash_scene_root_pane);
        
        AnchorPane main_scene_root_pane = main_scene.getRootPane();
        Scene scene = new Scene(main_scene_root_pane);
        
        primaryStage.setMaximized(true);
        primaryStage.setTitle("JVM Simulator");

                
        Label title_label = new Label("JVM Simulator");
        title_label.setId("title_label");

        primaryStage.setScene(splash_scene);
        primaryStage.show();                

        final int SPLASH_SCREEN_WAIT_TIME = 3;
        
        PauseTransition delay = new PauseTransition(Duration.seconds(SPLASH_SCREEN_WAIT_TIME));
        delay.setOnFinished(event -> primaryStage.setScene(scene));
        delay.play();
              
    }
    
}
