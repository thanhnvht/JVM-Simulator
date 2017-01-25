/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.File;
import java.io.IOException;


/**
 *
 * @author Ryan
 */
public class JavaPscriptTest 
{
    public static void main(String [] args)
    {
        // Runtime.getRuntime().exec("cmd /c start build.bat");
        // ^^ Use this later to run javap -p -v -c 'file'
        
        //java_p_build.bat\
        
        String file_path = new File("").getAbsolutePath() + "/src/main/com/rfrench/jvm/resources/scripts/";
        
        String class_file_path = "\"" + new File("").getAbsolutePath() + "/src/main/com/rfrench/jvm/simulated_programs/WhileTest.class\"";
       
        String command = "cmd /c start ";       
        
        String[] cmd_array = new String[1];
        
        cmd_array[0] = "cmd /c start javap \"" + file_path + "javap_build.bat\"";
        //cmd_array[1] = "cd \"" + file_pat;
                
        //cmd_array[1] = "java_p_build.bat";
        
        try
        {                    
            Runtime.getRuntime().exec(cmd_array);
            //System.out.println(command + "\"" + file_path + "javap_build.bat\"");
            
        }
        
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
    }
}
