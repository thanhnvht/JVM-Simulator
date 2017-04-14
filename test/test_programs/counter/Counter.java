
package test_programs.counter;


public class Counter 
{
    private int value;
    
    public Counter()
    {
        value = 0;
    }
    
    public void count()
    {
        value = value + 1;
    }
    
    public int getValue()
    {
        return value;
    }
}
