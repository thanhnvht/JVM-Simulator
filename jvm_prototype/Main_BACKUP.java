
package main.java.com.ryanfrench.jvm_prototype;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

import javafx.stage.Stage;
import javafx.stage.FileChooser;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/*
    Program Title: Main.java
    Author: Ryan French
    Created: 19-Oct-2016
    Version: 1.0
*/

public class Main extends Application
{    
    private final int HEX = 16;
    
    private Memory M;
    private InstructionSet IS;        
    private Input input_file;
    
    private Register LV;   
    private Register SP;
    private Register PC;    
    private Register CPP;
    
    private int PC;
    private int SP;
    private int LV;
    private int CPP;
    
    
    private int previous_PC;
                   
    private Label[] stack_label_array;
    private Label[] frame_label_array;    
    private Label[] list_label_array = new Label[100];
    private int[] mem_address_newline = new int[100];
          
    private final String[] frame_name_array = {"","i", "j", "k"};     
            
    private MenuBar menu_bar;
    
    private BorderPane main_pane;
    private GridPane grid_pane; 
    private FlowPane frame_pane;
    private ListView assembly_listview;
    private ListView constant_listview;
    private Pane stack_pane;
    
    private Button next_instruction_button;
    private Button reset_program_button;
                    
    @Override
    public void start(Stage primaryStage)
    {        
        IS = new InstructionSet();
        M = new Memory();
        input_file = new Input();                       
        
        input_file.parseFile("/main/resources/test_assembly_program_v2.txt");
        
        grid_pane = new GridPane();
        grid_pane.setHgap(15);
        grid_pane.setVgap(15);
        grid_pane.setPadding(new Insets(30));
        
        setupMenu(primaryStage);
        
        uiControlSetup();       
        bootstrapProgram();    
                
        stack_pane = new Pane();
        stack_pane.setMinSize(250, 150);

        grid_pane.add(stack_pane, 1, 1);
                        
        main_pane = new BorderPane();
        main_pane.setTop(menu_bar);
        main_pane.setCenter(grid_pane);
        
        Scene scene = new Scene(main_pane, 1000, 700);
        //scene.getStylesheets().add("style.css");
        
       // System.out.println(getClass().getResource("/main/resources/style.css"));
        
        primaryStage.setMaximized(true);
        primaryStage.setTitle("JVM Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();                
    }
    
    public static void main(String[] args)
    {
        launch(args);
    }

    private void bootstrapProgram()
    {                 
        frame_pane = new FlowPane();
        
        M.setLocalVariableFrame(1, 0); // i
        addFrameUI(1);

        M.setLocalVariableFrame(2, 2); // j
        addFrameUI(2); 

        M.setLocalVariableFrame(3, 1); // k
        addFrameUI(3); 
                        
        grid_pane.add(frame_pane, 1, 2);
        frame_pane.setOrientation(Orientation.VERTICAL);
    }
            
    private void addStackUI(String label_name)
    {              
        Label r_label = new Label(label_name);
        r_label.setAlignment(Pos.CENTER);
        r_label.setMinSize(300, 50);
        r_label.setTranslateY(100 + -50*SP.getValue());
        r_label.setStyle("-fx-background-color: aliceblue; -fx-border-color: black;");                        
        //stack_label_array[SP] = r_label;   
        stack_label_array[SP.getValue()] = r_label;  
        stack_pane.getChildren().add(r_label);
        //++SP;                                
        SP.incValue();
    }            
        
    private void removeStackUIControl()
    {        
        //--SP;                 
        SP.decValue();
        stack_pane.getChildren().remove(stack_label_array[SP.getValue()]);
    }
    
    private void uiControlSetup()
    {             
        stack_label_array = new Label[10];
        frame_label_array = new Label[10];
                
        setupUILabels();
        setupUIButtons();       
        setupConstantPoolPane();
    }
    
    private void setupUILabels()
    {                  
        assembly_listview = new ListView();
        assembly_listview.setMinSize(400, 100);
        grid_pane.add(assembly_listview, 2, 1, 1, 2);
        setupAssemblyPane();
        setupRegisters();
    }
    
    private void setupRegisters()
    {          
        LV = new Register("LV", 0);
        SP = new Register("SP", 0);
        PC = new Register("PC", 0);
        CPP = new Register("CPP", 0);
        previous_PC = 0;
                        
        ListView register_list = new ListView();
                
        register_list.getItems().addAll(PC.getLabel(), SP.getLabel(), LV.getLabel(), CPP.getLabel());
          
        grid_pane.add(register_list, 1, 3);
    }
        
    private void setupConstantPoolPane()
    {
        constant_listview = new ListView();
        constant_listview.setMinSize(200, 100);        
        grid_pane.add(constant_listview, 0, 1, 1, 2);
    }
    
    private void setupAssemblyPane()
    {                                     
        
        int count = 0;
        int row = 0;

        while(count < input_file.getProgramSize())
        {
            mem_address_newline[row] = count;            
            String row_string = (row+1) + "\t\t" + getOpcodeName(count) + "\t\t";
            int columns = getNoOfColumns(count);
                                    
            for(int i = 0; i < columns; i++)
            {                      
                row_string += (input_file.getElement(count)) + " ";                
                count++;
            }
     
            list_label_array[row] = new Label(row_string);                        
            assembly_listview.getItems().add(list_label_array[row]);
            row++;           
        }
        
    }
    
    private int getNoOfColumns(int index)
    {
        switch (input_file.getElement(index))
        {                
            case ("0x60") : 
            case ("0x64") : return 1;                             

            case ("0x36") : 
            case ("0x10") :              
            case ("0x15") : return 2;        

            case ("0x9F") : 
            case ("0xA7") : return 3;                   
        }

        return 0;
    }
    
    private String getOpcodeName(int index)
    {        
        int choice = 0;
        
        String opcode = "";
        
        switch (input_file.getElement(index))
        {                
            case ("0x60") : opcode = "IADD\t\t"; break;
            case ("0x64") : opcode = "ISUB\t\t\t"; break;       
            case ("0x36") : opcode = "ISTORE "; choice = 1; break;
            case ("0x15") : opcode = "ILOAD "; choice = 1; break;
            case ("0x10") : opcode = "BIPUSH "; choice = 2; break;
            case ("0x9F") : opcode = "IF_CMPEQ\t"; choice = 3; break;
            case ("0xA7") : opcode = "GOTO\t\t"; choice = 3; break;
        }

        switch(choice)
        {
            case (1) : opcode += frame_name_array[Integer.parseInt(input_file.getElement(index+1).substring(2,4), HEX)] + "\t\t"; break;
            case (2) : opcode += Integer.parseInt(input_file.getElement(index+1).substring(2,4), HEX) + "\t\t"; break;
        }
        
        return opcode;
    }
        
    private void highlightLabel(int index, Color c)
    {        
        int i = 0;
        while(i < 30)
        {
            if(mem_address_newline[i] == index){
                list_label_array[i].setTextFill(c);
                assembly_listview.getSelectionModel().select(i);             
                break;
            }                
            ++i;
        }
    }
                
    private void setupMenu(Stage primaryStage)
    {
        menu_bar = new MenuBar();
        menu_bar.prefWidthProperty().bind(primaryStage.widthProperty());
        Menu file_menu = new Menu("File");
        menu_bar.getMenus().add(file_menu);
        MenuItem open_file_item = new MenuItem("Open");
        
        open_file_item.setOnAction((ActionEvent event) -> 
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            input_file.parseFile(fileChooser.showOpenDialog(primaryStage));    
            setupAssemblyPane();
        });
        
        file_menu.getItems().add(open_file_item);
    }
    
