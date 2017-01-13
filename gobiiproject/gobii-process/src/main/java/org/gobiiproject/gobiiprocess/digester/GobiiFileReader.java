package org.gobiiproject.gobiiprocess.digester;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

import org.apache.commons.cli.*;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.utils.FileSystemInterface;
import org.gobiiproject.gobiimodel.tobemovedtoapimodel.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.impl.LoaderInstructionsDAOImpl;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.config.CropDbConfig;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.email.DigesterMessage;
import org.gobiiproject.gobiimodel.utils.email.MailInterface;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.digester.HelperFunctions.PGArray;
import org.gobiiproject.gobiiprocess.digester.csv.CSVFileReader;
import org.gobiiproject.gobiiprocess.digester.vcf.VCFFileReader;
import org.gobiiproject.gobiiprocess.digester.vcf.VCFTransformer;

import static org.gobiiproject.gobiimodel.types.DataSetType.VCF;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.mv;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rm;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.parseInstructionFile;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.tryExec;
import static org.gobiiproject.gobiimodel.utils.error.ErrorLogger.logError;

/**
 * Base class for processing instruction files. Start of chain of control for Digester. Takes first argument as instruction file, or promts user.
 * The File Reader runs off the Instruction Files, which tell it where the input files are, and how to process them.
 * {@link CSVFileReader} and {@link VCFFileReader} deal with specific file formats. Overall logic and program flow come from this class.
 *
 * This class deals with external commands and scripts, and coordinates uploads to the IFL and directly talks to HDF5 and MonetDB.
 * @author jdl232 Josh L.S.
 */
public class GobiiFileReader {
	private static String rootDir="../";
	private static String loaderScriptPath,extractorScriptPath,pathToHDF5;
	private static final String VARIANT_CALL_TABNAME="matrix";
	private static final String	LINKAGE_GROUP_TABNAME="linkage_group";
	private static final String GERMPLASM_PROP_TABNAME="germplasm_prop";
	private static final String GERMPLASM_TABNAME="germplasm";
	private static final String MARKER_TABNAME="marker";
	private static String pathToHDF5Files;
	private static boolean verbose;
	private static String errorLogOverride;
	private static String propertiesFile;

