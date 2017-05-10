package org.gobiiproject.gobiiprocess.extractor.flapjack;

import org.gobiiproject.gobiimodel.utils.HelperFunctions;

import java.io.File;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rm;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;

/**
 * Generates the output files for Flapjack import.
 * This class is split into generating the .map and .genotype files.
 */
public class FlapjackTransformer {

	/**
	 *
	 * @param markerFile marker file name *extended* if the extended flag is true (has map info)
	 * @param sampleFile sample file name
	 * @param chrLengthFile chromosome length file
	 * @param datasetId ID number of the dataset (unused)
	 * @param tempDir directory for temp (and output) files. This function does not distinguish between the two
	 * @param outFile output map file
	 * @param errorFile temporary file to redirect error stream to
	 * @param extended if the marker file is extended
	 * @return if successful (always true, ignore)
	 */
	public static boolean generateMapFile(String markerFile, String sampleFile, String chrLengthFile, String tempDir, String outFile, String errorFile, boolean extended) {
   /*
   		1) create the output header
   		2) remove the headers from chrLengthFile
		3) cut the columns related to the marker, the chromosome, and the position from markerFile
		4) remove the headers from such columns
		5) cat all the three files into the new output file
		Example for the marker file process: cut -f1,2,4 derp.txt | tail -n +2 > derp2.txt
	*/
   boolean chrLengthsExists=new File(chrLengthFile).exists();
		HelperFunctions.tryExec("echo # fjFile = MAP",tempDir+"map.header", errorFile);

		if(chrLengthsExists) {
			HelperFunctions.tryExec("tail -n +2 "+chrLengthFile, tempDir+"map.chrLengths",
					errorFile);
		}
		String locations="1,28,31";//extended marker locations of Marker Name (1) Linkage Group Name(28) and MarkerLinkageGroupStart(31)
		if(!extended)locations="1";
		HelperFunctions.tryExec("cut -f"+locations+" "+markerFile,tempDir+"tmp",errorFile);//Marker Name, Linkage Group Name, Marker Linkage Group Start
		HelperFunctions.tryExec("tail -n +2 "+tempDir+"tmp",tempDir+"map.body",errorFile);
		rm(tempDir+"tmp");
		
		HelperFunctions.tryExec("cat "+tempDir+"map.header "+
						(chrLengthsExists?tempDir:"")+ (chrLengthsExists?"map.chrLengths ":"")+
						tempDir+"map.body", outFile, errorFile);

		rm(tempDir+"map.header");
		rmIfExist(tempDir+"map.chrLengths");
		rmIfExist(chrLengthFile);
		rm(tempDir+"map.body");

		return true;
	}

	/**
	 * Generates a genotype file from the MDE outputs
	 * @param markerFile MDE output of markers
	 * @param sampleFile Sample MDE output file
	 * @param genotypeFile Genotype MDE output file
	 * @param datasetId Unused
	 * @param tempDir Directory to write temporary files
	 * @param outFile Output genotype file
	 * @param errorFile Temporary file to write error logs to
	 * @return true on success
	 */
	public static boolean generateGenotypeFile(String markerFile, String sampleFile, String genotypeFile, String tempDir, String outFile,String errorFile){
		/**
		 * Genotype file -
1) create response file
  # fjFile = GENOTYPE
2)
cut marker names
transpose marker names
3) attach to top of genotype matrix
4) get sample names
5) add blank in front of first name
6) paste sample names to genotype file
7) add response file to top
		 */
		//TODO: run slash adder on genotype file
		String tempFile = tempDir + "tmp";
		String markerList = tempDir + "genotype.markerList";
		String inverseMarkerList = tempDir + "genotype.markerIList";
		
		HelperFunctions.tryExec("echo # fjFile = GENOTYPE",tempDir+"map.response",errorFile);
		HelperFunctions.tryExec("echo ",tempDir+"blank.file",errorFile);
		
		//Markerlist contains all marker names, sequentially

		HelperFunctions.tryExec("cut -f1 "+markerFile, tempFile,errorFile);
		HelperFunctions.tryExec("tail -n +2 "+tempFile, markerList,errorFile);
		rm(tempFile);
		//Adds one line, then rotates 90 degrees, so each marker is a tab
		HelperFunctions.tryExec("cat "+tempDir+"blank.file "+markerList, tempFile,errorFile);
		HelperFunctions.tryExec("tr '\\n' '\\t'",inverseMarkerList,errorFile, tempFile);//Input redirection wheeee
		rm(tempFile);
		//Note, this file has no line ending.
		
		//Sample list is all samples, no response
		HelperFunctions.tryExec("cut -f1 "+sampleFile, tempFile,errorFile);
		HelperFunctions.tryExec("tail -n +2 "+tempFile,tempDir+"genotype.sampleList",errorFile);
		rm(tempFile);
		
		HelperFunctions.tryExec("paste "+ tempDir+"genotype.sampleList "+genotypeFile,tempDir+"sample.matrix",errorFile);//And now we have a matrix with the samples attached
		HelperFunctions.tryExec("cat "+tempDir+"map.response "+ inverseMarkerList+" " + tempDir+"blank.file "+ tempDir+"sample.matrix",outFile,errorFile);

		rm(tempDir+"map.response");
		rm(markerList);
		rm(inverseMarkerList);
		rm(tempDir+"genotype.sampleList");
		rm(tempDir+"sample.matrix");
		rm(tempDir+"blank.file");
		
		return true;
	}
}
