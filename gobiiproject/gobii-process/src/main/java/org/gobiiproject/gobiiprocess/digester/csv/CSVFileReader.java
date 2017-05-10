package org.gobiiproject.gobiiprocess.digester.csv;

import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.digester.LoaderGlobalConfigurations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rm;

/**
 * CSV-Specific File Loader class, used by {@link org.gobiiproject.gobiiprocess.digester.GobiiFileReader}
 * Contains methods specific to the reading of single-character separated text files, such as .csv, tab-delimited, and pipe-separated values.
 * @author v.calaminos
 * @date 4/14/2016
 * 
 */
public class CSVFileReader implements CSVFileReaderInterface {
	private static final String NEWLINE = "\n";//jdl232 - Lets make this a constant, so if we do have to change it, we can.
	private List<String> column_keys;
	private Map<String, String> constant_values = new HashMap<String, String>();
	private String tmpFileLocation = "E:\\GOBII\\temp_files";
	private String tmpFileSeparator="\\";
	private int maxSize = 0;
	Map<String, String> file_column_constant;
	Map<String, String> file_column_autoincrement;
	int countMax;
	int startNo = 0; // starting number for auto increment; can be 1; waiting for confirmation

	/**
	 * Creates a new CSVFileReader, specifying a location to store 'temporary files', and a folder separator used to delineate a level in the FS.
	 * For example, a temporary folder of ~/tmpFiles on a unix system would have a location of "~/tmpFiles" and a separator of "/"
	 * @param tmpFileLocation
	 * @param tmpFileSeparator
	 */
	public CSVFileReader(String tmpFileLocation, String tmpFileSeparator){
		this.tmpFileLocation=tmpFileLocation;
		this.tmpFileSeparator=tmpFileSeparator;
	}

	/**
	 * Same as {@link CSVFileReader#CSVFileReader(String, String)}, but with default parameters of "E:\\GOBII\\temp_files" and "\\".
	 * Primarily for ease of use.
	 */
	public CSVFileReader(){//Kept for backwards compatibility
	}
	
	/**
	 * Parses a given instruction file, and executes the loader on every instruction found within, by passing the objects to {@link CSVFileReader#processCSV(GobiiLoaderInstruction)}.
	 * This method can be called directly to simulate an instruction file being parsed by the reader.
	 */
	public static void parseInstructionFile(List<GobiiLoaderInstruction> instructions,String tmpFileLocation, String tmpFileSeparator) throws FileNotFoundException, IOException, ParseException{
		CSVFileReaderInterface reader;
		if(LoaderGlobalConfigurations.getSingleThreadFileRead()){
			for(GobiiLoaderInstruction i:instructions){
				try {
					reader=getInterface(tmpFileLocation,tmpFileSeparator,LoaderGlobalConfigurations.getVersionOneRead());
					reader.processCSV(i);
				} catch (InterruptedException e) {
					ErrorLogger.logError("CSVFileReader","Interrupted reading instruction", e);
				}catch(Exception e){
					ErrorLogger.logError("CSVFileReader","Unexpected Exception in reader",e);
				}
			}
			return;
		}

		List<Thread> threads=new LinkedList<>();
		if(instructions==null){
			ErrorLogger.logError("CSVFileReader","No instructions passed in");
		}
		//Create threads
		for(GobiiLoaderInstruction loaderInstruction:instructions){
			reader=getInterface(tmpFileLocation,tmpFileSeparator,LoaderGlobalConfigurations.getVersionOneRead());
			Thread processingThread=new Thread(new ReaderThread(reader,loaderInstruction));
			threads.add(processingThread);
			processingThread.start();
		}
		//Wait for all threads to complete, lets just wait for all of them in order (obviously, many will be done
		// before hand, in which case 'join' does nothing. Brilliant.
		for(Thread t:threads){
			try {
				t.join();
			}
			catch(InterruptedException e){
				ErrorLogger.logError("CSVFileReader","Interrupt",e);
			}
		}
	}
	private static CSVFileReaderInterface getInterface(String tmpFileLocation,String tmpFileSeparator,boolean oldVersion){
		if(oldVersion){
			return new CSVFileReader(tmpFileLocation,tmpFileSeparator);
		}
		else{
			return new CSVFileReaderV2(tmpFileLocation,tmpFileSeparator);
		}
	}