	/**
	 * Main class of Digester Jar file. Uses command line parameters to determine instruction file, and runs whole program.
	 * @param args See Digester.jar -? to get a list of arguments
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws Exception {
		//Section - Setup
		Options o = new Options()
         		.addOption("v", "verbose", false, "Verbose output")
         		.addOption("e", "errlog", true, "Error log override location")
         		.addOption("r", "rootDir", true, "Fully qualified path to gobii root directory")
         		.addOption("c","config",true,"Fully qualified path to gobii configuration file")
         		.addOption("h", "hdfFiles", true, "Fully qualified path to hdf files");
        DigesterMessage dm = new DigesterMessage();
		CommandLineParser parser = new DefaultParser();
        try{
			CommandLine cli = parser.parse( o, args );
            if(cli.hasOption("rootDir")) rootDir = cli.getOptionValue("rootDir");
            if(cli.hasOption("verbose")) verbose=true;
            if(cli.hasOption("errLog")) errorLogOverride = cli.getOptionValue("errLog");
            if(cli.hasOption("config")) propertiesFile = cli.getOptionValue("config");
            if(cli.hasOption("hdfFiles")) pathToHDF5Files = cli.getOptionValue("hdfFiles");
            args=cli.getArgs();//Remaining args passed through
                
		}catch(org.apache.commons.cli.ParseException exp ) {
			new HelpFormatter().printHelp("java -jar Digester.jar ","Also accepts input file directly after arguments\n" +
                		                  "Example: java -jar Digester.jar -c /home/jdl232/customConfig.properties -v /home/jdl232/testLoad.json",o,null,true);
               System.exit(2);
		}
		
     	extractorScriptPath=rootDir+"extractors/";
     	loaderScriptPath=rootDir+"loaders/";
     	pathToHDF5=loaderScriptPath+"hdf5/bin/";
    	
    	if(propertiesFile==null)propertiesFile=rootDir+"config/gobii-web.properties";
		
		boolean success=true;
		Map<String,File> loaderInstructionMap = new HashMap<>();//Map of Key to filename
		List<String> loaderInstructionList=new ArrayList<String>(); //Ordered list of loader instructions to execute, Keys to loaderInstructionMap
		DataSetType dst=null;
		DataSetOrientationType dso=null;
		
		ConfigSettings configuration=null;
		try {
			configuration = new ConfigSettings(propertiesFile);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		MailInterface mailInterface=new MailInterface(configuration);

		String instructionFile=null;
		if(args.length==0 ||args[0]==""){
			Scanner s=new Scanner(System.in);
			System.out.println("Enter Loader Instruction File Location:");
			instructionFile=s.nextLine();
		}
		else{
			instructionFile=args[0];
		}
		
		//Error logs go to a file based on crop (for human readability) and 
		dm.addPath("instruction file",new File(instructionFile).getAbsolutePath());
		ErrorLogger.logInfo("Digester","Beginning read of "+instructionFile);
		List<GobiiLoaderInstruction> list= parseInstructionFile(instructionFile);
		if(list==null || list.isEmpty()){
			logError("Digester","No instruction for file "+instructionFile);
			return;
		}
		GobiiLoaderInstruction zero=list.iterator().next();
		Integer dataSetId=zero.getDataSetId();

		dm.addIdentifier("Project",zero.getProject());
		dm.addIdentifier("Platform",zero.getPlatform());
		dm.addIdentifier("Experiment",zero.getExperiment());
		dm.addIdentifier("Dataset",zero.getDataSet());
		dm.addIdentifier("Mapset",zero.getMapset());
		dm.addIdentifier("Dataset Type",zero.getDatasetType());


		String dstFilePath=HelperFunctions.getDestinationFile(zero);//Intermediate 'file'
		File dstDir=new File(dstFilePath);
		if(!dstDir.isDirectory()){ //Note: if dstDir is a non-existant
			dstDir=new File(dstFilePath.substring(0, dstFilePath.lastIndexOf("/")));
		}
		dm.addPath("destination directory",dstDir.getAbsolutePath());//Convert to directory
		dm.addPath("input directory",zero.getGobiiFile().getSource());

		String crop=zero.getGobiiCropType();
		if(crop==null) crop=divineCrop(instructionFile);
		Path cropPath = Paths.get(rootDir+"crops/"+crop.toLowerCase());
		if (!(Files.exists(cropPath) &&
			  Files.isDirectory(cropPath))) {
			logError("Digester","Unknown Crop Type: "+crop);
			return;
		}
		CropConfig cropConfig= null;
		try {
			cropConfig = configuration.getCropConfig(crop);
		} catch (Exception e) {
			logError("Digester","Unknown loading error",e);
			return;
		}
		if (cropConfig == null) {
			logError("Digester","Unknown Crop Type: "+crop+" in the Configuration File");
			return;
		}
		if(pathToHDF5Files==null)pathToHDF5Files=cropPath.toString()+"/hdf5/";

		String errorPath=getLogName(zero,cropConfig,crop);

		//TODO: HACK - Job's name is 
		String jobName = getJobName(crop,list);
		String jobUser=zero.getContactEmail();
		dm.setUser(jobUser);

		String logDir=configuration.getFileSystemLog();
		if(logDir!=null) {
			String logFile=logDir+"/"+jobUser.substring(0,jobUser.indexOf('@'))+"_"+getSourceFileName(zero.getGobiiFile())+".log";
			ErrorLogger.logDebug("Error Logger","Moving error log to "+logFile);
			ErrorLogger.setLogFilepath(logFile);
			dm.addPath("Error Log",logFile);
			ErrorLogger.logDebug("Error Logger","Moved error log to "+logFile);
		}


		//Section - Processing
		ErrorLogger.logTrace("Digester","Beginning List Processing");
		success=true;
		for(GobiiLoaderInstruction inst:list){
			if(inst==null){
				logError("Digester","Missing or malformed instruction in " + instructionFile );
				continue;
			}
			if(dataSetId==null){
				dataSetId=inst.getDataSetId();//Pick it up from relevant instruction
			}
			GobiiFile file = inst.getGobiiFile();
			if(file==null){
				logError("Digester","Instruction " + instructionFile + " Table " + inst.getTable() + " has bad 'file' column" );
				continue;
			}
			GobiiFileType instructionFileType = file.getGobiiFileType();
			if(instructionFileType==null){
				logError("Digester","Instruction " + instructionFile + " Table " + inst.getTable() + " has missing file format" );
				continue;
			}
			
			switch(inst.getGobiiFile().getGobiiFileType()){
			case VCF:
				//INTENTIONAL FALLTHROUGH
			case GENERIC:
				CSVFileReader reader = new CSVFileReader(dstDir.getAbsolutePath(),"/");
				reader.processCSV(inst);
				break;

			case HAPMAP: //This is currently not used by the loaderUI
				String tmpFile=new File(dstDir,inst.getTable()+".tmp").getAbsolutePath();//DestinationDirectory plus table name
				ArrayList<GobiiLoaderInstruction> justTheOne=new ArrayList<>();
				justTheOne.add(inst);
				try {
					new LoaderInstructionsDAOImpl().writeInstructions(tmpFile, justTheOne );
				} catch (GobiiDaoException e) {
					logError("GobiiDAO","Instruction Writing Error",e);
				}
				HelperFunctions.tryExec(loaderScriptPath+"etc/parse_hmp.pl"+" "+tmpFile, null, errorPath);
				break;
			default:
				System.err.println("Unable to deal with file type " + inst.getGobiiFile().getGobiiFileType());
				break;
			}

			//Section - Matrix Post-processing
			//Dataset is the first non-empty dataset type
			for(GobiiFileColumn gfc:inst.getGobiiFileColumns()){
				if(gfc.getDataSetType()!=null){
					dst=gfc.getDataSetType();
					if(inst.getGobiiFile().getGobiiFileType().equals(GobiiFileType.VCF)){
						dst=VCF;
					}
					if(gfc.getDataSetOrientationType()!=null)dso=gfc.getDataSetOrientationType();
					break;
				}
			}
			if(dst!=null && inst.getTable().equals(VARIANT_CALL_TABNAME)){
				errorPath=getLogName(inst, cropConfig, crop, "Matrix_Processing"); //Temporary Error File Name
				String function=null;
				boolean functionStripsHeader=false;
				String fromFile=HelperFunctions.getDestinationFile(inst);
				String toFile=HelperFunctions.getDestinationFile(inst)+".2";
				boolean hasFunction=false;
				switch(dst){
				case NUCLEOTIDE_2_LETTER:
						function="python "+loaderScriptPath+"etc/SNPSepRemoval.py";
						functionStripsHeader=true;
						break;
					case IUPAC:
						function=loaderScriptPath+"etc/IUPACmatrix_to_bi.pl tab";
						break;
					case SSR_ALLELE_SIZE:
						//No Translation Needed. Done before GOBII
						break;
					case DOMINANT_NON_NUCLEOTIDE:
						//No Translation Needed. Done before GOBII
						break;
					case CO_DOMINANT_NON_NUCLEOTIDE:
						//No Translation Needed. Done before GOBII
						break;
					case VCF:
						hasFunction=true;
						File markerFile=loaderInstructionMap.get(MARKER_TABNAME);
						String markerFilename=markerFile.getAbsolutePath();
						String markerTmp=new File(markerFile.getParentFile(),"marker.mref").getAbsolutePath();
						generateMarkerReference(markerFilename,markerTmp,errorPath);
						new VCFTransformer(markerTmp,fromFile,toFile);
						break;
					default:System.err.println("Unknown type "+dst.toString());break;
				}
				if(function!=null){
					hasFunction=true;
					//Try running script (from -> to), then replace original file with new one.
					success&=HelperFunctions.tryExec(function+" "+fromFile+" "+toFile,null,errorPath);
					rm(fromFile);
					
				}
				if(!hasFunction) {
					mv(fromFile, toFile);
				}
				
				//toFile now contains data, we move it back to original position with second transformation (swap)
				
				if(!functionStripsHeader){	
					success&=HelperFunctions.tryExec("tail -n +2 ",fromFile,errorPath,toFile);
					rm(toFile);
				}else{
					success&=HelperFunctions.tryExec("mv "+toFile+" "+fromFile);
				}
				
				boolean isSampleFast=false;
				if(DataSetOrientationType.SAMPLE_FAST.equals(dso))isSampleFast=true;
				if(isSampleFast){
					//Rotate to marker fast before loading it - all data is marker fast in the system
					HelperFunctions.tryExec("python "+loaderScriptPath+"TransposeMatrix.py -i " + fromFile);
				}
			}
			
			String instructionName=inst.getTable();
			loaderInstructionMap.put(instructionName, new File(HelperFunctions.getDestinationFile(inst)));
			loaderInstructionList.add(instructionName);//TODO Hack - for ordering
			if(LINKAGE_GROUP_TABNAME.equals(instructionName)||GERMPLASM_TABNAME.equals(instructionName)||GERMPLASM_PROP_TABNAME.equals(instructionName)){
				success&=HelperFunctions.tryExec(loaderScriptPath+"LGduplicates.py -i "+HelperFunctions.getDestinationFile(inst));
			}
			if(MARKER_TABNAME.equals(instructionName)){//Convert 'alts' into a jsonb array
				String dest=HelperFunctions.getDestinationFile(inst);
				String tmp = dest+".tmp";
				success&=HelperFunctions.tryExec("mv "+dest+" "+tmp);
				new PGArray(tmp,dest,"alts").process();
			}
		}
		
		if(success){

			errorPath=getLogName(zero, cropConfig, crop, "IFLs");
			String pathToIFL=loaderScriptPath+"postgres/gobii_ifl/gobii_ifl.py";
			String outputDir = null;
			try {
				outputDir = " -o " + configuration.getProcessingPath(crop, GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES);
			} catch(Exception e) {
				logError("Path Retrieval", "Error retrieving intermediate file path", e);
			}
			String connectionString=" -c "+HelperFunctions.getPostgresConnectionString(cropConfig);
			
			//Load PostgreSQL
			boolean loadedData=false;
			for(String key:loaderInstructionList){
				if(!VARIANT_CALL_TABNAME.equals(key)){
					int totalLines= FileSystemInterface.lineCount(loaderInstructionMap.get(key).getAbsolutePath());
					String inputFile=" -i "+loaderInstructionMap.get(key);
					String outputFile=outputDir; //Output here is temporary files
					ErrorLogger.logInfo("Digester","Running IFL: "+pathToIFL+connectionString+inputFile+outputFile);
					//Lines affected returned by method call
					int fileLines=HelperFunctions.iExec(pathToIFL+connectionString+inputFile+outputFile,errorPath);

					//Default to 'we had an error'
					String totalLinesVal="error";
					String linesLoadedVal="error";
					String duplicateLinesVal="error";
					//If total lines/file lines less than 0, something's wrong. Also if total lines is < changed, something's wrong.
					if((totalLines>0) && (fileLines >=0) &&(totalLines>fileLines)){
						if(fileLines>0){
							loadedData=true;
						}
						totalLinesVal=(totalLines-1)+"";
						linesLoadedVal=(fileLines)+"";//Header
						duplicateLinesVal=((totalLines-1)-fileLines)+"";
					}
					else{
						ErrorLogger.logDebug("FileReader","Failed lines check with total lines:"+totalLines+" and loaded lines:"+fileLines);
					}
					dm.addEntry(key,totalLinesVal,linesLoadedVal,duplicateLinesVal);

				}
			}
			if(!loadedData)ErrorLogger.logError("FileReader","No Data Loaded, may be duplicates?");
			//Load Monet/HDF5
			errorPath=getLogName(zero, cropConfig, crop, "Matrix_Upload");
			String variantFilename="DS"+dataSetId;
			File variantFile=loaderInstructionMap.get(VARIANT_CALL_TABNAME);
			String markerFileLoc=pathToHDF5Files+"DS"+dataSetId+".marker_id";
			String sampleFileLoc=pathToHDF5Files+"DS"+dataSetId+".dnarun_id";

		if(variantFile!=null && dataSetId==null){
				logError("Digester","Data Set ID is null for variant call");
		}
		if((variantFile!=null)&&dataSetId!=null){
			String loadVariantMatrix=loaderScriptPath+"monet/loadVariantMatrix.py";
			//python loadVariantMatrix.py <Dataset Name> <Dataset_Identifier.variant> <Dataset_Identifier.marker_id> <Dataset_Identifier.dnarun_id> <hostname> <port> <dbuser> <dbpass> <dbname>
			if(false) {//TODO - Turned off MonetDB
				CropDbConfig monetConf = cropConfig.getCropDbConfig(GobiiDbType.MONETDB);
				String loadVariantUserPort = monetConf.getHost() + " " + monetConf.getPort() + " " + monetConf.getUserName() + " " + monetConf.getPassword() + " " + monetConf.getDbName();
				generateIdLists(cropConfig, markerFileLoc, sampleFileLoc, dataSetId, errorPath);
				ErrorLogger.logDebug("MonetDB", "python " + loadVariantMatrix + " DS" + dataSetId + " " + variantFile.getPath() + " " + new File(markerFileLoc).getAbsolutePath() + " " + new File(sampleFileLoc).getAbsolutePath() + " " + loadVariantUserPort);
				HelperFunctions.tryExec("python " + loadVariantMatrix + " DS" + dataSetId + " " + variantFile.getPath() + " " + new File(markerFileLoc).getAbsolutePath() + " " + new File(sampleFileLoc).getAbsolutePath() + " " + loadVariantUserPort, null, errorPath);
				//Clean up marker and sample data
				rm(markerFileLoc);
				rm(sampleFileLoc);
			}
				//HDF-5
				//Usage: %s <datasize> <input file> <output HDF5 file
				String loadHDF5=pathToHDF5+"loadHDF5";
				dm.addPath("matrix directory",pathToHDF5Files);
				String HDF5File=pathToHDF5Files+"DS_"+dataSetId+".h5";
				int size=0;
				switch(dst){		
					case NUCLEOTIDE_2_LETTER: case IUPAC:case VCF:
						size=2;break;
					case SSR_ALLELE_SIZE:size=8;break;
					case CO_DOMINANT_NON_NUCLEOTIDE:
					case DOMINANT_NON_NUCLEOTIDE:size=1;break;
					default:
						logError("Digester","Unknown type "+dst.toString());break;
				}
			ErrorLogger.logInfo("Digester","Running HDF5 Loader. HDF5 Generating at "+HDF5File);
			HelperFunctions.tryExec(loadHDF5+" "+size+" "+variantFile.getPath()+" "+HDF5File,null,errorPath);
			updateValues(configuration, crop, dataSetId,variantFilename, HDF5File);
			rm(variantFile.getPath());
			}
			if(success && ErrorLogger.success()){
				ErrorLogger.logInfo("Digester","Successfully Uploaded files");
			}
			else{
				ErrorLogger.logWarning("Digester","Unsuccessfully Uploaded files");
			}
		}//endif(success)
		else{
			ErrorLogger.logWarning("Digester","Unsuccessfully Generated files");
		}
		
		try{
			dm.setBody(jobName,ErrorLogger.getFirstErrorReason(),ErrorLogger.success(),ErrorLogger.getAllErrorStringsHTML());
			mailInterface.send(dm);
		}catch(Exception e){
			ErrorLogger.logError("MailInterface","Error Sending Mail",e);
		}
		HelperFunctions.completeInstruction(instructionFile,configuration.getProcessingPath(crop, GobiiFileProcessDir.LOADER_DONE));
	}

	/**
	 * Returns a human readable name for the job.
	 * @param cropName Name of the crop being run
	 * @param list List of instructions to read from
	 * @return a human readable name for the job
	 */
	private static String getJobName(String cropName, List<GobiiLoaderInstruction> list) {
		cropName=cropName.charAt(0)+cropName.substring(1).toLowerCase();// MAIZE -> Maize
		String jobName=cropName + " digest of ";
		String source = getSourceFileName(list.get(0).getGobiiFile());
		jobName+=source;
		
		return jobName;
	}

