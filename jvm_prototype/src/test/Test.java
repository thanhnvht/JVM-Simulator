
package test;

import main.com.rfrench.jvm.java.JVMFileReader;
import main.com.rfrench.jvm.java.Memory;

public class Test {
    
    public static void main(String [] args)
    {
        String FILE_PATH = "/main/resources/while_test2.txt";
                
        Memory M = new Memory();
        
        JVMFileReader A = new JVMFileReader(M);
           
        A.readFile(FILE_PATH);
        
        int x = (int)A.getLineNumbers().get("14");
        
        System.out.println(x);
        
        //A.readFileTest(FILE_PATH);
        
    }
    
}
