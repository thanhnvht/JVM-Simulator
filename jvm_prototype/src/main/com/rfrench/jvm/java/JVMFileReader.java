
package main.com.rfrench.jvm.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/*
    Program Title: Input.java
    Author: Ryan French
    Created: 19-Oct-2016
    Version: 1.0
*/

public class JVMFileReader
{    
    private final static int HEX = 16;
    private int NUMBER_OPCODES_PROGRAM; //number of different opcode instructions in javap file
    private int MEM_PROGRAM_SIZE; //number of memory elements used by javap program
    private int max_local_variable; //maximum size of local variable frame
    private Memory M; //Memory of JVM
    private HashMap opcodes; //Helper Hashmap containing information on what actions to take when reading opcodes   
    private HashMap line_numbers; //Helper Hashmap
    private ArrayList<String> bytecode_strings; 
    private int[] opcode_memory_locations;
    private String[] program_opcodes_list;        
        
    /**
     * JVMFileReader Constructor
     * @param M 
     */
    public JVMFileReader(Memory M)
    {  
        this.M = M;            
        
        initialiseOpcodes();  
    }
    
    /**
     * Initialises Opcodes List.
     * Used to input correct bytecode into memory according to what word is
     * read from javap file. 
     * Also has information on number of parameters and amount of space in
     * memory used
     */
    private void initialiseOpcodes()
    {               
        final int OPCODE_HASHMAP_SIZE = 255;        
        
        opcodes = new HashMap(OPCODE_HASHMAP_SIZE);  
        
        opcodes.put("ICONST_0", new int[]{Integer.valueOf("3", HEX), 0, 1});
        opcodes.put("ICONST_1", new int[]{Integer.valueOf("4", HEX), 0, 1});
        opcodes.put("ICONST_2", new int[]{Integer.valueOf("5", HEX), 0, 1});
        opcodes.put("ICONST_3", new int[]{Integer.valueOf("6", HEX), 0, 1});
        opcodes.put("ICONST_4", new int[]{Integer.valueOf("7", HEX), 0, 1});
        opcodes.put("ICONST_5", new int[]{Integer.valueOf("8", HEX), 0, 1});
        opcodes.put("BIPUSH", new int[]{Integer.valueOf("10", HEX), 1, 2});
        opcodes.put("ILOAD", new int[]{Integer.valueOf("15", HEX), 1, 2});
        opcodes.put("ILOAD_0", new int[]{Integer.valueOf("1A", HEX), 0, 1});
        opcodes.put("ILOAD_1", new int[]{Integer.valueOf("1B", HEX), 0, 1});
        opcodes.put("ILOAD_2", new int[]{Integer.valueOf("1C", HEX), 0, 1});
        opcodes.put("ILOAD_3", new int[]{Integer.valueOf("1D", HEX), 0, 1});
        opcodes.put("ISTORE", new int[]{Integer.valueOf("36", HEX), 1, 2});
        opcodes.put("ISTORE_0", new int[]{Integer.valueOf("3B", HEX), 0, 1});
        opcodes.put("ISTORE_1", new int[]{Integer.valueOf("3C", HEX), 0, 1});
        opcodes.put("ISTORE_2", new int[]{Integer.valueOf("3D", HEX), 0, 1});
        opcodes.put("ISTORE_3", new int[]{Integer.valueOf("3E", HEX), 0, 1});
        opcodes.put("IADD", new int[]{Integer.valueOf("60", HEX), 0, 1});
        opcodes.put("ISUB", new int[]{Integer.valueOf("64", HEX), 0, 1});
        opcodes.put("DUP", new int[]{Integer.valueOf("59", HEX), 0, 1});
        opcodes.put("IAND", new int[]{Integer.valueOf("7E", HEX), 0, 1});
        opcodes.put("IFEQ", new int[]{Integer.valueOf("99", HEX), 2, 3});
        opcodes.put("IFLT", new int[]{Integer.valueOf("9B", HEX), 2, 3});
        opcodes.put("IINC", new int[]{Integer.valueOf("84", HEX), 3, 3});
        opcodes.put("LDC_W", new int[]{Integer.valueOf("13", HEX), 2, 3});
        opcodes.put("NOP", new int[]{Integer.valueOf("0", HEX), 0, 1});
        opcodes.put("POP", new int[]{Integer.valueOf("57", HEX), 0, 1});
        opcodes.put("SWAP", new int[]{Integer.valueOf("5F", HEX), 0, 1});
        opcodes.put("IF_ICMPGQ", new int[]{159, 2, 3});
        opcodes.put("IF_ICMPGE", new int[]{Integer.valueOf("A2", HEX), 2, 3});
        opcodes.put("IF_ICMPNE", new int[]{Integer.valueOf("A0", HEX), 2, 3});
        opcodes.put("IF_ICMPEQ", new int[]{Integer.valueOf("9F", HEX), 2, 3});
        opcodes.put("GOTO", new int[]{Integer.valueOf("A7", HEX), 2, 3});
        opcodes.put("WIDE", new int[]{Integer.valueOf("C4", HEX), 5, 5});
        opcodes.put("RETURN", new int[] {Integer.valueOf("B1", HEX), 0, 1});
        
        opcodes.put("ALOAD_0", new int[] {Integer.valueOf("2A", HEX), 0, 1});
    }                  
          
