package org.gobiiproject.gobiimodel.utils;

import java.io.*;
import java.util.*;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.config.CropDbConfig;
import org.gobiiproject.gobiimodel.dto.instructions.loader.*;
import org.gobiiproject.gobiimodel.types.GobiiDbType;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
//import com.sun.jna.Library;
//import com.sun.jna.Native;

public class HelperFunctions {
	private static boolean showTempFiles=true;

	/**
	 * Takes a string <i>in</i> and filters it. Returns the part of in from the end of <i>from</i>
	 * to the beginning of <i>to</i>, exclusive.
	 * @param in string to be filtered
	 * @param from first match in substring is beginning of filtered string
	 * @param to last match in substring is end of filtered string
	 * @return filtered string, from the character after the end of from to the character before the beginning of to
	 */
	public static String filter(String in,String from, String to, String find, String replace){
		if(in==null)return null;
		String result = "";
		if(from==null)from="";
		if(to==null)to="";
		int startIndex,endIndex;
		if(in.contains(from)){
			startIndex=in.indexOf(from)+from.length();
		}else{
			startIndex=0;
		}
		String from_in = in.substring(startIndex);
		if(from_in.contains(to) && !to.equals("")){ // If to is null or blank, read to the end
			endIndex = startIndex + from_in.indexOf(to);
		}
		else{
			endIndex=in.length();
		}
		result = in.substring(startIndex, endIndex);
		
		if(find != null && replace != null){
			result = result.replaceAll(find, replace);
		}
		
		return result;
		//Too clever by a half- if from and too are null, returns substring(0,-1);
	}
	
	
	
	
	/*
	 *	Prints file to know that the job is done.
	 *	File contains table <tab> output file
	 *	@param path of the instruction file
	 *	@author v.calaminos
	 */
	
	public static void printDoneFile(String filePath) throws IOException{
		FileWriter writer = new FileWriter(filePath+".load", false);
		boolean first=true;
		List<GobiiLoaderInstruction> list= parseInstructionFile(filePath);
		if(list==null){
			writer.close();
			return;
		}
			for (GobiiLoaderInstruction inst : list){
				if(inst==null)break;
				if(!first)writer.write("\r\n");
				first=false;
				GobiiFile fileParams = inst.getGobiiFile(); 
				writer.write(inst.getTable());
				writer.write("\t");
				writer.write(getDestinationFile(inst));
			}
			writer.close();
		}
	public static String[] getDoneFileAsArray(String instructionFilePath){
		List<GobiiLoaderInstruction> list= parseInstructionFile(instructionFilePath);
		if(list==null || list.isEmpty())return null;
		String[] args = new String[list.size()];
		int i=0;
		for (GobiiLoaderInstruction inst : list){
			if(inst==null)break;
			args[i++]=inst.getTable()+"\t"+getDestinationFile(inst);
		}
		return args;
	}
	

	
	
	
	public static void main(String[] args) throws  IOException{
		System.out.println("Tests filter");
		System.out.println(filter("banana","a","a",null,null));
		System.out.println(filter("banana","b","","a","denden"));
		System.out.println(filter("banana",null,"n", "a", null));
	}
	