	/**
	 * Converts the File input into the FIRST of the source files.
	 * @param file Reference to Instruction's File object.
	 * @return String representation of first of source files
	 */
	public static String getSourceFileName(GobiiFile file) {
		String source=file.getSource();
		File sourceFolder=new File(source);
		File[] f=sourceFolder.listFiles();
		if(f.length!=0) source=f[0].getName();
		else{
			source=sourceFolder.getName();//Otherwise we get full paths in source.
		}
		return source;
	}

	/**
	 * Generates a log file location given a crop name, crop type, and process ID. (Given by the process calling this method).
	 *
	 * Currently works by placing logs in the intermediate file directory.
	 * @param config Crop configuration
	 * @return The logfile location for this process
	 */
	private static String getLogName(GobiiLoaderInstruction gli,CropConfig config,String cropName){
		String destination=gli.getGobiiFile().getDestination();
		String table=gli.getTable();
		return destination +"/"+cropName+"_Table-"+table+".log";
	}
	/**
	 * Generates a log file location given a crop name, crop type, and process ID. (Given by the process calling this method).
	 * 
	 * Currently works by placing logs in the intermediate file directory.
	 * @param config Crop configuration
	 * @return The logfile location for this process
	 */
	private static String getLogName(GobiiLoaderInstruction gli,CropConfig config,String cropName, String process){
		String destination=gli.getGobiiFile().getDestination();
		return destination +"/"+cropName+"_Process-"+process+".log";
	}
	/**
	 * Determine crop type by looking at the intruction file's location for the name of a crop.
	 * @param instructionFile
	 * @return GobiiCropType
	 */
	private static String divineCrop(String instructionFile) {
		String upper=instructionFile.toUpperCase();
		String from="/CROPS/";
		int fromIndex=upper.indexOf(from)+from.length();
		String crop=upper.substring(fromIndex,upper.indexOf('/',fromIndex));
		return crop;
	}

