package org.gobiiproject.gobiiprocess.digester.vcf;

import org.gobiiproject.gobiimodel.utils.FileSystemInterface;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiimodel.utils.ExternalFunctionCall;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.VcfParameters;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiiprocess.digester.GobiiFileReader;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static org.gobiiproject.gobiimodel.utils.ExternalFunctionCall.extern;
import static org.gobiiproject.gobiimodel.utils.ExternalFunctionCall.sarg;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rm;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.*;
import static org.gobiiproject.gobiimodel.utils.HelperIterators.getConstantIterator;
import static org.gobiiproject.gobiimodel.utils.HelperIterators.getFilteredIterator;

/**
 * Digester component for the reading of VCF Files. Used by {@link org.gobiiproject.gobiiprocess.digester.GobiiFileReader} in creating digest files from VCF sources.
 * Main method : parseInstruction
 * @author Josh L.S.
 *
 */
public class VCFFileReader {
	private String outputDelimiter ="\t";
	private String fileLocation;
	private String outLocation;
	private List<String> tempFiles = new LinkedList<String>();
	private String tpedFile;
	private String tempVCF;
	public boolean hasError=false;
	private static ExternalFunctionCall cut=extern("cut(unix)","cut");
	private String scriptsDirectory="/shared_data/gobii/loaders/";

	/**
	 * Creates a VCFFileReader object.
	 * @param scriptsDirectory Directory for assorted shell scripts used by the VCFFileReader.
	 * @param outputDelimiter Delimiter used for output. Defaults to the tab character.
	 */
	public VCFFileReader(String scriptsDirectory, String outputDelimiter){
		this(scriptsDirectory);
		this.outputDelimiter=outputDelimiter;
	}
	/**
	 * Creates a VCFFileReader object.
	 * @param scriptsDirectory Directory for assorted shell scripts used by the VCFFileReader.
	 */

	public VCFFileReader(String scriptsDirectory){
		this.scriptsDirectory=scriptsDirectory;
	}
	/**
	 * Creates a VCFFileReader object.
	 */
	public VCFFileReader() {
		//Default's on all parameters
	}

