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
package com.codeight.jrc.read;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.codeight.jrc.model.TestCase;
import com.codeight.jrc.model.TestClass;
import com.codeight.jrc.model.TestPackage;
import com.codeight.jrc.model.TestReport;

/**
 * JUnit XML parser class to parse
 * <ul>
 * 	<li>XML Report generated for each test class</li>
 * 	<li>Combined XML Report generated for all classes</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author Vivekanand S V
 *
 */
public class ParseXML
{
	/**
	 * Actual parser implementation.
	 * @param report
	 * @param path
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static void parse(TestReport report, Object path) throws SAXException, IOException, ParserConfigurationException
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setAttribute("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = null;
		if(path instanceof URL)
		{
			doc = dBuilder.parse(((URL) path).openStream());
		}
		else if(path instanceof File)
		{
			doc = dBuilder.parse((File)path);
		}
		doc.getDocumentElement().normalize();

		// This is used to parse the junit xml which contains all the test suites in it.
		if(doc.getDocumentElement().getNodeName().equals("testsuites"))
		{
			NodeList testSuites = doc.getElementsByTagName("testsuite");
			int noOfTestSuites = testSuites.getLength();
			for(int testSuiteNo = 0; testSuiteNo < noOfTestSuites ; testSuiteNo++)
			{
				Element testSuiteEl = (Element) testSuites.item(testSuiteNo);
				String className = testSuiteEl.getAttribute("name");
				String packageName = testSuiteEl.getAttribute("package");
				parseSuite(report, testSuiteEl, packageName, className);
			}
		}
		// This is used when individual test suite (class level report xmls) is given as input
		else if(doc.getDocumentElement().getNodeName().equals("testsuite"))
		{
			Element testSuiteEl = doc.getDocumentElement();
			String fullClassName = testSuiteEl.getAttribute("name");
			String className = fullClassName.substring(fullClassName.lastIndexOf('.')+1, fullClassName.length());
			String packageName = fullClassName.substring(0, fullClassName.lastIndexOf('.'));
	        parseSuite(report, testSuiteEl, packageName, className);			
		}
	}
	
	/** 
	 * This method will parse the common elements that are
	 * present in both individual/combined test suites.
	 * @param report
	 * @param testSuiteEl
	 * @param packageName
	 * @param className
	 */
	private static void parseSuite(TestReport report, Element testSuiteEl, String packageName, String className)
	{		
		TestPackage tp = report.getTestPackageByName(packageName);
		if (tp == null)
		{
			tp = new TestPackage(packageName);
			report.addTestPackage(tp);
		}
		TestClass testClass = tp.getTestClassByName(className);
		if(testClass == null)
		{
			testClass = new TestClass(className);
			tp.addClass(testClass);
		}
		
		testClass.setHostName(testSuiteEl.getAttribute("hostname"));
		testClass.setTests(Long.valueOf(testSuiteEl.getAttribute("tests")));
		testClass.setErrors(Long.valueOf(testSuiteEl.getAttribute("errors")));
		testClass.setFailures(Long.valueOf(testSuiteEl.getAttribute("failures")));
		testClass.setTimeTaken(testSuiteEl.getAttribute("time"));
		testClass.setTimestamp(testSuiteEl.getAttribute("timestamp"));
		
		NodeList cases = testSuiteEl.getElementsByTagName("testcase");
        int noOfCases = cases.getLength();
        for(int caseNo=0;caseNo<noOfCases;caseNo++)
        {
        	
        	Element testCaseEl = (Element) cases.item(caseNo);
        	String caseName = testCaseEl.getAttribute("name");
        	TestCase testCase = testClass.getTestCaseByName(caseName);
        	if(testCase == null)
        	{
        		testCase = new TestCase(caseName);
        		testClass.addCase(testCase);
        	}
        	
        	testCase.setFullClassName(testCaseEl.getAttribute("classname"));
        	testCase.setTimeTaken(testCaseEl.getAttribute("time"));
        	
        	NodeList failures = testCaseEl.getElementsByTagName("failure");
            int noOfFailures = failures.getLength();
            for(int failureNo=0;failureNo<noOfFailures;failureNo++)
            {
            	Node failureNode = failures.item(failureNo);
            	Element failureEl = (Element) failureNode;
            	testCase.setResult(TestCase.Result.FAIL);            	
            	testCase.setErrorMsg(failureEl.getAttribute("message"));
            	testCase.setErrorType(failureEl.getAttribute("type"));
            	testCase.setErrorTrace(failureEl.getTextContent());
            }
            
        	NodeList errors = testCaseEl.getElementsByTagName("error");
            int noOfErrors = errors.getLength();
            for(int errorNo=0;errorNo<noOfErrors;errorNo++)
            {
            	Node errorNode = errors.item(errorNo);
            	Element errorEl = (Element) errorNode;
            	testCase.setResult(TestCase.Result.ERROR);
            	testCase.setErrorMsg(errorEl.getAttribute("message"));
            	testCase.setErrorType(errorEl.getAttribute("type"));
            	testCase.setErrorTrace(errorEl.getTextContent());
            }
        }
	}
}