/*
 * The MIT License
 *
 * Copyright 2017 Ryan.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package main.com.rfrench.jvm.ui;

import java.util.Stack;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.TextAlignment;

/**
 * Controls the Operand Stack GUI Component
 * Values can be pushed, popped, or peeked
 * @author Ryan
 */
public class JVMOperandStackPane implements JVMStackPane
{
    
    private Canvas operandStackCanvas; //Canvas, Object in which objects are painted to    
    private GraphicsContext gc; //Allows painting of Objects         
    
    private Stack operandStack;
       
    /**
     * Create a Pane for the OperandStack
     * @param operandStackCanvas Canvas Object to paint objects to
     */
    public JVMOperandStackPane(Canvas operandStackCanvas)
    {
        this.operandStackCanvas = operandStackCanvas;
        
        this.gc = this.operandStackCanvas.getGraphicsContext2D();                               
        
        operandStack = new Stack();
    }
    
    /**
     * Push a value onto the Stack.
     * @param value, the value to be added to stack
     */
    @Override
    public void push(int value)
    {
        String string_value = Integer.toString(value);
        
        push(string_value);
    }
    
    /**
     * Push a value onto the Stack.
     * @param value, the value to be added to stack
     */
    @Override
    public void push(String value)
    {
        double stackObjectWidth = operandStackCanvas.getWidth() * 0.8;
        double stackObjectHeight = operandStackCanvas.getHeight() * 0.05;
        
        OperandStackObject newStackElement = new OperandStackObject(value, stackObjectWidth, stackObjectHeight);
        
        operandStack.push(newStackElement);
                        
        drawObject(newStackElement);        
    }
       
    /**
     * Draw New Stack Element to GUI
     * @param stackObject the Stack Element to Draw
     */
    private void drawObject(OperandStackObject stackObject)
    {
        int stackSize = operandStack.size();
        double canvasHeight = operandStackCanvas.getHeight(); 
        
        //Calculate start draw positions for stack element shape
        double[] shapePos = calculateShapePos(stackObject.getWidth(), stackObject.getHeight());
        
        int xIndex = 0;
        int yIndex = 1;
              
        //Calculate start draw positions for stack element texet
        double xTextPositionStart = shapePos[xIndex] + (stackObject.getWidth() / 2);
        double yTextPositionStart = shapePos[yIndex] + (stackObject.getHeight() / 2);
                                        
        //Draw object shape
        gc.setFill(stackObject.getFillColour());
        gc.fillRect(shapePos[xIndex], shapePos[yIndex], stackObject.getWidth(), stackObject.getHeight());
        gc.setStroke(stackObject.getLineColour());
        gc.strokeRect(shapePos[xIndex], shapePos[yIndex], stackObject.getWidth(), stackObject.getHeight());
        
        //Draw text value
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.strokeText(stackObject.getValue(), xTextPositionStart, yTextPositionStart);                
    }
    
    /**
     * Remove top-most Stack Object from GUI
     */
    private void clearObject()
    {
        OperandStackObject stackObject = (OperandStackObject)operandStack.peek();
        
        double[] shapePos = calculateShapePos(stackObject.getWidth(), stackObject.getHeight());
        
        int xIndex = 0;
        int yIndex = 1;
                        
        //Plus 1 to getHeight to clear the line as well as the shape
        gc.clearRect(shapePos[xIndex], shapePos[yIndex], stackObject.getWidth() + 1, stackObject.getHeight() + 1);
    }
    
    private double[] calculateShapePos(double objectWidth, double objectHeight)
    {
        int stackSize = operandStack.size();
        double canvasHeight = operandStackCanvas.getHeight();
        
        double xShapePositionStart = (operandStackCanvas.getWidth() - objectWidth) / 2;
        double yShapePositionStart = canvasHeight - objectHeight * stackSize - (canvasHeight * 0.05);
        
        double[] shapePos = {xShapePositionStart, yShapePositionStart};
        
        return shapePos;
    }
    
    
    /*
    * Remove a value from the Stack
    */
    @Override
    public void pop()
    {
        clearObject();
        
        operandStack.pop();
    }
    
    /**
     * View the top value on the Stack
     * @return String The top-most value to be returned
     */
    @Override
    public String peek()
    {
        //change to 'instance of' with conditional
        
        /*
        if (obj instanceof C) {
            //your code
        }
        */
        
        OperandStackObject stackObject = (OperandStackObject)operandStack.peek();                
        
        return stackObject.getValue();
    }

    /**
     * Return how many objects are currently in the stack
     * @return int number of objects in stack
     */
    @Override
    public int stackSize() 
    {
        return operandStack.size();
    }
}
