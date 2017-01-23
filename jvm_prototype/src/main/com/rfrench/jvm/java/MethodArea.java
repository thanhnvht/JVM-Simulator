
package main.com.rfrench.jvm.java;

import java.util.ArrayList;
import java.util.Stack;

/*
    Program Title: Memory.java
    Author: Ryan French
    Created: 19-Oct-2016
    Version: 1.0
*/

public class MethodArea 
{                
    
    private ArrayList<String> constant_pool;
    
    private ArrayList<Method> methods;
    
    private Stack operand_stack;
    
   
    
    private Stack call_stack;

                    
    public MethodArea(ClassLoader class_loader)
    {     
        operand_stack = new Stack();
        
        call_stack = new Stack();   
                        
        constant_pool = class_loader.getConstantPoolData();
        
        methods = class_loader.getMethods();
    }                        
    
    public ArrayList<String> getConstantPool()
    {
        return constant_pool;
    }
    
    public int popOperandStack()
    {
        int value = (int)operand_stack.pop();
        
        return value;
    }
    
    public void pushOperandStack(int value)
    {
        operand_stack.push(value);
    }
    
    public int getOperandStackSize()
    {
        return operand_stack.size();
    }
    
    public Method getMethod(int index)
    {
        return methods.get(index);
    }
    
    public void pushCallStack(int value)
    {
        call_stack.push(value);
    }
    
    public int popCallStack()
    {
        int value = (int)call_stack.pop();
        
        return value;
    }
    
}
