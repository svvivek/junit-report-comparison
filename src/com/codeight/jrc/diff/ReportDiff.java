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
package com.codeight.jrc.diff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.codeight.jrc.model.TestCase;
import com.codeight.jrc.model.TestClass;
import com.codeight.jrc.model.TestPackage;
import com.codeight.jrc.model.TestReport;

/**
 * Class which stores the difference between two reports.
 *
 * @since 1.0.0
 * @author Vivekanand S V
 *
 */
public class ReportDiff
{
	private TestReport oldReport;
	private TestReport newReport;
	
	private List<TestCase> addedCases = new ArrayList<TestCase>();
	private List<TestCase> deletedCases = new ArrayList<TestCase>();

	// The reason to use LinkedHashMap here is to maintain the order
	private Map<TestCase, TestCase> pf_Cases = new LinkedHashMap<TestCase, TestCase>();
	private Map<TestCase, TestCase> fp_Cases = new LinkedHashMap<TestCase, TestCase>();
	private Map<TestCase, TestCase> pe_Cases = new LinkedHashMap<TestCase, TestCase>();
	private Map<TestCase, TestCase> ep_Cases = new LinkedHashMap<TestCase, TestCase>();
	private Map<TestCase, TestCase> fe_Cases = new LinkedHashMap<TestCase, TestCase>();
	private Map<TestCase, TestCase> ef_Cases = new LinkedHashMap<TestCase, TestCase>();

	private Map<TestCase, TestCase> casesDifferingInFailure = new LinkedHashMap<TestCase, TestCase>();
	private Map<TestCase, TestCase> casesDifferingInError = new LinkedHashMap<TestCase, TestCase>();
	
	/**
	 * Generates difference between 2 reports.
	 * @param oldReport
	 * @param newReport
	 */
	public ReportDiff(TestReport oldReport, TestReport newReport)
	{
		this.oldReport = oldReport;
		this.newReport = newReport;
		generateDiff(oldReport, newReport);
	}

	/**
	 * @return the oldReport
	 */
	public TestReport getOldReport()
	{
		return oldReport;
	}

	/**
	 * @return the newReport
	 */
	public TestReport getNewReport()
	{
		return newReport;
	}

	/**
	 * @return the addedCases
	 */
	public List<TestCase> getAddedCases()
	{
		return addedCases;
	}

	/**
	 * @return the deletedCases
	 */
	public List<TestCase> getDeletedCases()
	{
		return deletedCases;
	}
	
	/**
	 * @return the pf_Cases
	 */
	public Map<TestCase, TestCase> getPF_Cases()
	{
		return pf_Cases;
	}

	/**
	 * @return the fp_Cases
	 */
	public Map<TestCase, TestCase> getFP_Cases()
	{
		return fp_Cases;
	}

	/**
	 * @return the pe_Cases
	 */
	public Map<TestCase, TestCase> getPE_Cases()
	{
		return pe_Cases;
	}

	/**
	 * @return the ep_Cases
	 */
	public Map<TestCase, TestCase> getEP_Cases()
	{
		return ep_Cases;
	}

	/**
	 * @return the fe_Cases
	 */
	public Map<TestCase, TestCase> getFE_Cases()
	{
		return fe_Cases;
	}

	/**
	 * @return the ef_Cases
	 */
	public Map<TestCase, TestCase> getEF_Cases()
	{
		return ef_Cases;
	}

	/**
	 * @return the casesDifferingInFailure
	 */
	public Map<TestCase, TestCase> getCasesDifferingInFailure()
	{
		return casesDifferingInFailure;
	}

	/**
	 * @return the casesDifferingInError
	 */
	public Map<TestCase, TestCase> getCasesDifferingInError()
	{
		return casesDifferingInError;
	}
	
