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

import java.io.File;
import java.io.IOException;

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
    private String saved_file_path;
    
    public DisassembledFileGenerator(String absolute_file_path)
    {
        System.out.println(absolute_file_path);
        
        String[] split_path = absolute_file_path.split("\\\\"); // Split string by backslash '/'
        
        extractFolderPath(split_path);
        extractFileName(split_path);      
        saveDissambledFile();
    }
    
    private void extractFolderPath(String[] split_path)
    {                
        folder_path = "";
        
        for(int i = 0; i < split_path.length - 1; i++)
        {
            folder_path += split_path[i] + "/";
        }        
    }
    
    private void extractFileName(String[] split_path)
    {        
        int end_word_count = split_path.length - 1;
        
        file_name = split_path[end_word_count];
        
        file_name = file_name.substring(0, file_name.indexOf('.'));               
    }
 
    private void saveDissambledFile()
    {
        saved_file_path =  new File("").getAbsolutePath() + "/src/main/com/rfrench/jvm/resources/javap/" + file_name + ".txt";                       
        String script_file_path = "\"" + new File("").getAbsolutePath() + "/src/main/com/rfrench/jvm/resources/scripts/test.bat\"";
        
        ProcessBuilder javap = new ProcessBuilder(script_file_path, folder_path, file_name, "\"" + saved_file_path + "\"");
        
        try
        {
            javap.start();
            
            //setup alert if error stream contains values
            //http://code.makery.ch/blog/javafx-dialogs-official/
                        
        }
        
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public String getSavedFilePath()
    {
        return saved_file_path;
    }
}
