
package test_programs.branch_tests;

public class IFEQ 
{
    public static void main(String[]args)
    {
        int a = 0;
        int b = 10;
        
        //Should branch so doesn't enter if statement body
        if(a != 0)
        {
            a = 777;
        }
        
        //Should not branch. Therefore will go into body and make b = 777
        if(b != 0)
        {
            b = 777;
        }
            
    }
}
