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

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
    Program Title: Method.java
    Author: Ryan French
    Created: 22-Jan-2017
    Version: 1.0
*/

public class Method 
{
    private final int HEX = 16;
    
    private ArrayList<String> parsed_code_data;
    private ArrayList<String> method_bytecode;
    private ArrayList<String> method_line_numbers;
    private ArrayList<Integer> method_opcode;    
    
    private String[] local_variable_frame;
        
    private final int MAX_STACK_SIZE;
    private final int MAX_LOCAL_VAR_SIZE;
    private final int MAX_ARG_SIZE;  
    
    private boolean STATIC;
    private boolean INSTANCE_METHOD;
    
    private String METHOD_NAME;
    private String METHOD_ACCESS;

            
    private HashMap bytecode_details_map;
    private JSONArray bytecode_details_json;
           
    public Method(HashMap bytecode_details_map, JSONArray bytecode_details_json, ArrayList<String> parsed_code_data, 
            ArrayList<String> method_details, int method_number)
    {        
        this.parsed_code_data = parsed_code_data;
        this.bytecode_details_map = bytecode_details_map;   
        this.bytecode_details_json = bytecode_details_json;        
        
        method_bytecode = new ArrayList<String>();
        method_line_numbers = new ArrayList<String>();  
        method_opcode = new ArrayList<Integer>();

        extractByteCode();        
        extractLineNumbers();              
        extractOpcodes();
        
        MAX_STACK_SIZE = extractMethodDetails("stack=");

        MAX_LOCAL_VAR_SIZE = extractMethodDetails("locals=");

        MAX_ARG_SIZE = extractMethodDetails("args_size=");
        
        local_variable_frame = new String[MAX_LOCAL_VAR_SIZE];
        
        METHOD_NAME = findMethodName(method_details.get(0));
        METHOD_ACCESS = method_details.get(1);
        INSTANCE_METHOD = Boolean.getBoolean(method_details.get(2));
        
        if(INSTANCE_METHOD)
        {
            local_variable_frame[0] = METHOD_NAME;
        }        
    }
        
    private String findMethodName(String full_name)
    {                
        String[] method_name_parts = full_name.split("\\.");
        
        int number_of_full_stops = method_name_parts.length;
        
        String method_name = method_name_parts[number_of_full_stops - 1];
                
        return method_name;
    }
    
    private void extractByteCode()
    {                               
        final int PARSED_DATA_SIZE = parsed_code_data.size();
        
        int count = 0;
        
        while(count < PARSED_DATA_SIZE)
        {
            String word = parsed_code_data.get(count).toUpperCase();
            
            if(bytecode_details_map.containsKey(word))
            {
                int bytecode_map_index = (int) bytecode_details_map.get(word);

                JSONObject bytecode_element = (JSONObject) bytecode_details_json.get(bytecode_map_index);

                final String ATTRIBUTE = "Total Operands";

                long b = (long) bytecode_element.get(ATTRIBUTE);

                int bytecode_operands = (int) b;
                                
                switch(bytecode_operands)
                {
                    case (0):
                        break;
                    case (1):
                    case (2):
                        count++;
                        word += " " + parsed_code_data.get(count);
                        break;
                    case (3):
                        count++;
                        word+= " " + parsed_code_data.get(count) ;
                        count++;
                        word+= " " + parsed_code_data.get(count);
                }
                
                method_bytecode.add(word);
            }
                            
            count++;
        }
    }
    
    private void extractLineNumbers()
    {
        final int PARSED_DATA_SIZE = parsed_code_data.size();
        
        String previous_word = null;
        
        for(int i = 0; i < PARSED_DATA_SIZE; i++)
        {
            String word = parsed_code_data.get(i).toUpperCase();
            
            if (bytecode_details_map.containsKey(word)) 
            {
                method_line_numbers.add(previous_word);
            }
            
            previous_word = word;
        }

    }
    
    private int extractMethodDetails(String keyword)
    {         
        final int PARSED_DATA_SIZE = parsed_code_data.size();
        
        int value = -1;
        
        boolean attribute_found = false;
        
        List<String> keywords = new ArrayList<String>();
        
        keywords.add(keyword);       
        
        String patternString = "\\b(" + StringUtils.join(keywords, "|") + ")\\b";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher;
              
        int count = 0;
        
        while(!attribute_found || count < PARSED_DATA_SIZE)
        {
            String word = parsed_code_data.get(count);
            
            matcher = pattern.matcher(word);
            
            if(matcher.find())
            {                
                value = Integer.parseInt(word.replaceAll("\\D+", ""));                  
                
                attribute_found = true;
            }
            
            count++;
        }               
        
        return value;
    }
           
    private void extractOpcodes()
    {
        final int PARSED_DATA_SIZE = parsed_code_data.size();
        
        int count = 0;
        
        while(count < PARSED_DATA_SIZE)
        {
            String word = parsed_code_data.get(count).toUpperCase();
            
            if (bytecode_details_map.containsKey(word)) 
            {
                int bytecode_map_index = (int) bytecode_details_map.get(word);

                JSONObject bytecode_element = (JSONObject) bytecode_details_json.get(bytecode_map_index);

                final String ATTRIBUTE = "Opcode";

                String bytecode = (String) bytecode_element.get(ATTRIBUTE);
               
                method_opcode.add(Integer.parseInt(bytecode, HEX));
                
                if(checkBranchOpcode(word, count))
                {
                    count++;
                }
                
                if(checkStackOpcode(word, count))
                {
                    count ++;
                }
                
                if(checkIINCOpcode(word, count))
                {
                    count += 2;
                }
                
                if(checkMethodOpcode(word, count))
                {
                    count += 2;
                }
            }
                        
            count++;
        }
    }        
          
