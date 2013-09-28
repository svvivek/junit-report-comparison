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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

	private static List<TestReport> reports = new ArrayList<TestReport>();
	private static List<String[]> comparisons = new ArrayList<String[]>();

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		readInputFile();
		generateDiff();
	}

	/**
	 * This method reads the input configuration file and parses reports that
	 * are required for comparison and reads the combinations for comparing the
	 * parsed builds given by the user.
	 */
	private static void readInputFile()
	{
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File("input.conf")));
		} catch (FileNotFoundException e) {
			System.out.println("\ninput.conf file is not found. Please create a input.conf file with your input");
			return;
		}
		String line = null;
		while ((line = getNextValidLine(reader)) != null) {
			TestReport report = null;

			// If report name is given then this is set as report name
			// then the next valid line will be assumed to give report path.
			if (line.startsWith("report.name")) {
				String[] n = line.split("=");
				String name = n[1].trim();
				report = getReportByName(name);
				if (report == null) {
					report = new TestReport(name);
					String l = getNextValidLine(reader);
					if (l != null && l.startsWith("report.path") && l.split("=").length == 2) {
						String[] path = l.split("=");
						report.setReportPath(path[1].trim());
						try {
							read(report, report.getReportPath());
						} catch (Exception e) {
							System.out.println("\nError while parsing \"" + report.getReportPath() + "\" : - " + e.getMessage());
							continue;
						}
						reports.add(report);

					} else {
						System.out.println("\nReport path for report "
										+ name
										+ " is not mentioned or the input.conf file is not written properly. Hence this report will be ignored.");
					}
				} else {
					System.out.println("\nReport named \"" + name
							+ "\" already exists, hence it is ignored.");
				}
			}

			// If report path is given directly, then report name is
			// generated dynamically based on the number of reports.
			else if (line.startsWith("report.path")) {
				report = new TestReport("Report" + (reports.size() + 1));
				String[] path = line.split("=");
				report.setReportPath(path[1].trim());
				try {
					read(report, report.getReportPath());
				} catch (Exception e) {
					System.out.println("\nError while parsing \"" + report.getReportPath() + "\" : - " + e.getMessage());
					continue;
				}
				reports.add(report);
			}

			// This part is where the required comparisons are mentioned.
			// Any number of comparisons can be done mentioning these repeatedly.
			else if (line.startsWith("compare.build1")) {
				String[] build1 = line.split("=");
				String l = getNextValidLine(reader);
				if (l != null && l.startsWith("compare.build2") && l.split("=").length == 2) {
					String[] build2 = l.split("=");
					comparisons.add(new String[] { build1[1], build2[1] });
				} else {
					System.out.println("\nBuild to be compared with "
									+ build1[1]
									+ " is not mentioned or the input.conf file is not written properly. Hence this comparison will be ignored.");
				}
			}
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
	private static void read(TestReport report, String path) throws Exception
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

	/**
	 * This method reads the combinations of comparison given by the user and
	 * then reading each combination it constructs {@link ReportDiff} object and
	 * passes it to {@link GenerateHTML#generateDiff(ReportDiff)} method, there
	 * by generating comparison report for each combination. <br>
	 * <br>
	 * If any reports are not available, then the info will be printed and will
	 * proceed to the next combination. <br>
	 * <br>
	 * If combinations are not given by the user then diff generation will
	 * happen by taking 1st report as base and other reports are compared with
	 * the 1st build one-by-one.
	 */
	private static void generateDiff() {
		if (reports.size() < 2) {
			System.out.println("\nThere are not enough reports to compare, please provide atleast 2 valid reports.");
			return;
		}

		if(comparisons.size() > 0) {
			for(String[] compare : comparisons) {
				TestReport report1 = getReportByName(compare[0]);
				TestReport report2 = getReportByName(compare[1]);

				if (report1 != null && report2 != null) {
					GenerateHTML.generateDiff(new ReportDiff(report1, report2));
				} else {
					if (report1 == null) {
						System.out.println("\nReport with name "
										+ compare[0]
										+ " is not available. Either it is not mentioned in input.conf file or there was a problem during parsing that report.");
					}
					if (report2 == null){
						System.out.println("\nReport with name "
										+ compare[1]
										+ " is not available. Either it is not mentioned in input.conf file or there was a problem during parsing that report.");
					}
					continue;
				}
			}
			return;
		}

		for (int i = 1; i < reports.size(); i++) {
			GenerateHTML.generateDiff(new ReportDiff(reports.get(0), reports.get(i)));
		}
	}

	/**
	 * This method checks and returns a {@link TestReport} object based on the
	 * name.
	 *
	 * @param reportName
	 * @return a {@link TestReport} object, if a report is already parsed with
	 *         the given report name, <code>null</code> otherwise.
	 */
	private static TestReport getReportByName(String reportName) {
		for (TestReport report : reports) {
			if (report.getReportName().equalsIgnoreCase(reportName)) {
				return report;
			}
		}
		return null;
	}

	/**
	 * This method reads lines from the <code>reader</code>, skips lines that
	 * starts with a '#' and empty lines and returns the lines that has valid
	 * content.
	 *
	 * @param reader
	 * @return a valid line, if one exists, <code>null</code> otherwise.
	 */
	private static String getNextValidLine(BufferedReader reader) {
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.length() == 0 || line.startsWith("#")) {
					return getNextValidLine(reader);
				}
				return line;
			}
		} catch (IOException e) {
			System.out.println("\nError while parsing input.conf file : - "
					+ e.getMessage());
		}
		return line;
	}
}