    /**
     * Create HashMap containing the starting line number in
     * the javap program of each java bytecode in the program
     */
    private void initialiseLineNumbers()
    {
        line_numbers = new HashMap(NUMBER_OPCODES_PROGRAM);
        
        int index = 0;
        for(int i = 0; i < NUMBER_OPCODES_PROGRAM; i++)
        {
            String line_number = bytecode_strings.get(index);
            line_number = line_number.substring(0, line_number.indexOf(':'));            
            line_numbers.put(line_number, i);
            index += 2;
        }
    }
    
    /**
     * Read a javap file. Retrieve relevant data and put into 
     * helper data structures to then be used later in program
     * @param file_name name/location of file to read
     */
    public void readFile(String file_name)
    {                     
        createByteCodeDisplayArray(file_name);
        assembleOpcodeProgramArray();
        writeOpcodeMemoryLocations();
        writeByteCodeToMemory();       
        initialiseLineNumbers();
        findMaxLocalVariable();
    }       
    
    /**
     * Parse bytecode line by line
     * @param file_name 
     */
    private void createByteCodeDisplayArray(String file_name)
    {                                
        bytecode_strings = new ArrayList<String>();
        
        Scanner sc = new Scanner(getClass().getResourceAsStream(file_name));
                        
        while(sc.hasNext())
        {          
            String line_number = sc.next();
            
            if(line_number.matches(".*\\d+.*"))
            {                
                String word = sc.next().toUpperCase();
                
                int[]opcode_values = (int[])opcodes.get(word);            
                                                
                switch(opcode_values[1])
                {
                    case (1) : 
                    case (2) : word += " " + sc.next(); break;                        
                    case (3) : word += " " + sc.next() + " " + sc.next(); break;                               
                }          
                
                bytecode_strings.add(line_number);
                bytecode_strings.add(word);
            }                                      
        }     
        
        NUMBER_OPCODES_PROGRAM = bytecode_strings.size() / 2;
    }
    
    /**
     * Create array containing only the opcode names used in the .class file    
     */
    private void assembleOpcodeProgramArray()
    {
        program_opcodes_list = new String[NUMBER_OPCODES_PROGRAM];
        
        int index = 1;
        
        for(int i = 0; i < NUMBER_OPCODES_PROGRAM; i++)
        {
            program_opcodes_list[i] = bytecode_strings.get(index);
            index += 2;
        }
    }
        
    private void writeOpcodeMemoryLocations()
    {
        opcode_memory_locations = new int[NUMBER_OPCODES_PROGRAM];
        
        int memory_location = 0;
        
        for(int i = 0; i < NUMBER_OPCODES_PROGRAM; i++)
        {
            String word = program_opcodes_list[i];
            
            if(word.indexOf(' ') != -1)            
               word = word.substring(0, word.indexOf(' '));                           
                        
            
            if(opcodes.containsKey(word))
            {
                opcode_memory_locations[i] = memory_location;
                int[] opcode_meta_data = (int[])opcodes.get(word);
                                               
                memory_location += opcode_meta_data[2];
            }                                                            
        }                        
    }       
    
