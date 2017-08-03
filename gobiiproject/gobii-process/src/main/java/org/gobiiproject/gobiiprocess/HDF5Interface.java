package org.gobiiproject.gobiiprocess;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.utils.FileSystemInterface;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.email.DigesterMessage;
import org.gobiiproject.gobiimodel.utils.email.ProcessMessage;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.digester.GobiiFileReader;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.checkFileExistence;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.tryExec;
import static org.gobiiproject.gobiimodel.utils.error.ErrorLogger.logDebug;
import static org.gobiiproject.gobiimodel.utils.error.ErrorLogger.logError;

/**
 * A repository of methods designed to interface with HDF5 files, both in the creation and in the execution of 
 */
public class HDF5Interface {


    private static String pathToHDF5;
    private static String pathToHDF5Files;
    //Paths

    public static void createHDF5FromDataset(ProcessMessage dm, String dst, ConfigSettings configuration, Integer dataSetId, String crop, String errorPath, String variantFilename, File variantFile) {
        //HDF-5
        //Usage: %s <datasize> <input file> <output HDF5 file
        String loadHDF5= getPathToHDF5() +"loadHDF5";
        dm.addPath("matrix directory", pathToHDF5Files);
        String HDF5File= getFileLoc(dataSetId);
        int size=8;
        switch(dst.toUpperCase()){
            case "NUCLEOTIDE_2_LETTER": case "IUPAC":case "VCF":
                size=2;break;
            case "SSR_ALLELE_SIZE":size=8;break;
            case "CO_DOMINANT_NON_NUCLEOTIDE":
            case "DOMINANT_NON_NUCLEOTIDE":size=1;break;
            default:
                logError("Digester","Unknown type "+dst.toString());break;
        }
        ErrorLogger.logInfo("Digester","Running HDF5 Loader. HDF5 Generating at "+HDF5File);
        HelperFunctions.tryExec(loadHDF5+" "+size+" "+variantFile.getPath()+" "+HDF5File,null,errorPath);
        GobiiFileReader.updateValues(configuration, crop, dataSetId,variantFilename, HDF5File);
    }

    public static String getPathToHDF5() {
        return pathToHDF5;
    }

    public static void setPathToHDF5(String pathToHDF5) {
        HDF5Interface.pathToHDF5 = pathToHDF5;
    }

    public static String getPathToHDF5Files() {
        return pathToHDF5Files;
    }

    public static void setPathToHDF5Files(String pathToHDF5Files) {
        HDF5Interface.pathToHDF5Files = pathToHDF5Files;
    }

    /**
     * Given a marker list extracts genotyping data from it. See getHDF5GenoFromSampleList for more information.
     * @param markerFast
     * @param errorFile
     * @param tempFolder
     * @param posFile
     * @return
     * @throws FileNotFoundException
     */
    public static String getHDF5GenoFromMarkerList(boolean markerFast, String errorFile, String tempFolder, String posFile) throws FileNotFoundException {
        return getHDF5GenoFromSampleList(markerFast,errorFile,tempFolder,posFile,null);
    }

    private static HashMap<String,String> getSamplePosFromFile(String inputFile) throws FileNotFoundException {
        HashMap<String, String> map = new HashMap<String, String>();
        BufferedReader sampR = new BufferedReader(new FileReader(inputFile));
        try{
            while (sampR.ready()) {
                    String sampLine = sampR.readLine();
                    if (sampLine != null) {
                        String[] sampSplit = sampLine.split("\t");
                        if(sampSplit.length>1){
                            map.put(sampSplit[0],sampSplit[1]);
                        }
                    }
                }
            }
        catch(Exception e){
            ErrorLogger.logError("GobiiExtractor", "Unexpected error in reading sample file",e);
        }
        return map;
    }

