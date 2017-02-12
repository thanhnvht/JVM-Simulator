
package main.com.rfrench.jvm.simulated_programs;


public class ConditionTest 
{
    public static void main(String [] args)
    {
        int a = 10;
        int b = 30;
        int c = 0;
        
        if(a < 20)
        {
            c = 0;
        }        
        else if (a <= 40)
        {
            c = 30;
        }
        else if (a > 20)
        {
            c = 10;
        }
        else if (a >= 50)
        {
            c = 30;
        }
        else if (a == 10)
        {
            c = 40;
        }
        else if (a != 30)
        {
            c = 60;
        }
        else
        {
            c = 100;
        }
                        
    }
}
