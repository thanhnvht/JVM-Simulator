
import main.com.rfrench.jvm.java.Memory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ryan
 */
public class MemoryTest {
    
    public static void main(String[]args)
    {
        Memory M = new Memory();
        
        System.out.println(M.getMethodMemoryElement(2000));
        System.out.println(M.getMethodMemoryElement(2001));
        System.out.println(M.getMethodMemoryElement(2002));
    }
}