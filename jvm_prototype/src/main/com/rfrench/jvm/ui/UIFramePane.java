
package main.com.rfrench.jvm.ui;

import java.util.ArrayList;
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
            
    private ArrayList<ArrayList<Label>> methods_local_frames_list;
    
    private ArrayList<Label> frame_labels;    
    
    private final String CSS_FRAME_ID = "FRAME";
    
    private int number_of_frames;
    
    public UIFramePane(Pane memory_pane)
    {
        this.java_stack_pane = memory_pane;
        
        number_of_frames = 0;
                       
        frame_labels = new ArrayList<Label>();
        
        methods_local_frames_list = new ArrayList<ArrayList<Label>>();
    }
    
    public void addFrameUI(int index, String frame_text, int current_method)
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
                
        frame_labels.add(f_label);
        
        java_stack_pane.getChildren().add(f_label);        
    }
            
    public void removeFrameUI(int index)
    {
        System.out.println("Frame to remove: " + index);
        
        java_stack_pane.getChildren().remove(index);
    }
    
    public int getNumberOfFrames()
    {
        return number_of_frames;
    }   
    
    public String getFrameName(int index)
    {
        return frame_labels.get(index).getText();        
    }
        
    public void updateFrameLabel(int index, String new_text)
    {
        frame_labels.get(index).setText(new_text);
    }
}