    /**
     * Write bytecode opcode and parameters into Memory method area
     */
    private void writeByteCodeToMemory()
    {                                                                      
        int memory_location = 0;
  
        for(int i = 0; i < NUMBER_OPCODES_PROGRAM; i++)
        {
            String word = program_opcodes_list[i];         
            String parameter = "";
            
            if(word.indexOf(' ') != -1)            
            {
               parameter = word.substring(word.indexOf(' ') + 1, word.length());
               word = word.substring(0, word.indexOf(' '));                              
            }

            if(opcodes.containsKey(word))
            {
                int[] opcodes_meta_data = (int[])opcodes.get(word);
                
                M.setMemoryAddress(memory_location, opcodes_meta_data[0]);
                memory_location++;
                
                int[] return_values = getValuesFromOpcode(opcodes_meta_data, parameter);               
                
                for(int j = 0; j < return_values.length; j++)
                {
                    M.setMemoryAddress(memory_location, return_values[j]);                 
                    memory_location++;
                }
            }
                        
           MEM_PROGRAM_SIZE = memory_location; 
        }           
    }
            
    /**
     * WriteBytecodeToMemory helper method. Decipher how to action
     * a opcode based on what it is
     * @param opcodes_meta_data
     * @param parameter
     * @return 
     */
    private int[] getValuesFromOpcode(int[] opcodes_meta_data, String parameter)
    {               
        //CHANGE SO ONLY ONE RETURN            
        
        switch(opcodes_meta_data[1])
        {
            
            case(0) :   return new int[]{};
            
            case(1) :   int p = Integer.parseInt(parameter);
                        return new int[]{p};
                                
            case(2) :   int offset = Integer.parseInt(parameter);
            
                        if(offset > 255)
                            return new int[]{offset - 255, 255};
                        else
                            return new int[]{0, offset};                                     
                
            case(3) :   int a = Integer.parseInt(parameter.substring(0, parameter.indexOf(',')));
                        int b = Integer.parseInt(parameter.substring(parameter.indexOf(' ') + 1, parameter.length()));
                        return new int[]{a, b};                             
        }
            
        return new int[]{};
    }
    
    private void findMaxLocalVariable()
    {
        for(int i = 0; i < program_opcodes_list.length; i++)
        {
            int temp_max = 0;
            
            switch(program_opcodes_list[i])
            {
                //case("ISTORE") : temp_max =
                case("ISTORE_1") : temp_max = 1; break;
                case("ISTORE_2") : temp_max = 2; break;
                case("ISTORE_3") : temp_max = 3; break;
            }
            
            if(temp_max > max_local_variable)
                max_local_variable = temp_max;
        }
    }
    
    public int getMaxLocalVariable()
    {
        return max_local_variable;
    }
    
    public String getElement(int index)
    {
        return bytecode_strings.get(index);
    }
    
    public ArrayList getProgram()
    {
        return bytecode_strings;
    }
    
    public int getProgramSize()
    {
        return MEM_PROGRAM_SIZE;
    }
    
    public void printProgramMachineCode()
    {
        for(int i = 0; i < MEM_PROGRAM_SIZE; i++)        
            System.out.println(M.getMemoryAddress(i));        
    }
    
    public void printProgramBytecode()
    {
        for(int i = 0; i < NUMBER_OPCODES_PROGRAM; i++)
            System.out.println(program_opcodes_list[i]);
    }
    
    public int getOpcodeMemoryLocation(int address)
    {
        return opcode_memory_locations[address];
    }
        
    public String getOpcodeProgram(int address)
    {
        return program_opcodes_list[address];
    }
    
    public int getNoOpcodes()
    {        
        return NUMBER_OPCODES_PROGRAM;
    }
    
    public HashMap getOpcodeMetaData()
    {
        return opcodes;
    }
    
    public HashMap getLineNumbers()
    {
        return line_numbers;
    }
}
