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

/**
 * A model to store Case level details of the report.
 * 
 * @since 1.0.0
 * @author Vivekanand S V
 *
 */
public class TestCase
{
	public enum Result
	{
		PASS, FAIL, ERROR
	}
	
	private String fullClassName;
	private String caseName;

	private String timeTaken;
	
	// Here we'll use same variables to store error or failure
	private String errorMessage;
	private String errorType;
	private String errorTrace;
	
	private Result result = Result.PASS;
	
	/**
	 * Creates a new instance with the given case name.
	 * @param caseName
	 */
	public TestCase(String caseName)
	{
		this.caseName = caseName;
	}

	/**
	 * @return the result
	 */
	public Result getResult()
	{
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Result result)
	{
		this.result = result;
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
	 * @return the fullClassName
	 */
	public String getFullClassName()
	{
		return fullClassName;
	}

	/**
	 * @param fullClassName the fullClassName to set
	 */
	public void setFullClassName(String fullClassName)
	{
		this.fullClassName = fullClassName;
	}

	/**
	 * @return the caseName
	 */
	public String getCaseName()
	{
		return caseName;
	}

	/**
	 * @param caseName the caseName to set
	 */
	public void setCaseName(String caseName)
	{
		this.caseName = caseName;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMsg()
	{
		return errorMessage;
	}

	/**
	 * @param errorMsg the errorMsg to set
	 */
	public void setErrorMsg(String errorMsg)
	{
		this.errorMessage = errorMsg;
	}

	/**
	 * @return the errorType
	 */
	public String getErrorType()
	{
		return errorType;
	}

	/**
	 * @param errorType the errorType to set
	 */
	public void setErrorType(String errorType)
	{
		this.errorType = errorType;
	}

	/**
	 * @return the errorTrace
	 */
	public String getErrorTrace()
	{
		return errorTrace;
	}

	/**
	 * @param errorTrace the errorTrace to set
	 */
	public void setErrorTrace(String errorTrace)
	{
		this.errorTrace = errorTrace;
	}
	
	@Override
	public String toString()
	{
		StringBuilder content = new StringBuilder();
		content.append("Full Class Name = ");
		content.append(this.getFullClassName());
		content.append("\nTest Result = ");
		content.append(this.getResult().toString());
		content.append("\nCase Name = ");
		content.append(this.getCaseName());
		content.append("\nError/Failures Message = ");
		content.append(this.getErrorMsg());
		content.append("\nTime Taken = ");
		content.append(this.getTimeTaken());
		content.append("\n\n");		
		return content.toString();
	}
}