
package controller.com.rfrench.jvm.java;

import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import main.com.rfrench.jvm.java.Register;
import ui.com.rfrench.jvm.java.MainScene;

/*
    Program Title: MainSceneController.java
    Author: Ryan French
    Created: 08-Nov-2016
    Version: 1.0
*/

public class MainSceneController 
{
    private MainScene main_scene;
    private int stack_pane_size;
    
    public MainSceneController(MainScene m)
    {                
        main_scene = m;
        stack_pane_size = 0;
    }
    
    public MainScene getMainScene()
    {
        return main_scene;
    }
    
    public void updateRegisterLabels(int PC, Register SP, Register LV, Register CPP)
    {
        String PC_string;
        
        if(PC < 16)
            PC_string = "PC  : 0x0" + Integer.toHexString(PC).toUpperCase();
        else
            PC_string = "PC  : 0x" + Integer.toHexString(PC).toUpperCase();
        
        main_scene.getRegister().updateLabel(PC_string, 1);
        main_scene.getRegister().updateLabel("SP  : 0x" + Integer.toHexString(SP.get()).toUpperCase(), 2);        
        main_scene.getRegister().updateLabel("LV  : 0x" + Integer.toHexString(LV.get()).toUpperCase(), 3);
        main_scene.getRegister().updateLabel("CPP : 0x" + Integer.toHexString(CPP.get()).toUpperCase(), 4);
    }   
    
    public void removeAllStack()
    {
        for(int i = 0; i <= stack_pane_size; i++)
        {
            main_scene.getStack().pop(i);
        }
        
        stack_pane_size = 0;
    }
    
    public void BIPUSH(String value)
    {
        ++stack_pane_size;
        main_scene.getStack().push(value, stack_pane_size); 
    }
    
    public void ALOAD_0(String value)
    {
        ++stack_pane_size;
        main_scene.getStack().push(value, stack_pane_size);
    }
    
    public void ICONST(String value)
    {
        ++stack_pane_size;
        main_scene.getStack().push(value, stack_pane_size); 
    }
    
    public void ILOAD(int frame_index)
    {
        ++stack_pane_size;
        main_scene.getStack().push(main_scene.getFrame().getFrameName(frame_index), stack_pane_size); 
        
    }
    
    public void IADD()
    {
        String label_name = main_scene.getStack().getStackText(stack_pane_size);          
        main_scene.getStack().pop(stack_pane_size);
        --stack_pane_size;
        
        label_name = main_scene.getStack().getStackText(stack_pane_size) + " + " + label_name;              
        main_scene.getStack().pop(stack_pane_size);                              
        --stack_pane_size;
        
        ++stack_pane_size;
        main_scene.getStack().push(label_name, stack_pane_size);
    }
    
    public void ISTORE(int frame_index, String frame_element)
    {
        main_scene.getStack().pop(stack_pane_size);     
        --stack_pane_size;
        String new_frame_text = main_scene.getFrame().getPartialFrameName(frame_index) + " = " + frame_element;
        main_scene.getFrame().updateFrameLabel(frame_index, new_frame_text);
        
    }
    
    public void ISUB()
    {
        String label_name = main_scene.getStack().getStackText(stack_pane_size);          
        main_scene.getStack().pop(stack_pane_size);
        --stack_pane_size;
        
        label_name = main_scene.getStack().getStackText(stack_pane_size) + " - " + label_name;              
        main_scene.getStack().pop(stack_pane_size);                              
        --stack_pane_size;
        
        ++stack_pane_size;
        main_scene.getStack().push(label_name, stack_pane_size);
    }
    
    public void IF_ICMPEQ()
    {
        main_scene.getStack().pop(stack_pane_size);
        --stack_pane_size;
        main_scene.getStack().pop(stack_pane_size);
        --stack_pane_size;
    }
    
    public void POP()
    {
        main_scene.getStack().pop(stack_pane_size);
        --stack_pane_size;   
    }
    
    public void IINC(int frame_index, String frame_value)
    {
        String new_frame_text = main_scene.getFrame().getPartialFrameName(frame_index) + " = " + frame_value;
        main_scene.getFrame().updateFrameLabel(frame_index, new_frame_text);
    }
    
    public void INVOKESPECIAL(int stack_size, int current_method, int max_local_var)
    {
        for(int i = 0; i < stack_size; i++)
        {
            main_scene.getStack().pop(stack_pane_size);            
            --stack_pane_size;            
        }
        
        for(int i = 0; i < max_local_var; i++)
        {
            main_scene.getFrame().addFrameUI(i, "");
        }
        
                
        SingleSelectionModel<Tab> selection_model = main_scene.getAssembly().getTabPane().getSelectionModel();
            
        selection_model.select(current_method);
        
    }
    
    public void RETURN(int current_method, int max_local_var)
    {
        SingleSelectionModel<Tab> selection_model = main_scene.getAssembly().getTabPane().getSelectionModel();
            
        selection_model.select(current_method);
        
        for(int i = 0; i < max_local_var; i++)
        {
            main_scene.getFrame().removeFrameUI(i);
        }
        
      
    }
}
