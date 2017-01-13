package org.gobiiproject.gobiiprocess.extractor.hapmap;

import org.gobiiproject.gobiimodel.utils.HelperFunctions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

//Note: This is not currently in use. Yaw has created a python script to do it.
public class HapmapTransformer {

	private static String delimiter = "\t";
	private static final String NEWLINE = "\n";
	public static boolean generateFile(String markerFile, String sampleFile, String projectFile, String tempFolder, String outFile, String errorFile) throws IOException{
		boolean success = true;
		int maxCount = 0;
		String addtlFilePath = tempFolder + "additional_columns.txt"; // this is for the ff columns: assembly#, center, protlsid, assaylsid, pannellsid, qccode. venice told me that they are almost usually NA. Not sure though.
		//jdl232 COMMENT: This is a correct assumption based on what I've heard from both Prasad and Liz. 
		FileWriter tmpFileWriter = new FileWriter(addtlFilePath);
		Scanner lr = new Scanner(markerFile);
		String addtlHeaderStr = "assembly#" + delimiter + "center" + delimiter + "protlsid" + delimiter + "assaylsid" + delimiter + "pannellsid" + delimiter + "qccode";
		
		int counter = 0;
		//so that the rows have the same count;
		while(lr.hasNextLine()){
			if(counter == 0){
				tmpFileWriter.write(addtlHeaderStr);
			}else{
				tmpFileWriter.write(NEWLINE);
				tmpFileWriter.write("NA"); //default to NA.
			}
			counter++;
		}
		tmpFileWriter.close();
		
		//construct sample columns
		//this is the part where I'm not sure. What I do is I get distinct sample names then convert it as columns in the file. But I don't know how to make sure that values for the "samples" are in the right row.
		String parsedFilePath = tempFolder + "parsedSampleFile.txt";
		HelperFunctions.tryExec("cut -d "+delimiter +" -f2,5 " +sampleFile,parsedFilePath,errorFile); //pull sample names and value from samplefile and put in a temp file 
		String fileListString = "";
		String line = "";
		FileWriter fw=null;
		lr = new Scanner(parsedFilePath);
		while(lr.hasNextLine()){
			line = lr.nextLine();
			String[] row = line.split(delimiter);
			if(row.length > 1 ){
				String sampleName = tempFolder + "tempFiles/"+row[0]+".txt";
				fileListString = fileListString + " " + sampleName;
				File f = new File(sampleName);
				if(f.exists()){ //if temp file exists already, append value
					fw = new FileWriter(sampleName, true);
					fw.write(NEWLINE);
					fw.write(row[1]);
				}else{
					fw = new FileWriter(sampleName, false);
					fw.write(row[0]);	//make the sample names as columns
					fw.write(NEWLINE);
					fw.write(row[1]);
				}
			}
		}
		fw.close();
		
		
		/* merge all files into one output file */
		String delimiterString = delimiter + "\\n";
		String commandStr = "paste -d"+delimiterString+markerFile+addtlFilePath+fileListString;

        success&=HelperFunctions.tryExec(commandStr, outFile,"err.log" );
		//remove temp files
        HelperFunctions.tryExec("rm " + addtlFilePath);
        HelperFunctions.tryExec("rm " + parsedFilePath);
        HelperFunctions.tryExec("rm" + fileListString);
        HelperFunctions.tryExec("rm -r " +tempFolder+"tempFiles"); //remove directory
        
		return success;
	}
	
}
