/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_programs.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class JavaPscriptTest 
{
    public static void main(String [] args) throws Exception
    {        
        
        
        //CHANGE BY OPEN FILE PART
        String class_file_path = "\"" + new File("").getAbsolutePath() + "/src/main/com/rfrench/jvm/simulated_programs/\"";
        
        //CHANGE BY OPEN FILE PART
        String java_class_file_name = "MultipleMethodTest";
        
        //STAY SAME
        String java_file_path =  "\"" + new File("").getAbsolutePath() + "/src/main/com/rfrench/jvm/resources/javap/tesasdt.txt\"";                       
        String javap_file_path = "\"" + new File("").getAbsolutePath() + "/src/main/com/rfrench/jvm/resources/scripts/test.bat\"";
                               
        ProcessBuilder javap_process = new ProcessBuilder(javap_file_path, class_file_path, java_class_file_name,  java_file_path);
        
        List <String> command = javap_process.command();
        
        for(String s : command)
        {
            System.out.println(s);
        }
                        
        try
        {                                    
            Process javap = javap_process.start();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(javap.getErrorStream()));
            
            String line;
            
            while((line = br.readLine()) != null)
                System.out.println(line);
                        
        }
        
        catch(IOException e)
        {
            e.printStackTrace();
        }                    
    }    
}
