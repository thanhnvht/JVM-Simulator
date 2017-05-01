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


import com.google.common.collect.BiMap;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.com.rfrench.jvm.java.Compiler;
import main.com.rfrench.jvm.java.ExecutionEngine;
import main.com.rfrench.jvm.java.JVMClassLoader;
import main.com.rfrench.jvm.java.Method;
import main.com.rfrench.jvm.java.MethodArea;
import main.com.rfrench.jvm.ui.AssemblyPane;
import main.com.rfrench.jvm.ui.BytecodeInfoPane;
import main.com.rfrench.jvm.ui.JVMOperandStackPane;
import main.com.rfrench.jvm.ui.LocalVariablePane;
import main.com.rfrench.jvm.ui.MainScene;

/*
    Program Title: MainController.java
    Author: Ryan French
    Created: 03-Feb-2016
    Version: 1.0
*/

/**
 * Controls GUI Components in Main Screen
 * Reflects changes to memory areas made by bytecodes
 * Gives additional information as to the bytecode being executed
 */
public class MainController implements Initializable 
{
    private AssemblyPane assembly_pane;
    private int before_branch_button_press;   
    private MainScene main_scene;
    private Stack button_presses_per_method;
    private BytecodeInfoPane bytecode_info_p;
    
    @FXML
    private Pane error_pane;
    
    @FXML
    private Tab bytecode_info_pane;    
    @FXML
    private TabPane bytecode_pane;   
    @FXML
    private TabPane class_pane;
    
    @FXML
    private Tab class_tab;
    
    private JVMClassLoader class_loader;
    @FXML
    private ListView constant_pool_listview;
    private ExecutionEngine execution_engine;
    @FXML
    private ListView java_code_listview;
    
    private LocalVariablePane local_variable_logic;
            
    @FXML
    private ScrollPane local_var_scroll_pane;
    
    @FXML
    private ScrollPane operand_scroll_pane;
    
    @FXML
    private Canvas local_var_canvas;    
    
    @FXML
    private Canvas operand_stack_canvas;
       
    @FXML
    private MenuItem menu_item_credits;
    private MethodArea method_area;
    @FXML
    private Button next_button;
    @FXML
    private Button open_button;
    @FXML
    private Button exit_button;
        
    //private OperandStackPane operand_stack_p;
    
    private JVMOperandStackPane operand_stack_p;
        
    @FXML
    private Button pause_button;
    @FXML
    private Button play_button;
    
    private Stage primary_stage;
    @FXML
    private StackPane register_pane;
    @FXML
    private Button reset_button;
        
    /**
     * MainController Constructor    
     */
    public MainController()
    {                
        button_presses_per_method = new Stack();
        
        button_presses_per_method.push(-1);
        
        before_branch_button_press = -1;     
                
    }
        
    /**
     * Load an array reference onto operand stack     
     * @param value array reference
     */
    public void ALOAD_0(String value) 
    {
        System.out.println("aload: " + value);
        
        operand_stack_p.push(value);
        
        bytecode_info_p.addByteCodeInfo(value);
    } 
    
    /**
     * Push a value onto the operand stack
     * @param value value to be pushed
     */
    public void BIPUSH(String value)
    {
        operand_stack_p.push(value);
        
        bytecode_info_p.addByteCodeInfo("Pushing a value onto the operand stack");                        
    }  
    
    /**
     * Duplicate existing value on operand stack
     */
    public void DUP()
    {
        String dup_string = operand_stack_p.peek();
        
        operand_stack_p.push(dup_string);
    }
    
    /**
     * Move to another part of code unconditionally
     * @param offset the offset to move by
     * @param Linenumbers the Linenumber in Java code
     * @param current_method the current method being executed
     */
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
        
    /**
     * Display program complete message
     */
    public void complete()
    {
        bytecode_info_p.addByteCodeInfo("*** Program Complete ***");
    }
    
    /**
     * Display information according to which arithmetic bytecode has been executed
     * @param arithmetic_symbol the arithmetic that has been executed e.g. +, -, /, *
     */
    public void IARITHMETIC(String arithmetic_symbol)
    {
        String label_name = operand_stack_p.peek();
        operand_stack_p.pop();
        
        label_name = operand_stack_p.peek() + " " + arithmetic_symbol + " " + label_name;
        operand_stack_p.pop();
        
        operand_stack_p.push(label_name);
    }
    
    
       
