
package test;

import main.com.rfrench.jvm.java.ClassLoader;
import main.com.rfrench.jvm.java.Memory;

public class Test {
    
    public static void main(String [] args)
    {
        String FILE_PATH = "/main/resources/main_method_raw.txt";
        //String FILE_PATH = "/main/resources/while_test2.txt"; 
        
        Memory M = new Memory();
        
        ClassLoader A = new ClassLoader(M);
           
        A.readFileTest(FILE_PATH);
        
       

        
    }
    
}
