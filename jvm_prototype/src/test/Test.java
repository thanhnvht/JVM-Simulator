
package test;

import main.com.rfrench.jvm.java.MethodArea;
import main.com.rfrench.jvm.java.ClassLoader;

public class Test {
    

    public static void main(String [] args)
    {
        String FILE_PATH = "/main/resources/javap/MultipleMethodTest.txt";
        //String FILE_PATH = "/main/resources/javap/while_test_verbose.txt";   
        
        MethodArea M = new MethodArea();
        
        ClassLoader A = new ClassLoader(M);
           
        A.readFileTest(FILE_PATH);
        
  

    }
    
}
