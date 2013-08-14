/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Vivekanand S V
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
package com.codeight.jrc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A model to store Package level details of the report.
 * 
 * @since 1.0.0
 * @author Vivekanand S V
 *
 */
public class TestPackage
{
	private String packageName;
	
	private List<TestClass> classes = new ArrayList<TestClass>();
	
	/**
	 * Creates a new instance with the given package name.
	 * @param packageName
	 */
	public TestPackage(String packageName)
	{
		this.packageName = packageName;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName()
	{
		return packageName;
	}

	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}

	/**
	 * @return the classes
	 */
	public List<TestClass> getClasses()
	{
		return classes;
	}
	
	/**
	 * @param testClass the testClass to add
	 */
	public void addClass(TestClass testClass)
	{
		this.classes.add(testClass);
	}
	
	/**
	 * A convenience method to get a test class of this package by passing its name.
	 * @param className
	 * @return {@link TestClass} instance if a class exists by the given name, null otherwise.
	 */
	public TestClass getTestClassByName(String className)
	{
		for(TestClass testClass : classes)
		{
			if(testClass.getClassName().equals(className))
			{
				return testClass;
			}
		}
		return null;
	}
	
	@Override
	public String toString()
	{
		StringBuilder content = new StringBuilder();
		content.append("Package Name = ");
		content.append(this.getPackageName());
		content.append("\nTest Classes = ");
		content.append(this.getClasses());
		content.append("\n\n");		
		return content.toString();
	}	
}