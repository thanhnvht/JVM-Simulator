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

package main.com.rfrench.jvm.java;

import main.com.rfrench.jvm.ui.MainScene;
import javafx.application.Application;
import javafx.stage.Stage;
import main.com.rfrench.jvm.controller.StageController;

/*
    Program Title: Main.java
    Author: Ryan French
    Created: 19-Oct-2016
    Version: 1.0
*/

public class Main extends Application
{    

    public static final String JSON_FILE_PATH = "/main/com/rfrench/jvm/resources/json/bytecodes.json";             

    @Override
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void start(Stage primaryStage)
    {      
        JVMClassLoader class_loader = new JVMClassLoader();
        
        MethodArea method_area = new MethodArea(); 
        Heap heap = new Heap();
        
        MainScene main_scene = new MainScene(method_area, primaryStage, class_loader);                 
                
        ExecutionEngine execution_engine = new ExecutionEngine(main_scene, method_area, heap);                                       
                
        StageController stage_controller = new StageController(primaryStage, main_scene);        
    }
        
    public static void main(String[] args)
    {
        launch(args);
    }          
}
