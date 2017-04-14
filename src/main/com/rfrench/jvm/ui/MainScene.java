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

package main.com.rfrench.jvm.ui;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import main.com.rfrench.jvm.java.MethodArea;

import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.com.rfrench.jvm.controller.MainController;
import main.com.rfrench.jvm.java.JVMClassLoader;

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
   
    private MainController controller;
    
    private AnchorPane root_pane;
          
    public MainScene(MethodArea method_area, Stage primary_stage, JVMClassLoader class_loader)
    {        
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/main/com/rfrench/jvm/controller/MainScene.fxml"));   
            Parent content = loader.load();
            controller = loader.<MainController>getController();            
            
            controller.setMainScene(this);
            controller.setClassLoader(class_loader);
            controller.setMethodArea(method_area);
            controller.setStage(primary_stage);
            controller.setupLocalVariableFrame(method_area);
            controller.setupBytecodeInfoPane();
            controller.setupOperandStackPane();
            controller.addConstantPoolData(method_area);          
            
            
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
    
    public MainController getFXMLController()
    {
        return controller;
    }
}
