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


/**
 *
 * @author Ryan
 */
public interface JVMStackPane 
{
    /**
     * Push a value onto the Stack.
     * @param value, the value to be added to stack
     */
    public void push(String value);
    
    /**
     * Push a value onto the Stack.
     * @param value, the value to be added to stack
     */
    public void push(int value);
    
    /*
    * Remove a value from the Stack
    */
    public void pop();
    
    /**
     * View the top value on the Stack
     * @return String The top-most value to be returned
     */
    public String peek();
    
    public int stackSize();
           
}
