
package main.com.rfrench.jvm.java;

/*
    Program Title: Memory.java
    Author: Ryan French
    Created: 19-Oct-2016
    Version: 1.0
*/

/*
    TODO : Use one memory instead of several. Change rest of program to 
           point to correct locations in this memory. This way real use of 
           registers.

           Method Area 0-999
           Constant Pool 1000 - 1999
           Local Variable 2000+
           Operatnd Stack 2000+
*/

public class Memory 
{            
    private final int HEX = 16;
    private final int MEM_SIZE = 3000;
    
    private final Register LV;
    private int LV_size;
    
    private int[] memory;
            
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
    
    public void setMem(int address, int value)
    {
        memory[address] = value;
    }        
        
    public void setMethodMemory(String[] text_data)
    {
        for(int i = 0; i < text_data.length; i++)
        {
            String element = text_data[i].substring(2, 4);            
            memory[i] = Integer.parseInt(element, HEX);
        }
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
