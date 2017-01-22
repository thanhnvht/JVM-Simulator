
package main.com.rfrench.jvm.java;

import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static main.com.rfrench.jvm.java.Main.JSON_FILE_PATH;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
         
    private HashMap line_numbers; //Helper Hashmap
                
    private HashMap bytecode_details_map;
        
    private ArrayList<String> single_method_bytecodes_and_lines;                 
          
    private JSONArray bytecode_json_array;
        
    /**
     * JVMFileReader Constructor
     * @param M 
     */
    public ClassLoader(Memory M)
    {  
        this.M = M;            
        
    }
                                  
    /**
     * Read a javap file. Retrieve relevant data and put into 
     * helper data structures to then be used later in program
     * @param file_name name/location of file to read
     */
    public void readFile(String file_name)
    {          
        try
        {
            JSONArray bytecode_json = createJSONParser();
            
            createByteCodeDetailsHashMap(bytecode_json);                
            
            createByteCodeDisplayArray(file_name);            

            writeByteCodeToMemory();    
            
            createLineNumbersArray();
            
            findMaxLocalVariable();
        }
        
        catch(IOException | ParseException e)
        {
            e.printStackTrace();
        }
    }       

    //DEBUGGING METHOD
    public void readFileTest(String file_name)
    {                  
        try
        {        
            JSONArray bytecode_json = createJSONParser();
            
            createByteCodeDetailsHashMap(bytecode_json);   
            
            extractNumberOfMethods(file_name);
            
            for(int i = 0; i < NUMBER_OF_METHODS; i++)
            {
                ArrayList<String> method_details = parseMethod(file_name, i);
                
                Method m = new Method(bytecode_details_map, bytecode_json_array, method_details);
            }
            
        }
        catch(Exception e) //change to filenotfoundexception
        {
            e.printStackTrace();
        }
    }   
        
    private JSONArray createJSONParser() throws IOException, ParseException
    {                        
        JSONParser parser = new JSONParser();
            
        Object obj = parser.parse(new InputStreamReader(getClass().getResourceAsStream(JSON_FILE_PATH)));

        JSONObject jsonObject = (JSONObject) obj;
            
        bytecode_json_array = (JSONArray) jsonObject.get("bytecodes");
        
        return bytecode_json_array;
    }
    
    private void createByteCodeDetailsHashMap(JSONArray bytecode_json)
    {
        final int MAX_BYTECODES = 255;
        
        final String ATTRIBUTE = "Name"; //Name of Attribute
        
        bytecode_details_map = new HashMap(MAX_BYTECODES);                      
        
        final int JSON_MAX_SIZE = bytecode_json.size();
        
        for(int i = 0; i < JSON_MAX_SIZE; i++)
        {
            JSONObject bytecode_element = (JSONObject) bytecode_json.get(i);
            String bytecode_name = (String)bytecode_element.get(ATTRIBUTE);
            bytecode_details_map.put(bytecode_name, i);    
        }
    }    
    
    /**
     * Parse through entire javap file. Look for keyword 'Code:'. This indicates
     * the start of the method information the simulator specifically needs.
     * Extract all words from this section into an arraylist. Stop extract when
     * keyword 'LineNumberTable:' occurs. As there may be more than one method 
     * in the class, use occurences to skip previous methods found.
     * @param file_name name of javap .txt file to extract from
     * @param occurences occurences of 'Code:' before begin extract
     * @return method_details ArrayList<String> Containing method data
     */
    private ArrayList<String> parseMethod(String file_name, int occurences)
    {
        Scanner sc = new Scanner(getClass().getResourceAsStream(file_name));
        
        ArrayList<String> method_details = new ArrayList<String>();
        
        final String START_KEYWORD = "Code:";
        final String END_KEYWORD = "LineNumberTable:";
        
        int current_occurences = 0;
        
        boolean start = false;
        boolean end = false;
        
        while(sc.hasNext() && !end)
        {
            String word = sc.next();
            
            if(word.equals(START_KEYWORD))
            {                                
                if(current_occurences == occurences)
                {
                    start = true;
                }
                
                current_occurences++;
            }
            
            if(word.equals(END_KEYWORD) && start)
            {
                end = true;
            }
            
            if(start)
            {
                method_details.add(word);
            }
        }
        
        return method_details;
    }
    
    private void extractNumberOfMethods(String file_name)
    {
        Scanner sc = new Scanner(getClass().getResourceAsStream(file_name));
        
        int method_count = 0;
                              
        while(sc.hasNext())
        {
            String word = sc.next();
            
            if(word.equals("Code:"))
            {
                method_count++;
            }            
        }                

        NUMBER_OF_METHODS = method_count;        
    }
    

        
    /**
     * Parse bytecode line by line
     * @param file_name 
     */  
    private void createByteCodeDisplayArray(String file_name)
    {               
        single_method_bytecodes_and_lines = new ArrayList<String>();        
        
        Scanner sc = new Scanner(getClass().getResourceAsStream(file_name));
                                       
        String previous_word = null;
        
        while(sc.hasNext())
        {               
            String word = sc.next();
                                    
            String word_upper = word.toUpperCase();                                        
                                                       
            if(bytecode_details_map.containsKey(word_upper))
            {
                int bytecode_map_index = (int)bytecode_details_map.get(word_upper); 
                
                JSONObject bytecode_element = (JSONObject) bytecode_json_array.get(bytecode_map_index);
                                
                final String ATTRIBUTE = "Total Operands";
                               
                long b= (long)bytecode_element.get(ATTRIBUTE);
                
                int bytecode_operands = (int)b;
                                          
                switch(bytecode_operands)
                {
                    case (1) : 
                    case (2) : word_upper += " " + sc.next(); break;                        
                    case (3) : word_upper += " " + sc.next() + " " + sc.next(); break;                               
                }  
                
                single_method_bytecodes_and_lines.add(previous_word);                                
                single_method_bytecodes_and_lines.add(word_upper);                   
                
            }
                
            previous_word = word_upper;
        }             
                   
        NUMBER_OPCODES_PROGRAM = single_method_bytecodes_and_lines.size() / 2;        
    }
                              
    /**
     * Write bytecode opcode and parameters into Memory method area
     */
    private void writeByteCodeToMemory()
    {                
        final int MAX_BYTE_VALUE = 255;
                
        Pattern pattern_branch = createBranchPattern();
        Matcher matcher_branch;
        
        Pattern pattern_operand = createOperandPattern();
        Matcher matcher_operand;
                
        int memory_location = 0;
        
        int index = 1;
        
        for(int i = 0; i < NUMBER_OPCODES_PROGRAM; i++)
        {      
            String word = single_method_bytecodes_and_lines.get(index);
            String parameter = "";
                                 
            matcher_branch = pattern_branch.matcher(word);
            matcher_operand = pattern_operand.matcher(word);
            
            if(word.indexOf(' ') != -1)            
            {
               parameter = word.substring(word.indexOf(' ') + 1, word.length());
               word = word.substring(0, word.indexOf(' '));                              
            }            

            if(bytecode_details_map.containsKey(word))
            {
                int bytecode_map_index = (int)bytecode_details_map.get(word); 
                
                JSONObject bytecode_element = (JSONObject) bytecode_json_array.get(bytecode_map_index);
                                
                final String ATTRIBUTE = "Opcode";
                
                String opcode_text = (String)bytecode_element.get(ATTRIBUTE);                
                
                int opcode = Integer.parseInt(opcode_text, HEX);
                
                M.setMemoryElementValue(memory_location, opcode);
                memory_location++;
                
                if(matcher_branch.find())
                {
                    int offset = Integer.parseInt(parameter);
                    int param_1 = 0;
                    int param_2 = offset;
                
                    if(param_2 > MAX_BYTE_VALUE)
                    {                
                        param_1 = offset - MAX_BYTE_VALUE;
                        param_2 = 255;
                    }

                    M.setMemoryElementValue(memory_location, param_1);
                    memory_location++;
                    M.setMemoryElementValue(memory_location, param_2);
                    memory_location++;
                }
                
                else if(word.equals("IINC"))
                {                    
                    int param_1 = Integer.parseInt(parameter.substring(0, parameter.indexOf(',')));
                    M.setMemoryElementValue(memory_location, param_1);
                    memory_location++;
                    int param_2 = Integer.parseInt(parameter.substring(parameter.indexOf(' ') + 1, parameter.length()));
                    M.setMemoryElementValue(memory_location, param_2);
                    memory_location++;   
                }                
                
                if(matcher_operand.find())
                {                   
                    int param_1 = Integer.parseInt(parameter);
                    M.setMemoryElementValue(memory_location, param_1);
                    memory_location++;
                }
                
            } 
            
           index += 2;
           MEM_PROGRAM_SIZE = memory_location; 
        }           
    }    
        
    private Pattern createBranchPattern()
    {
        List<String> branch_keywords = new ArrayList<String>();
        
        branch_keywords.add("IFEQ");
        branch_keywords.add("IFLT");
        branch_keywords.add("IF_ICMPGQ");  
        branch_keywords.add("IF_ICMPGE");
        branch_keywords.add("IF_ICMPNE");
        branch_keywords.add("IF_ICMPEQ");
        branch_keywords.add("GOTO");        
        
        String pattern_string_branch = "\\b(" + StringUtils.join(branch_keywords, "|") + ")\\b";
        
        Pattern pattern_branch = Pattern.compile(pattern_string_branch);
        
        return pattern_branch;
        
    }
    
    private Pattern createOperandPattern()
    {
        List<String> operand_keywords = new ArrayList<String>();
        
        operand_keywords.add("BIPUSH");
        operand_keywords.add("ILOAD");
        operand_keywords.add("ISTORE");
        
        String pattern_string_operand = "\\b(" + StringUtils.join(operand_keywords, "|") + ")\\b";
        
        Pattern pattern_operand = Pattern.compile(pattern_string_operand);
        
        return pattern_operand;
    }
        
    /**
     * Create HashMap containing the starting line number in
     * the javap program of each java bytecode in the program
     */
    private void createLineNumbersArray()
    {
        line_numbers = new HashMap(NUMBER_OPCODES_PROGRAM);
        
        int index = 0;
        for(int i = 0; i < NUMBER_OPCODES_PROGRAM; i++)
        {
            String line_number = single_method_bytecodes_and_lines.get(index);
            line_number = line_number.substring(0, line_number.indexOf(':'));            
            line_numbers.put(line_number, i);
            index += 2;
        }
    }         
    
    private void findMaxLocalVariable()
    {

        max_local_variable = 1;
    }
        
    public int getMaxLocalVariable()
    {
        return max_local_variable;
    }
    
    public String getElement(int index)
    {
        return single_method_bytecodes_and_lines.get(index);
    }
    
    public ArrayList getProgram()
    {
        return single_method_bytecodes_and_lines;
    }
    
    public int getTotalMemorySpotsUsed()
    {
        return MEM_PROGRAM_SIZE;
    }
    
    public void printProgramMachineCode()
    {
        for(int i = 0; i < MEM_PROGRAM_SIZE; i++)        
            System.out.println(M.getMemoryAddress(i));        
    }
                              
    public int getNoOpcodes()
    {        
        return NUMBER_OPCODES_PROGRAM;
    }
    
    public HashMap getLineNumbers()
    {
        return line_numbers;
    }
    
    public HashMap getByteCodeDetails()
    {
        return bytecode_details_map;
    }
}
