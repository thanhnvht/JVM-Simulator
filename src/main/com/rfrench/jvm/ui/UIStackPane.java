
package main.com.rfrench.jvm.ui;

import java.util.Stack;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

/*
    Program Title: UIStackPane.java
    Author: Ryan French
    Created: 07-Nov-2016
    Version: 1.0
*/

public class UIStackPane 
{
    private final String CSS_STACK_ID = "STACK";
    private Pane mem;
    private Pane stack_pane;

    
    private Stack operand_stack_labels; 
    
    public UIStackPane(Pane mem)
    {
        this.mem = mem;
        stack_pane = new Pane();
        stack_pane.setMinSize(250, 150);
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
                     
        stack_pane.getChildren().add(operand_stack_label);
        
        operand_stack_labels.push(operand_stack_label);
        
        operand_stack_label.setTranslateX(350);

        operand_stack_label.setTranslateY((-51 * (current_stack_size))+ (MainScene.HEIGHT_TENTH * 37));
        
        Tooltip tool_tip = new Tooltip();
        tool_tip.setText("Operand Stack element: " + current_stack_size);        
        operand_stack_label.setTooltip(tool_tip); 
        
        mem.getChildren().add(operand_stack_label);
    } 
    
    public void pop()
    {        
        Label operand_stack_label = (Label) operand_stack_labels.pop();
        
        mem.getChildren().remove(operand_stack_label);                        
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
