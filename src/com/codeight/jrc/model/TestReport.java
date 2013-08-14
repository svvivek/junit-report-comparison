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
 * A model to store complete JUnit test report.
 * 
 * @since 1.0.0
 * @author Vivekanand S V
 *
 */
public class TestReport
{
	
	private static enum Count
	{
		TOTAL_CASES, TOTAL_ERROR_CASES, TOTAL_FAILED_CASES
	}
	
	private String reportName;
	private String reportPath;
	
	private List<TestPackage> testPackages = new ArrayList<TestPackage>();
	
	/**
	 * Creates a new instance with the given report name.
	 * @param reportName
	 */
	public TestReport(String reportName)
	{
		this.reportName = reportName;
	}

	/**
	 * @return Report Name
	 */
	public String getReportName()
	{
		return reportName;
	}

	/**
	 * @param reportName the reportName to set
	 */
	public void setReportName(String reportName)
	{
		this.reportName = reportName;
	}
	
	/**
	 * @return the reportPath
	 */
	public String getReportPath()
	{
		return reportPath;
	}

	/**
	 * @param reportPath the reportPath to set
	 */
	public void setReportPath(String reportPath)
	{
		this.reportPath = reportPath;
	}

	/**
	 * @return the testPackages
	 */
	public List<TestPackage> getTestPackages()
	{
		return testPackages;
	}

	/**
	 * @param testPackage the testPackage to add
	 */
	public void addTestPackage(TestPackage testPackage)
	{
		this.testPackages.add(testPackage);
	}
	
	/**
	 * A convenience method to get a test package of this result by passing its name.
	 * @param packageName
	 * @return {@link TestPackage} instance if a package exists by the given name, null otherwise.
	 */
	public TestPackage getTestPackageByName(String packageName)
	{
		for(TestPackage testPackage : this.getTestPackages())
		{
			if(testPackage.getPackageName().equals(packageName))
			{
				return testPackage;
			}
		}
		return null;
	}
	
	/**
	 * @return the total time taken to run all the test cases
	 */
	public float getTotalTimeTaken()
	{
		float timeTaken = 0;
		for(TestPackage testPackage : this.getTestPackages())
		{
			for(TestClass testClass : testPackage.getClasses())
			{
				timeTaken = timeTaken + Float.valueOf(testClass.getTimeTaken());
			}
		}
		return timeTaken;
	}
	
	/** 
	 * @return the total number of classes in this report
	 */
	public long getTotalClasses()
	{
		long totalClasses = 0;
		for(TestPackage testPackage : this.getTestPackages())
		{
			totalClasses = totalClasses + testPackage.getClasses().size();
		}
		return totalClasses;
	}
	
	/**
	 * @return the total cases in this test report
	 */
	public long getTotalCases()
	{
		return getCasesCount(Count.TOTAL_CASES);
	}
	
	/**
	 * @return the total failed cases in this test report
	 */
	public long getTotalFailedCases()
	{
		return getCasesCount(Count.TOTAL_FAILED_CASES);
	}
	
	/**
	 * @return the total error cases in this test report
	 */
	public long getTotalErrorCases()
	{
		return getCasesCount(Count.TOTAL_ERROR_CASES);
	}
	
	/**
	 * This method returns one of the below count based on the type.
	 * <ul>
	 * 	<li>Total Cases in the report</li>
	 * 	<li>Total Error cases in the report</li>
	 * 	<li>Total Failed cases in the report</li>
	 * </ul>
	 * @param type
	 * @return the required count
	 */
	private long getCasesCount(Count type)
	{
		long totalCases = 0;
		for(TestPackage testPackage : this.getTestPackages())
		{
			for(TestClass testClass : testPackage.getClasses())
			{
				totalCases = totalCases + getRespectiveCount(testClass, type);
			}
		}
		return totalCases;
	}
	
	/**
	 * In this method, appropriate count for each class is returned based on the type given 
	 * @param testClass
	 * @param type
	 * @return the count based on the type for each class
	 */
	private static long getRespectiveCount(TestClass testClass, Count type)
	{
		switch(type)
		{
			case TOTAL_CASES:
				return testClass.getTests();
			case TOTAL_ERROR_CASES:
				return testClass.getErrors();
			case TOTAL_FAILED_CASES:
				return testClass.getFailures();
			default:
				throw new IllegalArgumentException("Invalid type \"" + type + "\" given.");
		}
	}
	
	/**
	 * @return the pass percentage
	 */
	public float getPassPercentage()
	{
		return ( ( (float)( this.getTotalCases() - ( this.getTotalFailedCases() + this.getTotalErrorCases() ) ) / (float)this.getTotalCases() ) * 100 );
	}
	
	/**
	 * @return the failure percentage
	 */
	public float getFailurePercentage()
	{
		return ( ( (float)( this.getTotalFailedCases() ) / (float)this.getTotalCases() ) * 100 );
	}
	
	/**
	 * @return the error percentage
	 */
	public float getErrorPercentage()
	{
		return ( ( (float)( this.getTotalErrorCases() ) / (float)this.getTotalCases() ) * 100 );
	}
	
	@Override
	public String toString()
	{
		StringBuilder content = new StringBuilder();
		content.append("Report Name = ");
		content.append(this.getReportName());
		content.append("Test Packages = ");
		content.append(this.getTestPackages());
		content.append("\n\n");
		return content.toString();
	}
}