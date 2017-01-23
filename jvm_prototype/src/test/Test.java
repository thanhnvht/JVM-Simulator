
package test;

import main.com.rfrench.jvm.java.MethodArea;
import main.com.rfrench.jvm.java.ClassLoader;

public class Test {
    

    public static void main(String [] args)
    {
        String FILE_PATH = "/main/resources/javap/multiple_methods.txt";
        //String FILE_PATH = "/main/resources/javap/object_test.txt";   
        
        MethodArea M = new MethodArea();
        
        ClassLoader A = new ClassLoader(M, FILE_PATH);
           
        A.readFileTest();
        
  

    }
    
}
