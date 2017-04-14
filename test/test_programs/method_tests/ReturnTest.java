
package test_programs.method_tests;


public class ReturnTest 
{
    public static void main(String[]args)
    {
        int x = 7;
        int y = -3;
        
        int total = addValues(x, y);
        
    }
    
    
    private static int addValues(int x, int y)
    {
        int total;
        
        total = x + y;
        
        return total;
    }
}