    /**
     * Push a value less than or equal to 5 onto operand stack
     * @param value value to be pushed
     */
    public void ICONST(String value)
    {
        operand_stack_p.push(value);       
        
        bytecode_info_p.addByteCodeInfo("Pushing a value onto stack");
    }
    
    /**
     * Increment a value within local variable frame
     * @param current_method_count the current method being executed
     * @param frame_index which frame element to increment
     * @param frame_value the value of the element in the frame
     */
    public void IINC(int current_method_count, int frame_index, String frame_value)
    {
        local_variable_logic.updateFrameLabel(current_method_count, frame_index, frame_value);
        
        bytecode_info_p.addByteCodeInfo("Incrementing a value in local variable frame");
    }
    
    /**
     * Load a value from the local variable frame onto the operand stack
     * @param frame_index the index of the value to load
     */
    public void ILOAD(int frame_index)
    {
        operand_stack_p.push(local_variable_logic.getFrameName(frame_index));
        
        bytecode_info_p.addByteCodeInfo("Loading a value from local variable frame");
    }
    
    /**
     * Invoke a method
     * @param stack_size max size of operand stack for new method
     * @param current_method the current method being executed
     * @param max_local_var the maximum size of the local variable frame for new
     *                      method
     */
    public void INVOKESPECIAL(int stack_size, int current_method, int max_local_var)
    {
        String[] stack_text = new String[stack_size];
        
        for(int i = 0; i < stack_size; i++)
        {
            stack_text[i] = operand_stack_p.peek();
        }
        
        for(int i = 0; i < stack_size; i++)
        {
            operand_stack_p.pop();
        }
        
        local_variable_logic.addMethodLocalFrame(stack_text, current_method, max_local_var);
        
        bytecode_info_p.addByteCodeInfo("Invoking Method");
    }
    
    
    /**
     * Store a variable from the operand stack into the local variable frame
     * @param current_method_count The current method being executed
     * @param frame_index The area where the value is to be stored
     * @param frame_value The value to be stored
     */
    public void ISTORE(int current_method_count, int frame_index, String frame_value)
    {
        operand_stack_p.pop();
        
        local_variable_logic.updateFrameLabel(current_method_count, frame_index, frame_value);
        
        bytecode_info_p.addByteCodeInfo("Storing a value from local variable frame");
        
    }
    
    /**
     * Retrieve a Long data type value from the constant pool
     * @param value The Long data type value
     */
    public void LDC2_W(String value)
    {
        operand_stack_p.push(value);
        
        bytecode_info_p.addByteCodeInfo("Retrieve a Long value from Constant Pool. Pushed onto Operand Stack");
    }
    
    /**
     * Switch command. 
     * @param switch_cases Number of cases in switch command
     * @param switch_branches number of branches in switch command
     */
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
    
    /**
     * Create a New Object
     * @param reference address of object
     * @param class_name name of class creating object
     */
    public void NEW(String reference, String class_name)
    {
        operand_stack_p.push(reference);
        
        bytecode_info_p.addByteCodeInfo("New Object created in Heap");
        
        Tab new_tab = new Tab();
        
        TabPane new_tabpane = new TabPane();
                                        
        new_tab.setContent(new_tabpane);        
        new_tab.setText(class_name);
        new_tab.setId(class_name);
        
        class_pane.getTabs().add(new_tab);
        
        assembly_pane.addNewAssembly(new_tabpane, method_area, 1);        
    }
    
    /**
     * Create an array
     * @param reference address of array in heap
     */
    public void NEWARRAY(String reference)
    {
        operand_stack_p.pop();
        
        operand_stack_p.push(reference);
        
        bytecode_info_p.addByteCodeInfo("New Array created in Heap");
    }
    
    /**
     * Pop a value from the operand stack
     */
    public void POP()
    {
        operand_stack_p.pop();
    }
    
    
    /**
     * Return from a method
     * @param current_method current method of execution
     */
    public void RETURN(int current_method)
    {
        
        System.out.println("return current method: " + current_method);
        
        local_variable_logic.removeFrameUI(current_method);
        
        bytecode_info_p.addByteCodeInfo("Returning from Method");
    }
    
    /**
     * Add constant pool data from javap file to the GUI
     * @param method_area memory area with constant pool data
     */
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
    
