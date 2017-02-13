
package test_programs.misc;

import main.com.rfrench.jvm.java.JVMClassLoader;

public class Test {
    

    public static void main(String [] args)
    {
        String FILE_PATH = "/main/resources/javap/multiple_methods.txt";
        //String FILE_PATH = "/main/resources/javap/object_test.txt";   
                
        JVMClassLoader A = new JVMClassLoader(FILE_PATH);
                   
  

    }
    
}
