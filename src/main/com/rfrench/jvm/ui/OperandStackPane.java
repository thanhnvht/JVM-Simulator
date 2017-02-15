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
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

/*
    Program Title: OperandStackPane.java
    Author: Ryan French
    Created: 03-Feb-2017
    Version: 1.0
*/

public class OperandStackPane 
{
    private final String CSS_STACK_ID = "STACK";
    private Pane operand_stack_pane;
    private Pane stack_pane;

    
    private Stack operand_stack_labels; 
    
    public OperandStackPane(Pane operand_stack_pane)
    {      
        this.operand_stack_pane = operand_stack_pane;
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Operand Stack");
                      
        operand_stack_labels = new Stack();
    }
    
    public void push(String label_name)
    {     
        int current_stack_size = operand_stack_labels.size();
        
        Label operand_stack_label = new Label(label_name);
        operand_stack_label.setId(CSS_STACK_ID);
        operand_stack_label.setAlignment(Pos.CENTER);
        operand_stack_label.setTranslateY((-51 * (current_stack_size))+ (MainScene.HEIGHT_TENTH * 55));
        
        Tooltip tool_tip = new Tooltip();
        tool_tip.setText("Operand Stack element: " + current_stack_size);        
        operand_stack_label.setTooltip(tool_tip); 
               
        operand_stack_labels.push(operand_stack_label);                       
        
        operand_stack_pane.getChildren().add(operand_stack_label);
    } 
    
    public void pop()
    {        
        Label operand_stack_label = (Label) operand_stack_labels.pop();
        
        operand_stack_pane.getChildren().remove(operand_stack_label);             
    }
    
    public String peek()
    {
        Label l = (Label)operand_stack_labels.peek();
        
        String element_text = l.getText();
        
        return element_text;
    }
    
    public Pane getStackPane()
    {
        return stack_pane;
    }
        
    public String getStackText()
    {
        Label operand_stack_label = (Label) operand_stack_labels.peek();
        
        String stack_element_text = operand_stack_label.getText();
       
        return stack_element_text;
    }
    
    public int getCurrentStackSize()
    {
        return operand_stack_labels.size();
    }
}
