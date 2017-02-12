/*
 * The MIT License
 *
 * Copyright 2017 Ryan.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package main.com.rfrench.jvm.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/*
    Program Title: DissambledFileGenerator.java
    Author: Ryan French
    Created: 05-Feb-2016
    Version: 1.0
*/
public class DisassembledFileGenerator 
{
    private String folder_path;
    private String file_name;
    private String file_type;
    private String javap_file_destination;
    private String[] split_path;
        
    private ArrayList<String> java_parsed_code;
    
    private final String FILE_ABSOLUTE_PATH;
    
    private static final String SCRIPTS_FOLDER_PATH =  "\"" + new File("").getAbsolutePath() + "/src/main/com/rfrench/jvm/resources/scripts/";
    private final String DESTINATION_FOLDER_PATH =  new File("").getAbsolutePath() + "/src/main/com/rfrench/jvm/resources/javap/";
    
    public DisassembledFileGenerator(String absolute_file_path)
    {
        FILE_ABSOLUTE_PATH = "\"" + absolute_file_path + "\"";
        split_path = FILE_ABSOLUTE_PATH.split("\\\\"); // Split string by backslash '/'
        
        detectFileType();
        
        extractFolderPath(split_path);
        extractFileName(split_path);      
        
        if(file_type.equals("java"))
        {
            parseJavaFile();
            runJavaCompiler();
        }
        
        saveJavaPFile();
    }
    
    private void detectFileType()
    {                
        file_type = FILE_ABSOLUTE_PATH.split("\\.")[1]; //Presuming all file paths have only one dot
        
        file_type = file_type.substring(0, file_type.length() - 1); //Remove trailing "                
    }
    
    private void extractFolderPath(String[] split_path)
    {                
        folder_path = "";
        
        for(int i = 0; i < split_path.length - 1; i++)
        {
            folder_path += split_path[i] + "/";
        }        
        
        folder_path += "\"";
    }
    
    private void extractFileName(String[] split_path)
    {        
        int end_word_count = split_path.length - 1;
        
        file_name = split_path[end_word_count];                
        
        file_name = file_name.substring(0, file_name.indexOf('.'));               
    }
 
    private void runJavaCompiler()
    {
        final String JAVAC_SCRIPT_PATH = SCRIPTS_FOLDER_PATH + "javac_script.bat\"";

        ProcessBuilder javac = new ProcessBuilder(JAVAC_SCRIPT_PATH, FILE_ABSOLUTE_PATH);
        
        try
        {
            Process p = javac.start();
            
            Thread.sleep(1000);
        }
        
        catch(IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    private void saveJavaPFile()
    {
        javap_file_destination =  "\"" + DESTINATION_FOLDER_PATH + file_name + ".txt\"";                       
                        
        final String JAVAP_SCRIPT_PATH = SCRIPTS_FOLDER_PATH + "javap_script.bat\"";

        ProcessBuilder javap = new ProcessBuilder(JAVAP_SCRIPT_PATH, folder_path, file_name, javap_file_destination);
        
        boolean error_found = false;
        
        try
        {
            Process p = javap.start();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            
            String line = null;
                        
            while((line = br.readLine()) != null)
            {
                if(!error_found)
                {
                    error_found = true;
                    System.out.println("* JavaP Script Error *");
                }
                
                System.out.println(line);
            }

            //setup alert if error stream contains values
            //http://code.makery.ch/blog/javafx-dialogs-official/
                 
            Thread.sleep(1000);
            
        }
        
        catch(IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    private void parseJavaFile()
    {
        String temp_path = FILE_ABSOLUTE_PATH.replaceAll("\"", "");
        
        java_parsed_code = new ArrayList<String>();
        
        try
        {
            Scanner sc = new Scanner(new File(temp_path));  
            
            while(sc.hasNext())
            {
                String line = sc.nextLine();
                java_parsed_code.add(line);
            }
        }
        
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
    
    public String getSavedFilePath()
    {
        return javap_file_destination.replaceAll("\"", "");
    }
    
    public ArrayList<String> getJavaCode()
    {
        return java_parsed_code;
    }
}