	/**
	 * Parses a single 'Loader Instruction', creating a digest.table file in the output directory.
	 * @param instruction GobiiLoaderInstruction object representing the instruction to load
	 * @return true on success, false on failure. This is a holdover from an earlier design
	 */
	public boolean parseInstruction(GobiiLoaderInstruction instruction, File destDir){
		ErrorLogger.logDebug("VCFFileReader","Beginning digest of " + instruction.getTable());
		VcfParameters params=instruction.getVcfParameters();
		String inFile= instruction.getGobiiFile().getSource()+"/"+GobiiFileReader.getSourceFileName(instruction.getGobiiFile());
		String outFile=new File(destDir,"digest."+instruction.getTable()).getAbsolutePath();
		String errorFile=instruction.getGobiiFile().getDestination()+"/ERRORS";//TODO: Better error file name
		VCFFileReader vfr=new VCFFileReader();
		vfr.setFile(inFile);
		vfr.setOutput(outFile);
		List<Iterator<String>> columnIterators = new LinkedList<>();
		List<String> tempFiles = new LinkedList<>();
		for(GobiiFileColumn c:instruction.getGobiiFileColumns()){
			GobiiColumnType type = c.getGobiiColumnType();
			ErrorLogger.logDebug("VCFFileReader","Creating column type "+type);
			Iterator<String> iter=null;
			switch(type) {
				case VCF_SAMPLE:
					String sampleTmp = outFile + ".samples.tmp";
					vfr.saveSampleFile(sampleTmp, null,errorFile);//Not implementing regular expressions
					//tempFiles.add(sampleTmp);
					iter = getFilteredIterator(sampleTmp, c.getFilterFrom(), c.getFilterTo());
					if (iter == null)
						ErrorLogger.logError("VCFFileReader", "Unable to create IUPAC reader for samples");
					columnIterators.add(iter);
					break;
				case VCF_MARKER:
					String markerTmp = outFile + ".markers.tmp";
					vfr.saveMarkerFile(inFile,markerTmp, errorFile);
					//tempFiles.add(markerTmp);
					ErrorLogger.logInfo("Iterators","Creating iterator from file "+markerTmp+" from "+c.getFilterFrom()+" to "+c.getFilterTo());
					iter = getFilteredIterator(markerTmp, c.getFilterFrom(), c.getFilterTo());
					if (iter == null)
						ErrorLogger.logError("VCFFileReader", "Unable to create IUPAC reader for markers");
					columnIterators.add(iter);
					break;
				case VCF_VARIANT:
					boolean convertToIUPAC = params.isToIupac();
					//convertToIUPAC=true;
					String IUPACTemp = outFile + ".IUPAC.tmp";

					if (convertToIUPAC) {
						vfr.saveIUPACFile(inFile,IUPACTemp, errorFile);
						tempFiles.add(IUPACTemp);
						iter = getFilteredIterator(IUPACTemp, c.getFilterFrom(), c.getFilterTo());
						if (iter == null)
							ErrorLogger.logError("VCFFileReader", "Unable to create IUPAC reader for variants");
						columnIterators.add(iter);
					} else {
						ErrorLogger.logError("VCFFileReader", "Non-IUPAC data storage is not implemented");
					}

					break;
				case VCF_METADATA: //This 'Column' is really a lot of tab-separated columns
					String metadataId = c.getName();

					String oFile = vfr.saveMetadataFile(metadataId, "GT", params, errorFile);
					ErrorLogger.logInfo("VCFFileReader", "Creating Metadata File at " + oFile);
					if (oFile == null) {
						ErrorLogger.logError("VCFFileReader", "Metadata File creation failed");
						break;
					}
					tempFiles.add(oFile);
					iter = getFilteredIterator(oFile, c.getFilterFrom(), c.getFilterTo());
					columnIterators.add(iter);
					break;
				case VCF_INFO:
					ErrorLogger.logError("VCFFileReader", "VCF Info columns unsupported");
					break;
				case CONSTANT:
					columnIterators.add(getConstantIterator(c.getConstantValue()));
					break;
				case VCF_MARKER_POS:
					ErrorLogger.logError("VCFFileReader", "Marker Position unsupported");
					columnIterators.add(getConstantIterator("0"));//TODO: Fix
					break;
				default:
					ErrorLogger.logError("VCFFileReader", "Cannot process type " + type.toString());
					break;
			}
		}
		//Begin processing

		ErrorLogger.logDebug("VCFFileReader","Beginning emitting of digest file "+vfr.outLocation);
		boolean done=false;
		try {
			PrintWriter pw=new PrintWriter(new File(vfr.outLocation));//Add 'digest.word' to the folder
			boolean first=true;
			for(GobiiFileColumn c:instruction.getGobiiFileColumns()){
				pw.write((first?"": outputDelimiter)+c.getName());
				first=false;
			}
			pw.write("\n");
			ErrorLogger.logDebug("VCFFileReader","Number of iterators "+columnIterators.size());
			while(!done){
				for(Iterator<String> iter:columnIterators){
					if(!iter.hasNext()) {
						ErrorLogger.logDebug("Iterators","Iterator empty "+iter.getClass().getName());
						done = true;
						break; //If any iterator is empty, we're done
					}
				}
				first=true;
				for(Iterator<String> iter:columnIterators){
					if(done)break;
					pw.write((first?"": outputDelimiter)+iter.next());
					first=false;
				}
				pw.write("\n");
				pw.flush(); //Something here has a 16384 character buffer overrun. :(
			}
		} catch (FileNotFoundException e) {
			ErrorLogger.logError("VCFFileReader","Error referencing file",e);
		}

		for(String file:tempFiles){
			rm(file);
		}
		return ErrorLogger.success();
	}
	

	
	private void setOutput(String out) {
		outLocation=out;
	}