    private void setupUIButtons()
    {                       
        setupNextInstructionButton();
        setupResetButton();        
        
        VBox button_box = new VBox(10, next_instruction_button, reset_program_button);
        grid_pane.add(button_box, 2, 3);
    }
        
    private void setupNextInstructionButton()
    {
        next_instruction_button = new Button("Next Instruction");  
        next_instruction_button.setMinWidth(100.0);
        next_instruction_button.setOnAction(new EventHandler<ActionEvent>()        
        {            
            @Override
            public void handle(ActionEvent event)
            {
                instructionLogic();
            }
        });                 
    }
    
    private void setupResetButton()
    {
        reset_program_button = new Button("Reset Program");
        reset_program_button.setMinWidth(100.0);   
        reset_program_button.setOnAction((ActionEvent event) -> 
        {
            highlightLabel(previous_PC, Color.BLACK);
            PC.setValue(0);
            
            int max_size = M.stack.size();
            for(int i = 0; i < max_size; ++i)
            {
                removeStackUIControl();
                M.stack.pop();
            }   
        });
    }
    
    private void addFrameUI(int index)
    {                        
        Label f_label = new Label(frame_name_array[index] + " = " + M.getLocalVariableFrame(index));
        f_label.setAlignment(Pos.CENTER);
        f_label.setStyle("-fx-background-color: darksalmon; -fx-border-color: black;");
        f_label.setMinSize(300, 50);

        frame_label_array[index] = f_label;              
              
        frame_pane.getChildren().add(f_label);        
    }
        
