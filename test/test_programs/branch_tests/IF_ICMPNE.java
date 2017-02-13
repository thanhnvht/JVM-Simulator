
package test_programs.branch_tests;


public class IF_ICMPNE 
{
    public static void main(String[]args)
    {
        int a = 10;        
        int b = 20;
        int c = 10;
        int d = 0;
        
        if(a == b)
        {
            d = 123;
        }
                
        if(a == c)
        {
            d = 456;
        }
    }
}