	public boolean setFile(String fileLocation) {
		this.fileLocation=fileLocation;
		File inFile=new File(fileLocation);//Check that the file is real
		if(!inFile.exists()){
			return false;
		}
		return inFile.canRead();
	}


	
	//////
	// File Digester methods
	//
	//////

	/**
	 * Creates a TPED (transposed ped) file based on the VCF. This creates an IUPAC file with no headers, but
	 * reversed from the standard plink file format (otherwise plink will choke on it).
	 * Note, + symbols (Inserts) need to be removed from the data beforehand.
	 * @param inputFile VCF File location
	 * @param tpedFileStart creates a tped file at this location, plus a tpedFileStart.noPlus.vcf temporary file.
	 * @param errorFile temporary error file location
	 */
	private void setTpedFile(String inputFile,String tpedFileStart, String errorFile){
		ErrorLogger.logDebug("VCFFileReader","Reading file "+inputFile);
		ErrorLogger.logDebug("VCFFileReader", "awk -f "+scriptsDirectory+"etc/removePlus.awk "+inputFile + "  to "+tpedFileStart+".noPlus.vcf");
		if(!tryExec("awk -f "+scriptsDirectory+"etc/removePlus.awk "+inputFile,tpedFileStart+".noPlus.vcf",errorFile)){
			ErrorLogger.logError("VCFFileReader","error running awk script",errorFile);
		}
		ErrorLogger.logTrace("VCFFileReader","plink --vcf " +tpedFileStart+".noplus.vcf -out " + tpedFileStart + " --recode transpose --vcf-half-call m --const-fid 0");
		if(!tryExec("plink --vcf " +tpedFileStart+".noPlus.vcf -out " + tpedFileStart + " --recode transpose --vcf-half-call m --const-fid 0",null,errorFile)){//Creates outFilename.tped
			ErrorLogger.logError("VCFFileReader","Plink encode failed",errorFile);
		}
		//rm(tpedFileStart+".noPlus.vcf");
		
		this.tpedFile=tpedFileStart+".tped";
		this.tempFiles.add(tpedFile);
	}

	/**
	 * Saves a file containing marker names to 'outFilename' if 'inputFile' is a tped file
	 * @param inputFile VCF Input file
	 * @param outFilename Name of the output file (marker list)
	 * @param errorFile temporary error file
	 */
	private void saveMarkerFile(String inputFile,String outFilename,String errorFile){
		if(tpedFile==null)setTpedFile(inputFile,outFilename,errorFile);
		ErrorLogger.logDebug("VCFFileReader",scriptsDirectory+"etc/cutMarkerName.sh"+ " "+tpedFile + " "+outFilename);
		if(!tryExec(scriptsDirectory+"etc/cutMarkerName.sh"+ " "+tpedFile + " "+outFilename,null,errorFile)) { //List of markers
			ErrorLogger.logError("VCFFileReader","cut failed",errorFile);
		}
	}

	/**
	 * Used to filter a VCF file based on vcf arguments. Currently unused
	 * @param tempVCF VCF file string.
	 * @param params VCFParameters - list of input files VCF Parameters
	 * @param errorFile Temporary file for error logging
	 */
	private void saveTempVCF(String tempVCF,VcfParameters params,String errorFile){
		this.tempVCF=tempVCF;
			
		String filters = getFilterArguments(params);//Note, space padded on both sides
		tryFunc(extern("BCFTools","bcftools"," view"+filters+"-Ov -o "+tempVCF+" "+fileLocation),errorFile);
		tempFiles.add(tempVCF);
	}

