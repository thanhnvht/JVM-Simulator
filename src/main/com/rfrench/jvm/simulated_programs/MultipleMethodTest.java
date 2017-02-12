
package main.com.rfrench.jvm.simulated_programs;

public class MultipleMethodTest 
{
    public static void main(String [] args)
    {
        method_1();
    }
    
    private static void method_1()
    {
        int a = 10;
        
        method_2();
        
        int b = a + 10;
    }
    
    private static void method_2()
    {
        int b = 20;
    }
}
