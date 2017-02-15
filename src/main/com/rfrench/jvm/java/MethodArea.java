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

package main.com.rfrench.jvm.java;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/*
    Program Title: Memory.java
    Author: Ryan French
    Created: 19-Oct-2016
    Version: 1.0
*/

public class MethodArea 
{                
    private HashMap bytecode_details_map;
        
    public final static BiMap<String, Integer> ARRAY_TYPES_MAP = HashBiMap.create();
    
    private ArrayList<String> constant_pool;    
    private ArrayList<Method> methods;
    
    private Stack operand_stack;
    private Stack call_stack;
    
    private int NUMBER_OF_METHODS;                    
    private boolean hasMethods;
       
    public MethodArea()
    {
        operand_stack = new Stack();
        call_stack = new Stack();  
        
        constant_pool = new ArrayList<String>();        
        methods = new ArrayList<Method>();
        
        NUMBER_OF_METHODS = 0;
        
        bytecode_details_map = new HashMap(); 
        
        hasMethods = false;
        
        setupArrayDataTypesMap();
    }
        
    
    public MethodArea(JVMClassLoader class_loader)
    {     
        operand_stack = new Stack();        
        call_stack = new Stack();   
                        
        constant_pool = class_loader.getConstantPoolData();        
        methods = class_loader.getMethods();
        
        NUMBER_OF_METHODS = methods.size();
        
        bytecode_details_map = class_loader.getByteCodeDetails();
        
        hasMethods = true;
    }                        
    
    private void setupArrayDataTypesMap()
    {
        ArrayList<String> data_types = new ArrayList<String>();
        
        data_types.add("int");
        data_types.add("char");
        data_types.add("byte");
        data_types.add("boolean");
        data_types.add("long");
        
        for(int i = 0; i < data_types.size(); i++)
        {
            ARRAY_TYPES_MAP.put(data_types.get(i), i);
        }

    }
    
    public void setupMethodArea(JVMClassLoader class_loader)
    {
        operand_stack = new Stack();        
        call_stack = new Stack();   
                        
        constant_pool = class_loader.getConstantPoolData();        
        methods = class_loader.getMethods();
        
        NUMBER_OF_METHODS = methods.size();
        
        bytecode_details_map = class_loader.getByteCodeDetails();
        
        hasMethods = true;
    }
    
    public void setConstantPoolData(ArrayList<String> constant_pool_data)
    {
        constant_pool = constant_pool_data;
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
        Method M;

        M = methods.get(index);
                
        return M;
    }
    
    public int getNumberOfMethods()
    {
        return NUMBER_OF_METHODS;
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
    
    public HashMap getBytecodeDetails()
    {
        return bytecode_details_map;
    }
    
    public boolean hasMethods()
    {
        return hasMethods;
    }
    
}
