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
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

/*
    Program Title: LocalVariablePane.java
    Author: Ryan French
    Created: 05-Feb-2016
    Version: 1.0
*/

public class LocalVariablePane 
{
    private Canvas local_var_canvas;
    private GraphicsContext gc;

    private final double RECT_WIDTH;
    private final double RECT_HEIGHT;
    private final double RECT_X_OFFSET;    
    private final double CANVAS_HEIGHT;
        
    private ArrayList<ArrayList<String>> method_local_frames_list;
    
    private ArrayList<Paint> frame_colours;
    
    private ArrayList<String> local_frame_text;    

    
    private int number_of_frames; // used to calculate where to put next frame in UI
    
    public LocalVariablePane(Canvas local_var_canvas)
    {
        this.local_var_canvas = local_var_canvas;
        this.gc = this.local_var_canvas.getGraphicsContext2D();
       
        RECT_WIDTH = local_var_canvas.getWidth() * 0.8;
        RECT_HEIGHT = local_var_canvas.getHeight() * 0.05;
        RECT_X_OFFSET = 75;
        CANVAS_HEIGHT = local_var_canvas.getHeight(); 
        
        number_of_frames = 0;
        
        method_local_frames_list = new ArrayList<ArrayList<String>>();
        
        setupFrameColors();
    }
    
    private void setupFrameColors()
    {
        frame_colours = new ArrayList<Paint>();
        
        frame_colours.add(Color.YELLOW);
        frame_colours.add(Color.YELLOWGREEN);
        frame_colours.add(Color.DARKGOLDENROD);
        frame_colours.add(Color.DARKORANGE);
        frame_colours.add(Color.SANDYBROWN);
        frame_colours.add(Color.CHOCOLATE);
    }
        
    public void addMethodLocalFrame(String[] frame_text, int current_method_count, int max_local_var)    
    {        
        local_frame_text = new ArrayList<String>();

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
                    
        int colour_index = current_method_count;
        
        if(current_method_count > frame_colours.size())
        {
            colour_index = current_method_count % frame_colours.size();
            
        }
        
        System.out.println("current method count: " + current_method_count);
        System.out.println("Adding stuff, colour index is: " + colour_index);
        
        Paint frame_colour = frame_colours.get(colour_index);
        
        for(int i = 0; i < max_local_var; i++)
        {           
            double rect_y_pos = CANVAS_HEIGHT - ((number_of_frames * RECT_HEIGHT) + 100);
           
            gc.setFill(frame_colour);        
            gc.fillRect(RECT_X_OFFSET, rect_y_pos, RECT_WIDTH, RECT_HEIGHT);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(RECT_X_OFFSET, rect_y_pos, RECT_WIDTH, RECT_HEIGHT);
            
            String frame_element_text = "";
            
            if(i < frame_text.length)
            {
                frame_element_text = frame_text[i];
            }
            
            double text_x_pos = RECT_X_OFFSET + (RECT_WIDTH / 2);
            double text_y_pos = CANVAS_HEIGHT - ((number_of_frames * RECT_HEIGHT) + 100) + (RECT_HEIGHT / 2);
                        
            gc.setStroke(Color.BLACK);  
            gc.strokeText(frame_element_text, text_x_pos, text_y_pos);
            
            number_of_frames++;
            
            local_frame_text.add(frame_element_text);
        }
        
        method_local_frames_list.add(local_frame_text); 
    }
                    
    public void removeFrameUI(int current_method_count)
    {                       
        int number_of_local_vars = method_local_frames_list.get(current_method_count).size();
                
        System.out.println("no: " + number_of_local_vars);
        
        for(int i = 0; i < number_of_local_vars; i++)
        {
            number_of_frames--;
            
            double rect_y_pos = CANVAS_HEIGHT - ((number_of_frames * RECT_HEIGHT) + 100);
            
            gc.clearRect(RECT_X_OFFSET, rect_y_pos, RECT_WIDTH+1, RECT_HEIGHT);
                        
        }
    }
    
    public int getNumberOfFrames()
    {
        return number_of_frames;
    }   
    
    public String getFrameName(int index)
    {
        return local_frame_text.get(index);        
    }
        
    public void updateFrameLabel(int current_method_count, int index, String new_text)
    {        

        //CAN CHANGE TO HAVE RUNNING COUNT OF ALL FRAMES - (MAX_LOCAL_VAR_CURRENT_FRAME - index)
        int number_of_elements_in_frame = 0;
        
        for(int i = 0; i < current_method_count; i++)
        {
            number_of_elements_in_frame += method_local_frames_list.get(i).size();
        }
        
        int index_frame_to_update = number_of_elements_in_frame + index;
        
        System.out.println("index: " + index_frame_to_update);
        
        double rect_y_pos = CANVAS_HEIGHT - ((index_frame_to_update * RECT_HEIGHT) + 100);
        int colour_index = current_method_count;
        
        if(current_method_count > frame_colours.size())
        {
            colour_index = current_method_count % frame_colours.size();
            
        }       
        Paint frame_colour = frame_colours.get(colour_index);
        gc.setFill(frame_colour); 
        gc.setStroke(Color.BLACK);
        gc.clearRect(RECT_X_OFFSET, rect_y_pos, RECT_WIDTH, RECT_HEIGHT);
        gc.fillRect(RECT_X_OFFSET, rect_y_pos, RECT_WIDTH, RECT_HEIGHT);
        gc.strokeRect(RECT_X_OFFSET, rect_y_pos, RECT_WIDTH, RECT_HEIGHT);
        
        double text_x_pos = RECT_X_OFFSET + (RECT_WIDTH / 2);
        double text_y_pos = CANVAS_HEIGHT - ((index_frame_to_update * RECT_HEIGHT) + 100) + (RECT_HEIGHT / 2);
        
        gc.strokeText(new_text, text_x_pos, text_y_pos);
                                      
        method_local_frames_list.get(current_method_count).set(index, new_text);
    }
}
