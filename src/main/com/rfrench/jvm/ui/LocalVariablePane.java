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

package main.com.rfrench.jvm.ui;

import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

/*
    Program Title: LocalVariablePane.java
    Author: Ryan French
    Created: 05-Feb-2016
    Version: 1.0
*/

public class LocalVariablePane 
{
    private Pane java_stack_pane;
            
    private ArrayList<ArrayList<Label>> method_local_frames_list;
    
    private ArrayList<Label> frame_labels;    
    
    private final String CSS_FRAME_ID = "FRAME";
    
    private int number_of_frames; // used to calculate where to put next frame in UI
    
    public LocalVariablePane(Pane memory_pane)
    {
        this.java_stack_pane = memory_pane;
        
        number_of_frames = 0;
                                       
        method_local_frames_list = new ArrayList<ArrayList<Label>>();
    }
    
    public void addFrameUI(String[] frame_text, int current_method_count, int max_local_var)
    {            
                   
        double PANE_HEIGHT = java_stack_pane.getHeight();
        double PANE_WIDTH  = java_stack_pane.getWidth();
        
        frame_labels = new ArrayList<Label>();
        
        for(int i = 0; i < max_local_var; i++)        
        {
            String f_text = "";
            
            if(i < frame_text.length)
            {
                f_text = frame_text[i];
            }
            
            Label f_label = new Label(f_text);
        
            f_label.setId(CSS_FRAME_ID);        
            f_label.setAlignment(Pos.CENTER);             
            f_label.setTranslateX(10);        
            f_label.setTranslateY((-51 * number_of_frames) + (MainScene.HEIGHT_TENTH * 55));

            Tooltip tool_tip = new Tooltip();

            tool_tip.setText("Local Frame Variable: " + i);        

            f_label.setTooltip(tool_tip); 

            ++number_of_frames;

            frame_labels.add(f_label);
            
            java_stack_pane.getChildren().add(f_label); 
        }
        
        method_local_frames_list.add(frame_labels);
       
    }
            
    public void removeFrameUI(int current_method_count)
    {
        ArrayList<Label> current_frame_list = method_local_frames_list.get(current_method_count);
                       
        int number_of_local_vars = method_local_frames_list.get(current_method_count).size();
        
        for(int i = 0; i < number_of_local_vars; i++)
        {
            Label frame_label = current_frame_list.get(i);
            
            java_stack_pane.getChildren().remove(frame_label);
        }
    }
    
    public int getNumberOfFrames()
    {
        return number_of_frames;
    }   
    
    public String getFrameName(int index)
    {
        return frame_labels.get(index).getText();        
    }
        
    public void updateFrameLabel(int current_method_count, int index, String new_text)
    {
        Label label = method_local_frames_list.get(current_method_count).get(index);
        
        label.setText(new_text);
    }
}
