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
package com.codeight.jrc;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.codeight.jrc.diff.ReportDiff;
import com.codeight.jrc.model.TestReport;
import com.codeight.jrc.read.ParseXML;
import com.codeight.jrc.write.GenerateHTML;

/**
 * Starting point of this tool. Takes the input report files,
 * parses them and generates difference by taking first report
 * as base and all other reports are compared with the base
 * one-by-one and a difference html is generated for each.
 * 
 * @since 1.0.0
 * @author Vivekanand S V
 *
 */
public class Main
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		if(args.length == 0)
		{
			System.out.println("\nPlease read the README.md file present in the downloaded zip for usage details.\n");
			System.exit(0);
		}
		
		int totalArgs = args.length;
		
		List<TestReport> reports = new ArrayList<TestReport>();
		
		for(int i = 0; i<totalArgs; i++)
		{
			String argument = args[i];
			String reportName = null;
			String path = null;
			
			if(argument.equalsIgnoreCase("--name"))
			{
				reportName = args[++i];
				path = args[++i];
			}
			else
			{ 
				path = args[i];
			}
			if(reportName == null)
			{
				reportName = "Report"+(reports.size()+1);
			}
			
			// We are doing this to avoid parsing same reports twice.
			boolean duplicateReport = false;
			
			for(TestReport testReport : reports)
			{
				// equalsIgnoreCase() is not used as path may be case-sensitive in some OS'
				if(testReport.getReportPath().equals(path))
				{
					System.out.println("\nReports path \"" + path + "\" already parsed for the report \"" + testReport.getReportName() + "\". Hence it is skipped");
					duplicateReport = true;
					continue;
				}
			}
			if(duplicateReport)
			{
				continue;
			}
			TestReport result = new TestReport(reportName); 
			result.setReportPath(path);
			try
			{
				read(result, path);
			}
			catch (Exception e)
			{
				System.out.println("\nError while reading \"" + path + "\" : - " + e.getMessage());
				continue;
			}
			reports.add(result);
		}
		
		if(reports.size() == 1)
		{
			System.out.println("\nOnly 1 report is available, please provide atleast 2 valid reports to compare.");
		}
		for(int i = 1 ; i < reports.size() ; i ++)
		{
			GenerateHTML.generateDiff(new ReportDiff(reports.get(0),reports.get(i)));
		}
	}
	
	/**
	 * This method reads the test reports 
	 * (recursively, if need be) and store
	 * them in the {@link TestReport} class.
	 * @param report
	 * @param path
	 * @throws Exception 
	 */
	public static void read(TestReport report, String path) throws Exception
	{
		if(path.startsWith("http://"))
		{
			ParseXML.parse(report, new URL(path));
		}
		else
		{
			File file = new File(path);
			if(file.isDirectory())
			{
				File[] files = file.listFiles();
				for (File f : files)
				{
					read(report, f.getAbsolutePath());
				}
			}
			else
			{
				ParseXML.parse(report, file);
			}
		}
	}
}