	/**
	 * Generates appropriate monetDB files from the MDE by reverse-digesting the data we just loaded.
     * Reason - Ensures Postgres and MonetDB are in sync
	 * @param cropConfig Connection String
	 * @param markerFile No header
	 * @param dnaRunFile With header
	 * @param dsid Because
	 * @param errorFile temporary file to store error information in
	 */
	private static void generateIdLists(CropConfig cropConfig,String markerFile,String dnaRunFile,int dsid,String errorFile){
		//Create files and get paths because gobii_mde must run on absolute paths, not relative ones
		markerFile=new File(markerFile).getAbsolutePath();
		dnaRunFile=new File(dnaRunFile).getAbsolutePath();
		String gobiiIFL="python " + extractorScriptPath+"postgres/gobii_mde/gobii_mde.py"+" -c "+HelperFunctions.getPostgresConnectionString(cropConfig)+
			" -m "+markerFile+".tmp"+
			" -s "+dnaRunFile+".tmp"+
			" -d "+dsid;
		ErrorLogger.logDebug("MonetDB",gobiiIFL);
		tryExec(gobiiIFL, null, errorFile);
        tryExec("cut -f1 "+markerFile+".tmp",markerFile+".tmp2",errorFile);
		tryExec("tail -n +2", markerFile, errorFile,markerFile+".tmp2");
        tryExec("cut -f1 "+dnaRunFile+".tmp", dnaRunFile, errorFile);

        rm(markerFile+".tmp");
        rm(markerFile+".tmp2");
		rm(dnaRunFile+".tmp");
	}

