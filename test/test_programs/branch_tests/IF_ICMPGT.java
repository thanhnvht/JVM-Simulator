
package test_programs.branch_tests;


public class IF_ICMPGT 
{
    public static void main(String[]args)
    {
        int a = 20;        
        int b = 10;
        int c = 20;
        int d = 0;
        
        if(a <= b)
        {
            d = 123;
        }
                
        if(a <= c)
        {
            d = 456;
        }
    }
}
