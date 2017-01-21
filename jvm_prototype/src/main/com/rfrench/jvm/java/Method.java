
package main.com.rfrench.jvm.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static main.com.rfrench.jvm.java.Main.FILE_PATH;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Method 
{
    private final int HEX = 16;
    
    private ArrayList<String> method_bytecode;
    private ArrayList<String> method_line_numbers;
    private ArrayList<Integer> memory_data;
    
    private int MAX_STACK_SIZE;
    private int MAX_LOCAL_VAR_SIZE;
    private int MAX_ARG_SIZE;
    private int NUMBER_OPCODES_PROGRAM;
    
    private HashMap bytecode_details_map;
    private JSONArray bytecode_details_json;
            
    public Method(HashMap bytecode_details_map, JSONArray bytecode_details_json)
    {
        method_bytecode = new ArrayList<String>();
        method_line_numbers = new ArrayList<String>();
        
        this.bytecode_details_map = bytecode_details_map;   
        this.bytecode_details_json = bytecode_details_json;
        
        MAX_STACK_SIZE = 0;
        MAX_LOCAL_VAR_SIZE = 0;
        MAX_ARG_SIZE = 0;
        NUMBER_OPCODES_PROGRAM = 0;
    }
    
    public void setupMethod()
    {
        extractMethodDet(FILE_PATH, method_bytecode, "bytecode");
        extractMethodDet(FILE_PATH, method_line_numbers, "linenumbers");
        extractMethodDetails(FILE_PATH);        
    }
        
    private void extractMethodDet(String file_name, ArrayList<String> details_array, String choice)
    {              
        Scanner sc = new Scanner(getClass().getResourceAsStream(file_name));
                                       
        String previous_word = null;
        String start_keyword = "Code:";
        String end_keyword = "LineNumberTable:";
        
        boolean start = false;
        boolean end = false;
        
        while(sc.hasNext() || !end)
        {                              
            String word = sc.next();
                                    
            if(word.equals(start_keyword))
            {
                start = true;
            }
                           
            if(word.equals(end_keyword))
            {
                end = true;
            }
                                        
            String word_upper = word.toUpperCase();                                        
                                                       
            if(bytecode_details_map.containsKey(word_upper) && start)
            {
                int bytecode_map_index = (int)bytecode_details_map.get(word_upper); 
                
                JSONObject bytecode_element = (JSONObject) bytecode_details_json.get(bytecode_map_index);
                                
                final String ATTRIBUTE = "Total Operands";
                               
                long b= (long)bytecode_element.get(ATTRIBUTE);
                
                int bytecode_operands = (int)b;
                                          
                switch (bytecode_operands) 
                {
                    case (1):
                    case (2):
                        word_upper += " " + sc.next();
                        break;
                    case (3):
                        word_upper += " " + sc.next() + " " + sc.next();
                        break;
                }
                                              
                if(choice.equals("bytecode"))
                {
                    details_array.add(word_upper);
                }
                else if(choice.equals("linenumbers"))
                {
                    details_array.add(previous_word); 
                }                                                   
            }      
            previous_word = word_upper;
        }             
                   
        NUMBER_OPCODES_PROGRAM = method_bytecode.size(); 
    }
    
    private void extractMethodDetails(String file_name)
    {
        Scanner sc = new Scanner(getClass().getResourceAsStream(file_name));
                
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
                                               
                switch (match) 
                {
                    case ("stack="):
                        MAX_STACK_SIZE = value;
                        break;

                    case ("locals="):
                        MAX_LOCAL_VAR_SIZE = value;
                        break;

                    case ("args_size="):
                        MAX_ARG_SIZE = value;
                        break;
                }                 
            }
        }
    }
    
    private void writeByteCodeToMemory()
    {                
        final int MAX_BYTE_VALUE = 255;
                
        Pattern pattern_branch = createBranchPattern();
        Matcher matcher_branch;
        
        Pattern pattern_operand = createOperandPattern();
        Matcher matcher_operand;
                
        int memory_location = 0;
                
        for(int i = 0; i < NUMBER_OPCODES_PROGRAM; i++)
        {      
            String word = method_bytecode.get(i);
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
                
                JSONObject bytecode_element = (JSONObject) bytecode_details_json.get(bytecode_map_index);
                                
                final String ATTRIBUTE = "Opcode";
                
                String opcode_text = (String)bytecode_element.get(ATTRIBUTE);                
                
                int opcode = Integer.parseInt(opcode_text, HEX);
                
                memory_data.add(opcode);
               
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

                    memory_data.add(param_1);
                    memory_data.add(param_2);
                }
                
                else if(word.equals("IINC"))
                {                    
                    int param_1 = Integer.parseInt(parameter.substring(0, parameter.indexOf(',')));
                    memory_data.add(param_1);
                    int param_2 = Integer.parseInt(parameter.substring(parameter.indexOf(' ') + 1, parameter.length()));
                    memory_data.add(param_2); 
                }                
                
                if(matcher_operand.find())
                {                   
                    int param_1 = Integer.parseInt(parameter);
                    memory_data.add(param_1);
                }
                
            } 
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
            
    public final int getStackSize()
    {
        return MAX_STACK_SIZE;
    }
    
    public final int getLocalSize()
    {
        return MAX_LOCAL_VAR_SIZE;
    }
    
    public final int getArgSize()
    {
        return MAX_ARG_SIZE;
    }
}