	/**
	 * Reads the input file specified by the loader instruction and creates a digest file based on the instruction. For more detailed discussions on the resulting digest file's format
	 * see either the documentation of the IFLs or {@link org.gobiiproject.gobiiprocess.digester.GobiiFileReader} documentation.
	 * @param loaderInstruction Singular instruction, specifying input and output directories
	 * @throws IOException If an unexpected filesystem error occurs
	 * @throws InterruptedException If interrupted (Signals, etc)
	 */
	@Override
	public void processCSV(GobiiLoaderInstruction loaderInstruction) throws IOException, InterruptedException{
		List<String> tempFiles = new ArrayList<>();
		file_column_constant = new HashMap<>();
		file_column_autoincrement= new HashMap<>();
		try {
			column_keys = new ArrayList<>();
			List<GobiiFileColumn> columns = loaderInstruction.getGobiiFileColumns();
	        // Print the name from the list....
			String fileListString="",delimiterString="";
			
			boolean first=true;
			for(GobiiFileColumn column : columns) {
	        	String tmpFileName = tmpFileLocation + tmpFileSeparator + column.getName() + ".tmp";//jdl232 - lets call these what they are.
	        	FileWriter tmpFileWriter = new FileWriter(tmpFileName);
	        	tempFiles.add(tmpFileName);
	        	fileListString = fileListString + " " + tmpFileName;//jdls - Generate this up here. NOTE: fileListString now starts with a space character. 
	        	if(!first)delimiterString=delimiterString+(column.isSubcolumn()?"\\0":"\\t"); //delimiter string is between previous column and this column
	        	first=false;

	        	//add row for headers
	        	column_keys.add(column.getName());
	        	String columnName=column.getName();
	        	if(column.isSubcolumn())columnName="";//remove column name from subcolumns
	        	tmpFileWriter.write(columnName);
	        	tmpFileWriter.write(NEWLINE);

	        	File file = new File(loaderInstruction.getGobiiFile().getSource());
	        	countMax = 0;
	        	if(file.isDirectory()){
	        		listFilesFromFolder(file, tmpFileWriter, column, loaderInstruction, true, tmpFileName);
	        	}else{
	        		writeToTempFiles(file, tmpFileWriter, column, loaderInstruction, true, tmpFileName);
	        	}
	        	
	    		tmpFileWriter.close();
	        }
	        

			 //Build temp file for columns with CONSTANT and AUTOINCREMENT type

	        FileWriter fw;
	        for (String fileName : tempFiles){
	        	
	        	if(file_column_constant.containsKey(fileName)){
	        		fw = new FileWriter(fileName, false);
	        		String column_key = file_column_constant.get(fileName);
	        		String columnName=column_key;
	        		for(GobiiFileColumn c:columns){
	        			if(c.getName().equals(column_key)){
	        				if(c.isSubcolumn())columnName="";
	        				break;
	        			}
	        		}
	        		fw.write(columnName);
	        		for(int i=0; i<maxSize; i++){
	        			fw.write(NEWLINE);
	        			fw.write(constant_values.get(column_key));
	        		}
	        		fw.close();
	        	}
	        	else if(file_column_autoincrement.containsKey(fileName)){
	        		fw = new FileWriter(fileName, false);
	        		String column_key = file_column_autoincrement.get(fileName);
	        		String columnName = column_key;
	        		for(GobiiFileColumn c:columns){
	        			if(c.getName().equals(column_key)){
	        				if(c.isSubcolumn())columnName="";
	        				break;//jdl232 - Remove column names from subcolumns
	        			}
	        		}
	        		fw.write(columnName);
	        		for(int i=startNo;i<maxSize;i++){
	        			fw.write(NEWLINE);
	        			fw.write(Integer.toString(i));
	        		}
	        		fw.close();
	        	}
	        }
	        
	        /*
	         * Merge all temporary files into one intermediate file using "paste"
	         */
	        delimiterString=delimiterString+"\\n";
	        String commandStr = "paste -d"+delimiterString+""+fileListString;//+" > "+loaderInstruction.getFile().getDestination()+" 2>>/home/jdl232/err.log";
	       HelperFunctions.tryExec(commandStr, HelperFunctions.getDestinationFile(loaderInstruction),"err.log" );
	        for(String filename:new HashSet<String>(tempFiles)){ //Deduplicate before calling RM
	        	rm(filename);
	        }
		} catch (FileNotFoundException e) {
			ErrorLogger.logError("CSVReader","Unexpected Missing File",e);
		} catch (IOException e) {
			ErrorLogger.logError("CSVReader","Unexpected IO Error",e);
		}
	}

	/**
	 * Recursively finds all files in {@code folder} and all subfolders, reading each file for the data needed for {@code column}, and writing to {@code tmpFileWriter}.
	 * Called once for each output column, creating a separate output file each time.
	 * Input files from {@code folder} are read sequentially.
	 * @param folder Folder in the filesystem to start from (input folder)
	 * @param tmpFileWriter Writer to write temporary columns to
	 * @param column Column looked for
	 * @param loaderInstruction Instruction column is from
	 * @param first If this is the first file to be returned (can be set to false by calling method. Passed to callee. *Dragons*
	 * @param tmpFileName Name of the folder where temporary files can be stored
	 */
	public void listFilesFromFolder(File folder, FileWriter tmpFileWriter, GobiiFileColumn column, GobiiLoaderInstruction loaderInstruction, boolean first, String tmpFileName){
		if(folder==null){
			ErrorLogger.logWarning("CSVFileReader","Read from null folder");
			return;
		}
		for(File file : folder.listFiles()){
			if(file.isDirectory()){
				listFilesFromFolder(file, tmpFileWriter, column, loaderInstruction, first, tmpFileName);
			}else{
				try {
					writeToTempFiles(file, tmpFileWriter, column, loaderInstruction, first, tmpFileName);
				} catch (IOException e) {
					ErrorLogger.logError("CSVReader","Failure to write temp files",e);
				}
				first = false;
			}
		}
		return;
	}