	private String getFilterArguments(VcfParameters params) {
		Float minDepth=params.getMinDp();
		Float minQuality=params.getMinQ();
		Float maf=params.getMaf();
		
		String filter="";
		String mafTerm=" ";
		if(maf !=null){
			mafTerm = " -q "+maf+" ";
		}
		if(minDepth!=null)filter="MIN(DP)>"+minDepth;
		if(minQuality!=null){
			if(filter.equals(""))filter="QUAL>"+minQuality;
			else filter=filter+" & "+"QUAL>"+minQuality;
		}
		String filterTerm="";
		if(!filter.equals(""))filterTerm=" -f \""+filter+"\" ";
		return mafTerm+filterTerm;
	}

	/**
	 * Pulls metadata file out from VCF, taking a single metadata field from each column
	 * @param outFilename Metadata output file stem.
	 * @param metadata The specific metadata item to pull, EG (GT)
	 * @param params VCFParameters to filter by
	 * @param errorFile Temporary error file location
	 * @return
	 */
	private String saveMetadataFile(String outFilename, String metadata, VcfParameters params, String errorFile){
		String metadataFile=outFilename+".metadata."+metadata;
		if(tempVCF==null){
			saveTempVCF(outFilename+".tmp.vcf",params,errorFile);
		}
		tryFunc(extern("grep(unix)","grep","-v \"#\" "+tempVCF),outFilename+".noHeader",errorFile);
		tryFunc(cut.setArgs("-f9 + "+ outFilename+".noHeader"),outFilename+".genoList",errorFile); //List of markers
		tryFunc(cut.setArgs("--complement -f1,2,3,4,5,6,7,8,9 " + tempVCF),outFilename + ".noInfo",errorFile);
		BufferedReader genoList,vcfFile;
		PrintWriter writer;
		try{
			StringBuilder sb=new StringBuilder();
			genoList = new BufferedReader(new FileReader(outFilename+".genoList"));
			vcfFile = new BufferedReader(new FileReader(outFilename+".noHeader"));
			writer=new PrintWriter(metadataFile);
			
			String genoLine=null,vcfLine=null;
			try{
			genoLine=genoList.readLine();
			vcfLine=vcfFile.readLine();
			}catch(Exception e){
			}
			while(genoLine!=null&&vcfLine!=null){	
				int position=0;
				int index=genoLine.indexOf(metadata);
				if(index!=0){
					position=genoLine.substring(0, index-1).split(":").length;//0 to colon
				}
				String[] vcfElements=vcfLine.split("\t");
				for(String s:vcfElements){
					String geno=s.split(":")[position+1];//+1 for the 'genotype' in the beginning. 
					sb.append(geno+"\t");
				}
				sb.deleteCharAt(sb.length()-1);//Remove final tab
				writer.println(sb.toString());
				//Get a new line from both files
				genoLine=vcfLine=null;
				try{
					genoLine=genoList.readLine();
					vcfLine=vcfFile.readLine();
				}catch(Exception e){
					ErrorLogger.logError("VCFFileReader","Error saving metadata file",e);
				}
			}
			writer.close();
			genoList.close();
			vcfFile.close();
		}catch(Exception e){
			ErrorLogger.logError("VCFFileReader","Error saving metadata file",e);
		}

		rm(outFilename+".noHeader");
		rm(outFilename+".genoList");
		return metadataFile;
	}

	
	public void saveIUPACFile(String inputFile,String outFilename,String errorFile){
		if(tpedFile==null)setTpedFile(inputFile,outFilename,errorFile);
		
		tryExec("cut --complement -f1,2,3,4 -d \" \" " + tpedFile,outFilename+".cut",errorFile );
		tryExec("awk/tpedToBi.awk",outFilename+".bi", errorFile,outFilename+".cut");//Move 'Cut' file to 'Bi'
		
		tryExec("awk/BiToIUPAC.awk", outFilename,errorFile,outFilename+".bi");//Move 'BI' file to output
		rm(outFilename + ".cut");
		rm(outFilename + ".bi");
	}
	
	


