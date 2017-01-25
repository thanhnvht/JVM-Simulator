
package main.com.rfrench.jvm.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Label[] stack_labels;
    private int STACK_MAX_SIZE = 100;
    
    public UIStackPane(Pane mem)
    {
        this.mem = mem;
        stack_pane = new Pane();
        stack_pane.setMinSize(250, 150);
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Operand Stack in Memory");
        
        stack_labels = new Label[STACK_MAX_SIZE];        
    }
    
    public void push(String label_name, int stack_number)
    {              
        Label r_label = new Label(label_name);
        r_label.setId(CSS_STACK_ID);
        r_label.setAlignment(Pos.CENTER);
                     
        stack_labels[stack_number] = r_label;   
        stack_pane.getChildren().add(r_label);
        
        r_label.setTranslateX(350);
        //r_label.setTranslateY((-51* (2+index))+ 615);
        r_label.setTranslateY((-51 * (stack_number - 1))+ (MainScene.HEIGHT_TENTH * 4));
        
        Tooltip tool_tip = new Tooltip();
        tool_tip.setText("Stack Element: " + stack_number);        
        r_label.setTooltip(tool_tip); 
        
        mem.getChildren().add(r_label);
    } 
    
    public void pop(int index)
    {            
        stack_pane.getChildren().remove(stack_labels[index]);
        mem.getChildren().remove(stack_labels[index]);
        
    }
    
    public Pane getStackPane()
    {
        return stack_pane;
    }
    
    private void setupStackPointer()
    {
        Label label1 = new Label("Search");
        Image image = new Image(getClass().getResourceAsStream("labels.jpg"));
        label1.setGraphic(new ImageView(image));
    }
    
    public String getStackText(int index)
    {
        return stack_labels[index].getText();
    }
}