	/**
	 * Actual method where the diff generation takes place.
	 * @param oldReport
	 * @param newReport
	 */
	private void generateDiff(TestReport oldReport, TestReport newReport)
	{
		List<TestPackage> oldTestPackages = oldReport.getTestPackages();
		Iterator<TestPackage> oldPackageIterator = oldTestPackages.iterator();
		while(oldPackageIterator.hasNext())
		{
			TestPackage oldTestPackage = oldPackageIterator.next();
			TestPackage newTestPackage = newReport.getTestPackageByName(oldTestPackage.getPackageName());
			// This check is done to see if a package is deleted.
			if(newTestPackage == null)
			{
				for(TestClass testClass : oldTestPackage.getClasses())
				{
					deletedCases.addAll(testClass.getCases());
				}
				continue;
			}

			List<TestClass> oldTestClasses = oldTestPackage.getClasses();
			
			Iterator<TestClass> oldTestClassIterator = oldTestClasses.iterator();
			while(oldTestClassIterator.hasNext())
			{
				TestClass oldTestClass = oldTestClassIterator.next();
				TestClass newTestClass = newTestPackage.getTestClassByName(oldTestClass.getClassName());
				// This check is done to see if a class is deleted.
				if(newTestClass == null)
				{
					deletedCases.addAll(oldTestClass.getCases());
					continue;
				}
				
				List<TestCase> oldTestCases = oldTestClass.getCases();
				
				Iterator<TestCase> oldTestCaseIterator = oldTestCases.iterator();
				while(oldTestCaseIterator.hasNext())
				{
					TestCase oldTestCase = oldTestCaseIterator.next();
					TestCase newTestCase = newTestClass.getTestCaseByName(oldTestCase.getCaseName());
					// This check is done to see if a case is deleted.
					if(newTestCase == null)
					{
						deletedCases.add(oldTestCase);
						continue;
					}
					
					if(oldTestCase.getResult() == TestCase.Result.PASS)
					{
						if(newTestCase.getResult() == TestCase.Result.FAIL)
						{
							pf_Cases.put(oldTestCase, newTestCase);
						}
						else if(newTestCase.getResult() == TestCase.Result.ERROR)
						{
							pe_Cases.put(oldTestCase, newTestCase);
						}
					}
					else if(oldTestCase.getResult() == TestCase.Result.FAIL)
					{
						if(newTestCase.getResult() == TestCase.Result.PASS)
						{
							fp_Cases.put(oldTestCase, newTestCase);
						}
						else if(newTestCase.getResult() == TestCase.Result.ERROR)
						{
							fe_Cases.put(oldTestCase, newTestCase);
						}
						else if (!oldTestCase.getErrorMsg().equals(newTestCase.getErrorMsg()) || 
									!oldTestCase.getErrorType().equals(newTestCase.getErrorType()) )
						{
							casesDifferingInFailure.put(oldTestCase, newTestCase);
						}
					}
					else if(oldTestCase.getResult() == TestCase.Result.ERROR)
					{
						if(newTestCase.getResult() == TestCase.Result.PASS)
						{
							ep_Cases.put(oldTestCase, newTestCase);
						}
						else if(newTestCase.getResult() == TestCase.Result.FAIL)
						{
							ef_Cases.put(oldTestCase, newTestCase);
						}
						else if (!oldTestCase.getErrorMsg().equals(newTestCase.getErrorMsg()) || 
								!oldTestCase.getErrorType().equals(newTestCase.getErrorType()) )
						{
							casesDifferingInError.put(oldTestCase, newTestCase);
						}
					}					
				}
				// This check is done to see if a case is added.
				for(TestCase newTestCase : newTestClass.getCases())
				{
					if(oldTestClass.getTestCaseByName(newTestCase.getCaseName()) == null)
					{
						addedCases.add(newTestCase);
					}
				}
			}
			// This check is done to see if a class is added.
			for(TestClass newTestClass : newTestPackage.getClasses())
			{
				if(oldTestPackage.getTestClassByName(newTestClass.getClassName()) == null)
				{
					addedCases.addAll(newTestClass.getCases());
				}
			}
		}
		// This check is done to see if a package is added.
		for(TestPackage newTestPackage : newReport.getTestPackages())
		{
			if(oldReport.getTestPackageByName(newTestPackage.getPackageName()) == null)
			{
				for(TestClass testClass : newTestPackage.getClasses())
				{
					addedCases.addAll(testClass.getCases());
				}
			}
		}
	}
}