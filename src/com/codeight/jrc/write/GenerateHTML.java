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
package com.codeight.jrc.write;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.codeight.jrc.diff.ReportDiff;

/**
 * Class used for generating HTML files.
 * 
 * @since 1.0.0
 * @author Vivekanand S V
 *
 */
public class GenerateHTML
{	
	/**
	 * This method reads the {@link ReportDiff} object
	 * and passes it to the velocity, so that it can
	 * be used while generating difference HTML
	 * @param diff
	 */
	public static void generateDiff(ReportDiff diff)
	{		
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty("file.resource.loader.path", "./templates/");
		ve.init();
		
		Template template = ve.getTemplate("diff_template.vm");
		
		VelocityContext context = new VelocityContext();
		context.put("diff", diff);
		StringWriter wr = new StringWriter();
		template.merge(context, wr);
		try
		{
			File destination = new File(diff.getOldReport().getReportName()+"_Vs_"+diff.getNewReport().getReportName()+".html");
			if(!destination.exists())
			{
				destination.createNewFile();
			}
			FileWriter fw = new FileWriter(destination);
			fw.write(wr.toString());
			fw.flush();
			fw.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}