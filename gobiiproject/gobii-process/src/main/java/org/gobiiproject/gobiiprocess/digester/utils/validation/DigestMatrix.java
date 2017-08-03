package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiimodel.types.DataSetType;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang.StringUtils.reverse;


public class DigestMatrix {

    private static long startTime, endTime, duration;
    private static String fSep="\t";

    /***
     * Validating digest.matrix(output of CSVReaderV2) for invalid data types.
     * @param inFile - digest.matrix
     * @param dataSetType - loaderInstruction dataset type
     * @return
     */
    public static boolean validatematrix (File inFile, String dataSetType){
        startTime = System.currentTimeMillis();
        try (BufferedReader buffIn=new BufferedReader(new FileReader(inFile))){
            String iLine;
            String errorBase;
            while ((iLine = buffIn.readLine()) != null) {
                if(iLine.equals("matrix")) continue;
                String[] iNucl = iLine.split(fSep);
                errorBase = validateDatasetList(iNucl,dataSetType);
                if(!errorBase.equals(null)){
                    ErrorLogger.logError("Validate Dataset Matrix", "Invalid data found in the matrix - '" + errorBase + "'");
                    return false;
                }
            }
            buffIn.close();
            endTime = System.currentTimeMillis();
            duration = endTime-startTime;
            ErrorLogger.logTrace("Validation Matrix","Time taken:"+ duration/1000 + " Secs");
        }
        catch (FileNotFoundException e){
            ErrorLogger.logError("Validation Matrix", "File not found.", e);
        }
        catch (IOException e){
            ErrorLogger.logError("Validation Matrix", "Unable to open the matrix file.", e);
        }
        catch(Exception e){
            ErrorLogger.logError("Digest Matrix",e);
        }
        return true;
    }


    private static String validateDatasetList(String[] rowList, String dataSetType){
        List<String> elements = null;
        DataSetType dataSetTypeE = DataSetType.valueOf(dataSetType);
        switch (dataSetTypeE) {
            case NUCLEOTIDE_2_LETTER: case  IUPAC:
                elements = initNucleotide2letterList();
                break;
            case CO_DOMINANT_NON_NUCLEOTIDE:
                elements = initCoDominantList();
                break;
            case DOMINANT_NON_NUCLEOTIDE:
                elements = initDominantList();
                break;
            case SSR_ALLELE_SIZE:
                /***
                 * since ssr data has only digits (upto 8)
                 */
                for (String base : rowList){
                    if (base.contains("/")){
                        String[] bases = base.split("/");
                        for(String digit: bases){
                            if(!digit.matches("\\d+") && !digit.equals("N") && (digit.length() > 8)){//Checks of the data length if more that 8 digits. (to save it from HDF5)
                                return digit;
                            }
                        }
                    }
                    else{
                        if(!base.matches("\\d+") && !base.equals("N") && (base.length() > 8)){ //Checks of the data length if more that 8 digits. (to save it from HDF5)
                            return base;
                        }
                    }
                    return null;
                }
                return null;
            case VCF:
                return null;
            default:
                ErrorLogger.logError("Validate Dataset Matrix","ERROR: Invalid dataset type");
                break;
        }
        for (String base: rowList) {
            if (!elements.contains(base) && !base.equals("") && !elements.contains(reverse(base))) {
                return base;
            }
        }
        return null;
    }

    /***
     * Assign data in respective data types.
     * @return
     */
    private static List<String> initNucleotide2letterList(){
        List<String> elements = Arrays.asList("AA", "TT", "CC", "GG", "AT", "AG", "AC", "TG", "TC", "GC", "NN", "++", "--", "+-");
        return elements;
    }

    private static List<String> initIupacList(){
        List<String> elements = Arrays.asList("A", "T", "G", "C", "N");
        return elements;
    }

    private static List<String> initDominantList(){
        List<String> elements = Arrays.asList("0", "1", "N");
        return elements;
    }

    private static List<String> initCoDominantList(){
        List<String> elements = Arrays.asList("0","1","2", "N");
        return elements;
    }
}
