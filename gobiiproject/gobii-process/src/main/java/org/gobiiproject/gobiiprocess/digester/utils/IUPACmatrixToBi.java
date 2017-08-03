package org.gobiiproject.gobiiprocess.digester.utils;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiimodel.types.NucIupacCodes;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import static org.gobiiproject.gobiimodel.types.NucIupacCodes.*;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.checkFileExistence;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by siva on 18-05-2017.
 * Converts IUPAC single code matrix to bi-allelic matrix. (WARNING: does not work with multi(2+)-allelic codes)
 */
public class IUPACmatrixToBi {

    private static long startTime, endTime, duration;
    private static String fSep;

    public static boolean convertIUPACtoBi(String sep, String iFile, String oFile) throws FileNotFoundException {

        if (!checkFileExistence(iFile)) {
            ErrorLogger.logError("IUPAC to Bi","Input file provided does not exists.\n");
            throw new FileNotFoundException("IUPAC to Bi:\t" + iFile +"does not exists");
        }

        Map<String, NucIupacCodes> hash = new HashMap<>();

        initNuclHash(hash);
        switch (sep) {
            case "tab":
                fSep = "\t";
                break;
            case "csv":
                fSep = ",";
                break;
            default:
                ErrorLogger.logError("IUPAC to Bi","Given file format can not be processed.");
                break;
        }
        startTime = System.currentTimeMillis();
        BufferedReader buffIn = new BufferedReader(new FileReader(iFile));
        try (BufferedWriter buffOut=new BufferedWriter(new FileWriter(oFile)))
        {
            String iLine;
            while ((iLine = buffIn.readLine()) != null) {
                if(iLine.equals("matrix")){
                    buffOut.newLine();
                    continue;
                }
                String[] iNucl = iLine.split(fSep);
                String[] oNucl;
                oNucl = new String[(iNucl.length)];
                for (int i = 0; i < iNucl.length; i++) {
                    if(iNucl[i].length() > 1){ // takes care of "+/+" or "+/-" or "-/-" cases
                        oNucl[i] = Character.toString(iNucl[i].charAt(0)) + Character.toString(iNucl[i].charAt(iNucl[i].length()-1));
                    }
                    else{
                        oNucl[i] = hash.get(iNucl[i].toUpperCase()).toString();
                        if(oNucl[i].equals(null)){
                            oNucl[i] = iNucl[i] + iNucl[i]; // takes care of "+" or "-" in the input. Converts to "++" and "--" respectively
                        }
                    }
                }
                buffOut.write(StringUtils.join(oNucl, fSep));
                buffOut.newLine();
            }
            buffOut.close();
            buffIn.close();
            endTime = System.currentTimeMillis();
            duration = endTime-startTime;
            ErrorLogger.logTrace("IUPAC to Bi","Time taken:"+ duration/1000 + " Secs");
        } catch (FileNotFoundException e){
            ErrorLogger.logError("IUPAC to Bi","Missing output File:", e);
        } catch (IOException e){
            ErrorLogger.logError("IUPAC to Bi","Unexpected error", e);
        }
        return true;
    }

    /***
     * Initializing IUPAC nucleotide Dictionary
     * @param hash
     */
    private static void initNuclHash(Map<String,NucIupacCodes> hash){
        hash.put("A",AA);
        hash.put("T",TT);
        hash.put("G",GG);
        hash.put("C",CC);
        hash.put("N",NN);
        hash.put("W",AT);
        hash.put("R",AG);
        hash.put("M",AC);
        hash.put("K",TG);
        hash.put("Y",TC);
        hash.put("S",GC);
    }
}
