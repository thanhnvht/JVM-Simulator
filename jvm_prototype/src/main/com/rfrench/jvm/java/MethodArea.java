
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
    private final Register LV;
    
    private int[] memory;
    
    private ArrayList<String> constant_pool;
    
    private ArrayList<Method> methods;
    
    private Stack operand_stack;
    
    private Stack call_stack;
            
    // Frame Memory
    // Operand Stack Memory
                    
    public MethodArea(ClassLoader class_loader)
    {     
        memory = new int[3000];        
        
        operand_stack = new Stack();
        
        call_stack = new Stack();
                
        LV = new Register(2000);  
        
        memory[LV.get()] = 0;
        memory[LV.get() + 1] = 0; //i
        memory[LV.get() + 2] = 0; //j
        memory[LV.get() + 3] = 0; //k      
        
        constant_pool = class_loader.getConstantPoolData();
        
        methods = class_loader.getMethods();
    }
               
    public int getMemoryAddress(int address)
    {
        return memory[address];
    }         
          
    public void setMemoryElementValue(int address, int value)
    {
        memory[address] = value;
    }
    
    public int[] getMethodMemory()
    {
        return memory;
    }
    
    public int getMethodMemoryElement(int address)
    {
        return memory[address];        
    }
    
    public int getLocalFrameElement(Register lv, int index)
    {
        return memory[index + lv.get()];
    }
    
    public void setLocalFrameElement(Register lv, int index, int value)
    {
        memory[index + lv.get()] = value;
    }
    
    public void pushStackMem(Register SP, int value)
    {       
        SP.inc();      
        memory[SP.get()] = value;
    }
    
    public int popStackMem(Register SP)
    {                
        SP.dec();      
        
        return memory[SP.get() + 1];                
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
