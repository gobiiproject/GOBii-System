package org.gobiiproject.gobiiprocess.digester;

import java.io.*;
import java.nio.file.*;
import java.text.ParseException;
import java.util.*;

import org.apache.commons.cli.*;
import org.gobiiproject.gobiiapimodel.payload.Header;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.config.*;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.*;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.gobiiproject.gobiimodel.utils.FileSystemInterface;
import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.instructions.loader.*;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.*;
import org.gobiiproject.gobiimodel.utils.email.*;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.GobiiConfig;
import org.gobiiproject.gobiiprocess.HDF5Interface;
import org.gobiiproject.gobiiprocess.digester.HelperFunctions.*;
import org.gobiiproject.gobiiprocess.digester.csv.CSVFileReaderV2;
import org.gobiiproject.gobiiprocess.digester.utils.validation.DigestMatrix;
import org.gobiiproject.gobiiprocess.digester.vcf.VCFFileReader;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rm;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.getDestinationFile;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.parseInstructionFile;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.tryExec;
import static org.gobiiproject.gobiimodel.utils.error.ErrorLogger.logError;

/**
 * Base class for processing instruction files. Start of chain of control for Digester. Takes first argument as instruction file, or promts user.
 * The File Reader runs off the Instruction Files, which tell it where the input files are, and how to process them.
 * {@link CSVFileReaderV2} and {@link VCFFileReader} deal with specific file formats. Overall logic and program flow come from this class.
 * <p>
 * This class deals with external commands and scripts, and coordinates uploads to the IFL and directly talks to HDF5 and MonetDB.
 *
 * @author jdl232 Josh L.S.
 */
public class GobiiFileReader {
	private static String rootDir="../";
	private static String loaderScriptPath;
	private static String extractorScriptPath;
	private static final String VARIANT_CALL_TABNAME="matrix";
	private static final String	LINKAGE_GROUP_TABNAME="linkage_group";
	private static final String GERMPLASM_PROP_TABNAME="germplasm_prop";
	private static final String GERMPLASM_TABNAME="germplasm";
	private static final String MARKER_TABNAME="marker";
	private static final String DS_MARKER_TABNAME="dataset_marker";
	private static final String DS_SAMPLE_TABNAME="dataset_dnarun";
	private static final String SAMPLE_TABNAME="dnarun";
	private static String pathToHDF5Files;
	private static boolean verbose;
	private static String errorLogOverride;
	private static String propertiesFile;
	private static GobiiUriFactory gobiiUriFactory;
	private static boolean enableMonet=false;
	//To calculate RunTime of Extraction
	private static long startTime, endTime, duration;
	//Not null if QC Extract is happening
	private static GobiiExtractorInstruction qcExtractInstruction = null;