	public static List<GobiiLoaderInstruction> parseInstructionFile(String filename){
		  ObjectMapper objectMapper = new ObjectMapper();
		 GobiiLoaderInstruction[] file = null;
		 
		try {
			file = objectMapper.readValue(new FileInputStream(filename), GobiiLoaderInstruction[].class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(file==null)return null;
		 return Arrays.asList(file);
	}
	
	
/**
 * Helper method which executes a string as a command line argument, and waits for it to complete using Runtime.getRuntime.exec.
 * @return true if successful
 */
	public static boolean tryExec(String toExec){
		return tryExec(toExec,null,null);
	}

	
	public static boolean tryExec(String execString,String outputFile, String errorFile){
		return tryExec(execString,outputFile,errorFile,null);
	}
	
	/**
	 * Like TryExec, but tries an "External Function Call"
	 */
	public static void tryFunc(ExternalFunctionCall efc,String outputFile,String errorFile){
		boolean success=tryExec(efc.getCommand(),outputFile,errorFile);
		if(!success){
			try {
				ErrorLogger.logError(efc.functionName, "Non-zero exit code", errorFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void tryFunc(ExternalFunctionCall efc,String errorFile){
		tryFunc(efc,null,errorFile);
	}
	
	//Null outputFIle to get output to standard out.
	public static boolean tryExec(String execString,String outputFile, String errorFile, String inputFile){
		String[] exec=execString.split(" ");
		String executedProcName=exec[0];
		if(executedProcName.equals("python")){
			executedProcName=exec[1];
		}
        ProcessBuilder builder = new ProcessBuilder(exec);
        if(outputFile!=null)builder.redirectOutput(new File(outputFile));
        if(errorFile!=null)builder.redirectError(new File(errorFile));
        if(inputFile!=null)builder.redirectInput(new File(inputFile));
        Process p;
		try {
			p = builder.start();
		    p.waitFor();
		} catch (Exception e) {
			ErrorLogger.logError(executedProcName,"Exception in process",e);
			ErrorLogger.logError(executedProcName,"Error File Contents",errorFile);
			return false;
		}
		if(p.exitValue()!=0){
			if(executedProcName.equals("rm")){ //TODO: Temporary removal of 'RM' errors as 'ERROR' type
				ErrorLogger.logDebug(executedProcName,"Unable to rm "+ exec[1]);
				return false;
			}
			if(executedProcName.contains("gobii_ifl.py")){
				String textToReturn="";
				try {
					BufferedReader br = new BufferedReader(new FileReader(errorFile));
					while(br.ready()){
						textToReturn+=br.readLine()+"\n";
					}
				}catch(Exception e){
					//meh
				}
				ErrorLogger.logError(executedProcName,textToReturn,errorFile);
			}
			else{
				ErrorLogger.logError(executedProcName,"Exit code " + p.exitValue(),errorFile);
			}
			return false;
		}
		return true;
	}

	/**
	 * Returns a string from the output of a process
	 * @param execString
	 * @param errorFile
	 * @return
	 */
	public static int iExec(String execString, String errorFile){
		ProcessBuilder builder = new ProcessBuilder(execString.split(" "));
		if(errorFile!=null)builder.redirectError(new File(errorFile));
		Process p;
		BufferedReader reader=null;
		try {
			p = builder.start();
			reader = new BufferedReader(new InputStreamReader(p.getInputStream()));//What terrible person makes 'InputStream' the type of the output of a process
			p.waitFor();
			if(p.exitValue()!=0){
				ErrorLogger.logError(execString.substring(0,execString.indexOf(" ")),"Exit code " + p.exitValue(),errorFile);
				return -1;
			}
			return Integer.parseInt(reader.readLine().split(" ")[0]);
		} catch (Exception e) {
			ErrorLogger.logError(execString.substring(0,execString.indexOf(" ")),e.getMessage(),e);
			return -1;
		}
	}

	//For a folder destination, returns /digest.<tablename>
 public static String getDestinationFile(GobiiLoaderInstruction instruction){
	 String destination=instruction.getGobiiFile().getDestination();
	 char last = destination.charAt(destination.length()-1);
	 if(last == '\\' || last =='/'){
		 return destination+"digest."+instruction.getTable();
	 }
	 else return destination+"/"+"digest."+instruction.getTable();
 }
	public static String getPostgresConnectionString(CropConfig config){
	 CropDbConfig crop=config.getCropDbConfig(GobiiDbType.POSTGRESQL);
	 String ret = "postgresql://"
	 		+ crop.getUserName()
	 		+ ":"
	 		+ crop.getPassword()
	 		+ "@"
	 		+ crop.getHost()
	 		+ ":"
	 		+ crop.getPort()
	 		+ "/"
	 		+ crop.getDbName();
	 return ret;
 }

	public static boolean sendEmail(String jobName, String fileLocation,boolean success,String errorLogLoc, ConfigSettings config, String recipientAddress){
		return sendEmail(jobName,fileLocation,success,errorLogLoc,config,recipientAddress,null);
	}
	public static boolean sendEmail(String jobName, String fileLocation,boolean success,String errorLogLoc, ConfigSettings config, String recipientAddress,String[] digestTempFiles){
		String host=config.getEmailSvrDomain();
		String port=config.getEmailServerPort().toString();
		config.getEmailSvrHashType();//ignore
		String password=config.getEmailSvrPassword();
		String protocol=config.getEmailSvrType().toLowerCase();
		String fromUser=config.getEmailSvrUser();
		String emailAddress=recipientAddress;
		String user=fromUser;
		try{
			sendEmail(jobName,fileLocation,success,errorLogLoc,host,port,emailAddress,fromUser,password,user,protocol,digestTempFiles);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 *
	 * @param jobName
	 * @param fileLocation
	 * @param success
	 * @param host smtp.cornell.edu
	 * @param port 587
	 * @param emailAddress
	 * @param fromUser
	 * @param password
	 * @param username
	 * @param protocol "smtp"
	 * @throws Exception
	 */
	private static void sendEmail(String jobName, String fileLocation,boolean success,String errorLogLoc, String host,String port, String emailAddress,String fromUser,String password,String username, String protocol,String[] digestTempFiles) throws Exception{
		if(emailAddress==null || emailAddress.equals(""))return;
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.starttls.enable", "true");
		props.setProperty("mail.smtp.starttls.required", "true");
		props.setProperty("mail.transport.protocol", protocol);
		props.setProperty("mail.smtp.host", host);
		props.setProperty("mail.smtp.port", port);
		props.setProperty("mail.host", host);
		props.setProperty("mail.port", port);
		props.setProperty("mail.user", username);
		props.setProperty("mail.password", password);

		Session mailSession = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
			});
		Transport transport = mailSession.getTransport(protocol);

		MimeMessage message = new MimeMessage(mailSession);
		message.setFrom(new InternetAddress(fromUser));
		String subject=jobName+(success?" Completed Successfully":" Failed");
		message.setSubject(subject);
		String content=subject+"\n";
		if(success && fileLocation!=null)content+="Your file is available at "+fileLocation;
		if(success && showTempFiles){
			if(digestTempFiles != null) {
				for (String file : digestTempFiles) {
					if (checkFileExistance(file.substring(file.indexOf('\t') + 1, file.length()))) {
						content += "\nThe loader has created digest file: " + file;
					}
				}
			}
		}
		if(!success){
			content+="\nAn unexpected error occurred when processing your request.";
			if(digestTempFiles != null){
				for(String file:digestTempFiles){
					if(checkFileExistance(file.substring(file.indexOf('\t')+1,file.length()))){
						content+="\nThe loader has created digest file: "+file;
					}
				}
			}
		}
		message.setContent(content, "text/plain");
		message.addRecipient(Message.RecipientType.TO,
				new InternetAddress(emailAddress));
		transport.connect(username,password);
		transport.sendMessage(message,
				message.getRecipients(Message.RecipientType.TO));
		transport.close();
	}

	/**
	 * Moves an instruction file from it's current folder to the 'done' folder
	 * @param instructionFile Fully qualified path to the instruction file
	 */
	public static void completeInstruction(String instructionFile, String doneFolder){
		//Move instruction file
		FileSystemInterface.mv(instructionFile,doneFolder);
	}

	/**
	 * Checks if a file exists AND is non-empty.
	 * @param fileLocation String representation of the file's location (absolute or relative).
	 * @return true if non-empty file exists
	 */
	public static boolean checkFileExistance(String fileLocation) {
	if(fileLocation==null)return false;
	File f = new File(fileLocation);
	return f.exists() && f.getTotalSpace()!=0;
}



	/**
	 * Wizardry.
	 * Uses C Library to (On Unix systems) get the PID of the current process.
	 * @return Process ID
	 *
	public static int getPID(){
	 return CLibrary.Instance.getpid();
 }
	private interface CLibrary extends Library{
	CLibrary Instance = (CLibrary) Native.loadLibrary("c",CLibrary.class);
	int getpid();
	}*/
}
