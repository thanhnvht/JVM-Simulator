
package test_programs.branch_tests;

public class SwitchTest 
{
    public static void main(String [] args)
    {
        int a = 100;
        int b = 0;
        
        switch(a)
        {
            case(10) : b = 20; break;
            case(50) : b = 50; break;
            case(200) : b = 100; break;
        }
    }
}