	/**
	 * Main class of Digester Jar file. Uses command line parameters to determine instruction file, and runs whole program.
     *
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
         		.addOption("h", "hdfFiles", true, "Fully qualified path to hdf files")
				.addOption("em","enableMonet",false,"Enable Monet");
		LoaderGlobalConfigs.addOptions(o);
        ProcessMessage pm = new ProcessMessage();
		CommandLineParser parser = new DefaultParser();
        try{
			CommandLine cli = parser.parse( o, args );
            if(cli.hasOption("rootDir")) rootDir = cli.getOptionValue("rootDir");
            if(cli.hasOption("verbose")) verbose=true;
            if(cli.hasOption("errLog")) errorLogOverride = cli.getOptionValue("errLog");
            if(cli.hasOption("config")) propertiesFile = cli.getOptionValue("config");
            if(cli.hasOption("hdfFiles")) HDF5Interface.setPathToHDF5Files(cli.getOptionValue("hdfFiles"));
			if(cli.hasOption("enableMonet")) enableMonet=true;
			LoaderGlobalConfigs.setFromFlags(cli);
            args=cli.getArgs();//Remaining args passed through

		}catch(org.apache.commons.cli.ParseException exp ) {
			new HelpFormatter().printHelp("java -jar Digester.jar ","Also accepts input file directly after arguments\n" +
                		                  "Example: java -jar Digester.jar -c /home/jdl232/customConfig.properties -v /home/jdl232/testLoad.json",o,null,true);
               System.exit(2);
		}

     	extractorScriptPath=rootDir+"extractors/";
     	loaderScriptPath=rootDir+"loaders/";
     	HDF5Interface.setPathToHDF5(loaderScriptPath+"hdf5/bin/");

    	if(propertiesFile==null)propertiesFile=rootDir+"config/gobii-web.xml";

		boolean success=true;
		Map<String,File> loaderInstructionMap = new HashMap<>();//Map of Key to filename
		List<String> loaderInstructionList=new ArrayList<String>(); //Ordered list of loader instructions to execute, Keys to loaderInstructionMap
		String dst=null;
		DataSetOrientationType dso=null;

		ConfigSettings configuration=null;
		try {
			configuration = new ConfigSettings(propertiesFile);
		} catch (Exception e1) {
            e1.printStackTrace();
        }

        MailInterface mailInterface = new MailInterface(configuration);

        String instructionFile = null;
        if (args.length == 0 || "".equals(args[0])) {
            Scanner s = new Scanner(System.in);
            System.out.println("Enter Loader Instruction File Location:");
            instructionFile = s.nextLine();
        } else {
            instructionFile = args[0];
        }

        //Error logs go to a file based on crop (for human readability) and
        pm.addPath("instruction file", new File(instructionFile).getAbsolutePath());
        ErrorLogger.logInfo("Digester", "Beginning read of " + instructionFile);
        List<GobiiLoaderInstruction> list = parseInstructionFile(instructionFile);
		if(list==null || list.isEmpty()){
			logError("Digester","No instruction for file "+instructionFile);
			return;
		}

		// Instruction file Validation
		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(list);
		instructionFileValidator.processInstructionFile();
		String validationStatus = instructionFileValidator.validateMarkerUpload();
		if(validationStatus != null){
			ErrorLogger.logError("Marker validation failed.", validationStatus);
		}

		validationStatus = instructionFileValidator.validateSampleUpload();
		if(validationStatus != null){
			ErrorLogger.logError("Sample validation failed.",validationStatus);
		}

		validationStatus = instructionFileValidator.validate();
		if(validationStatus != null){
			ErrorLogger.logError("Validation failed.", validationStatus );
		}

		GobiiLoaderInstruction zero=list.iterator().next();
		Integer dataSetId=zero.getDataSetId();

		pm.addIdentifier("Project",zero.getProject());
		pm.addIdentifier("Platform",zero.getPlatform());
		pm.addIdentifier("Experiment",zero.getExperiment());
		pm.addIdentifier("Dataset",zero.getDataSet());
		pm.addIdentifier("Mapset",zero.getMapset());
		pm.addIdentifier("Dataset Type",zero.getDatasetType());


		String dstFilePath= getDestinationFile(zero);//Intermediate 'file'
		File dstDir=new File(dstFilePath);
		if(!dstDir.isDirectory()){ //Note: if dstDir is a non-existant
			dstDir=new File(dstFilePath.substring(0, dstFilePath.lastIndexOf("/")));
		}
		pm.addPath("destination directory",dstDir.getAbsolutePath());//Convert to directory
		pm.addPath("input directory",zero.getGobiiFile().getSource());

		String crop=zero.getGobiiCropType();
		if(crop==null) crop=divineCrop(instructionFile);
		Path cropPath = Paths.get(rootDir+"crops/"+crop.toLowerCase());
		if (!(Files.exists(cropPath) &&
			  Files.isDirectory(cropPath))) {
			logError("Digester","Unknown Crop Type: "+crop);
			return;
		}
		GobiiCropConfig gobiiCropConfig = null;
		try {
			gobiiCropConfig = configuration.getCropConfig(crop);
		} catch (Exception e) {
			logError("Digester","Unknown loading error",e);
			return;
        }
        if (gobiiCropConfig == null) {
			logError("Digester","Unknown Crop Type: "+crop+" in the Configuration File");
			return;
		}
		if(HDF5Interface.getPathToHDF5Files() ==null) HDF5Interface.setPathToHDF5Files(cropPath.toString()+"/hdf5/");

		String errorPath=getLogName(zero, gobiiCropConfig,crop);

		//TODO: HACK - Job's name is
		String jobName = getJobName(crop,list);
        String jobUser = zero.getContactEmail();
		pm.setUser(jobUser);

		String logDir=configuration.getFileSystemLog();
		String logFile=null;
		if(logDir!=null) {
			String instructionName=new File(instructionFile).getName();
			instructionName=instructionName.substring(0,instructionName.lastIndexOf('.'));
			logFile=logDir+"/"+instructionName+".log";
			String oldLogFile=ErrorLogger.getLogFilepath();
			ErrorLogger.logDebug("Error Logger","Moving error log to "+logFile);
			ErrorLogger.setLogFilepath(logFile);
			ErrorLogger.logDebug("Error Logger","Moved error log to "+logFile);
			FileSystemInterface.rmIfExist(oldLogFile);
		}

		SimpleTimer.start("FileRead");

		boolean qcCheck;//  zero.isQcCheck();
//		if (qcCheck) {//QC - Subsection #1 of 3
//			qcExtractInstruction = createQCExtractInstruction(zero, crop);
//		}

		//Pre-processing - make sure all files exist, find the cannonical dataset id
		for(GobiiLoaderInstruction inst:list) {
			if (inst == null) {
				logError("Digester", "Missing or malformed instruction in " + instructionFile);
                continue;
            }
            if (dataSetId == null) {
                try {
                    dataSetId = inst.getDataSetId();//Pick it up from relevant instruction
                } catch (Exception e) {
                    ErrorLogger.logInfo("GobiiFileReader", "Attempting to read dataset ID" + e.getMessage());
                }
                try {
                    dataSetId = inst.getDataSet().getId(); //If it's not there, try the 'Dataset'
                } catch (Exception e) {
                    ErrorLogger.logInfo("GobiiFileReader", "Attempting to read dataset ID" + e.getMessage());
                }
            }
            GobiiFile file = inst.getGobiiFile();
            if (file == null) {
                logError("Digester", "Instruction " + instructionFile + " Table " + inst.getTable() + " has bad 'file' column");
				continue;
			}
			GobiiFileType instructionFileType = file.getGobiiFileType();
			if (instructionFileType == null) {
				logError("Digester", "Instruction " + instructionFile + " Table " + inst.getTable() + " has missing file format");
				continue;
			}
		}


		//Section - Processing
		ErrorLogger.logTrace("Digester", "Beginning List Processing");
		success = true;
		switch (zero.getGobiiFile().getGobiiFileType()) { //All instructions should have the same file type, all file types go through CSVFileReader(V2)
			case HAPMAP:
				//INTENTIONAL FALLTHROUGH
			case VCF:
				//INTENTIONAL FALLTHROUGH
			case GENERIC:
				CSVFileReaderV2.parseInstructionFile(list);
				break;
			default:
				System.err.println("Unable to deal with file type " + zero.getGobiiFile().getGobiiFileType());
				break;
		}

		//Database Validation
		DatabaseQuerier querier=new DatabaseQuerier(gobiiCropConfig.getCropDbConfig(GobiiDbType.POSTGRESQL));

		//If we're doing a DS upload and there is no DS_Marker
		if(loaderInstructionMap.containsKey(VARIANT_CALL_TABNAME) && loaderInstructionMap.containsKey(DS_MARKER_TABNAME) && !loaderInstructionMap.containsKey(MARKER_TABNAME)) {
			querier.checkMarkerInPlatform(loaderInstructionMap.get(DS_MARKER_TABNAME),zero.getPlatform().getId());
		}
		//If we're doing a DS upload and there is no DS_Sample
		if(loaderInstructionMap.containsKey(VARIANT_CALL_TABNAME) && loaderInstructionMap.containsKey(DS_SAMPLE_TABNAME) && !loaderInstructionMap.containsKey(SAMPLE_TABNAME)) {
			querier.checkDNARunInExperiment(loaderInstructionMap.get(DS_SAMPLE_TABNAME),zero.getExperiment().getId());
		}


		if(loaderInstructionMap.containsKey(MARKER_TABNAME)){
			querier.checkMarkerExistence(loaderInstructionMap.get(MARKER_TABNAME));
		}
		if(loaderInstructionMap.containsKey(GERMPLASM_TABNAME)){
			querier.checkGermplasmTypeExistence(loaderInstructionMap.get(GERMPLASM_TABNAME));
			querier.checkGermplasmSpeciesExistence(loaderInstructionMap.get(GERMPLASM_TABNAME));
		}
		querier.close();

		boolean sendQc= false;
		for (GobiiLoaderInstruction inst:list) {
			qcCheck = inst.isQcCheck();
			//Section - Matrix Post-processing
			//Dataset is the first non-empty dataset type
			for (GobiiFileColumn gfc : inst.getGobiiFileColumns()) {
				if (gfc.getDataSetType() != null) {
					dst = getDatasetType(inst, gfc);
					boolean isVCF = inst.getGobiiFile().getGobiiFileType().equals(GobiiFileType.VCF);
					if (isVCF && !dst.equals("IUPAC")) {
						dst = "VCF";
					}
					if (gfc.getDataSetOrientationType() != null) dso = gfc.getDataSetOrientationType();

					break;
				}
			}
			String fromFile = getDestinationFile(inst);
			SequenceInPlaceTransform intermediateFile=new SequenceInPlaceTransform(fromFile,errorPath);
			if (dst != null && inst.getTable().equals(VARIANT_CALL_TABNAME)) {
				errorPath = getLogName(inst, gobiiCropConfig, crop, "Matrix_Processing"); //Temporary Error File Name
				boolean transformStripsHeader = false;
				MobileTransform mainTransform=null;
				switch (dst.toUpperCase()) {
					case "NUCLEOTIDE_2_LETTER":
						mainTransform=MobileTransform.getSNPTransform("python " + loaderScriptPath + "etc/SNPSepRemoval.py",loaderScriptPath + "etc/missingIndicators.txt");
						transformStripsHeader = true;
                        break;
                    case "IUPAC":
                        mainTransform = MobileTransform.IUPACToBI;
                        break;
                    case "SSR_ALLELE_SIZE":
                    case "DOMINANT_NON_NUCLEOTIDE":
                    case "CO_DOMINANT_NON_NUCLEOTIDE":
                        //No Translation Needed in these cases. Done before GOBII
                        break;
                    case "VCF":
                        File markerFile = loaderInstructionMap.get(MARKER_TABNAME);
						mainTransform=MobileTransform.getVCFTransform(markerFile);
						break;
					default:
						ErrorLogger.logError("GobiiFileReader", "Unknown Data type " + dst);
						break;
				}
				if (mainTransform != null) {
					intermediateFile.transform(mainTransform);
				}
				if (!transformStripsHeader) {
					intermediateFile.transform(MobileTransform.stripHeader);
				}
				boolean isSampleFast = false;
				if (DataSetOrientationType.SAMPLE_FAST.equals(dso)) isSampleFast = true;
				if (isSampleFast) {
					//Rotate to marker fast before loading it - all data is marker fast in the system
					intermediateFile.transform(MobileTransform.getTransposeMatrix(getDestinationFile(inst)));
				}
			}

			String instructionName = inst.getTable();
			loaderInstructionMap.put(instructionName, new File(getDestinationFile(inst)));
			loaderInstructionList.add(instructionName);//TODO Hack - for ordering
			if (LINKAGE_GROUP_TABNAME.equals(instructionName) || GERMPLASM_TABNAME.equals(instructionName) || GERMPLASM_PROP_TABNAME.equals(instructionName)) {
				success &= HelperFunctions.tryExec(loaderScriptPath + "LGduplicates.py -i " + getDestinationFile(inst));
			}
			if (MARKER_TABNAME.equals(instructionName)) {//Convert 'alts' into a jsonb array
				intermediateFile.transform(MobileTransform.PGArray);
			}

			if(loaderInstructionMap.containsKey(VARIANT_CALL_TABNAME)) {
				boolean valid=DigestMatrix.validatematrix(loaderInstructionMap.get(VARIANT_CALL_TABNAME), zero.getDatasetType().getName().toString());
				if (!valid) {
					ErrorLogger.logError("Validate Dataset Matrix", "Matrix validation Failed.");
					success=false;
				}
			}

			if (qcCheck) {//QC - Subsection #2 of 3
				qcExtractInstruction = createQCExtractInstruction(zero, crop);
				setQCExtractPaths(inst, configuration, crop, instructionFile);
				sendQc = true;
			}

			intermediateFile.returnFile(); // replace intermediateFile where it came from
		}

		if(success){

			errorPath=getLogName(zero, gobiiCropConfig, crop, "IFLs");
			String pathToIFL=loaderScriptPath+"postgres/gobii_ifl/gobii_ifl.py";
			String connectionString=" -c "+HelperFunctions.getPostgresConnectionString(gobiiCropConfig);

			//Load PostgreSQL
			boolean loadedData=false;
			for(String key:loaderInstructionList){
				if(!VARIANT_CALL_TABNAME.equals(key)){
					String inputFile=" -i "+loaderInstructionMap.get(key);
					String outputFile=" -o "+dstDir.getAbsolutePath()+ "/"; //Output here is temporary files, needs terminal /

					ErrorLogger.logInfo("Digester","Running IFL: "+pathToIFL+" <conntection string> "+inputFile+outputFile);
					//Lines affected returned by method call - THIS IS NOW IGNORED
					HelperFunctions.tryExec(pathToIFL+connectionString+inputFile+outputFile+" -l",verbose?dstDir.getAbsolutePath()+"/iflOut":null,errorPath);

					IFLLineCounts counts=calculateTableStats(pm, loaderInstructionMap, dstDir, key);

                    if (counts.loadedData == 0) {
                        ErrorLogger.logDebug("FileReader", "No data loaded for table " + key);
                    } else {
                        loadedData = true;
                    }
					if(counts.invalidData >0 && !isVariableLengthTable(key)){
						ErrorLogger.logError("FileReader","Error in table "+key);
					}

				}


			}
			if(!loadedData){
				ErrorLogger.logError("FileReader", "No new data was uploaded.");
			}
			//Load Monet/HDF5
			errorPath=getLogName(zero, gobiiCropConfig, crop, "Matrix_Upload");
			String variantFilename="DS"+dataSetId;
			File variantFile=loaderInstructionMap.get(VARIANT_CALL_TABNAME);
			String markerFileLoc= HDF5Interface.getPathToHDF5Files() +"DS"+dataSetId+".marker_id";
			String sampleFileLoc= HDF5Interface.getPathToHDF5Files() +"DS"+dataSetId+".dnarun_id";

			if(variantFile!=null && dataSetId==null){
				logError("Digester","Data Set ID is null for variant call");
			}
			if((variantFile!=null)&&dataSetId!=null){ //Create an HDF5 and a Monet
				if(enableMonet) {//Turned off by default
					uploadToMonet(dataSetId, gobiiCropConfig, errorPath, variantFile, markerFileLoc, sampleFileLoc);
				}

                HDF5Interface.createHDF5FromDataset(pm, dst, configuration, dataSetId, crop, errorPath, variantFilename, variantFile);
                rmIfExist(variantFile.getPath());

                if (sendQc) {//QC - Subsection #3 of 3
                    sendQCExtract(configuration, crop);
                }
            }
            if (success && ErrorLogger.success()) {
                ErrorLogger.logInfo("Digester", "Successfully Uploaded files");
            } else {
                ErrorLogger.logWarning("Digester", "Unsuccessfully Uploaded files");
            }
        }//endif(success)
		else{
			ErrorLogger.logWarning("Digester","Unsuccessfully Generated files");
		}

		try{
			pm.addPath("Error Log", logFile);
			pm.setBody(jobName,zero.getGobiiFile().getGobiiFileType().name(),SimpleTimer.stop("FileRead"),ErrorLogger.getFirstErrorReason(),ErrorLogger.success(),ErrorLogger.getAllErrorStringsHTML());
			mailInterface.send(pm);
		}catch(Exception e){
			ErrorLogger.logError("MailInterface","Error Sending Mail",e);
        }
        HelperFunctions.completeInstruction(instructionFile, configuration.getProcessingPath(crop, GobiiFileProcessDir.LOADER_DONE));

    }

    private static void uploadToMonet(Integer dataSetId, GobiiCropConfig gobiiCropConfig, String errorPath, File variantFile, String markerFileLoc, String sampleFileLoc) {
        String loadVariantMatrix = loaderScriptPath + "monet/loadVariantMatrix.py";
        //python loadVariantMatrix.py <Dataset Name> <Dataset_Identifier.variant> <Dataset_Identifier.marker_id> <Dataset_Identifier.dnarun_id> <hostname> <port> <dbuser> <dbpass> <dbname>

        GobiiCropDbConfig monetConf = gobiiCropConfig.getCropDbConfig(GobiiDbType.MONETDB);
		String loadVariantUserPort = monetConf.getHost() + " " + monetConf.getPort() + " " + monetConf.getUserName() + " " + monetConf.getPassword() + " " + monetConf.getContextPath();
		generateIdLists(gobiiCropConfig, markerFileLoc, sampleFileLoc, dataSetId, errorPath);
		ErrorLogger.logDebug("MonetDB", "python " + loadVariantMatrix + " DS" + dataSetId + " " + variantFile.getPath() + " " + new File(markerFileLoc).getAbsolutePath() + " " + new File(sampleFileLoc).getAbsolutePath() + " " + loadVariantUserPort);
		HelperFunctions.tryExec("python " + loadVariantMatrix + " DS" + dataSetId + " " + variantFile.getPath() + " " + new File(markerFileLoc).getAbsolutePath() + " " + new File(sampleFileLoc).getAbsolutePath() + " " + loadVariantUserPort, null, errorPath);
		//Clean up marker and sample data
		rm(markerFileLoc);
		rm(sampleFileLoc);
	}

	private static GobiiExtractorInstruction createQCExtractInstruction(GobiiLoaderInstruction zero, String crop) {
		GobiiExtractorInstruction gobiiExtractorInstruction;
		ErrorLogger.logInfo("Digester", "qcCheck detected");
		ErrorLogger.logInfo("Digester","Entering into the QC Subsection #1 of 3...");
		gobiiExtractorInstruction = new GobiiExtractorInstruction();
		gobiiExtractorInstruction.setContactEmail(zero.getContactEmail());
		gobiiExtractorInstruction.setContactId(zero.getContactId());
		gobiiExtractorInstruction.setGobiiCropType(crop);
		gobiiExtractorInstruction.getMapsetIds().add(zero.getMapset().getId());
		gobiiExtractorInstruction.setQcCheck(true);
		ErrorLogger.logInfo("Digester","Done with the QC Subsection #1 of 3!");
		return gobiiExtractorInstruction;
	}

	private static void setQCExtractPaths(GobiiLoaderInstruction inst, ConfigSettings configuration, String crop, String instructionFile) throws Exception {
        ErrorLogger.logInfo("Digester", "Entering into the QC Subsection #2 of 3...");
        GobiiDataSetExtract gobiiDataSetExtract = new GobiiDataSetExtract();
        gobiiDataSetExtract.setAccolate(false);  // It is unused/unsupported at the moment
        gobiiDataSetExtract.setDataSet(inst.getDataSet());
        gobiiDataSetExtract.setGobiiDatasetType(inst.getDatasetType());

        // According to Liz, the Gobii extract filter type is always "WHOLE_DATASET" for any QC job
        gobiiDataSetExtract.setGobiiExtractFilterType(GobiiExtractFilterType.WHOLE_DATASET);
        gobiiDataSetExtract.setGobiiFileType(GobiiFileType.HAPMAP);
        // It is going to be set by the Gobii web services
        gobiiDataSetExtract.setGobiiJobStatus(null);
        qcExtractInstruction.getDataSetExtracts().add(gobiiDataSetExtract);
        ErrorLogger.logInfo("Digester", "Done with the QC Subsection #2 of 3!");
	}

	private static void sendQCExtract(ConfigSettings configuration, String crop) throws Exception {
		ErrorLogger.logInfo("Digester","Entering into the QC Subsection #3 of 3...");
        ExtractorInstructionFilesDTO extractorInstructionFilesDTOToSend = new ExtractorInstructionFilesDTO();
        extractorInstructionFilesDTOToSend.getGobiiExtractorInstructions().add(qcExtractInstruction);
        extractorInstructionFilesDTOToSend.setInstructionFileName("extractor_" + DateUtils.makeDateIdString());
        GobiiClientContext gobiiClientContext = GobiiClientContext.getInstance(configuration, crop, GobiiAutoLoginType.USER_RUN_AS);
        if (LineUtils.isNullOrEmpty(gobiiClientContext.getUserToken())) {
            ErrorLogger.logError("Digester", "Unable to log in with user " + GobiiAutoLoginType.USER_RUN_AS.toString());
            return;
        }
        String currentCropContextRoot = GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot();
        gobiiUriFactory = new GobiiUriFactory(currentCropContextRoot);
        PayloadEnvelope<ExtractorInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(extractorInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ExtractorInstructionFilesDTO> gobiiEnvelopeRestResourceForPost = new GobiiEnvelopeRestResource<>(gobiiUriFactory
                .resourceColl(GobiiServiceRequestId.URL_FILE_EXTRACTOR_INSTRUCTIONS));
        PayloadEnvelope<ExtractorInstructionFilesDTO> extractorInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResourceForPost.post(ExtractorInstructionFilesDTO.class,
                payloadEnvelope);

        if (extractorInstructionFileDTOResponseEnvelope != null) {

            Header header = extractorInstructionFileDTOResponseEnvelope.getHeader();
            if (header.getStatus().isSucceeded()) {
                ErrorLogger.logInfo("Digester", "Extractor Request Sent");

            } else {

                String messages = extractorInstructionFileDTOResponseEnvelope.getHeader().getStatus().messages();

                for (HeaderStatusMessage currentStatusMesage : header.getStatus().getStatusMessages()) {
                    messages += (currentStatusMesage.getMessage()) + "; ";
                }

                ErrorLogger.logError("Digester", "Error sending extract request: " + messages);

            }
        } else {
            ErrorLogger.logInfo("Digester", "Error Sending Extractor Request");
        }
        ErrorLogger.logInfo("Digester", "Done with the QC Subsection #3 of 3!");
    }

    /**
     * Read ppd and nodups files to determine their length, and add the row corresponding to the key to the digester message status.
     * Assumes IFL was run with output of dstDir on key in instructionMap.
     *
     * @param pm                   ProcessMessage to record data to
     * @param loaderInstructionMap Map of key/location of loader instructions
	 * @param dstDir Destination directory for IFL call run on key's table
	 * @param key Key in loaderInstructionMap
	 * @return
	 */
	private static IFLLineCounts calculateTableStats(ProcessMessage pm, Map<String, File> loaderInstructionMap, File dstDir, String key) {

		String ppdFile=new File(dstDir,"ppd_digest."+key).getAbsolutePath();
		//If there is a deduplicated PPD file, use it instead of the ppd file
		String ddpPpdFile=new File(dstDir,"ddp_ppd_digest."+key).getAbsolutePath();
		if(new File(ddpPpdFile).exists()){
			ppdFile=ddpPpdFile;
		}

		String noDupsFile=new File(dstDir,"nodups_ppd_digest."+key).getAbsolutePath();
		//If there is a deduplicated nodups file, use it instead of the nodups file
		String ddpNoDupsFile=new File(dstDir,"nodups_ddp_ppd_digest."+key).getAbsolutePath();
		if(new File(ddpNoDupsFile).exists()){
			noDupsFile=ddpNoDupsFile;
		}


		//Default to 'we had an error'
		String totalLinesVal,linesLoadedVal,existingLinesVal,invalidLinesVal;
		totalLinesVal=linesLoadedVal=existingLinesVal=invalidLinesVal="error";

		//-1 lines for header
		int totalLines= FileSystemInterface.lineCount(loaderInstructionMap.get(key).getAbsolutePath()) -1;
		int ppdLines= FileSystemInterface.lineCount(ppdFile) -1;
		int noDupsLines = FileSystemInterface.lineCount(noDupsFile) -1;
		//They're -1 if the file is missing.
		if(totalLines<0)totalLines=0;
		if(ppdLines<0)ppdLines=0;
		if(noDupsLines<0)noDupsLines=0;

		boolean noDupsFileExists=new File(noDupsFile).exists();
		if(!noDupsFileExists)noDupsLines=ppdLines;
		//Begin Business Logic Zone
		int loadedLines=noDupsLines;
		int existingLines=ppdLines-noDupsLines;
		int invalidLines=totalLines-ppdLines;
		//End Business Logic Zone - regular logic can resume

		//If total lines/file lines less than 0, something's wrong. Also if total lines is < changed, something's wrong.


		if(isVariableLengthTable(key)){
			totalLinesVal=totalLines+"";
			linesLoadedVal=loadedLines+"";
			//Existing and Invalid may be absolutely random numbers in EAV JSON objects
			//Also, loaded may be waaaay above total, this is normal. So lets not report these two fields at all
			existingLinesVal="";
			invalidLinesVal="";

			//We can still warn people if no lines were loaded
            if (loadedLines == 0) {
                linesLoadedVal = "<b style=\"background-color:yellow\">" + loadedLines + "</b>";
            }
        } else {
            totalLinesVal = totalLines + "";
            linesLoadedVal = loadedLines + "";//Header
            existingLinesVal = existingLines + "";
            invalidLinesVal = invalidLines + "";
            if (!noDupsFileExists) {
                existingLinesVal = "";
			}
			if(invalidLines!=0) {
				invalidLinesVal = "<b style=\"background-color:red\">" + invalidLines + "</b>";
			}
			if(loadedLines==0) {
				linesLoadedVal = "<b style=\"background-color:yellow\">" + loadedLines + "</b>";
			}
		}
		IFLLineCounts counts=new IFLLineCounts();
		counts.loadedData=loadedLines;
		counts.existingData=existingLines;
        counts.invalidData = invalidLines;
        pm.addEntry(key, totalLinesVal, linesLoadedVal, existingLinesVal, invalidLinesVal);
        return counts;
    }

