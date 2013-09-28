junit-report-comparison
=======================

A tool to generate difference between JUnit reports. This tool parses the JUnit xml report files, compares them and generates a
HTML file containing the difference (viz., cases failed in new build, error cases in new build, added/deleted cases, etc).

Download
--------

You can download the tool from [**HERE**](http://sourceforge.net/projects/junit-report-comparison/ "Click here to go to download page")

Usage Details
-------------

java -jar JRC.jar

Input
-----

Input for this tool should be given in the input.conf file. There are 2 parts in that file

	1. First part contains input regarding reports are to be compared.
	2. Second part takes the combination of reports to be compared.

Please open input.conf file for further details regarding this.

NOTE
----

* If build comparison combination is given in input.conf file then comparison will be done based on that.
* If build comparison details are not given then, first report is taken as base and all the other reports are compared with this base report one at a time and the difference reports are generated.
*For example, if there are 4 reports, 1st report will be the base and it will be compared with 2nd, 3rd, and 4th reports and 3 difference files will be generated.*

* Output file generated will be named as **oldReportName_Vs_newReportName.html**.