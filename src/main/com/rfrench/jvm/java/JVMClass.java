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

import java.util.ArrayList;

public class JVMClass 
{
    private ArrayList<Method> methods;
    
    private String file_path;
    private String class_name;
    private String FQN;
    
    
    public JVMClass()
    {
        methods = new ArrayList<Method>();
    }
    
    public void setMethods(ArrayList<Method> method_array)
    {
        methods = method_array;
    }
    
    public void addMethod(Method m)
    {
        methods.add(m);
    }

    public Method getMethod(int index)
    {
        Method M;

        M = methods.get(index);
                
        return M;
    }
    
    public String getClassName() 
    {
        return class_name;
    }

    public void setClassName(String class_name)
    {
        this.class_name = class_name;
    }

    public String getFQN() 
    {
        return FQN;
    }

    public void setFQN(String FQN) 
    {
        this.FQN = FQN;
    }
    
    public void setFilePath(String file_path)
    {
        this.file_path = file_path;
    }
    
    public String getFilePath()
    {
        return file_path;
    }
    
    public int getNumberOfMethods()
    {
        return methods.size();
    }
    
}