    /**
     * Returns a human readable name for the job.
     *
     * @param cropName Name of the crop being run
     * @param list     List of instructions to read from
     * @return a human readable name for the job
     */
    private static String getJobName(String cropName, List<GobiiLoaderInstruction> list) {
        cropName = cropName.charAt(0) + cropName.substring(1).toLowerCase();// MAIZE -> Maize
        String jobName = "[GOBII - Loader]: " + cropName + " - digest of \"" + getSourceFileName(list.get(0).getGobiiFile()) + "\"";
        return jobName;
    }

    /**
     * Converts the File input into the FIRST of the source files.
     *
     * @param file Reference to Instruction's File object.
     * @return String representation of first of source files
     */
    public static String getSourceFileName(GobiiFile file) {
        String source = file.getSource();
        File sourceFolder = new File(source);
        File[] f = sourceFolder.listFiles();
        if (f.length != 0) source = f[0].getName();
        else {
            source = sourceFolder.getName();//Otherwise we get full paths in source.
        }
        return source;
    }

    /**
     * Generates a log file location given a crop name, crop type, and process ID. (Given by the process calling this method).
     * <p>
     * Currently works by placing logs in the intermediate file directory.
     *
     * @param config Crop configuration
     * @return The logfile location for this process
     */
    private static String getLogName(GobiiLoaderInstruction gli, GobiiCropConfig config, String cropName) {
        String destination = gli.getGobiiFile().getDestination();
        String table = gli.getTable();
        return destination + "/" + cropName + "_Table-" + table + ".log";
    }

