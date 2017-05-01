
[![forthebadge](http://forthebadge.com/images/badges/60-percent-of-the-time-works-every-time.svg)](http://forthebadge.com)

# JVM - Simulator

## Description
A tool to show how Java code is executed within the Java Virtual Machine (JVM). It shows a subset of the main mechanisms within the JVM and how they change during runtime. It also details how Java code and Bytecode correspond with each other whilst executing a .class file.

The JVM-Simulator can simulate Java files that contain:
* Arithmetic
* Variables - int, long, short, byte and char
* Loops
* Condtional Statements
* Method Invocation and Return
* Arrays

It cannot simulate:
* Strings
* Floats and Doubles
* I/O
* Object creation
* Advanced OO operations

## Motivation
This project aims were to visualise the JVM components in order to more easily teach the JVM's functionality. Most information regarding the JVM is only in text format. Hence, by varying the types of information regarding the JVM, prospective JVM learners have more resources to learn from.

## Installation
Download the latest version from the *Releases* tab.
Java must already be installed on your machine.
Ensure your PATH system variable hs an entry for the binarys within the JDK.
Do do this, browse to: Control Panel > System > Advanced System Settings > Environment Variables
Within, double click on the PATH system variable, and add the JDK entry
e.g. C:\Program Files\Java\jdk1.8.0_101\bin
The JDK folder will be different according to the version of Java you have installed

## Tests
Manually create your own Java source files, then load them into the simulator. These files must be able to be compiled by javac.

## Test Example
```java
public class Test
{
	public static void main (String[]args)
	{
		int x = 10;

		for(int i = 0; i < 5; i++)
		{
			x = x * 2;
		}
	}
}
```
## Authors
Ryan French - Main Developer

## License
MIT License

Copyright (c) 2017 Ryan French

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

## Acknowledgments
I would like to thank Steve Marriott for his guidance and ideas throughout the project. 

