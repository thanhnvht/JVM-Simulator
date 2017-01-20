
package main.com.rfrench.jvm.java;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    Program Title: Input.java
    Author: Ryan French
    Created: 19-Oct-2016
    Version: 1.0
*/

public class ClassLoader
{    
    private final static int HEX = 16; //Hex Base
    
    private int NUMBER_OPCODES_PROGRAM; //number of different opcode instructions in javap file
    private int NUMBER_OF_METHODS;
    private int MEM_PROGRAM_SIZE; //number of memory elements used by javap program
    private int max_local_variable; //maximum size of local variable frame
    
    private Memory M; //Memory of JVM
    private HashMap opcodes; //Helper Hashmap containing information on what actions to take when reading opcodes   
    private HashMap line_numbers; //Helper Hashmap
        
    private ArrayList<String> bytecode_strings;     
    
    private ArrayList<ArrayList<String>> method_bytecodes;
    
    private ArrayList<ArrayList<String>> opcodes_list;
    
    private int[] method_start_address_array;
        
    private List<int[]> method_details_list;
    
    private int[] opcode_memory_locations;
    private String[] program_opcodes_list;        
        
    /**
     * JVMFileReader Constructor
     * @param M 
     */
    public ClassLoader(Memory M)
    {  
        this.M = M;            
        
        initialiseOpcodes();  

        method_bytecodes = new ArrayList<ArrayList<String>>();
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

    //DEBUGGING METHOD
    public void readFileTest(String file_name)
    {                     
        extractNumberOfMethods(file_name);
        
        if(method_details_list.size() > 0)
            extractMethodDetails(file_name);
        
        createByteCodeDisplayArrayTest(file_name);
        assembleOpcodeProgramArrayTest();
        writeOpcodeMemoryLocationsTest();
        writeByteCodeToMemoryTest();       
        initialiseLineNumbers();
    }   
    
    
    private void extractNumberOfMethods(String file_name)
    {
        Scanner sc = new Scanner(getClass().getResourceAsStream(file_name));
        
        int method_count = 0;
        
        method_details_list = new ArrayList<int[]>();
                      
        while(sc.hasNext())
        {
            String word = sc.next();
            
            if(word.equals("Code:"))
            {
                method_count++;
                method_details_list.add(new int[]{0, 0, 0});
            }            
        }                

        NUMBER_OF_METHODS = method_count;
        
//        if(method_count == 0)
//        {
//            throw new InvalidJavaPFileException("No Code keyword");
//        }
    }
    
    private void extractMethodDetails(String file_name)
    {
        Scanner sc = new Scanner(getClass().getResourceAsStream(file_name));
        
        int method_index = 0;
        
        List<String> keywords = new ArrayList<String>();
        
        keywords.add("stack=");
        keywords.add("locals=");
        keywords.add("args_size=");        
        
        String patternString = "\\b(" + StringUtils.join(keywords, "|") + ")\\b";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher;
        
        while(sc.hasNext())
        {
            String word = sc.next();
            
            matcher = pattern.matcher(word); 
            
            if(matcher.find())
            {                
                String match = matcher.group(1);
                int value = Integer.parseInt(word.replaceAll("\\D+",""));
                match = match.replaceAll("\\d","");
                                               
                switch(match) // java wont allow variable strings in the cases!?!
                {
                    case("stack=") : method_details_list.get(method_index)[0] = value; 
                                     break;
                                     
                    case("locals=") : method_details_list.get(method_index)[1] = value;
                                      break;
                                      
                    case("args_size=") : method_details_list.get(method_index)[2] = value; 
                                         method_index++;
                                         break;
                }                         
            }
        }
    }
    
    
    /**
     * Parse bytecode line by line
     * @param file_name 
     */  
    private void createByteCodeDisplayArray(String file_name)
    {               
        bytecode_strings = new ArrayList<String>();
        
        final int INDEX_NUMBER_OF_PARAMETERS = 1;
        
        Scanner sc = new Scanner(getClass().getResourceAsStream(file_name));
                                       
        String previous_word = null;
        
        while(sc.hasNext())
        {               
            String word = sc.next();
                                    
            String word_upper = word.toUpperCase();                                        
                                                                       
            int[]opcode_values = (int[])opcodes.get(word_upper);
                
            if(opcode_values != null)
            {       
                switch(opcode_values[INDEX_NUMBER_OF_PARAMETERS])
                {
                    case (1) : 
                    case (2) : word_upper += " " + sc.next(); break;                        
                    case (3) : word_upper += " " + sc.next() + " " + sc.next(); break;                               
                }  
                
                bytecode_strings.add(previous_word);                
                bytecode_strings.add(word_upper);                                
            }
                
            previous_word = word_upper;
        }             
           
        
        NUMBER_OPCODES_PROGRAM = bytecode_strings.size() / 2;        
    }
    
    private void createByteCodeDisplayArrayTest(String file_name)
    {                               
       
        final int INDEX_NUMBER_OF_PARAMETERS = 1;
        
        Scanner sc = new Scanner(getClass().getResourceAsStream(file_name));
                                       
        String previous_word = null;
        
        while(sc.hasNext())
        {               
            String word = sc.next();
                         
            if(word.equals("Code:"))
            {
                                                
                if(bytecode_strings != null)
                {
                    method_bytecodes.add(bytecode_strings);
                }
                
                bytecode_strings = new ArrayList<String>();                
            }
            
            String word_upper = word.toUpperCase();                                        
                                                                       
            int[]opcode_values = (int[])opcodes.get(word_upper);
                
            if(opcode_values != null)
            {       
                switch(opcode_values[INDEX_NUMBER_OF_PARAMETERS])
                {
                    case (1) : 
                    case (2) : word_upper += " " + sc.next(); break;                        
                    case (3) : word_upper += " " + sc.next() + " " + sc.next(); break;                               
                }  
                
                bytecode_strings.add(previous_word);                
                bytecode_strings.add(word_upper);                                
            }
                
            previous_word = word_upper;
        }             
           
        method_bytecodes.add(bytecode_strings);                
        
        calculateTotalNumberofOpcodes();
    }
    
    private void calculateTotalNumberofOpcodes()
    {
        
        for(int i = 0; i < NUMBER_OF_METHODS; i++)
        {
            NUMBER_OPCODES_PROGRAM += method_bytecodes.get(i).size();
        }
        
        NUMBER_OPCODES_PROGRAM /= NUMBER_OF_METHODS;
    }
    
    
    /**
     * Write program to program_opcodes_list containing only the opcode names used in the .class file    
     * 
     */
    private void assembleOpcodeProgramArray()
    {
        System.out.println(NUMBER_OPCODES_PROGRAM);
        
        program_opcodes_list = new String[NUMBER_OPCODES_PROGRAM];
        
        int index = 1;
                
        for(int i = 0; i < NUMBER_OPCODES_PROGRAM; i++)
        {
            program_opcodes_list[i] = bytecode_strings.get(index);
            index += 2;
        }
    }
        
    private void assembleOpcodeProgramArrayTest()
    {
        opcodes_list = new ArrayList<ArrayList<String>>();
                
        for(int i = 0; i < NUMBER_OF_METHODS; i++)
        {
            ArrayList<String> program_opcodes_list_new = new ArrayList<String>();
            
            int number_opcodes_method = method_bytecodes.get(i).size() / 2;
            
            int index = 1;
            
            for(int j = 0; j < number_opcodes_method;j++)
            {
                program_opcodes_list_new.add(method_bytecodes.get(i).get(index));
                index += 2;
            }
            
            opcodes_list.add(program_opcodes_list_new);
        }        
    }
    
    private void writeOpcodeMemoryLocations()
    {
        opcode_memory_locations = new int[NUMBER_OPCODES_PROGRAM];
        
        int memory_location = 0;
        
        for(int i = 0; i < NUMBER_OPCODES_PROGRAM; i++)
        {
            String word = program_opcodes_list[i];
            
            
            if(word.indexOf(' ') != -1)      //REMOVE TRAILING CHARACTERS
            {
               word = word.substring(0, word.indexOf(' '));                           
            }

            
            if(opcodes.containsKey(word))
            {
                opcode_memory_locations[i] = memory_location;
                int[] opcode_meta_data = (int[])opcodes.get(word);
                                               
                memory_location += opcode_meta_data[2];
            }                                                            
        }                        
    }     
    
    private void writeOpcodeMemoryLocationsTest()
    {
        int memory_location = 0;
        int count = 0;
        int OPCODES_INDEX = 2;
        
        opcode_memory_locations = new int[NUMBER_OPCODES_PROGRAM];
        method_start_address_array = new int[NUMBER_OF_METHODS];
                
        for(int i = 0; i < NUMBER_OF_METHODS; i++)
        {
            int method_opcodes_total = opcodes_list.get(i).size();
            
            method_start_address_array[i] = memory_location;
            
            for(int j = 0; j < method_opcodes_total; j++)
            {
                String word = opcodes_list.get(i).get(j);
                
                if(word.indexOf(' ') != -1)            
                {
                    word = word.substring(0, word.indexOf(' '));   
                }
                
                if(opcodes.containsKey(word))
                {
                    opcode_memory_locations[count] = memory_location;
                    count++;
                    int[] opcode_meta_data = (int[])opcodes.get(word);                                               
                    memory_location += opcode_meta_data[OPCODES_INDEX];
                }
                else
                {
                    System.out.println("Invalid Opcode: " + word);
                }
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
    
    private void writeByteCodeToMemoryTest()
    {                                                                      
        int memory_location = 0;
  
        for(int i = 0; i < NUMBER_OF_METHODS; i++)
        {
            int method_opcodes_total = opcodes_list.get(i).size();
            
            for(int j = 0; j < method_opcodes_total; j++)
            {
                String word = opcodes_list.get(i).get(j);               
                
                String parameter = "";
                                
                if(word.indexOf(' ') != -1)            
                {
                    parameter = word.substring(word.indexOf(' ') + 1, word.length());
                    word = word.substring(0, word.indexOf(' '));                              
                }                
                
                //DETECT INVOKE SPECIAL
                
                if(opcodes.containsKey(word))
                {
                    int[] opcodes_meta_data = (int[])opcodes.get(word);

                    M.setMemoryAddress(memory_location, opcodes_meta_data[0]);
                    memory_location++;

                    int[] return_values = getValuesFromOpcode(opcodes_meta_data, parameter);               

                    for(int k = 0; k < return_values.length; k++)
                    {
                        M.setMemoryAddress(memory_location, return_values[k]);                 
                        memory_location++;
                    }
                }
                        
                MEM_PROGRAM_SIZE = memory_location; 
            }
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
        final int INDEX_NUMBER_OF_PARAMETERS = 1;
        final int MAX_BYTE_VALUE = 255;
        int[] parameter_values;
        
        
        switch(opcodes_meta_data[INDEX_NUMBER_OF_PARAMETERS])
        {
            
            case(0) :   parameter_values = new int[]{}; 
            
                        break;
            
            case(1) :   parameter_values = new int[]{Integer.parseInt(parameter)};                        
            
                        break;
                                
            case(2) :   int offset = Integer.parseInt(parameter);
            
                        if(offset > MAX_BYTE_VALUE)
                        {
                            parameter_values = new int[]{offset - MAX_BYTE_VALUE, MAX_BYTE_VALUE};                            
                        }
                        
                        else
                        {
                            parameter_values = new int[]{0, offset};                                                                 
                        }
                        
                        break;
                
            case(3) :   int param_1 = Integer.parseInt(parameter.substring(0, parameter.indexOf(',')));
            
                        int param_2 = Integer.parseInt(parameter.substring(parameter.indexOf(' ') + 1, parameter.length()));
                        
                        parameter_values = new int[]{param_1, param_2};     
                        
                        break;
                        
            default :   parameter_values = new int[]{}; break;
        }
            
        return parameter_values;
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
        
        //how many parameters the opcode has
        final int ZERO_PARAMETERS = 0;
        final int ONE_PARAMETER = 1;
        final int TWO_PARAMETERS = 2;
        final int THREE_PARAMETERS = 3;
        
        //last parameter in opcodes is how many memory slots needed for that bytecode
        final int WORD_SIZE_1 = 1;
        final int WORD_SIZE_2 = 2;
        final int WORD_SIZE_3 = 3;                
        
        opcodes = new HashMap(OPCODE_HASHMAP_SIZE);  
        
        opcodes.put("ICONST_0", new int[]{Integer.valueOf("3", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("ICONST_1", new int[]{Integer.valueOf("4", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("ICONST_2", new int[]{Integer.valueOf("5", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("ICONST_3", new int[]{Integer.valueOf("6", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("ICONST_4", new int[]{Integer.valueOf("7", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("ICONST_5", new int[]{Integer.valueOf("8", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("BIPUSH", new int[]{Integer.valueOf("10", HEX), ONE_PARAMETER, WORD_SIZE_2});
        opcodes.put("ILOAD", new int[]{Integer.valueOf("15", HEX), ONE_PARAMETER, WORD_SIZE_2});
        opcodes.put("ILOAD_0", new int[]{Integer.valueOf("1A", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("ILOAD_1", new int[]{Integer.valueOf("1B", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("ILOAD_2", new int[]{Integer.valueOf("1C", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("ILOAD_3", new int[]{Integer.valueOf("1D", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("ISTORE", new int[]{Integer.valueOf("36", HEX), ONE_PARAMETER, WORD_SIZE_2});
        opcodes.put("ISTORE_0", new int[]{Integer.valueOf("3B", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("ISTORE_1", new int[]{Integer.valueOf("3C", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("ISTORE_2", new int[]{Integer.valueOf("3D", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("ISTORE_3", new int[]{Integer.valueOf("3E", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("IADD", new int[]{Integer.valueOf("60", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("ISUB", new int[]{Integer.valueOf("64", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("DUP", new int[]{Integer.valueOf("59", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("IAND", new int[]{Integer.valueOf("7E", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("IFEQ", new int[]{Integer.valueOf("99", HEX), TWO_PARAMETERS, WORD_SIZE_3});
        opcodes.put("IFLT", new int[]{Integer.valueOf("9B", HEX), TWO_PARAMETERS, WORD_SIZE_3});
        opcodes.put("IINC", new int[]{Integer.valueOf("84", HEX), THREE_PARAMETERS, WORD_SIZE_3});
        opcodes.put("LDC_W", new int[]{Integer.valueOf("13", HEX), TWO_PARAMETERS, WORD_SIZE_3});
        opcodes.put("NOP", new int[]{Integer.valueOf("0", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("POP", new int[]{Integer.valueOf("57", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("SWAP", new int[]{Integer.valueOf("5F", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        opcodes.put("IF_ICMPGQ", new int[]{159, TWO_PARAMETERS, WORD_SIZE_3});
        opcodes.put("IF_ICMPGE", new int[]{Integer.valueOf("A2", HEX), TWO_PARAMETERS, WORD_SIZE_3});
        opcodes.put("IF_ICMPNE", new int[]{Integer.valueOf("A0", HEX), TWO_PARAMETERS, WORD_SIZE_3});
        opcodes.put("IF_ICMPEQ", new int[]{Integer.valueOf("9F", HEX), TWO_PARAMETERS, WORD_SIZE_3});
        opcodes.put("GOTO", new int[]{Integer.valueOf("A7", HEX), TWO_PARAMETERS, WORD_SIZE_3});
        opcodes.put("WIDE", new int[]{Integer.valueOf("C4", HEX), 5, 5});
        opcodes.put("RETURN", new int[] {Integer.valueOf("B1", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
        
        opcodes.put("ALOAD_0", new int[] {Integer.valueOf("2A", HEX), ZERO_PARAMETERS, WORD_SIZE_1});
       // opcodes.put("INVOKESPECIAL", new int[] {Integer.valueOf("B7", HEX), TWO_PARAMETERS, WORD_SIZE_3});
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
        for(int i = 0; i < NUMBER_OF_METHODS; i++)
        {
            int method_size = opcodes_list.get(i).size();
            
            System.out.println("**********");
            
            for(int j = 0; j < method_size; j++)
            {
                System.out.println(opcodes_list.get(i).get(j));
            }
        }
        
        
//        for(int i = 0; i < NUMBER_OPCODES_PROGRAM; i++)
//            System.out.println(program_opcodes_list[i]);
    }
    
    public void printAllMethodsBytecode()
    {
        final int NUMBER_OF_METHODS = method_bytecodes.size();
        
        
        for(int i = 0; i < NUMBER_OF_METHODS; i++)
        {
            int number_of_bytecodes = method_bytecodes.get(i).size() / 2;
            
            System.out.println("******");
            int count = 0;
            
            for(int j = 0; j < number_of_bytecodes; j++)
            {
                System.out.print(method_bytecodes.get(i).get(count) + " ");
                System.out.println(method_bytecodes.get(i).get(count+1));
                count += 2;
            }
            
        }
    }
    
    public void printMethodDetails()
    {
        final int NUMBER_OF_METHODS = method_details_list.size();
        final int NUMBER_OF_DETAILS_PER_METHOD = 3;
                        
        String[] method_detail_titles = new String[]{"max_stack_size", 
            "max_local_variable_size", "arg_size"};
        
        for(int i = 0; i < NUMBER_OF_METHODS; i++)
        {
            System.out.println("***********");
            
            for(int j = 0; j < NUMBER_OF_DETAILS_PER_METHOD; j++)
            {
                System.out.println(method_detail_titles[j] + ": " + method_details_list.get(i)[j]);
            }
            System.out.println("");
        }
    }
    
    public void printOpcodeMemoryLocations()
    {
        for(int i = 0; i < opcode_memory_locations.length; i++)
        {
            System.out.println(opcode_memory_locations[i]);
        }
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