	/**
	 * Updates Postgresql through the webservices to update the DataSet's monetDB and HDF5File references.
	 * @param config Configuration settings, used to determine connections
	 * @param cropName Name of the crop
	 * @param dataSetId Data set to update
	 * @param monetTableName Name of the table in the monetDB database for this dataset.
	 * @param hdfFileName Name of the HDF5 file for this dataset (Note, these should be obvious)
	 */
	public static void updateValues(ConfigSettings config,String cropName, Integer dataSetId,String monetTableName,String hdfFileName) {
		try{
			// set up authentication and so forth
			// you'll need to get the current from the instruction file
			ClientContext context=ClientContext.getInstance(config,cropName);
			//context.setCurrentClientCrop(cropName);
			SystemUsers systemUsers = new SystemUsers();
			SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());

			if(! context.login(userDetail.getUserName(), userDetail.getPassword())){
				logError("Digester","Login error for login:"+userDetail.getUserName()+ "-"+userDetail.getPassword());
				return;
			}

			String currentCropContextRoot = context.getInstance(null, false).getCurrentCropContextRoot();
			UriFactory uriFactory = new UriFactory(currentCropContextRoot);

			RestUri projectsUri = uriFactory
					.resourceByUriIdParam(ServiceRequestId.URL_DATASETS);
			projectsUri.setParamValue("id", dataSetId.toString());
			GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceForDatasets = new GobiiEnvelopeRestResource<>(projectsUri);
			PayloadEnvelope<DataSetDTO> resultEnvelope = gobiiEnvelopeRestResourceForDatasets
					.get(DataSetDTO.class);

			DataSetDTO dataSetResponse;
			if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
				System.out.println();
				logError("Digester","Data set response response errors");
				for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
					logError("HeaderError",currentStatusMesage.getMessage());
				}
				return;
			} else {
				dataSetResponse = resultEnvelope.getPayload().getData().get(0);
			}

			dataSetResponse.setDataTable(monetTableName);
			dataSetResponse.setDataFile(hdfFileName);

			resultEnvelope = gobiiEnvelopeRestResourceForDatasets
					.put(DataSetDTO.class,new PayloadEnvelope<>(dataSetResponse,GobiiProcessType.UPDATE));


			//dataSetResponse = dtoProcessor.process(dataSetResponse);
			// if you didn't succeed, do not pass go, but do log errors to your log file
			if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
				logError("Digester","Data set response response errors");
				for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
					logError("HeaderError",currentStatusMesage.getMessage());
				}
				return;
			}
		}
		catch(Exception e){
			logError("Digester","Exception while processing data sets",e);
			return;
		}
	}

	/**
	 * Generates a marker reference file from a marker file
	 * If input is name ref alt blah blah
	 * output is ref alt
	 * @param markerFile marker file
	 * @param outFile
	 */
	private static void generateMarkerReference(String markerFile,String outFile,String errorPath) throws IOException {
		BufferedReader br=new BufferedReader(new FileReader(markerFile));
		String[] headers = br.readLine().split("\\s+");
		br.close();
		String ref="ref",alt="alt";
		int refPos=-1;
		int altPos=-1;
		for(int i=0;i<headers.length;i++){
			if(headers[i].contains(ref)){
				refPos=i+1;break;//cut is 1 based
			}
		}
		for(int i=0;i<headers.length;i++){
			if(headers[i].contains(alt)){
				altPos=i+1;break;//cut is 1 based
			}

		}
		if((refPos==-1)||(altPos==-1)){
			throw new IOException("Could not find one of Ref or Alt in file: "+markerFile);
		}

		HelperFunctions.tryExec("cut -f"+refPos+","+altPos+ " "+markerFile,outFile,errorPath);
	}
}