    /**
     * Add code data from java file to GUI
     * @param java_code_data the code data
     */
    private void addJavaCodeData(ArrayList<String> java_code_data)
    {
        for(int i = 0; i < java_code_data.size(); i++)
        {
            String java_code_line = java_code_data.get(i);
            Label java_code_label = new Label(java_code_line);
            java_code_listview.getItems().add(java_code_label);
        }
    }
    
    /**
     * Load the array value from Heap
     * @param array_value value to be loaded and shown
     */
    public void arrayLoad(String array_value)
    {
        operand_stack_p.pop();
        operand_stack_p.pop();
        operand_stack_p.push(array_value);
    }
    
    /**
     * Store a value within array. Pop all values on operand stack which that 
     * have now been used
     */
    public void arrayStore()
    {
        operand_stack_p.pop();        
        operand_stack_p.pop();
        operand_stack_p.pop();
    }
    
    /**
     * Change Operand Stack according to comparison to non-0 bytecode
     */
    public void branchComparisionNonZero()
    {
        operand_stack_p.pop();
        operand_stack_p.pop();
        
        bytecode_info_p.addByteCodeInfo("Conditional Branch");
    }
    
    /**
     * Change Operand Stack according to comparison to 0 bytecode
     */
    public void branchComparisionZero()
    {
        operand_stack_p.pop();
        bytecode_info_p.addByteCodeInfo("Conditional Branch");
    }        
    
    /**
     * Change the method tab
     * @param current_method current method being executed
     */
    public void changeTab(int current_method)
    {                                
        SingleSelectionModel<Tab> selection_model = assembly_pane.getTabPane().getSelectionModel();
            
        selection_model.select(current_method);    
    }
    
    /**
     * Show information of assets used in GUI
     */
    public void creditsMenuItem()
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Credits");
        alert.setContentText("Icons made by: Freepik");
        
