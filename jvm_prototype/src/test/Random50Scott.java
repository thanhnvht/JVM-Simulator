/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.Random;

/**
 *
 * @author Ryan
 */
public class Random50Scott {
    
    public static void main(String[] args) 
    {
        char[] alphabet = {'a','b','c','d','e','f','g','h','i', 'j','k','l', 'm','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        String[] stringArray = new String[50];
        Random r = new Random(); 
        char tempCharArray[] = new char[3];        
 
        int count = 0;
        boolean duplicate = false;
        
        while(count < 50)
        {
            for(int i = 0; i < 3; i++)
                tempCharArray[i] = alphabet[r.nextInt(26)];

            String randomString = new String(tempCharArray);
                        
            for(int i = 0; i < count; i++)
            {
                String temp = stringArray[i];
                
                if(randomString.equals(temp))
                {
                    duplicate = true;
                    break;
                }
            }
            
            if(duplicate)
            {
                duplicate = false;
                System.out.println("duplicate!");
            }
            else if(!duplicate)
            {
                stringArray[count] = randomString;
                count++;
            }
                                        
        }
                        
        for(int i = 0; i < 50; i++)
        {            
            if( i % 5 == 0)            
                System.out.println();
                                                
            System.out.print(stringArray[i] + " ");
        }
       
    }
    
}
