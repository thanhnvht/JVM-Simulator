
package main.com.rfrench.jvm.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/*
    Program Title: UIButtonPane.java
    Author: Ryan French
    Created: 07-Nov-2016
    Version: 1.0
*/

public class UIButtonPane 
{
    private VBox button_vbox;
    
    private Button next_instruction_button;
    private Button reset_program_button;
    private Button open_file_button;
    private Button run_program;
    private Button pause_program;
    
    private final int BUTTON_WIDTH = 100;
    
    public UIButtonPane()
    {       
        next_instruction_button = new Button("Next Instruction");  
        next_instruction_button.setMinWidth(BUTTON_WIDTH);
        reset_program_button = new Button("Reset Program");
        reset_program_button.setMinWidth(BUTTON_WIDTH); 
        open_file_button = new Button("Open File");
        open_file_button.setMinWidth(BUTTON_WIDTH);
        
        run_program = new Button("Run Program");
        pause_program = new Button("Pause Program");
        
                
        //button_vbox = new VBox(10, next_instruction_button, reset_program_button, open_file_button);
        button_vbox = new VBox(10, next_instruction_button, run_program, pause_program);
        button_vbox.setMinWidth(MainScene.WIDTH_TENTH * 30);
        button_vbox.setMinHeight(MainScene.HEIGHT_TENTH * 30);
    }
           
    public VBox getButtonVBox()
    {
        return button_vbox;
    }
    
    public Button openFileButton()
    {
        return open_file_button;
    }
    
    public Button getNextInstructionButton()
    {
        return next_instruction_button;
    }
    
    public Button getPlayProgramButton()
    {
        return run_program;
    }
    
    public Button getResetProgramButton()
    {
        return reset_program_button;
    }
    
    public Button getPauseProgramButton()
    {
        return pause_program;
    }
}
