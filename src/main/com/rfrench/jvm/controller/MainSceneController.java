
package main.com.rfrench.jvm.controller;

import java.util.ArrayList;
import java.util.Stack;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import main.com.rfrench.jvm.ui.MainScene;

/*
    Program Title: MainSceneController.java
    Author: Ryan French
    Created: 08-Nov-2016
    Version: 1.0
*/

public class MainSceneController 
{
    private MainScene main_scene;
    
    private Stack button_presses_per_method;
    
    private int before_branch_button_press;
    
    public MainSceneController(MainScene m)
    {                
        main_scene = m;
        
        button_presses_per_method = new Stack();
        
        button_presses_per_method.push(-1);
        
        before_branch_button_press = -1;        
    }
    
    public MainScene getMainScene()
    {
        return main_scene;
    }
    
    public void hightlightLine(int current_method, int button_press_count)
    {
        ListView current_listview = main_scene.getAssembly().getCurrentListView(current_method);
        
        current_listview.getSelectionModel().select(button_press_count);                
    }
    
    
    public void changeTab(int current_method)
    {                                
        SingleSelectionModel<Tab> selection_model = main_scene.getAssembly().getTabPane().getSelectionModel();
            
        selection_model.select(current_method);    
    }
    
    public void updateRegisterLabels(int PC)
    {
        String PC_string;
        
        if(PC < 16)
            PC_string = "PC  : 0x0" + Integer.toHexString(PC).toUpperCase();
        else
            PC_string = "PC  : 0x" + Integer.toHexString(PC).toUpperCase();
        
        main_scene.getRegister().updateLabel(PC_string, 1);
    }   
    
    public Stack getButtonStack()
    {
        return button_presses_per_method;
    }    
    
    public void BIPUSH(String value)
    {
        main_scene.getStack().push(value); 
    }
    
    public void ALOAD_0(String value)
    {
        main_scene.getStack().push(value);   
        
        main_scene.getByteCodeInfoPane().addByteCodeInfo("Load a reference from Local Variable 0");
    }
    
    public void ICONST(String value)
    {
        main_scene.getStack().push(value); 
    }
    
    public void ILOAD(int frame_index)
    {
        main_scene.getStack().push(main_scene.getFrame().getFrameName(frame_index));         
    }
    
    public void IADD()
    {
        
        String label_name = main_scene.getStack().getStackText();          
        main_scene.getStack().pop();
        
        label_name = main_scene.getStack().getStackText() + " + " + label_name;              
        main_scene.getStack().pop();                              

        main_scene.getStack().push(label_name);
    }
    
    public void ISTORE(int current_method_count, int frame_index, String frame_value)
    {        
        main_scene.getStack().pop();                     
                
        main_scene.getFrame().updateFrameLabel(current_method_count, frame_index, frame_value);
        
    }
    
    public void ISUB()
    {
        String label_name = main_scene.getStack().getStackText();          
        main_scene.getStack().pop();
        
        label_name = main_scene.getStack().getStackText() + " - " + label_name;              
        main_scene.getStack().pop();                              

        main_scene.getStack().push(label_name);
    }
    
    public void IF_ICMPEQ()
    {
        main_scene.getStack().pop();

        main_scene.getStack().pop();
      
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
        
        ListView current_listview = main_scene.getAssembly().getCurrentListView(current_method);
        
        current_listview.getSelectionModel().select(before_branch_button_press);     
        
    }
    
    public void POP()
    {
        main_scene.getStack().pop();
    }
    
    public void IINC(int current_method_count, int frame_index, String frame_value)
    {        
        main_scene.getFrame().updateFrameLabel(current_method_count, frame_index, frame_value);
    }
    
    public void INVOKESPECIAL(int stack_size, int current_method, int max_local_var)
    {        
        String[] stack_text = new String[stack_size];
        
        for(int i = 0; i < stack_size; i++)
        {
            stack_text[i] = main_scene.getStack().getStackText();
        }
   
        for(int i = 0; i < stack_size; i++)
        {
            main_scene.getStack().pop();                      
        }
        
        main_scene.getFrame().addFrameUI(stack_text, current_method, max_local_var);
 
    }
    

    
    public void RETURN(int current_method)
    {
        
//        SingleSelectionModel<Tab> selection_model = main_scene.getAssembly().getTabPane().getSelectionModel();
//            
//        selection_model.select(current_method);
        
          main_scene.getFrame().removeFrameUI(current_method);
      
    }
}
