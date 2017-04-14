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

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


public class OperandStackObject implements StackObject
{
    
    private final Paint FILL_COLOUR;
    private final Paint LINE_COLOUR;
    
    private final double WIDTH;
    private final double HEIGHT;
    private String value;
    
    /**
     * Create an Operand Stack Object. A visual element for the GUI
     * @param value the String to be viewed by the user.
     * @param width width of object
     * @param height height of object
     */
    public OperandStackObject(String value, int width, int height)
    {
        this(value, (double)width, (double) height);
    }
    
    /**
     * Create an Operand Stack Object. A visual element for the GUI
     * @param value the String to be viewed by the user.
     * @param width width of object
     * @param height height of object
     */
    public OperandStackObject(String value, double width, double height)
    {        
        WIDTH = width;
        HEIGHT = height;
        this.value = value;
        FILL_COLOUR = Color.GREEN;
        LINE_COLOUR = Color.BLACK;
    }

    /**
     * Get the fill colour of the object
     * @return Paint FILL_COLOUR
     */
    @Override
    public Paint getFillColour() 
    {
        return FILL_COLOUR;
    }
    
    /**
     * Get the stroke colour of the object
     * @return Paint STROKE_COLOUR
     */
    @Override
    public Paint getLineColour() 
    {
        return LINE_COLOUR;
    }
    
    /**
     * Height of the Object
     * @return double height
     */
    @Override
    public double getHeight() 
    {
        return HEIGHT;
    }

    /**
     * Current value of the Object
     * @return String value
     */
    @Override
    public String getValue() 
    {
        return value;
    }
    
    /**
     * Change the value of the Object
     * @param value value to change to
     */
    @Override
    public void setValue(String value) 
    {
        this.value = value;
    }

    /**
     * Width of the Object
     * @return double width
     */
    @Override
    public double getWidth() 
    {
        return WIDTH;
    }
    
}
