package org.gobiiproject.gobiimodel.utils.error;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ch.qos.logback.classic.Level.ERROR;
import static ch.qos.logback.classic.Level.WARN;

/**
 * Horribly simplistic error logger.
 * Allows for multiple errors to be logically distinguished, 
 * so I don't have to hear 'the log file says moo' complaints all the time.
 * @author jdl232
 * Hopefully HelperFunctions will just return Errors instead of true/false
 */


public class ErrorLogger {
	private static final Logger log = LoggerFactory.getLogger("Error Log");
	public static List<Error> errors = new ArrayList();

	/**
	 * Assuming Logback under slf4j, sets the log level to the logback item. Otherwise this will burn.
	 * @param level Level object from Logback
	 */
	public static void setLogLevel(Level level) {
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
		root.setLevel(level);
	}

	/**
	 * Sets property for logging directory and resets the logger (so the new file is used).
	 * Currently rewritten very brittlly in that it calls out a FileAppender by name ("FILE").
	 * Sorry if that one bit you.
	 * @param filepath
	 */
	public static void setLogFilepath(String filepath){
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		context.putProperty("log-dir",filepath);
		FileAppender<ILoggingEvent> appender = (FileAppender<ILoggingEvent>)context.getLoggerList().get(0).getAppender("FILE");
		appender.stop();
		appender.setFile(filepath);
		appender.start();
	}

	/**
	 * Returns the file of the first FileAppender in the logging method.
	 * @return the file path
	 */
	public static String getLogFilepath(){
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		FileAppender<ILoggingEvent> appender = (FileAppender<ILoggingEvent>)context.getLoggerList().get(0).getAppender("FILE");
		return appender.getFile();
	}

	/**
	 * Sets Root level logging level for Logback.
	 * If string is not a valid logging level, sets logging level to 'ERROR' only.
	 * @param level Level of logging to set to. One of {ERROR, INFO, TRACE, DEBUG, WARN,ALL,OFF}
	 * @return true if log level is valid, else false.
	 */
public static boolean setLogLevel(String level){
		Level logLevel=null;
		logLevel=Level.toLevel(level,logLevel);//New log level, or null
		if(logLevel==null){
			return false;
		}
		setLogLevel(logLevel);//Ignoring the param just makes it 'debug', which is suboptimal
		return true;
	}

	/**
	 * Logs an error that has occurred in the process.
	 * @param e "Error" to be logged
	 */
	public static void logError(Error e){
		errors.add(e);
		log(Level.ERROR,e);
		if(e.file!=null){
			logErrorFile(new File(e.file));
		}
	}

	public static void logError(String name, String message, Throwable e){
		Error err=new Error(name,message);
		errors.add(err);
		log.error(name+":"+message,e);
	}

	public static void logError(String name, Throwable e){
		logError(name,e.getMessage(),e);
	}

	//For Debugging Purposes Only
	public static void logError(String name, String message, Throwable e,boolean ignore){
		if(!ignore){
			logError(name,message,e);return;
		}
		//Error err=new Error(name,message);
		//errors.add(err);
		log.error(name+":"+message,e);
	}


	/**
	 * Adds an entire file to the error stream
	 * @param f file to add to error stream
	 */
	private static void logErrorFile(File f){
		if(!f.exists()) return; //No file
		if(f.length()==0) return; //No file
		BufferedReader br=null;
		try {
			br=new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			//This is... .very unlikely, as we check file existance a line ago.
			log.error("File Not Found in ErrorLogger.logErrorFile",e);
			return;
		}

		log.error("Contents of File {}",f.getAbsoluteFile());
		String s=null;
		try {
			while ((s = br.readLine())!=null) {
				log.error(s);
			}
		}catch(IOException e){

		}
	}
	/**
	 * Logs an error through ErrorLogger, as well as notifying ErrorLogger that an important error
	 * has occurred (see {@link ErrorLogger#success success})
	 * @param name Name of the component being logged
	 * @param reason Reason given for error
	 * @param file File containing additional error details (if exists) Nullable
	 */
	public static void logError(String name, String reason, String file){
		logError(new Error(name,reason,file));
	}
	public static void logError(String name, String reason){
		logError(new Error(name,reason,null));
	}

	public static void logWarning(String name, String reason){
		log(Level.WARN,new Error(name,reason));
	}
	private static void log(Level l, Error e){
		if(l.equals(Level.ERROR))
			log.error("{}: {}", e.name, e.reason);
		else if(l.equals(Level.WARN))
			log.warn("{}: {}", e.name, e.reason);
		else if(l.equals(Level.DEBUG))
			log.debug("{}: {}", e.name, e.reason);
		else if(l.equals(Level.INFO))
			log.info("{}: {}", e.name, e.reason);
		else if(l.equals(Level.TRACE))
			log.trace("{}: {}", e.name, e.reason);
		else
			log.error("Invalid log level",new Throwable());
	}

	public static void logDebug(String name, String reason){
		log(Level.DEBUG,new Error(name,reason));
	}
	public static void logInfo(String name, String reason){
		log(Level.INFO,new Error(name,reason));
	}

	public static void logTrace(String name, String reason){
		log(Level.TRACE,new Error(name,reason));
	}

	/**
	 * Determine if the current process is 'successful'.
	 * "Successful" means no errors were received by the process so far.
	 * @return true if there are no errors that have been logged
	 */
	public static boolean success(){
		return errors.isEmpty();
	}

	/**
	 * List of Error objects in the system.
	 */
	public static List<Error> getAllErrors(){
		return errors;
	}

	/**
	 * List of errors in the system in printable string format
	 * @return A string composed of all toStrings for all errors in the system, deliniated by newlines.
	 */
	public static String getAllErrorStrings(){
		StringBuilder sb=new StringBuilder();
		for(Error e:errors){
			sb.append(e);
			sb.append("\n");
		}
		return sb.toString();
	}

	public static String getAllErrorStringsHTML(){
		return getAllErrorStrings().replaceAll("\n","<br/>");
	}

	public static String getFirstErrorReason(){
		if(errors.isEmpty()) return null;
		return errors.iterator().next().reason;
	}

}

/*Package*/ class Error{
	String name;
	String reason;
	String file;
	Error(String name, String reason, String file){
		this.name=name;
		this.reason=reason;
		this.file=file;
	}
	Error(String name, String reason){
		this(name,reason,null);
	}
	public String toString(){
		return name+": "+reason+(file!=null?"\nAn log for this error may be available at "+file:"");
	}
}
