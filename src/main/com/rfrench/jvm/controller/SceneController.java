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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.fxml.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.com.rfrench.jvm.java.DisassembledFileGenerator;
import main.com.rfrench.jvm.java.ExecutionEngine;
import main.com.rfrench.jvm.java.JVMClassLoader;
import main.com.rfrench.jvm.java.MethodArea;
import main.com.rfrench.jvm.ui.AssemblyPane;
import main.com.rfrench.jvm.ui.BytecodeInfoPane;
import main.com.rfrench.jvm.ui.LocalVariablePane;
import main.com.rfrench.jvm.ui.MainScene;
import main.com.rfrench.jvm.ui.OperandStackPane;

/*
    Program Title: SceneController.java
    Author: Ryan French
    Created: 03-Feb-2016
    Version: 1.0
*/

public class SceneController implements Initializable {

    @FXML
    private Pane local_variable_pane;    
    @FXML
    private TabPane bytecode_pane;   
    @FXML
    private TitledPane bytecode_info_pane;
    @FXML
    private Button next_button;
    @FXML
    private Button play_button;
    @FXML
    private Button pause_button;
    @FXML
    private Button open_button;
    @FXML
    private Button reset_button;
    @FXML
    private Pane operand_stack_pane;
    @FXML
    private ListView constant_pool_listview;
    @FXML
    private ListView java_code_listview;
    @FXML
    private StackPane register_pane;
    @FXML
    private MenuItem menu_item_credits;
    
    private MainScene main_scene;
    private AssemblyPane assembly_pane;
    private OperandStackPane operand_stack_p;
    private LocalVariablePane local_variable_p;
    private ExecutionEngine execution_engine;
    private BytecodeInfoPane bytecode_info_p;
    
    private Stage primary_stage;
    
    private MethodArea method_area;
    private JVMClassLoader class_loader;
        
    private Stack button_presses_per_method;
    
    private int before_branch_button_press;

    public SceneController()
    {
        button_presses_per_method = new Stack();
        
        button_presses_per_method.push(-1);
        
        before_branch_button_press = -1;     
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        String image_file_path = "/main/com/rfrench/jvm/resources/images/";
        
        next_button.setGraphic(new ImageView(image_file_path + "forward-button.png"));
        play_button.setGraphic(new ImageView(image_file_path + "play-button.png"));
        pause_button.setGraphic(new ImageView(image_file_path + "pause-button.png"));       
        open_button.setGraphic(new ImageView(image_file_path + "open-folder.png"));
        reset_button.setGraphic(new ImageView(image_file_path + "reset.png"));
        
        setupRegisterPane();
    }    
    
    public void addConstantPoolData(MethodArea method_area)
    {        
        final int CONSTANT_POOL_SIZE = method_area.getConstantPool().size();
        
        for(int i = 0; i < CONSTANT_POOL_SIZE; i++)
        {
            String method_ref = method_area.getConstantPool().get(i);
            Label constant_pool_label = new Label(method_ref);
            constant_pool_listview.getItems().add(constant_pool_label);
        }                        
    }        
    
    public void setupBytecodeTab(MethodArea method_area)
    {
       assembly_pane = new AssemblyPane(bytecode_pane, method_area);
    }

    public void setupLocalVariableFrame(MethodArea method_area)
    {

        if(method_area.hasMethods())
        {
            local_variable_p = new LocalVariablePane(local_variable_pane);
        
            int MAX_LOCAL_VAR = method_area.getMethod(0).getLocalSize();
        
            String init_method = "<init>";
        
            String[] test_array = new String[1];
            test_array[0] = init_method;
        
            local_variable_p.addFrameUI(test_array, 0, MAX_LOCAL_VAR);
        }

    }
    
    public void setMethodArea(MethodArea method_area)
    {
        this.method_area = method_area;
    }
    
    public void setStage(Stage primary_stage)
    {
        this.primary_stage = primary_stage;
    }
    
    public void setupRegisterPane()
    {
        Label PC_label =  new Label("PC  : ");       
        
        Tooltip tool_tip = new Tooltip();
        tool_tip.setText("PC - Program Counter, the location of what bytecode to run next");
        PC_label.setTooltip(tool_tip);
        
        register_pane.getChildren().add(PC_label);
    }
    
