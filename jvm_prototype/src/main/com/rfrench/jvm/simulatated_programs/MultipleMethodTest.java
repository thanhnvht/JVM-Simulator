/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.com.rfrench.jvm.simulatated_programs;

/**
 *
 * @author Ryan
 */
public class MultipleMethodTest 
{
    public static void main(String [] args)
    {
        method_1();
    }
    
    private static void method_1()
    {
        int a = 10;
        
        method_2();
    }
    
    private static void method_2()
    {
        int b = 20;
    }
}
