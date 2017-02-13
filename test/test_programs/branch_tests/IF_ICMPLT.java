
package test_programs.branch_tests;


public class IF_ICMPLT 
{
    public static void main(String[]args)
    {
        int a = 20;        
        int b = 20;
        int c = 25;
        int d = 0;
        
        if(a >= c)
        {
            d = 123;
        }
                
        if(a >= b)
        {
            d = 456;
        }
    }
}
