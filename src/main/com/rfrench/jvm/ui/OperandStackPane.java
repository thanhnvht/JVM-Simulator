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

import java.util.Stack;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/*
    Program Title: OperandStackPane.java
    Author: Ryan French
    Created: 03-Feb-2017
    Version: 1.0
*/

public class OperandStackPane implements JVMStackPane
{    
    private Canvas operand_stack_canvas;
    private GraphicsContext gc;
    
    private Pane stack_pane;

    private final double RECT_WIDTH;
    private final double RECT_HEIGHT;
    private final double RECT_X_OFFSET;    
    private final double CANVAS_HEIGHT;
    
    private Stack operand_stack_text; 

    public OperandStackPane(Canvas operand_stack_canvas)
    {
        this.operand_stack_canvas = operand_stack_canvas;
        this.gc = this.operand_stack_canvas.getGraphicsContext2D();
     
        RECT_WIDTH = operand_stack_canvas.getWidth() * 0.8;
        RECT_HEIGHT = operand_stack_canvas.getHeight() * 0.05;
        RECT_X_OFFSET = (operand_stack_canvas.getWidth() - RECT_WIDTH) / 2;
        CANVAS_HEIGHT = operand_stack_canvas.getHeight();         
        
        operand_stack_text = new Stack();
    }

    public void push (int operand_value)
    {
        String string_value = Integer.toString(operand_value);
        
        push(string_value);
    }
    
    public void push(String operand_element_text)
    {
        int current_stack_size = operand_stack_text.size();
        
        System.out.println(operand_element_text);
        
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);     
        
        double rect_y_pos = CANVAS_HEIGHT - ((current_stack_size * RECT_HEIGHT) + 100);
            
        gc.setFill(Color.GREEN);        
        gc.fillRect(RECT_X_OFFSET, rect_y_pos, RECT_WIDTH, RECT_HEIGHT);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(RECT_X_OFFSET, rect_y_pos, RECT_WIDTH, RECT_HEIGHT);

        double text_x_pos = RECT_X_OFFSET + (RECT_WIDTH / 2);
        double text_y_pos = CANVAS_HEIGHT - ((current_stack_size * RECT_HEIGHT) + 100) + (RECT_HEIGHT / 2);
            
        gc.setStroke(Color.BLACK);  
        gc.strokeText(operand_element_text, text_x_pos, text_y_pos);

        operand_stack_text.push(operand_element_text);
    }
    
    public void pop()
    {
        operand_stack_text.pop();
        
        int current_stack_size = operand_stack_text.size();                
        
        double rect_y_pos = CANVAS_HEIGHT - ((current_stack_size * RECT_HEIGHT) + 100);
      
        gc.clearRect(RECT_X_OFFSET, rect_y_pos, RECT_WIDTH+1, RECT_HEIGHT+1);                
    }
        
    public String peek()
    {
        return (String)operand_stack_text.peek();
    }
    
    public Pane getStackPane()
    {
        return stack_pane;
    }
        
    public int getCurrentStackSize()
    {
        return operand_stack_text.size();
    }
    
    public int stackSize()
    {
        return operand_stack_text.size();
    }
}
