
package ui.com.rfrench.jvm.java;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

/*
    Program Title: UIFramePane.java
    Author: Ryan French
    Created: 07-Nov-2016
    Version: 1.0
*/

public class UIFramePane 
{
    private Pane java_stack_pane;
            
    private Label[] frame_labels;
    
    private final String[] frame_names = {"","i", "j", "k"};  
    
    private final String CSS_FRAME_ID = "FRAME";
    
    private int number_of_frames;
    
    public UIFramePane(Pane memory_pane)
    {
        this.java_stack_pane = memory_pane;
        
        number_of_frames = 0;
               
        frame_labels = new Label[100];                
    }
    
    public void addFrameUI(int index, String frame_text)
    {                        
        Label f_label = new Label(frame_text);
        
        f_label.setId(CSS_FRAME_ID);
        
        f_label.setAlignment(Pos.CENTER);     
        
        f_label.setTranslateX(10);
        
        f_label.setTranslateY((-51 * number_of_frames) + (MainScene.HEIGHT_TENTH * 8));
        
        Tooltip tool_tip = new Tooltip();
        
        tool_tip.setText("Local Frame Variable: " + index);        
        
        f_label.setTooltip(tool_tip); 
        
        ++number_of_frames;
        
        frame_labels[index] = f_label;
        
        java_stack_pane.getChildren().add(f_label);        
    }
            
    public void removeFrameUI(int index)
    {
        java_stack_pane.getChildren().remove(index);
    }
    
    public int getNumberOfFrames()
    {
        return number_of_frames;
    }   
    
    public String getFrameName(int index)
    {
        return frame_labels[index].getText();
    }
    
    public String getPartialFrameName(int index)
    {
        return frame_names[index];
    }
    
    public void updateFrameLabel(int index, String new_text)
    {
        frame_labels[index].setText(new_text);
    }
}
