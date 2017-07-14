package org.gobiiproject.gobiimodel.utils;


/**
 * Class to encompass external program calls.
 * @author Josh L.S.
 *
 */
public class ExternalFunctionCall {
	public String functionName,execString,argString;
	public ExternalFunctionCall(String functionName, String execString,String argString){
		this.functionName=functionName;
		this.execString=execString;
		this.argString=argString;
	}
	/**
	 * Creates a new ExternalFunctionCall
	 * @param functionName
	 * @param execString
	 * @param argString
	 * @return
	 */
	public static ExternalFunctionCall extern(String functionName, String execString,String argString){
		return new ExternalFunctionCall(functionName,execString,argString);
	}
	/**
	 * Creates a new ExternalFunctionCall
	 * @param functionName
	 * @param execString
	 * @return
	 */
	public static ExternalFunctionCall extern(String functionName, String execString){
		return extern(functionName,execString,null);
	}
	/**
	 * Set arguments
	 * @param efc External Function Call
	 * @param arg Arguments for call
	 * @return	New EFC with the arguments set
	 */
	public static ExternalFunctionCall sarg(ExternalFunctionCall efc, String arg){
		return new ExternalFunctionCall(efc.functionName,efc.execString,arg);
	}
	public String getCommand(){
		return execString+(argString!=null?" "+argString:"");
	}
	public ExternalFunctionCall setArgs(String argString){
		this.argString=argString;
		return this;
	}
}