    public void updateRegister(int value)
    {
        Label PC_label = (Label)register_pane.getChildren().get(0);        
        String PC_value = Integer.toHexString(value).toUpperCase();        
        PC_label.setText("PC : 0x" + PC_value);
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
        // final float REPEAT_TIME = 0.5f;
        
        final float REPEAT_TIME = 1.5f;
        
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(REPEAT_TIME),eve -> 
        {
            if(!execution_engine.isPaused())
            {
                if(!execution_engine.isProgramComplete())
                    execution_engine.executeInstruction();
            }                                           
        }));
                                        
        timeline.setCycleCount(Animation.INDEFINITE);                
        timeline.play();                                                
    }
    
    public void openButton()
    {
        FileChooser file_chooser = new FileChooser();
        
        File file = file_chooser.showOpenDialog(primary_stage);
        
        String absolute_file_path = file.getAbsolutePath();
        
        DisassembledFileGenerator dfg = new DisassembledFileGenerator(absolute_file_path);
        
        String javap_file_path = dfg.getSavedFilePath();
        ArrayList<String> java_code_data = dfg.getJavaCode();
        
        class_loader.readFile(javap_file_path);
                        
        method_area.setupMethodArea(class_loader);
        method_area.setConstantPoolData(class_loader.getConstantPoolData());
               
        bytecode_pane.getTabs().remove(0);
        
        setupBytecodeTab(method_area);
        setupLocalVariableFrame(method_area);                        
        addConstantPoolData(method_area);
        addJavaCodeData(java_code_data);
        
    }
    
    private void addJavaCodeData(ArrayList<String> java_code_data)
    {
        for(int i = 0; i < java_code_data.size(); i++)
        {
            String java_code_line = java_code_data.get(i);
            Label java_code_label = new Label(java_code_line);
            java_code_listview.getItems().add(java_code_label);
        }
    }
    
    public void pauseButton()
    {
        execution_engine.changePause();
    }
    
    public void resetButton()
    {
        
    }
    
    public void creditsMenuItem()
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Credits");
        alert.setContentText("Icons made by: Freepik");

        alert.showAndWait();
    }
    
    
    public void setMainScene(MainScene main_scene)
    {
        this.main_scene = main_scene;
    }
    
    public void setClassLoader(JVMClassLoader class_loader)
    {
        this.class_loader = class_loader;
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
        
    public Stack getButtonStack()
    {
        return button_presses_per_method;
    }    
    
    public void ALOAD_0(String value)
    {
        operand_stack_p.push(value);
        
        bytecode_info_p.addByteCodeInfo(value);
    }
    
    public void BIPUSH(String value)
    {
        operand_stack_p.push(value); 
        
        bytecode_info_p.addByteCodeInfo("Pushing a value onto the operand stack");
    }
    
    public void LDC2_W(String value)
    {
        operand_stack_p.push(value);
        
        bytecode_info_p.addByteCodeInfo("Retrieve a Long value from Constant Pool. Pushed onto Operand Stack");
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
        String label_name = operand_stack_p.getStackText();     
        operand_stack_p.pop();
        
        label_name = operand_stack_p.getStackText() + " - " + label_name;                                           
        operand_stack_p.pop();
        
        operand_stack_p.push(label_name);
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
        
            System.out.println("return current method: " + current_method);
        
          local_variable_p.removeFrameUI(current_method);        
          
          bytecode_info_p.addByteCodeInfo("Returning from Method");     
    }
    
    public void LOOKUPSWITCH(ArrayList<Integer> switch_cases, ArrayList<Integer> switch_branches)
    {
        operand_stack_p.pop();
        
        int number_of_cases = switch_cases.size();
        
        bytecode_info_p.addByteCodeInfo("Switch Statement");
        
        for(int i = 0; i < number_of_cases; i++)
        {
            if(i == number_of_cases - 1)
            {
                 bytecode_info_p.addByteCodeInfo("    Default, Branch to: " + switch_branches.get(i));
            }
            else
            {
                bytecode_info_p.addByteCodeInfo("    Case: " + switch_cases.get(i) + ": Branch to: " + switch_branches.get(i));
            }
        }
                
    }
    
}
