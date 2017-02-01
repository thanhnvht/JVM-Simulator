/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.com.rfrench.jvm.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import main.com.rfrench.jvm.java.MethodArea;
import main.com.rfrench.jvm.ui.AssemblyPane;
import main.com.rfrench.jvm.ui.BytecodeInfoPane;
import main.com.rfrench.jvm.ui.LocalVariablePane;

/**
 * FXML Controller class
 *
 * @author Ryan
 */
public class TestFXMLController implements Initializable {

    @FXML
    private Pane local_variable_pane;
    
    @FXML
    private TabPane bytecode_pane;
    
    @FXML
    private ScrollPane bytecode_info_pane;


    public TestFXMLController()
    {
        System.out.println("test");
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {

                              
    }    
    
    public void setupBytecodeTab(MethodArea method_area)
    {
       new AssemblyPane(bytecode_pane, method_area);
    }

    public void setupLocalVariableFrame(MethodArea method_area)
    {
        LocalVariablePane l = new LocalVariablePane(local_variable_pane);
        
        int MAX_LOCAL_VAR = method_area.getMethod(0).getLocalSize();
        
        String test = method_area.getMethod(0).getMethodName();
        
        String[] test_array = new String[1];
        test_array[0] = test;
        
        //System.out.println(test);
        
        l.addFrameUI(test_array, 0, MAX_LOCAL_VAR);
    }
    
    public void setupBytecodeInfoPane()
    {
        new BytecodeInfoPane(bytecode_info_pane);
    }
    
}
