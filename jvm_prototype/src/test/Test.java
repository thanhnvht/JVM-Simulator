
package test;

import main.com.rfrench.jvm.java.Memory;
import main.com.rfrench.jvm.java.ClassLoader;

public class Test {
    

    public static void main(String [] args)
    {
        //String FILE_PATH = "/main/resources/javap/main_method_raw.txt";
        String FILE_PATH = "/main/resources/javap/while_test2.txt"; 
        
        Memory M = new Memory();
        
        ClassLoader A = new ClassLoader(M);
           
        A.readFileTest(FILE_PATH);
        
  

    }
    
}
