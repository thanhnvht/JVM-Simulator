/*
 * The MIT License
 *
 * Copyright 2017 Ryan.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package main.com.rfrench.jvm.java;

import com.google.common.collect.BiMap;
import java.util.HashMap;
import java.util.Map;

/*
    Program Title: Heap.java
    Author: Ryan French
    Created: 14-Feb-2016
    Version: 1.0
*/

public class Heap 
{
    
    private BiMap ARRAY_TYPES_MAP = MethodArea.ARRAY_TYPES_MAP;

    private Map<Integer, Object> ARRAYS_MAP;

    private static int array_count;
    
    public Heap()
    {
        ARRAYS_MAP = new HashMap();
        
        array_count = 0;
    }
    
    public int addArray(int array_type_code, int array_size)
    {
       String array_type = (String)ARRAY_TYPES_MAP.inverse().get(array_type_code);
       
       int array_reference = array_count;
       
       switch(array_type)
       {
           case("int") : int[] int_array = new int[array_size];
                         ARRAYS_MAP.put(array_reference, int_array);
                         break;
                         
           case("char") : char[] char_array = new char[array_size];
                          ARRAYS_MAP.put(array_reference, char_array);
                          break;
                          
           case("byte") : byte[] byte_array = new byte[array_size];
                          ARRAYS_MAP.put(array_reference, byte_array);
                          break;
                          
            //NEED: SHORT, LONG, BOOLEAN
       }
       
       
       array_count++;
       
       return array_reference;
    }
    
    public void setElement(int value, int index, int ref)
    {
        if(ARRAYS_MAP.get(ref) instanceof int[])
        {
            int[] temp_int_array = (int[])ARRAYS_MAP.get(ref);
            
            temp_int_array[index] = value;
        }
        
        else if(ARRAYS_MAP.get(ref) instanceof char[])
        {
            char[] temp_char_array = (char[])ARRAYS_MAP.get(ref);
                    
            temp_char_array[index] = (char)value;
        }
        
        else if(ARRAYS_MAP.get(ref) instanceof byte[])
        {
            byte[] temp_array = (byte[])ARRAYS_MAP.get(ref);
                    
            temp_array[index] = (byte)value;
        }
                
    }
    
    public int getElement(int index, int ref)
    {
        int element = 0;
        
        if(ARRAYS_MAP.get(ref) instanceof int[])
        {
            int[] temp_int_array = (int[])ARRAYS_MAP.get(ref);
            
            element = temp_int_array[index];
        }
        
        else if(ARRAYS_MAP.get(ref) instanceof char[])
        {
            char[] temp_char_array = (char[])ARRAYS_MAP.get(ref);
                    
            element = (char)temp_char_array[index];
        }
        
        else if(ARRAYS_MAP.get(ref) instanceof byte[])
        {
            byte[] temp_array = (byte[])ARRAYS_MAP.get(ref);
                    
            element = (byte)temp_array[index];
        }
                
        return element;
    }

}
