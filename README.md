junit-report-comparison
=======================

A tool to generate difference between JUnit reports. This tool parses the JUnit xml report files, compares them and generates a
HTML file containing the difference (viz., cases failed in new build, error cases in new build, added/deleted cases, etc).

Download
--------

You can download the tool from [**HERE**](http://sourceforge.net/projects/junit-report-comparison/ "Click here to go to download page")

Usage Details
-------------

java -jar JRC.jar [OPTIONS]

	report_path 			: give path to location containing the report file(s)
	
optional arguments
	
	--name report_name 		: use this option to assign a name to each report

1. **If --name argument is used, it should be given before the report_path**.

2. **Any number of reports can be included**.

3. *report_path* can be 

> * Directory containing JUnit xml(s). [This option is given for users who have JUnit Xmls generated for each class instead of one single file]

> * Path to JUnit xml file. **This can point to files in local files system or files located over the web (give full http link in this case)**

> * Combinations of above 2 is also allowed.

4. If names are not given then name will be generated as **Report_index** *where index = position of this report in given arguments*.

Examples
--------

* java -jar JRC.jar /home/test/build1/ /home/test/build2/

* java -jar JRC.jar /home/test/build1/TESTS-TestSuites.xml /home/test/build2/TESTS-TestSuites.xml

* java -jar JRC.jar http://www.example.com/build1/TESTS-TestSuites.xml http://www.example.com/build2/TESTS-TestSuites.xml

* java -jar JRC.jar --name Old_Build ./build1/ --name New_Build ./build2/

* java -jar JRC.jar --name Old_Report ./build1/TESTS-TestSuites.xml --name New_Report ./build2/TESTS-TestSuites.xml

* java -jar JRC.jar --name Old_Report http://www.example.com/build1/TESTS-TestSuites.xml --name New_Report http://www.example.com/build2/TESTS-TestSuites.xml

* java -jar JRC.jar --name Build1 ./build1/ --name Build2 ./build2/TESTS-TestSuites.xml --name Build3 http://www.example.com/build3/TESTS-TestSuites.xml



NOTE
----

* Here first report is taken as base and all the other reports are compared with this base report one at a time and the difference reports are generated.
*For example, if there are 4 reports, 1st report will be the base and it will be compared with 2nd, 3rd, and 4th reports and 3 difference files will be generated.*

* Output file generated will be named as **oldReportName_Vs_newReportName.html**.