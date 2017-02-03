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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import main.com.rfrench.jvm.java.ExecutionEngine;
import main.com.rfrench.jvm.java.MethodArea;
import main.com.rfrench.jvm.ui.AssemblyPane;
import main.com.rfrench.jvm.ui.BytecodeInfoPane;
import main.com.rfrench.jvm.ui.LocalVariablePane;
import main.com.rfrench.jvm.ui.MainScene;
import main.com.rfrench.jvm.ui.OperandStackPane;

/*
    Program Title: TestFXMLController.java
    Author: Ryan French
    Created: 03-Feb-2016
    Version: 1.0
*/

public class TestFXMLController implements Initializable {

    @FXML
    private Pane local_variable_pane;    
    @FXML
    private TabPane bytecode_pane;    
    @FXML
    private ScrollPane bytecode_info_pane;
    @FXML
    private Button next_button;
    @FXML
    private Button play_button;
    @FXML
    private Button pause_button;
    @FXML
    private Pane operand_stack_pane;
    
    private MainScene main_scene;
    private AssemblyPane assembly_pane;
    private OperandStackPane operand_stack_p;
    private LocalVariablePane local_variable_p;
    private ExecutionEngine execution_engine;
    private BytecodeInfoPane bytecode_info_p;
    
    
    private Stack button_presses_per_method;
    
    private int before_branch_button_press;

    public TestFXMLController()
    {
        button_presses_per_method = new Stack();
        
        button_presses_per_method.push(-1);
        
        before_branch_button_press = -1;     
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        next_button.setGraphic(new ImageView("/main/com/rfrench/jvm/resources/images/forward-button.png"));
        play_button.setGraphic(new ImageView("/main/com/rfrench/jvm/resources/images/play-button.png"));
        pause_button.setGraphic(new ImageView("/main/com/rfrench/jvm/resources/images/pause-button.png"));
                              
    }    
    
    public void setupBytecodeTab(MethodArea method_area)
    {
       assembly_pane = new AssemblyPane(bytecode_pane, method_area);
    }

    public void setupLocalVariableFrame(MethodArea method_area)
    {
        local_variable_p = new LocalVariablePane(local_variable_pane);
        
        int MAX_LOCAL_VAR = method_area.getMethod(0).getLocalSize();
        
        String test = method_area.getMethod(0).getMethodName();
        
        String[] test_array = new String[1];
        test_array[0] = test;
        
        local_variable_p.addFrameUI(test_array, 0, MAX_LOCAL_VAR);
    }
    
    public void setExecutionEngine(ExecutionEngine execution_engine)
    {
        this.execution_engine = execution_engine;
    }
    
    public void setupBytecodeInfoPane()
    {
        bytecode_info_p = new BytecodeInfoPane(bytecode_info_pane);
    }
    
    public void setupOperandStackPane()
    {
        operand_stack_p = new OperandStackPane(operand_stack_pane);
    }
    
    public BytecodeInfoPane getBytecodeInfoPane()
    {
        return bytecode_info_p;
    }
    
    public void nextButton()
    {
        execution_engine.executeInstruction();
    }
    
