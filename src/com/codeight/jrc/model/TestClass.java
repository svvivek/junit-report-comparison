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
 * A model to store Class level details of the report.
 * 
 * @since 1.0.0
 * @author Vivekanand S V
 *
 */
public class TestClass
{
	private long errors = 0;
	private long failures = 0;
	private long tests = 0;
	
	private String timeTaken = null;	
	private String hostName = null;
	private String className = null;
	private String timestamp = null;
	
	private List<TestCase> cases = new ArrayList<TestCase>();
	
	/**
	 * Creates a new instance with the given class name.
	 * @param className
	 */
	public TestClass(String className)
	{
		this.className = className;
	}

	/**
	 * @return the className
	 */
	public String getClassName()
	{
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName()
	{
		return hostName;
	}

	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName)
	{
		this.hostName = hostName;
	}

	/**
	 * @return the timeTaken
	 */
	public String getTimeTaken()
	{
		return timeTaken;
	}

	/**
	 * @param timeTaken the timeTaken to set
	 */
	public void setTimeTaken(String timeTaken)
	{
		this.timeTaken = timeTaken;
	}

	/**
	 * @return the tests
	 */
	public long getTests()
	{
		return tests;
	}

	/**
	 * @param tests the tests to set
	 */
	public void setTests(long tests)
	{
		this.tests = tests;
	}

	/**
	 * @return the failures
	 */
	public long getFailures()
	{
		return failures;
	}

	/**
	 * @param failures the failures to set
	 */
	public void setFailures(long failures)
	{
		this.failures = failures;
	}

	/**
	 * @return the errors
	 */
	public long getErrors()
	{
		return errors;
	}

	/**
	 * @param errors the errors to set
	 */
	public void setErrors(long errors)
	{
		this.errors = errors;
	}
	
	/**
	 * @param testCase the case to add
	 */
	public void addCase(TestCase testCase)
	{
		cases.add(testCase);
	}
	
	/**
	 * @return the testCases
	 */
	public List<TestCase> getCases()
	{
		return cases;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp()
	{
		return timestamp;
	}


	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}
	
	/**
	 * A convenience method to get a test case of this class by passing its name.
	 * @param caseName
	 * @return {@link TestCase} instance if a case exists by the given name, null otherwise.
	 */
	public TestCase getTestCaseByName(String caseName)
	{
		for(TestCase testCase : cases)
		{
			if(testCase.getCaseName().equals(caseName))
			{
				return testCase;
			}
		}
		return null;
	}
	
	@Override
	public String toString()
	{
		StringBuilder content = new StringBuilder();
		content.append("Class Name = ");
		content.append(this.getClassName());
		content.append("\nTotal Test = ");
		content.append(this.getTests());
		content.append("\nTotal Errors = ");
		content.append(this.getErrors());
		content.append("\nTotal Failures = ");
		content.append(this.getFailures());
		content.append("\nHostname = ");
		content.append(this.getHostName());
		content.append("\n\n");		
		return content.toString();
	}
}