    /**
     * Gets a pared down list of markers and samples based on position file and sample position file
     * @param markerFast if the output is 'markerFast' or sample fast
     * @param errorFile Location of temporary error file (mostly but not entirely ignored
     * @param tempFolder Location of folder to store temporary files
     * @param posFile the marker position list, known as a posfile. Each line contains a dataset ID and a list of marker positions, where
     *                the positions refer to the positions in the HDF5 file created for that dataset. The datasets are known by name as their
     *                name in the FS is based on their id in the system.
     * @param samplePosFile As in posFile, this is a list of dataset -> sample position sets. If null, performas a marker list extract unfiltered
     *                      Otherwise, only datasets with lines in here AND in posFile are actually extracted.
     * @return String location of the output file on the filesystem.
     * @throws FileNotFoundException if the datasets provided contain an invalid dataset, or the temporary file folder is badly chmodded
     */
    public static String getHDF5GenoFromSampleList(boolean markerFast, String errorFile, String tempFolder, String posFile, String samplePosFile) throws FileNotFoundException{
        if(!new File(posFile).exists()){
            ErrorLogger.logError("Genotype Matrix","No positions generated - Likely no data");
            return null;
        }
        BufferedReader posR=new BufferedReader(new FileReader(posFile));
        BufferedReader sampR=null;
        boolean hasSampleList=false;
        HashMap<String,String> samplePos=null;
        if(checkFileExistence(samplePosFile)){
            hasSampleList=true;
            sampR=new BufferedReader(new FileReader(samplePosFile));
            samplePos=getSamplePosFromFile(samplePosFile);
        }
StringBuilder genoFileString=new StringBuilder();
        try{
            posR.readLine();//header
            if(sampR!=null)sampR.readLine();
            while(posR.ready()) {
                String[] line = posR.readLine().split("\t");
                if(line.length < 2){
                    ErrorLogger.logDebug("MarkerList","Skipping line " + Arrays.deepToString(line));
                    continue;
                }
                int dsID=Integer.parseInt(line[0]);

                String positionList=line[1].replace(',','\n');
                String positionListFileLoc=tempFolder+"position.list";
                FileSystemInterface.rmIfExist(positionListFileLoc);
                FileWriter w = new FileWriter(positionListFileLoc);
                w.write(positionList);
                w.close();

                String sampleList=null;
                if(hasSampleList){
                    sampleList=samplePos.get(line[0]);
                }
                String genoFile=null;
                if(!hasSampleList || (sampleList!=null)) {
                    genoFile = getHDF5Genotype(markerFast, errorFile, dsID, tempFolder, positionListFileLoc, sampleList);
                }
                else{
                    //We have a marker position but not a sample position. Do not create a genotype file in the first place
                }
                if(genoFile!=null){
                    genoFileString.append(" "+genoFile);
                }
            }
        }catch(IOException e) {
            ErrorLogger.logError("GobiiExtractor", "MarkerList reading failed", e);
        }

        //Coallate genotype files
        String genoFile=tempFolder+"markerList.genotype";
        logDebug("MarkerList", "Accumulating markers into final genotype file");
        String genotypePartFileIdentifier=genoFileString.toString();
        if(markerFast) {
            tryExec("paste" + genotypePartFileIdentifier, genoFile, errorFile);
        }
        else{
            tryExec("cat" + genotypePartFileIdentifier, genoFile, errorFile);
        }
        for(String tempGenoFile:genotypePartFileIdentifier.split(" ")) {
            rmIfExist(tempGenoFile);
        }
        return genoFile;
    }

    /**
     * Convenience method for getHDF5Genotype(boolean, String, Integer, String, String, String).
     * MarkerList and sampleList are passed in as null
     * @return see getHDF5Genotype(boolean,String, Integer, String, String, String)
     */
    public static String getHDF5Genotype(boolean markerFast, String errorFile, Integer dataSetId, String tempFolder) {
        return getHDF5Genotype( markerFast, errorFile,dataSetId,tempFolder,null,null);
    }