    public void playButton()
    {
//        boolean is_paused = execution_engine.isBranch()
//        
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                eve -> 
                {
//                    if(!pause_program)
//                    {
//                        if(!program_done)
                            execution_engine.executeInstruction();
//                    }                                           
                }   
                    
                ));
            
                             
                timeline.setCycleCount(Animation.INDEFINITE);                
                timeline.play();           
                                     
    }
    
    public void setMainScene(MainScene main_scene)
    {
        this.main_scene = main_scene;
    }
    
    public void ALOAD_0(String value)
    {
        operand_stack_p.push(value);
        
        bytecode_info_p.addByteCodeInfo(value);
    }
    
    public void hightlightLine(int current_method, int button_press_count)
    {
        ListView current_listview = assembly_pane.getCurrentListView(current_method);
  
        
        current_listview.getSelectionModel().select(button_press_count);                
    }
    
    
    public void changeTab(int current_method)
    {                                
        SingleSelectionModel<Tab> selection_model = assembly_pane.getTabPane().getSelectionModel();
            
        selection_model.select(current_method);    
    }
    
    public void updateRegisterLabels(int PC)
    {
        String PC_string;
        
        if(PC < 16)
            PC_string = "PC  : 0x0" + Integer.toHexString(PC).toUpperCase();
        else
            PC_string = "PC  : 0x" + Integer.toHexString(PC).toUpperCase();
        
      //  main_scene.getRegister().updateLabel(PC_string, 1);
    }   
    
    public Stack getButtonStack()
    {
        return button_presses_per_method;
    }    
    
    public void BIPUSH(String value)
    {
        operand_stack_p.push(value); 
        
        bytecode_info_p.addByteCodeInfo("Pushing a value onto the operand stack");
    }
    
    public void ICONST(String value)
    {        
        operand_stack_p.push(value);

        bytecode_info_p.addByteCodeInfo("Pushing a value onto stack");
    }
    
    public void ILOAD(int frame_index)
    {        
        operand_stack_p.push(local_variable_p.getFrameName(frame_index));             
        
        bytecode_info_p.addByteCodeInfo("Loading a value from local variable frame");
    }
    
    public void IADD()
    {       
        String label_name = operand_stack_p.getStackText();     
        operand_stack_p.pop();
        
        label_name = operand_stack_p.getStackText() + " + " + label_name;                                           
        operand_stack_p.pop();
        
        operand_stack_p.push(label_name);
    }
    
    public void ISTORE(int current_method_count, int frame_index, String frame_value)
    {        
        operand_stack_p.pop();
                   
        local_variable_p.updateFrameLabel(current_method_count, frame_index, frame_value);    
        
        bytecode_info_p.addByteCodeInfo("Storing a value from local variable frame");
        
    }
    
    public void ISUB()
    {
//        String label_name = main_scene.getStack().getStackText();          
//        main_scene.getStack().pop();
//        
//        label_name = main_scene.getStack().getStackText() + " - " + label_name;              
//        main_scene.getStack().pop();                              
//
//        main_scene.getStack().push(label_name);
    }
    
    public void IF_ICMPEQ()
    {
        operand_stack_p.pop();
        operand_stack_p.pop();

        bytecode_info_p.addByteCodeInfo("Branch bytecode");      
    }
    
    public void GOTO(int offset, ArrayList<String> Linenumbers, int current_method)
    {          
        String offset_string = Integer.toString(offset);
        
        boolean line_found = false;
                
        int count = 0;
        
        while(!line_found || count > Linenumbers.size())
        {
            String next_line_number = Linenumbers.get(count);
            
            if(next_line_number.contains(offset_string))
            {
                before_branch_button_press = (int)button_presses_per_method.pop() + 1;    
                
                button_presses_per_method.push(count);                
                line_found = true;                
            }
            
            count++;
        }   
        
        ListView current_listview = assembly_pane.getCurrentListView(current_method);
        
        current_listview.getSelectionModel().select(before_branch_button_press);     

        bytecode_info_p.addByteCodeInfo("GOTO bytecode");
        
    }
    
    public void POP()
    {
        operand_stack_p.pop();
    }
    
    public void IINC(int current_method_count, int frame_index, String frame_value)
    {        
        local_variable_p.updateFrameLabel(current_method_count, frame_index, frame_value);
        
        bytecode_info_p.addByteCodeInfo("Incrementing a value in local variable frame");
    }
    
    public void INVOKESPECIAL(int stack_size, int current_method, int max_local_var)
    {        
        String[] stack_text = new String[stack_size];
        
        for(int i = 0; i < stack_size; i++)
        {            
            stack_text[i] = operand_stack_p.getStackText();            
        }
   
        for(int i = 0; i < stack_size; i++)
        {                                
            operand_stack_p.pop();
        }
                
        local_variable_p.addFrameUI(stack_text, current_method, max_local_var);
        
        bytecode_info_p.addByteCodeInfo("Invoking Method"); 
    }
    

    
    public void RETURN(int current_method)
    {
        
//        SingleSelectionModel<Tab> selection_model = main_scene.getAssembly().getTabPane().getSelectionModel();
//            
//        selection_model.select(current_method);
        
          local_variable_p.removeFrameUI(current_method);        
          
          bytecode_info_p.addByteCodeInfo("Returning from Method");     
    }
    
}
