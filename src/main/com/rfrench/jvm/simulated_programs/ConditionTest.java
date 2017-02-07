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
package main.com.rfrench.jvm.simulated_programs;

/**
 *
 * @author Ryan
 */
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
