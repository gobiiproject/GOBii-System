package org.gobiiproject.gobiiprocess.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.*;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.extractor.flapjack.FlapjackTransformer;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rm;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.*;
import static org.gobiiproject.gobiimodel.utils.error.ErrorLogger.logError;

public class GobiiExtractor {
	//Paths
	private static String  pathToHDF5, propertiesFile,pathToHDF5Files;
	
	private static String lastErrorFile=null;
	private static String errorLogOverride;
	private static boolean verbose;
	private static String rootDir="../";
	public static void main(String[] args) throws Exception {
		Options o = new Options()
         		.addOption("v", "verbose", false, "Verbose output")
         		.addOption("e", "errlog", true, "Error log override location")
         		.addOption("r", "rootDir", true, "Fully qualified path to gobii root directory")
         		.addOption("c","config",true,"Fully qualified path to gobii configuration file")
         		.addOption("h", "hdfFiles", true, "Fully qualified path to hdf files");
        
		 CommandLineParser parser = new DefaultParser();
         try{
               CommandLine cli = parser.parse( o, args );
               if(cli.hasOption("verbose")) verbose=true;
               if(cli.hasOption("errLog")) errorLogOverride = cli.getOptionValue("errLog");
               if(cli.hasOption("config")) propertiesFile = cli.getOptionValue("config");
                      if(cli.hasOption("rootDir")){
                rootDir = cli.getOptionValue("rootDir");
               }
               if(cli.hasOption("hdfFiles")) pathToHDF5Files = cli.getOptionValue("hdfFiles");
               args=cli.getArgs();//Remaining args passed through
                
         }catch(org.apache.commons.cli.ParseException exp ) {
               new HelpFormatter().printHelp("java -jar Extractor.jar ","Also accepts input file directly after arguments\n"
               		+ "Example: java -jar Extractor.jar -c /home/jdl232/customConfig.properties -v /home/jdl232/testLoad.json",o,null,true);
               
               System.exit(2);
         }
		
     	String extractorScriptPath=rootDir+"extractors/";
    	pathToHDF5=extractorScriptPath+"hdf5/bin/";
    	
    	if(propertiesFile==null)propertiesFile=rootDir+"config/gobii-web.properties";
		
		boolean success=true;
		ConfigSettings configuration=null;
		try {
			configuration = new ConfigSettings(propertiesFile);
		} catch (Exception e) {
			logError("Extractor","Failure to read Configurations",e);
			return;
		}
		String logDir=configuration.getFileSystemLog();
		ErrorLogger.setLogFilepath(logDir);
		String instructionFile=null;
		if(args.length==0 ||args[0]==""){
			Scanner s=new Scanner(System.in);
			System.out.println("Enter Extractor Instruction File Location:");
			instructionFile=s.nextLine();
		    if(instructionFile.equals("")) instructionFile="scripts//jdl232_01_pretty.json";
		    s.close();
		}
		else{
			instructionFile=args[0];
		}

		List<GobiiExtractorInstruction> list= parseExtractorInstructionFile(instructionFile);
		if(list==null){
			ErrorLogger.logError("Extractor","No instruction for file "+instructionFile);
			return;
		}
		for(GobiiExtractorInstruction inst:list){
			String crop = inst.getGobiiCropType();
			if(crop==null) crop=divineCrop(instructionFile);
			Path cropPath = Paths.get(rootDir+"crops/"+crop.toLowerCase());
			if (!(Files.exists(cropPath) &&
				  Files.isDirectory(cropPath))) {
				ErrorLogger.logError("Extractor","Unknown Crop Type: "+crop);
				return;
			}
			CropConfig cropConfig= null;
			try {
				cropConfig = configuration.getCropConfig(crop);
			} catch (Exception e) {
				logError("Extractor","Unknown exception getting crop",e);
				return;
			}
			if (cropConfig == null) {
				logError("Extractor","Unknown Crop Type: "+crop+" in the Configuration File");
				return;
			}
			if(pathToHDF5Files==null)pathToHDF5Files=cropPath.toString()+"/hdf5/";
			for(GobiiDataSetExtract extract:inst.getDataSetExtracts()){
				String extractDir=extract.getExtractDestinationDirectory();
				tryExec("rm -f "+extractDir+"*");
				//TODO: Fix underlying permissions issues
				//tryExec("chmod -R 777 " +extractDir.substring(0, extractDir.lastIndexOf('/')));
				String markerFile=extractDir+"marker.file";
				String sampleFile=extractDir+"sample.file";
				String projectFile=extractDir+"project.file";
				String chrLengthFile = new StringBuilder(extractDir).append("marker.file.chr").toString();
				Path mdePath = FileSystems.getDefault().getPath(new StringBuilder(extractorScriptPath).append("postgres/gobii_mde/gobii_mde.py").toString());
				if (!(mdePath.toFile().exists() &&
					  mdePath.toFile().isFile())) {
					ErrorLogger.logDebug("Extractor", new StringBuilder(mdePath.toString()).append(" does not exist!").toString());
					return;
				}
				String gobiiMDE = "python "+ mdePath+
						" -c " + HelperFunctions.getPostgresConnectionString(cropConfig) +
						" -m " + markerFile +
						" -s " + sampleFile +
						" -p " + projectFile +
						" -d " + extract.getDataSetId() +
						" -l -v ";
				String errorFile=getLogName(extract,cropConfig,extract.getDataSetId());
				ErrorLogger.logInfo("Extractor","Executing MDEs");
				ErrorLogger.logDebug("Extractor",gobiiMDE);
				tryExec(gobiiMDE, null, errorFile);
				Integer dataSetId=extract.getDataSetId();

				//HDF5
				String tempFolder=extractDir;
				String genoFile=tempFolder+"DS-"+dataSetId+".genotype";
				String hdf5Extractor=pathToHDF5+"dumpdataset";
				String HDF5File=pathToHDF5Files+"DS_"+dataSetId+".h5";
				// %s <orientation> <HDF5 file> <output file>
				boolean markerFast=false;
				if(extract.getGobiiFileType()==GobiiFileType.HAPMAP)markerFast=true;
				String ordering="samples-fast";
				if(markerFast)ordering="markers-fast";
				ErrorLogger.logDebug("Extractor","HDF5 Ordering is "+ordering);
				ErrorLogger.logInfo("Extractor","Executing: " + hdf5Extractor+" "+ordering+" "+HDF5File+" "+genoFile);
				HelperFunctions.tryExec(hdf5Extractor+" "+ordering+" "+HDF5File+" "+genoFile,null,errorFile);
				success&=ErrorLogger.success();
				ErrorLogger.logDebug("Extractor",(success?"Success ":"Failure " + hdf5Extractor+" "+ordering+" "+HDF5File+" "+genoFile));
				
				switch(extract.getGobiiFileType()){

				case FLAPJACK:
					String genoOutFile=extractDir+"DS"+dataSetId+".genotype";
					String mapOutFile=extractDir+"DS"+dataSetId+".map";
					lastErrorFile=errorFile;
					if(new File(genoOutFile).exists() && new File(mapOutFile).exists()&&false){ //TODO: Remove when stable
						HelperFunctions.sendEmail(extract.getDataSetName()+ " Map Extract", mapOutFile, success&&ErrorLogger.success(), errorFile, configuration, inst.getContactEmail());
						HelperFunctions.sendEmail(extract.getDataSetName()+ " Genotype Extract", genoOutFile, success&&ErrorLogger.success(), errorFile, configuration, inst.getContactEmail());
					}
					else {
						FlapjackTransformer.generateMapFile(markerFile, sampleFile, chrLengthFile, dataSetId, tempFolder, mapOutFile, errorFile);
						HelperFunctions.sendEmail(extract.getDataSetName()+ " Map Extract", mapOutFile, success&&ErrorLogger.success(), errorFile, configuration, inst.getContactEmail());
						FlapjackTransformer.generateGenotypeFile(markerFile, sampleFile, genoFile, dataSetId, tempFolder, genoOutFile,errorFile);
						HelperFunctions.sendEmail(extract.getDataSetName()+ " Genotype Extract", genoOutFile, success&&ErrorLogger.success(), errorFile, configuration, inst.getContactEmail());
					}
					break;
				
				case HAPMAP:
					String hapmapOutFile = extractDir+"DS"+dataSetId+".hmp.txt";
					try{
						System.out.println("Executing HapMap creation");
						String hapmapTransform="python "+extractorScriptPath+"HapmapExtractor.py"+
								" -k "+markerFile+
								" -s "+sampleFile+
								" -p "+projectFile+
								" -m "+genoFile+ 
								" -o "+hapmapOutFile;
						//HapmapTransformer.generateFile(markerFile,sampleFile,projectFile,tempFolder,hapmapOutFile,errorFile);
						HelperFunctions.tryExec(hapmapTransform, null, errorFile);
						rm(genoFile);
					}catch(Exception e){
						ErrorLogger.logError("Extractor","Exception in HapMap creation",e);
					}
					HelperFunctions.sendEmail(extract.getDataSetName()+" Hapmap Extract",hapmapOutFile,success&&ErrorLogger.success(),errorFile,configuration,inst.getContactEmail());
					break;

					default:
						ErrorLogger.logError("Extractor","Unknown Extract Type "+extract.getGobiiFileType());
						HelperFunctions.sendEmail(extract.getDataSetName()+" "+extract.getGobiiFileType()+" Extract",null,false,errorFile,configuration,inst.getContactEmail());
				}
				ErrorLogger.logDebug("Extractor","DataSet "+dataSetId+" Created");
			}
			HelperFunctions.completeInstruction(instructionFile,configuration.getProcessingPath(crop, GobiiFileProcessDir.EXTRACTOR_DONE));
		}
	}

	public static List<GobiiExtractorInstruction> parseExtractorInstructionFile(String filename){
		ObjectMapper objectMapper = new ObjectMapper();
		GobiiExtractorInstruction[] file = null;
		 
		try {
			file = objectMapper.readValue(new FileInputStream(filename), GobiiExtractorInstruction[].class);
		} catch (Exception e) {
			ErrorLogger.logError("Extractor","ObjectMapper could not read instructions",e);
		}
		if(file==null)return null;
		return Arrays.asList(file);
	}
	
	private static String divineCrop(String instructionFile) {
		String upper=instructionFile.toUpperCase();
		String crop = null;
		for(GobiiCropType c:GobiiCropType.values()){
				if(upper.contains(c.toString())){
					crop=c.name();
					break;
				}
		}
		return crop;
	}
	
	private static String getLogName(GobiiExtractorInstruction gli, CropConfig config, Integer dsid) {
		return getLogName(gli.getDataSetExtracts().get(0),config,dsid);
	 }

	private static String getLogName(GobiiDataSetExtract gli, CropConfig config, Integer dsid) {
		String cropName=config.getGobiiCropType();
		String destination=gli.getExtractDestinationDirectory();
		return destination +"/"+cropName+"_DS-"+dsid+".log";
	}
}