	/**
	 * Optional method to filter sample file and save new VCF file based on bcftools and a regular expression
	 * @param outFilename
	 * @param sampleRegex
	 */
	public void saveSampleFile(String outFilename,String sampleRegex, String errorFile) {
		tryExec("bcftools query -l " + fileLocation,outFilename,errorFile);
		
		//Create a new 'filtered' .samples. Note: changes the base 'filename' to the new, filtered VCF file
		if(sampleRegex!=null){
			FileSystemInterface.mv(outFilename,outFilename+".slist");
			tryExec("grep " + sampleRegex + " "+outFilename+".slist",outFilename,errorFile);
			FileSystemInterface.rm(outFilename+".slist");
			tryExec("bcftools convert -S " + outFilename + " -o "+fileLocation+".vcf "+fileLocation);
			fileLocation=fileLocation+".vcf";
		}
	}


	/**
	 * Saves a file of 'Info' items, two dimensional arrays of infomration based on an info column.
	 * So if the data is GT:GQ:DR and the metadata parameter is GQ, only the middle element is pulled from the VCF matrix.
	 * @param outFilename temp file to put info to
	 * @param metadata the id of the metadata element
	 * @param params VCF calling parameters, currently ignored
	 * @param errorFile Where errors should be saved, a unique temporary file handle
	 * @return
	 */
	private String saveInfoFile(String outFilename, String metadata, VcfParameters params, String errorFile){
		//grep -v "#" test100.vcf | cut -f8
		//grep -v "#" test100.vcf | cut -f8 | sed 's/^.*CNV=\([^;]*\).*/\1/g'

		//grep -v "#" test100.vcf | cut -f8 | sed '/^.*TGN=\([^;]*\).*/!d;s//\1/'

		//grep -v "#" test100.vcf | cut -f8 | sed 's/.*TGN=\([^;]*\).*/\1/g' | sed 's/.*=.*//g'

		String metadataFile=outFilename+".metadata."+metadata;
		if(tempVCF==null){
			saveTempVCF(outFilename+".tmp.vcf",params,errorFile);
		}
		tryExec("grep -v \"#\" "+tempVCF,outFilename+".noHeader",errorFile);
		tryFunc(sarg(cut,"-f9 " + outFilename+".noHeader"),outFilename+".genoList",errorFile); //List of markers
		tryFunc(sarg(cut,"--complement -f1,2,3,4,5,6,7,8,9 "+outFilename+".noHeader"),outFilename + ".noInfo",errorFile); //Data with no info columns
		BufferedReader genoList,vcfFile;
		PrintWriter writer;
		try{
			StringBuilder sb=new StringBuilder();
			genoList = new BufferedReader(new FileReader(outFilename+".genoList"));
			vcfFile = new BufferedReader(new FileReader(outFilename+".noInfo"));
			writer=new PrintWriter(metadataFile);

			String genoLine=null,vcfLine=null;
			try{
				genoLine=genoList.readLine();
				vcfLine=vcfFile.readLine();
			}catch(Exception e){
			}
			while(genoLine!=null&&vcfLine!=null){
				int position=0;
				int index=genoLine.indexOf(metadata);
				if(index!=0){
					position=genoLine.substring(0, index-1).split(":").length;//0 to colon
				}
				String[] vcfElements=vcfLine.split("\t");
				for(String s:vcfElements){
					String geno=s.split(":")[position+1];//+1 for the 'genotype' in the beginning.
					sb.append(geno).append("\t");
				}
				sb.deleteCharAt(sb.length()-1);//Remove final tab
				writer.println(sb.toString());
				//Get a new line from both files
				genoLine=vcfLine=null;
				try{
					genoLine=genoList.readLine();
					vcfLine=vcfFile.readLine();
				}catch(Exception e){}
			}
			writer.close();
			genoList.close();
			vcfFile.close();
		}catch(Exception e){
			ErrorLogger.logError("VCFReader","Error in processing info file",e);
			e.printStackTrace();
		}

		rm(outFilename+".noHeader");
		rm(outFilename+".genoList");
		return metadataFile;
	}

}