    private void instructionLogic()
    {       
        if(PC.getValue() < input_file.getProgramSize()-1){            
            
            highlightLabel(previous_PC, Color.BLACK);
            previous_PC = PC.getValue();
            highlightLabel(PC.getValue(), Color.BLUE);            
                        
            switch(input_file.getElement(PC.getValue()))
            {
                case ("0x10"):     BIPUSH(); break;                                       
                case ("0x15"):     ILOAD(); break;                    
                case ("0x36"):     ISTORE(); break; 
                case ("0x60"):     IADD(); break;
                case ("0x64"):     ISUB(); break;                    
                case ("0x9F"):     IF_ICMPEQ(); break;                                   
                case ("0xA7"):     GOTO(); break;
            }                                                        
        }
        
        else        
            highlightLabel(previous_PC, Color.BLACK);              
    }    
        
    private void BIPUSH()
    {   
        PC.incValue();
        int bi_integer = Integer.parseInt(input_file.getElement(PC.getValue()).substring(2,4), HEX); 
        IS.BIPUSH(bi_integer, M.stack);

        addStackUI(Integer.toString(bi_integer));
        PC.incValue();
    }
    
    private void ILOAD()
    {
        PC.incValue();
        int frame_index = Integer.parseInt(input_file.getElement(PC.getValue()).substring(2,4), HEX);            
        IS.ILOAD(M, frame_index, M.stack);
        addStackUI(frame_name_array[frame_index]);
        PC.incValue();
    }
    
    private void ISTORE()
    {
        PC.incValue();
        int frame_index = Integer.parseInt(input_file.getElement(PC.getValue()).substring(2,4), HEX);                                    
        IS.ISTORE(M, frame_index, M.stack);
        removeStackUIControl();       
        frame_label_array[frame_index].setText(frame_name_array[frame_index] + " = " + M.getLocalVariableFrame(frame_index));
        PC.incValue();
    }
    
    private void IADD()
    {
        IS.IADD(M.stack);                                   
        String label_name = stack_label_array[SP.getValue() - 1].getText();
        removeStackUIControl();
        label_name = stack_label_array[SP.getValue() - 1].getText() + " + " + label_name; 

        removeStackUIControl();
        addStackUI(label_name);                               
        PC.incValue();
    }
    
    private void ISUB()
    {
        IS.ISUB(M.stack);
        String label_name = stack_label_array[SP.getValue() - 1].getText();
        removeStackUIControl();
        label_name = stack_label_array[SP.getValue() - 1].getText() + " - " + label_name; 

        removeStackUIControl();
        addStackUI(label_name);                               
        PC.incValue();
    }
    
    private void IF_ICMPEQ()
    {
        PC.incValue();
        int offset = Integer.parseInt(input_file.getElement(PC.getValue()).substring(2,4), HEX); 
        PC.incValue();
        offset += Integer.parseInt(input_file.getElement(PC.getValue()).substring(2,4), HEX);
        PC.setValue(PC.getValue() + IS.IF_ICMPEQ(M.stack, offset));

        removeStackUIControl();
        removeStackUIControl();
    }
    
    private void GOTO()
    {
        PC.incValue();
        int offset = Integer.parseInt(input_file.getElement(PC.getValue()).substring(2,4), HEX);
        PC.incValue();
        offset += Integer.parseInt(input_file.getElement(PC.getValue()).substring(2,4), HEX);
        PC.setValue(PC.getValue() + IS.GOTO(offset));
    }
}
