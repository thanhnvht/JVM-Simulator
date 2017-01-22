
package main.com.rfrench.jvm.java;

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
    Program Title: ClassLoader.java
    Author: Ryan French
    Created: 19-Oct-2016
    Version: 1.0
*/

public class ClassLoader
{    
       
    private int NUMBER_OF_METHODS;
                             
    private HashMap bytecode_details_map;              
          
    private JSONArray bytecode_json_array;
    
    private ArrayList<Method> methods;
        
    /**
     * JVMFileReader Constructor
    */
    public ClassLoader(Memory M)
    {          
        
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
            
            extractNumberOfMethods(file_name);
            
            methods = new ArrayList<Method>();
            
            for(int i = 0; i < NUMBER_OF_METHODS; i++)
            {
                ArrayList<String> method_details = parseMethod(file_name, i);
                
                Method m = new Method(bytecode_details_map, bytecode_json_array, method_details);
                
                methods.add(m);
            }
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
}