    private boolean checkMethodOpcode(String word, int index)
    {
        boolean is_method_opcode = false;
        
        List<String> method_keywords = new ArrayList<String>();
        
        method_keywords.add("INVOKESPECIAL");
        method_keywords.add("INVOKESTATIC");
        
        String pattern_string_operand = "\\b(" + StringUtils.join(method_keywords, "|") + ")\\b";
        
        Pattern pattern = Pattern.compile(pattern_string_operand);
        
        Matcher matcher = pattern.matcher(word);        
        
        if(matcher.find())
        {            
            int operand_index = index + 1;
            
            String operand_string = parsed_code_data.get(operand_index);
            
            @SuppressWarnings("ReplaceStringBufferByString")
            StringBuilder sb = new StringBuilder(operand_string);
            
            sb.deleteCharAt(operand_string.indexOf("#"));
            
            operand_string = sb.toString();
            
            int operand = Integer.parseInt(operand_string);            
                        
            method_opcode.add(0);
            method_opcode.add(operand);
            
            is_method_opcode = true;
        }
        
        return is_method_opcode;
    }
    
    private boolean checkStackOpcode(String word, int index)
    {
        boolean stack_opcode = false;
        
        List<String> operand_keywords = new ArrayList<String>();
        
        operand_keywords.add("BIPUSH");
        operand_keywords.add("ILOAD");
        operand_keywords.add("ISTORE");
        
        String pattern_string_operand = "\\b(" + StringUtils.join(operand_keywords, "|") + ")\\b";
        
        Pattern pattern = Pattern.compile(pattern_string_operand);
        
        Matcher matcher = pattern.matcher(word);        
        
        if(matcher.find())
        {
            int operand_index = index + 1;                        
                                    
            int operand = Integer.parseInt(parsed_code_data.get(operand_index));
                        
            method_opcode.add(operand);
            
            stack_opcode = true;
        }
                
        return stack_opcode;
    }
    
    private boolean checkIINCOpcode(String word, int index)
    {
        boolean iinc_opcode = false;
        
        if(word.equals("IINC"))
        {
            int operand_index = index + 1;
                        
            String operand_string = parsed_code_data.get(operand_index);
            
            operand_string = operand_string.substring(0, operand_string.indexOf(',')); //Remove Comma from String
            
            int operand = Integer.parseInt(operand_string, HEX);
            
            method_opcode.add(operand);
            
            operand_index = index + 2;
            
            operand = Integer.parseInt(parsed_code_data.get(operand_index), HEX);
            
            method_opcode.add(operand);            
        }
                        
        return iinc_opcode;
    }
    
    private boolean checkBranchOpcode(String word, int index)
    {
        final int MAX_BYTE_VALUE = 255;
        
        boolean branch_opcode = false;
        
        List<String> branch_keywords = new ArrayList<String>();
        
        branch_keywords.add("IFEQ");
        branch_keywords.add("IFLT");
        branch_keywords.add("IF_ICMPGQ");  
        branch_keywords.add("IF_ICMPGE");
        branch_keywords.add("IF_ICMPNE");
        branch_keywords.add("IF_ICMPEQ");
        branch_keywords.add("GOTO");        
        
        String pattern_string_branch = "\\b(" + StringUtils.join(branch_keywords, "|") + ")\\b";
        
        Pattern pattern = Pattern.compile(pattern_string_branch);
        
        Matcher matcher = pattern.matcher(word);
        
        if(matcher.find())
        {
            int operand_index = index + 1;
            
            int offset = Integer.parseInt(parsed_code_data.get(operand_index));

            int param_1 = 0;          
            int param_2 = offset;

            if (param_2 > MAX_BYTE_VALUE)
            {
                param_1 = offset - MAX_BYTE_VALUE;                
                param_2 = MAX_BYTE_VALUE;
            }
            
            method_opcode.add(param_1);            
            method_opcode.add(param_2);
            
            branch_opcode = true;
        }
                
        return branch_opcode;
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
    
    public ArrayList<String> getMethodBytecode()
    {
        return method_bytecode;
    }
    
    public ArrayList<String> getMethodLineNumbers()
    {
        return method_line_numbers;
    }
    
    public ArrayList<Integer> getMethodOpcodes()
    {
        return method_opcode;
    }
    
    public int getNumberOfOpcodes()
    {
        return method_opcode.size();
    }
    
    public int getNumberOfBytecodes()
    {
        return method_bytecode.size();
    }
    
    public String getMethodName()
    {
        return METHOD_NAME;
    }
    
    public String getMethodAccess()
    {
        return METHOD_ACCESS;
    }
    
    public boolean getInstanceMethod()
    {
        return INSTANCE_METHOD;
    }
    
    public String getLocalVariable(int index)
    {
        String local_var = null;
        
        if(index > MAX_LOCAL_VAR_SIZE - 1)
        {
            System.out.println("Index out of bounds");
        }
        else
        {
           local_var = local_variable_frame[index];
        }
            
        return local_var;
    }
    
    public void setLocalVariable(int index, String value)
    {
        local_variable_frame[index] = value;
    }
    
}
