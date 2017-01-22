
package main.com.rfrench.jvm.java;

/*
    Program Title: Memory.java
    Author: Ryan French
    Created: 19-Oct-2016
    Version: 1.0
*/

public class Memory 
{                
    private final Register LV;
    private int LV_size;
    
    private int[] memory;
    
    // Frame Memory
    // Operand Stack Memory
        
            
    public Memory()
    {     
        memory = new int[3000];        
        
        LV = new Register(2000);  
        
        memory[LV.get()] = 0;
        memory[LV.get() + 1] = 0; //i
        memory[LV.get() + 2] = 0; //j
        memory[LV.get() + 3] = 0; //k      

        
        LV_size = 3;
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
}