        alert.showAndWait();
    }
        
    /**
     * Get Button Stack, what button has been pressed
     * @return button stack
     */
    public Stack getButtonStack()
    {
        return button_presses_per_method;
    }    
    
    /**
     * Get Bytecode Info Pane
     * @return bytecode info pane
     */
    public BytecodeInfoPane getBytecodeInfoPane()
    {
        return bytecode_info_p;
    }
    
    /**
     * Set the Class Loader object
     * @param class_loader JVMClassloader object
     */
    public void setClassLoader(JVMClassLoader class_loader)
    {
        this.class_loader = class_loader;
    }
    
    /**
     * Set Execution engine reference
     * @param execution_engine ExecutionEngine object
     */
    public void setExecutionEngine(ExecutionEngine execution_engine)
    {
        this.execution_engine = execution_engine;
    }
    
    /**
     * Set Main Scene Object
     * @param main_scene MainScene object
     */
    public void setMainScene(MainScene main_scene)
    {
        this.main_scene = main_scene;
    }
    
    /**
     * Set MethodArea object
     * @param method_area method area object
     */
    public void setMethodArea(MethodArea method_area)
    {
        this.method_area = method_area;
    }
    
    /**
     * Set Stage Object
     * @param primary_stage stage object
     */
    public void setStage(Stage primary_stage)
    {
        this.primary_stage = primary_stage;
    }


    /**
     * Highlight current line of execution in bytecode
     * @param current_method current method being executed
     * @param button_press_count how many button presses have been made
     */
    public void hightlightLine(int current_method, int button_press_count)
    {
        ListView current_listview = assembly_pane.getCurrentListView(current_method);    
        
        current_listview.getSelectionModel().select(button_press_count);
    }
    
    /**
     * Highlight current line of execution in Java code
     * @param current_method current method being executed
     * @param PC Program Counter
     */
    public void highlightJavaLine(int current_method, int PC)
    {
        Method m = method_area.getMethod(current_method);
        
        BiMap java_line_numbers = m.getJavaLineNumbers();
        
        java_line_numbers = java_line_numbers.inverse();
                     
        if(java_line_numbers.containsKey(PC))
        {
            int current_java_line = (int)java_line_numbers.get(PC) - 1;
            
            java_code_listview.getSelectionModel().select(current_java_line);    
        }            
    }
    
    /**
     * Initialise main variables for the Scene
     * @param url Path to image assets
     * @param rb ResourceBundle object
     */
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
    
    /**
     * ActionListener for the Next Button
     */
    public void nextButton()
    {
        execution_engine.executeInstruction();
    }
    
    /**
     * ActionListener for the Open Button
     */
    public void openButton()
    {
        FileChooser file_chooser = new FileChooser();

        File file = file_chooser.showOpenDialog(primary_stage);

        String absolute_file_path = file.getAbsolutePath();

        Compiler compiler = new Compiler(absolute_file_path);

        String javap_file_path = compiler.getSavedFilePath();
        
        ArrayList<String> java_code_data = compiler.getJavaCode();

        class_loader.readJavaInfoFile(javap_file_path);

        method_area.setupMethodArea(class_loader);
        method_area.setConstantPoolData(class_loader.getConstantPoolData());

        bytecode_pane.getTabs().remove(0);

        setupAssemblyCodePane(method_area);
        setupLocalVariableFrame(method_area);
        addConstantPoolData(method_area);
        addJavaCodeData(java_code_data); 

        String class_name = method_area.getJVMClass(0).getClassName();
        class_tab.setText(class_name);
        
    }
    
    /**
     * Action Listener for the Exit Button
     */
    public void exitButton()
    {
        try
        {
            File jar = new File(MainController.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());       

            String jar_path = jar.getAbsolutePath();
            
            String jar_file = "jvm_prototype.jar";
            
            int x = jar_path.lastIndexOf(jar_file);
            
            jar_path = jar_path.substring(0, x);
            
            jar_path = "\"" + jar_path + "\"";
            
            bytecode_info_p.addByteCodeInfo(jar_path);
            bytecode_info_p.addByteCodeInfo(Integer.toString(x));
            
            ProcessBuilder jar_process = new ProcessBuilder(jar_path, "-jar", jar_file);
                        
            Process p = jar_process.start();
        }
        
        catch(IOException | URISyntaxException e)
        {
            e.printStackTrace();
        }
        
    }
    
    /**
     * Action Listener for the Pause Button
     */
    public void pauseButton()
    {
        execution_engine.changePause(); 
    }
    
    /**
     * Action Listener for the Play Button
     */
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
    

    /**
     * Setup the Bytecode Pane
     * @param method_area method area data
     */
    public void setupAssemblyCodePane(MethodArea method_area)
    {
        assembly_pane = new AssemblyPane(bytecode_pane, method_area);        
    }
    
    /**
     * Setup the Local Variable Pane
     * @param method_area method area data
     */
    public void setupLocalVariableFrame(MethodArea method_area)
    {        
        local_var_scroll_pane.setVvalue(1.0);
        
        if(method_area.hasMethods())
        {
            local_variable_logic = new LocalVariablePane(local_var_canvas);
        
            int MAX_LOCAL_VAR = method_area.getMethod(0).getLocalSize();

            String init_method = "<init>";

            String[] test_array = new String[1];
            test_array[0] = init_method;

            local_variable_logic.addMethodLocalFrame(test_array, 0, MAX_LOCAL_VAR);
        }        
    }
    
    /**
     * Setup Bytecode Information Pane
     */
    public void setupBytecodeInfoPane()
    {
        bytecode_info_p = new BytecodeInfoPane(bytecode_info_pane); 
    }
    
    /**
     * Setup Operand Stack Pane
     */
    public void setupOperandStackPane()
    {
        operand_scroll_pane.setVvalue(1.0);
        
        //operand_stack_p = new OperandStackPane(operand_stack_canvas);           
        
        operand_stack_p = new JVMOperandStackPane(operand_stack_canvas); 
    }
    
    /**
     * Setup Register Pane (PC Counter)
     */
    public void setupRegisterPane()
    {
        Label PC_label =  new Label("PC  : ");
        
        Tooltip tool_tip = new Tooltip();
        tool_tip.setText("PC - Program Counter, the location of what bytecode to run next");
        PC_label.setTooltip(tool_tip);
        
        register_pane.getChildren().add(PC_label);
    }
    
    /**
     * Update PC Register value
     * @param value new value of PC
     */
    public void updateRegister(int value)
    {
        Label PC_label = (Label)register_pane.getChildren().get(0);
        String PC_value = Integer.toHexString(value).toUpperCase();
        PC_label.setText("PC : 0x" + PC_value);
    }           
}