    /**
     * Generates a log file location given a crop name, crop type, and process ID. (Given by the process calling this method).
     * <p>
     * Currently works by placing logs in the intermediate file directory.
     *
     * @param config Crop configuration
     * @return The logfile location for this process
     */
    private static String getLogName(GobiiLoaderInstruction gli, GobiiCropConfig config, String cropName, String process) {
        String destination = gli.getGobiiFile().getDestination();
        return destination + "/" + cropName + "_Process-" + process + ".log";
    }

    /**
     * Determine crop type by looking at the intruction file's location for the name of a crop.
     *
     * @param instructionFile
     * @return GobiiCropType
     */
    private static String divineCrop(String instructionFile) {
		String upper=instructionFile.toUpperCase();
		String from="/CROPS/";
		int fromIndex=upper.indexOf(from)+from.length();
        String crop = upper.substring(fromIndex, upper.indexOf('/', fromIndex));
        return crop;
    }

    /**
     * Generates appropriate monetDB files from the MDE by reverse-digesting the data we just loaded.
     * Reason - Ensures Postgres and MonetDB are in sync
     *
     * @param gobiiCropConfig Connection String
     * @param markerFile      No header
     * @param dnaRunFile      With header
     * @param dsid            Because
     * @param errorFile       temporary file to store error information in
     */
    private static void generateIdLists(GobiiCropConfig gobiiCropConfig, String markerFile, String dnaRunFile, int dsid, String errorFile) {
        //Create files and get paths because gobii_mde must run on absolute paths, not relative ones
        markerFile = new File(markerFile).getAbsolutePath();
        dnaRunFile = new File(dnaRunFile).getAbsolutePath();
        String gobiiIFL = "python " + extractorScriptPath + "postgres/gobii_mde/gobii_mde.py" + " -c " + HelperFunctions.getPostgresConnectionString(gobiiCropConfig) +
                " -m " + markerFile + ".tmp" +
                " -s " + dnaRunFile + ".tmp" +
                " -d " + dsid;
        ErrorLogger.logDebug("MonetDB", gobiiIFL);
        tryExec(gobiiIFL, null, errorFile);
        tryExec("cut -f1 " + markerFile + ".tmp", markerFile + ".tmp2", errorFile);
        tryExec("tail -n +2", markerFile, errorFile, markerFile + ".tmp2");
        tryExec("cut -f1 " + dnaRunFile + ".tmp", dnaRunFile, errorFile);

        rm(markerFile + ".tmp");
        rm(markerFile + ".tmp2");
        rm(dnaRunFile + ".tmp");
    }