    /**
     * Performs the basic genotype extraction on a dataset given by dataSetId, filtered by the string entry from the marker list
     * and sample list files.
     * If marker list is null, do a dataset extract. Else, do a marker list extract on the dataset. If sampleList is also set, filter by samples afterwards
     * @param markerFast if the output is 'markerFast' or sample fast
     * @param errorFile where error logs can be stored temporarily
     * @param dataSetId Dataset ID to be pulled from
     * @param tempFolder folder to store intermediate results
     * @param markerList nullable - determines what markers to extract. File containing a list of marker positions, comma separated
     * @param sampleList nullable - list of comma delimited samples to cut out
     * @return file location of the dataset output.
     */
    private static String getHDF5Genotype( boolean markerFast, String errorFile, Integer dataSetId, String tempFolder, String markerList, String sampleList) {
        String genoFile=tempFolder+"DS-"+dataSetId+".genotype";

        String HDF5File= getFileLoc(dataSetId);
        // %s <orientation> <HDF5 file> <output file>
        String ordering="samples-fast";
        if(markerFast)ordering="markers-fast";

        logDebug("Extractor","HDF5 Ordering is "+ordering);

        if(markerList!=null) {
            String hdf5Extractor=pathToHDF5+"fetchmarkerlist";
            ErrorLogger.logInfo("Extractor","Executing: " + hdf5Extractor+" "+ ordering +" "+HDF5File+" "+markerList+" "+genoFile);
            HelperFunctions.tryExec(hdf5Extractor + " " + ordering+" " + HDF5File+" "+markerList+" "+genoFile, null, errorFile);
        }
        else {
            String hdf5Extractor=pathToHDF5+"dumpdataset";
            ErrorLogger.logInfo("Extractor","Executing: " + hdf5Extractor+" "+ordering+" "+HDF5File+" "+genoFile);
            HelperFunctions.tryExec(hdf5Extractor + " " + ordering + " " + HDF5File + " " + genoFile, null, errorFile);
        }
        if(sampleList!=null){
            filterBySampleList(genoFile,sampleList,markerFast, errorFile);
        }
        ErrorLogger.logDebug("Extractor",(ErrorLogger.success()?"Success ":"Failure " +"Extracting with "+ordering+" "+HDF5File+" "+genoFile));
        return genoFile;
    }

    private static String getFileLoc(Integer dataSetId) {
        return pathToHDF5Files + "DS_" + dataSetId + ".h5";
    }

    /**
     * Filters a matrix passed back by the HDF5 extractor by a sample list
     * @param filename path to extract naked matrix
     * @param sampleList Comma separated list of sample positions
     */
    private static void filterBySampleList(String filename, String sampleList, boolean markerFast, String errorFile){
        String tmpFile=filename+".tmp";
        FileSystemInterface.mv(filename,tmpFile);
        String cutString=getCutString(sampleList);
        if(!markerFast) {
            String sedString=cutString.replaceAll(",","p;");//1,2,3 => 1p;2p;3   (p added later)
            tryExec("sed -n "+sedString+"p",filename,errorFile,tmpFile); //Sed parameters need double quotes to be a single parameter
        }
        else{
            tryExec("cut -f"+getCutString(sampleList),filename,errorFile,tmpFile);
        }
        rmIfExist(tmpFile);
    }

    /**
     * Converts a string of 1,2,-1,4,5,6,-1,2 (Arbitrary -1's and NOT -1's into a comma delimited set of lines from 1-N
     * excluding positions where a -1 exists, ONE BASED.
     *
     * Note: Since input is zero-based list anyway, I probably could have removed the -1's and added 1 to every entry. This seemed derpier.
     *
     * Examples:
     * 0,1,2,-1,4,5 -> 1,2,3,5,6
     * 7,-1,7,-1,7,-1 -> 1,3,5
     * @param sampleList Input string
     * @return Output string (see above)
     */
    private static String getCutString(String sampleList){
        String[] entries=sampleList.split(",");
        StringBuilder cutString=new StringBuilder();//Cutstring -> 1,2,4,5,6
        int i=1;
        for(String entry:entries){
            int val=-1;
            try {
                //For some reason, spaces are everywhere, and Integer.parseInt is not very lenient
                String entryWithoutSpaces=entry.trim().replaceAll(" ","");
                val=Integer.parseInt(entryWithoutSpaces);
            }catch(Exception e){
                ErrorLogger.logDebug("GobiiExtractor NFE",e.toString());
            }
            if( val != -1){
                cutString.append(i+",");
            }
            i++;
        }
        cutString.deleteCharAt(cutString.length()-1);
        return cutString.toString();
    }
}
