
package test_programs.counter;


public class CounterTester 
{
    public static void main(String[]args)
    {
        Counter c = new Counter();
                
        for(int i = 0; i < 5; i++)
        {
            c.count();
        }
        
        int total = c.getValue();
    }
}