    /**
     * Updates Postgresql through the webservices to update the DataSet's monetDB and HDF5File references.
     *
     * @param config         Configuration settings, used to determine connections
     * @param cropName       Name of the crop
     * @param dataSetId      Data set to update
     * @param monetTableName Name of the table in the monetDB database for this dataset.
     * @param hdfFileName    Name of the HDF5 file for this dataset (Note, these should be obvious)
     */
    public static void updateValues(ConfigSettings config, String cropName, Integer dataSetId, String monetTableName, String hdfFileName) {
        try {
            // set up authentication and so forth
            // you'll need to get the current from the instruction file
            GobiiClientContext context = GobiiClientContext.getInstance(config, cropName, GobiiAutoLoginType.USER_RUN_AS);
            //context.setCurrentCropId(cropName);

            if (LineUtils.isNullOrEmpty(context.getUserToken())) {
                logError("Digester", "Unable to login with user " + GobiiAutoLoginType.USER_RUN_AS.toString());
                return;
            }

            String currentCropContextRoot = context.getInstance(null, false).getCurrentCropContextRoot();
            GobiiUriFactory gobiiUriFactory = new GobiiUriFactory(currentCropContextRoot);

            RestUri projectsUri = gobiiUriFactory
                    .resourceByUriIdParam(GobiiServiceRequestId.URL_DATASETS);
            projectsUri.setParamValue("id", dataSetId.toString());
            GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceForDatasets = new GobiiEnvelopeRestResource<>(projectsUri);
            PayloadEnvelope<DataSetDTO> resultEnvelope = gobiiEnvelopeRestResourceForDatasets
                    .get(DataSetDTO.class);

            DataSetDTO dataSetResponse;
            if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
                System.out.println();
                logError("Digester", "Data set response response errors");
                for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
                    logError("HeaderError", currentStatusMesage.getMessage());
                }
                return;
            } else {
                dataSetResponse = resultEnvelope.getPayload().getData().get(0);
            }

