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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/*
    Program Title: DissambledFileGenerator.java
    Author: Ryan French
    Created: 05-Feb-2016
    Version: 1.0
*/
public class Compiler 
{
    private String folder_path;
    private String file_name;
    private String file_type;
    private String javap_file_destination;
    private String[] split_path;
        
    private ArrayList<String> java_parsed_code;
    
    private String FILE_ABSOLUTE_PATH;
   
    
    public Compiler()
    {
        
    }
    
    public Compiler(String absolute_file_path)
    {
        FILE_ABSOLUTE_PATH = "\"" + absolute_file_path + "\"";        
        
        split_path = FILE_ABSOLUTE_PATH.split("\\\\"); // Split string by backslash '/'
        
        System.out.println(FILE_ABSOLUTE_PATH);
        
        detectFileType();
        
        extractFolderPath(split_path);
        extractFileName(split_path);      
        
        switch(file_type)
        {
            case("java") :  parseJavaFile();
                            runJavaCompiler();                            
                
            case("class") : saveJavaPFile();
                            break;
        }
                
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
        try
        {
            PrintWriter writer = new PrintWriter("javac_script.bat");
            writer.println("javac %1");
            writer.close();
        }
        
        catch(IOException e)
        {
            e.printStackTrace();
        }    
        
        String javacScriptPath = new File("").getAbsolutePath() + "/javac_script.bat";
        
        File temp = new File(javacScriptPath);
        
        if(!temp.exists())
            System.out.println("File not found");
        
        ProcessBuilder javac = new ProcessBuilder(javacScriptPath, FILE_ABSOLUTE_PATH);
                        
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
        javap_file_destination =  "\"" + new File("").getAbsolutePath() + "/" + file_name + ".txt\"";                       
                                
        String javapScriptPath = new File("").getAbsolutePath() + "/javap_script.bat";
               
        try
        {
            PrintWriter writer = new PrintWriter("javap_script.bat");
            writer.println("javap -v -c -p -cp %1 %2 > %3");
            writer.close();
        }
        
        catch(IOException e)
        {
            e.printStackTrace();
        }    
        
        
        ProcessBuilder javap = new ProcessBuilder(javapScriptPath, folder_path, file_name, javap_file_destination);
        
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
                    System.out.println("JAVAP SCRIPT ERROR");
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
