
package main.com.rfrench.jvm.java;

/**
 *
 * @author Ryan
 */
public class Register 
{
    private int value;
        
    public Register()
    {
        value = 0;
    }
    
    public Register(int value)
    {
        this.value = value;
    }
    
    public void inc()
    {
        ++value;
    }
    
    public void dec()
    {
        --value;
    }
    
    public void set(int value)
    {
        this.value = value;
    }
    
    public int get()
    {
        return value;
    }    
}
