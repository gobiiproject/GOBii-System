package org.gobiiproject.gobiiprocess.digester.HelperFunctions;

import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.digester.vcf.VCFTransformer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;
import static org.gobiiproject.gobiiprocess.digester.utils.IUPACmatrixToBi.convertIUPACtoBi;
import static org.gobiiproject.gobiiprocess.digester.utils.TransposeMatrix.transposeMatrix;

/**
 * A class representing a transformation that is not 'in place'. This transformation may take several parameters, but the
 * transformative step requires a file to operate on and a place for it to place the output file.
 */

public abstract class MobileTransform {
    /**
     * Perform transformation from 'from' location to 'to' location optionally making use of the error path provided
     * @param fromFileLocation String representation of the from location on the filesystem
     * @param toFileLocation String representation of the to location on the filesystem
     * @param errorPath place to use for temporary error files
     */
    public abstract void transform(String fromFileLocation, String toFileLocation, String errorPath);


    public static final MobileTransform stripHeader=new MobileTransform(){
        public void transform(String fromFile, String toFile, String errorPath){
            HelperFunctions.tryExec("tail -n +2 ", toFile, errorPath, fromFile);
        }
    };
    public static final MobileTransform IUPACToBI=new MobileTransform(){

        public void transform(String fromFile, String toFile, String errorPath){
            try {
                convertIUPACtoBi("tab", fromFile, toFile);
            }catch(IOException e){
                ErrorLogger.logError("IUPACToBI","Exception opening files for IUPAC to BI conversion",e);
            }
        }
    };
    public static final MobileTransform PGArray=new MobileTransform(){

        public void transform(String fromFile, String toFile, String errorPath){
            new PGArray(fromFile, toFile, "alts").process();
        }
    };

    public static MobileTransform getTransformFromExecString(String exec){
        return new MobileTransform(){
            public void transform(String fromFile, String toFile, String errorPath){
                HelperFunctions.tryExec(exec+ " " + fromFile + " " + toFile, errorPath+".tfmlog", errorPath);
                rmIfExist(errorPath+".tfmlog");
            }
        };
    }
    public static MobileTransform getSNPTransform(String exec, String missingFile){
        return new MobileTransform(){
            public void transform(String fromFile, String toFile, String errorPath){
                HelperFunctions.tryExec(exec+ " " + fromFile + " " + missingFile+ " " + toFile, errorPath+".tfmlog", errorPath);
                rmIfExist(errorPath+".tfmlog");
            }
        };
    }

    public static MobileTransform getVCFTransform(File markerFile){
        return new MobileTransform(){
            public void transform(String fromFile, String toFile, String errorPath){
                String markerFilename = markerFile.getAbsolutePath();
                String markerTmp = new File(markerFile.getParentFile(), "marker.mref").getAbsolutePath();
                try {
                    VCFTransformer.generateMarkerReference(markerFilename, markerTmp, errorPath);
                    new VCFTransformer(markerTmp, fromFile, toFile);
                } catch (Exception e) {
                    ErrorLogger.logError("VCFTransformer", "Failure loading dataset", e);
                }
            }
        };
    }

    public static MobileTransform getTransposeMatrix(String dest){
        return new MobileTransform(){
            public void transform(String fromFile, String toFile, String errorPath){
                try {
                    transposeMatrix("tab", fromFile, toFile, dest);
                } catch (FileNotFoundException e) {
                    ErrorLogger.logError("Matrix Transpose", "Missing File", e);
                }
            }
        };
    }
}