            dataSetResponse.setDataTable(monetTableName);
            dataSetResponse.setDataFile(hdfFileName);

            resultEnvelope = gobiiEnvelopeRestResourceForDatasets
                    .put(DataSetDTO.class, new PayloadEnvelope<>(dataSetResponse, GobiiProcessType.UPDATE));


            //dataSetResponse = dtoProcessor.process(dataSetResponse);
            // if you didn't succeed, do not pass go, but do log errors to your log file
            if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
                logError("Digester", "Data set response response errors");
                for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
                    logError("HeaderError", currentStatusMesage.getMessage());
                }
                return;
            }
        } catch (Exception e) {
            logError("Digester", "Exception while referencing data sets in Postgresql", e);
            return;
        }
    }

    /**
     * Since the ENUM is deprecated
     * This is the best we've got.
     *
     * @param gfc file column
     * @param i   gobii loader instruction
     * @return String representation of the dataset type of the column
     */
    private static String getDatasetType(GobiiLoaderInstruction i, GobiiFileColumn gfc) {
        DataSetType dst = gfc.getDataSetType(); //Old way
        if (dst != null) {
            return dst.toString();
        } else {
            return i.getDatasetType().getName(); //Get the name from the instruction.
        }
    }

    private static String getJDBCConnectionString(GobiiCropConfig config) {
        return config.getCropDbConfig(GobiiDbType.POSTGRESQL).getConnectionString();
    }

    /**
     * Given a string key, determine if the table is one-to-one with relation to the input file size.
     * If not, several metrics become meaningless.
     *
     * @param tableKey
     * @return true if the table will have different PPD rows than input rows
     */
    private static boolean isVariableLengthTable(String tableKey) {
        return tableKey.contains("_prop");
    }
}

class IFLLineCounts {
    int loadedData, existingData, invalidData;
}
