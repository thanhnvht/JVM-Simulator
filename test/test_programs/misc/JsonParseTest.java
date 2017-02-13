/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_programs.misc;

import java.io.InputStreamReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Ryan
 */
public class JsonParseTest 
{
    
    public JsonParseTest()
    {
                JSONParser parser = new JSONParser();
        
        try
        {
            
            
            Object obj = parser.parse(new InputStreamReader(getClass().getResourceAsStream("/main/resources/bytecodes.json")));

            JSONObject jsonObject = (JSONObject) obj;
            
            JSONArray bytecode = (JSONArray) jsonObject.get("bytecodes");
                                    
            //String name = (String) jsonObject.get("Name");           
            
            for(int i = 0; i < bytecode.size(); i++)
            {
                JSONObject b = (JSONObject) bytecode.get(i);
                
                if(b.get("Name").equals("BIPUSH"))
                    System.out.println("Found BIPUSH");
                
              
            }                        
          
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
