/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.com.rfrench.jvm.java;

/**
 *
 * @author Ryan
 */
public class InvalidJavapFileException extends Exception
{
    public InvalidJavapFileException(String error)
    {
        switch(error)
        {
            case("No Code Keyword") : break;
        }
    }
}