	/**
	 * Gets a single column's data from a single input file, to be written to a single temporary file (referenced by {@code }tmpFileWriter}.
	 * This method is primarily called by {@link CSVFileReader#listFilesFromFolder(File, FileWriter, GobiiFileColumn, GobiiLoaderInstruction, boolean, String)}
	 * @param file File to read from
	 * @param tmpFileWriter Writer to write temporary columns to
	 * @param column Column looked for
	 * @param loaderInstruction Instruction column is from
	 * @param first If this is the first file to be returned (can be set to false by calling method. Passed to callee. *Dragons*
	 * @param tmpFileName Name of the folder where temporary files can be stored
	 * @throws IOException when the requisite file is missing or cannot be read
	 */
	private void writeToTempFiles(File file, FileWriter tmpFileWriter, GobiiFileColumn column, GobiiLoaderInstruction loaderInstruction, boolean first, String tmpFileName) throws IOException{
		Scanner lr = null;
		String line = "";
		int counter;
		lr = new Scanner(file);
		GobiiColumnType type = column.getGobiiColumnType();
		switch(type){
		case CONSTANT:
			constant_values.put(column.getName(), column.getConstantValue());
			file_column_constant.put(tmpFileName, column.getName());
			tmpFileWriter.write(column.getConstantValue());
			break;
		case CSV_COLUMN:
    		//set line number to the starting row number
			counter = 0;
			while(lr.hasNextLine()){
				line = lr.nextLine();
				if(counter >= column.getrCoord() && line != null){
					if(!first) tmpFileWriter.write(NEWLINE);
					first = false;
					String[] row = line.split(loaderInstruction.getGobiiFile().getDelimiter());
					if(row.length > column.getcCoord()){
						String newValue = HelperFunctions.filter(row[column.getcCoord()], column.getFilterFrom(), column.getFilterTo(),column.getFindText(),column.getReplaceText());
						tmpFileWriter.write(newValue);
					}else{
						tmpFileWriter.write("");
					}
					countMax++;
				}
				counter++;
			}
    		if(countMax > maxSize){maxSize = countMax;}
    	break;
    	
		case CSV_ROW:
    		counter = 0;
    		while(lr.hasNextLine()){
    			line = lr.nextLine();
    			if(counter == column.getrCoord() && line != null){
    				if(!first) tmpFileWriter.write(NEWLINE);
    				first = false;
    				String[] row = line.split(loaderInstruction.getGobiiFile().getDelimiter());
    				//loop through the columns
    				for (int i=column.getcCoord(); i<row.length; i++){
    					String newValue = HelperFunctions.filter(row[i], column.getFilterFrom(), column.getFilterTo(),column.getFindText(),column.getReplaceText());
    					tmpFileWriter.write(newValue);
    					tmpFileWriter.write(NEWLINE);
    					countMax++;
    				}
    			}
    			counter++;
    		}
    		if(countMax > maxSize){maxSize = countMax;}
    	break;
		case CSV_BOTH:
    		counter = 0;
    		while(lr.hasNextLine()){
    			line = lr.nextLine();
    			if(counter >= column.getrCoord() && line != null){
    				if(!first) tmpFileWriter.write(NEWLINE);
    				first = false;
    				String tempStr = "";
    				String[] row = line.split(loaderInstruction.getGobiiFile().getDelimiter());
    				for (int i=column.getcCoord(); i<row.length; i++){
    					String newValue = HelperFunctions.filter(row[i], column.getFilterFrom(), column.getFilterTo(),column.getFindText(),column.getReplaceText());
    					tempStr = tempStr + (tempStr.length()>0?"\t":"") +newValue;
    				}
    				tmpFileWriter.write(tempStr);
    				countMax++;
    			}
    			counter++;
    		}
    		if(countMax > maxSize){maxSize = countMax;}
    	break;
		case AUTOINCREMENT:
			file_column_autoincrement.put(tmpFileName, column.getName());
		break;
		default:
			ErrorLogger.logError("CSVReader","No method for " + type);
			break;
		}
		lr.close();
	}
}
class ReaderThread implements Runnable{
	private CSVFileReaderInterface reader;
	private GobiiLoaderInstruction instruction;
	protected ReaderThread(CSVFileReaderInterface reader,GobiiLoaderInstruction instruction){
		this.reader=reader;
		this.instruction=instruction;
	}
	@Override
	public void run() {
		try {
			reader.processCSV(instruction);
		}
		catch(Exception e){
			ErrorLogger.logError("ReaderThread","Error processing file read",e);
		}
	}
}