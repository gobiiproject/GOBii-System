package org.gobiiproject.gobiimodel.utils;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static org.gobiiproject.gobiimodel.utils.HelperFunctions.tryExec;

public class FileSystemInterface {

	/**
	 * As unix RM command.
	 * @param file
	 */
	public static void rm(String file){
		HelperFunctions.tryExec("rm "+file);
	}
	/**
	 * As unix MV command.
	 * @param from
	 * @param to
	 */
	public static void mv(String from, String to){
		HelperFunctions.tryExec("mv "+from + " " + to);
	}

	public static int lineCount(String file){
		String [] exec={"wc","-l",file};
		int retVal=-1;
		try {
			ProcessBuilder builder = new ProcessBuilder(exec);
			Process p = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));//What terrible person makes 'InputStream' the type of the output of a process
			p.waitFor();
			retVal=Integer.parseInt(reader.readLine().split(" ")[0]);

		}
		catch(Exception e){
			ErrorLogger.logError("FileSystemInterface","Unable to call wc",e);
		}
		return retVal;
	}
}
