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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import static main.com.rfrench.jvm.java.Main.JSON_FILE_PATH;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
    Program Title: JVMClassLoader.java
    Author: Ryan French
    Created: 19-Oct-2016
    Version: 1.0
*/

public class JVMClassLoader
{    
       
    private int NUMBER_OF_METHODS;
                             
    private HashMap bytecode_details_map;              
          
    private JSONArray bytecode_json_array;
    
    private ArrayList<Method> methods;    
    private ArrayList<String> constant_pool_data;    
    
    private String FILE_PATH;    
    private final String TEMP_FOLDER_FILE_PATH;
       
    public JVMClassLoader()
    {
        TEMP_FOLDER_FILE_PATH = new File("src/main/com/rfrench/jvm/resources/temp/").getAbsolutePath();
    }
    
    /**
     * JVMFileReader Constructor
     * @param FILE_PATH
    */
    public JVMClassLoader(String FILE_PATH)
    {          
        this.FILE_PATH = FILE_PATH;

        TEMP_FOLDER_FILE_PATH = new File("src/main/com/rfrench/jvm/resources/temp/").getAbsolutePath();
    }
          

    
    /**
     * Read a javap file. Retrieve relevant data and put into 
     * helper data structures to then be used later in program
     */
    public void readFile()
    {          
        try
        {
            JSONArray bytecode_json = createJSONParser();           
            createByteCodeDetailsHashMap(bytecode_json);                            
            extractNumberOfMethods();
                                    
            methods = new ArrayList<Method>();
            
            ArrayList<String> method_names_list = findMethodNames();            
            ArrayList<String> method_access_type_list = findMethodAccess();            
            ArrayList<Boolean> is_method_instance_list = checkInstanceMethod(method_names_list);                        
            
            addConstantPoolMethodReferences(method_names_list);
            
            for(int i = 0; i < NUMBER_OF_METHODS; i++)
            {
                writeMethodDetailsJSON("/method_" + i + ".json", method_names_list.get(i), method_access_type_list.get(i), is_method_instance_list.get(i));
            }
            
            for(int i = 0; i < NUMBER_OF_METHODS; i++)
            {
                ArrayList<String> method_code = parseMethodCode(i);                                                                
                Method m = new Method(bytecode_details_map, bytecode_json_array, method_code, i);                                                             
                methods.add(m);
            }
        }
        
        catch(IOException | ParseException e)
        {
            e.printStackTrace();
        }
    }       
       
    
    public void readFile(String file_path)
    {
        try
        {
            this.FILE_PATH = file_path;
            
            JSONArray bytecode_json = createJSONParser();           
            createByteCodeDetailsHashMap(bytecode_json);                            
            extractNumberOfMethods();
                                    
            methods = new ArrayList<Method>();
            
            ArrayList<String> method_names_list = findMethodNames();            
            ArrayList<String> method_access_type_list = findMethodAccess();            
            ArrayList<Boolean> is_method_instance_list = checkInstanceMethod(method_names_list);                        
            
            addConstantPoolMethodReferences(method_names_list);
            
            for(int i = 0; i < NUMBER_OF_METHODS; i++)
            {
                writeMethodDetailsJSON("/method_" + i + ".json", method_names_list.get(i), method_access_type_list.get(i), is_method_instance_list.get(i));
            }

            for(int i = 0; i < NUMBER_OF_METHODS; i++)
            {
                ArrayList<String> method_code = parseMethodCode(i);                                                                
                Method m = new Method(bytecode_details_map, bytecode_json_array, method_code, i);                                                             
                methods.add(m);
            }
        }
        
        catch(IOException | ParseException e)
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
    
    private void writeMethodDetailsJSON(String file_name, String method_name, String method_access, boolean is_instance_method)
    {
        try
        {
            JSONObject obj = new JSONObject();
            
            obj.put("Name", method_name);           
            obj.put("Access", method_access);
            obj.put("Instance Method", is_instance_method);
            
            FileWriter file = new FileWriter(TEMP_FOLDER_FILE_PATH + file_name);
            
            file.write(obj.toJSONString());
            file.flush();
            file.close();
        }
        
        catch(IOException e)
        {
            System.out.println("JSON WRITE ERROR");
            e.printStackTrace();
        }
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
    
    private void addConstantPoolMethodReferences(ArrayList<String> method_names_list)
    {
        constant_pool_data = new ArrayList<String>();
        
        for(int i = 0; i < method_names_list.size(); i++)
        {
            if(i != 0)
            {
                constant_pool_data.add(method_names_list.get(i));
            }            
        }
    }        
    
    private String findFQN() throws FileNotFoundException
    {
        //Scanner sc = new Scanner(getClass().getResourceAsStream(FILE_PATH));
        Scanner sc = new Scanner(new File(FILE_PATH));
        
        String first_keyword = "public";
        String second_keyword = "class";
        
        String FQN = null;
        
        boolean FQN_found = false;
                        
        while(sc.hasNext() && !FQN_found)
        {
            String word = sc.next();
            
            if(word.equals(first_keyword))
            {
                word = sc.next();
                
                if(word.equals(second_keyword))
                {
                    FQN = sc.next();
                    FQN_found = true;
                }
            }
        }
        
        return FQN;
    }
    
    private ArrayList<Boolean> checkInstanceMethod(ArrayList<String> method_names) throws FileNotFoundException
    {
        ArrayList<Boolean> is_instance_method = new ArrayList<Boolean>();
        
        String FQN = findFQN();
        
        for(int i = 0; i < NUMBER_OF_METHODS; i++)
        {
            String method_name = method_names.get(i);
            
            if(method_name.contains(FQN))
            {
                is_instance_method.add(true);
            }
            else
            {
                is_instance_method.add(false);
            }
        }
                
        return is_instance_method;
    }
    
    private ArrayList<String> findMethodNames() throws FileNotFoundException
    {
        //Scanner sc = new Scanner(getClass().getResourceAsStream(FILE_PATH));
        Scanner sc = new Scanner(new File(FILE_PATH));
        
        ArrayList<String> method_names = new ArrayList<String>();
        
        String first_keyword = "public";
        String second_keyword = "private";
        String remove_keyword = "class";
        
        //DOES NOT WORK WITH OBJECTS THAT HAVE FIELDS !!!!!!!!!!!!!!!!!!!!!
        
        while(sc.hasNext())
        {
            String word = sc.next();
            
            if(word.equals(first_keyword) || word.equals(second_keyword))
            {    
                String method_name = word + sc.nextLine();
                
                if(!method_name.contains(remove_keyword)) // Ensure no class names are in list
                {      
                    @SuppressWarnings("ReplaceStringBufferByString")
                    StringBuilder sb = new StringBuilder(method_name);
                    
                    sb.deleteCharAt(method_name.indexOf(";"));
                    
                    method_name = sb.toString();
                    
                    String[] method_parts = method_name.split(" ");
                                        
                    int method_name_index = method_parts.length - 1;
                    
                    method_name = method_parts[method_name_index];
                    
                    method_names.add(method_name);
                }
                
            }
            else
            {
                sc.nextLine();
            }
        }
                
        return method_names;
    }
    
    private ArrayList<String> findMethodAccess() throws FileNotFoundException
    {
        //Scanner sc = new Scanner(getClass().getResourceAsStream(FILE_PATH));
        Scanner sc = new Scanner(new File(FILE_PATH));
        
        ArrayList<String> method_flags = new ArrayList<String>();
        
        String first_keyword = "public";
        String second_keyword = "private";
        String third_keyword = "protected";
        
        String remove_keyword = "class";
        
        while(sc.hasNext())
        {
            String word = sc.next();
            
            if(word.equals(first_keyword) || word.equals(second_keyword) || word.equals(third_keyword)) 
            {    
                String method_name = word + sc.nextLine();
                
                if(!method_name.contains(remove_keyword)) // Ensure no class names are in list
                {       
                    method_flags.add(word);
                }
                
            }
            else
            {
                sc.nextLine();
            }
        }     
        
        
        return method_flags;
    }
        
    /**
     * Parse through entire javap file. Look for keyword 'Code:'. This indicates
     * the start of the method information the simulator specifically needs.
     * Extract all words from this section into an arraylist. Stop extract when
     * keyword 'LineNumberTable:' occurs. As there may be more than one method 
     * in the class, use occurences to skip previous methods found.
     * @param occurences occurences of 'Code:' before begin extract
     * @return method_details ArrayList<String> Containing method data
     */
    private ArrayList<String> parseMethodCode(int occurences) throws FileNotFoundException
    {
        //Scanner sc = new Scanner(getClass().getResourceAsStream(FILE_PATH));
        Scanner sc = new Scanner(new File(FILE_PATH));
        
        ArrayList<String> method_code = new ArrayList<String>();
        
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
                method_code.add(word);
            }
        }
        
        return method_code;
    }
    
    private void extractNumberOfMethods() throws FileNotFoundException
    {
        //Scanner sc = new Scanner(getClass().getResourceAsStream(FILE_PATH));
        Scanner sc = new Scanner(new File(FILE_PATH));
        
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

        System.out.println("number of methods: " + NUMBER_OF_METHODS);
        
    }
       
    public int getNumberOfMethods()
    {
        return NUMBER_OF_METHODS;
    }
    
    public HashMap getByteCodeDetails()
    {
        return bytecode_details_map;
    }
    
    public ArrayList<Method> getMethods()
    {
        return methods;
    }
    
    public ArrayList<String> getConstantPoolData()
    {
        return constant_pool_data;
    }
}
